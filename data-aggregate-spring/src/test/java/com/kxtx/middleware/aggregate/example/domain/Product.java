package com.kxtx.middleware.aggregate.example.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/23.
 */
@ToString
@Getter
@Setter
public class Product {
    private Integer productId;
    private String productName;
    private double price;

    public Product(Integer productId) {
        this.productId = productId;
    }
}
