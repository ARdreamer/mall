package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;

public interface IShippingService {
    public ServerResponse add(Integer userId, Shipping shipping);

    public ServerResponse<String> del(Integer userId, Integer shippingId);

    public ServerResponse update(Integer userId, Shipping shipping);

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
