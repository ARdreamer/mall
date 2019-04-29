package com.mall.task;

import com.mall.common.Const;
import com.mall.common.RedissonManager;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import com.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private RedissonManager redissonManager;

    @PreDestroy
    public void delLock() {
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

    //    @Scheduled(cron = "0 */1 * * * ?")//一分钟
    public void closeOrderTaskV1() {
        log.info("关闭订单，定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.close(hour);
        log.info("关闭订单，定时任务关闭");
    }

    //    @Scheduled(cron = "0 */1 * * * ?")//一分钟
    public void closeOrderTaskV2() {
        log.info("关闭订单，定时任务启动");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        //返回1代表设置成功，获取锁
        if (setnxResult != null && setnxResult == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获取分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单，定时任务关闭");
    }

    //    @Scheduled(cron = "0 */1 * * * ?")//一分钟
    public void closeOrderTaskV3() {
        log.info("关闭订单，定时任务启动");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        //返回1代表设置成功，获取锁
        if (setnxResult != null && setnxResult == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                //再次用当前时间戳getset，返回key的旧值，判断是否可以获取锁
                //当key没有旧值时，即key不存在，返回nil->null,
                //== null  说明有别的进程删除了上一个key
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(getSetResult, lockValueStr))) {
                    //获取到锁了
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获取分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获取分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单，定时任务关闭");
    }

    @Scheduled(cron = "0 */1 * * * ?")//一分钟
    public void closeOrderTaskV4() {
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                log.info("Redisson获取分布式锁:{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
                iOrderService.close(hour);
            } else {
                log.info("Redisson没有获取分布式锁:{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson获取分布式锁异常", e);
        } finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.error("Redisson获取分布式锁释放");

        }

    }

    private void closeOrder(String lockName) {
        //有效期5秒，防止死锁
        RedisShardedPoolUtil.expire(lockName, 5);
        log.info("获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.close(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        log.info("====================================");
    }
}
