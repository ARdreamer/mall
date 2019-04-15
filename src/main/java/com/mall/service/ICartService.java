package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.vo.CartVo;

public interface ICartService {
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> deleteProduct(String productId, Integer userId);

    public ServerResponse<CartVo> list(Integer userId);

    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId);

    public ServerResponse<Integer> getCartProductCount(Integer userId);
}
