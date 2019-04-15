package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVo;

public interface IProductService {
    public ServerResponse saveOrUpdateProduct(Product product);

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    public ServerResponse getProductList(int pageNum, int pageSize);

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
