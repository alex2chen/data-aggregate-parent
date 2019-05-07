package com.github.middleware.aggregate.example.domain;

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
public class Address {
    private Integer addressId;
    private String consigneeName;
    private String phone;
    private String addressDetail;

    public Address(Integer addressId) {
        this.addressId = addressId;
    }
}
