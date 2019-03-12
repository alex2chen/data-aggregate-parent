package com.kxtx.middleware.aggregate.example;

import com.google.common.collect.Lists;
import com.kxtx.middleware.aggregate.example.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: alex
 * @Description: 这是产品服务
 * @Date: created in 2019/1/23.
 */
@Service("productService")
public class ProductService {

    public List<Product> listByIds(List<Integer> productIds) throws InterruptedException {
        if (productIds == null) {
            return null;
        }
        Thread.sleep(2000);
        return productIds.stream().map(x -> new Product(x)).collect(Collectors.toList());
    }

    public List<Product> listByIds2(List<Product> products, List<Integer> productIds) {
        return products.stream().filter(x -> productIds.contains(x.getProductId())).collect(Collectors.toList());
    }
}
