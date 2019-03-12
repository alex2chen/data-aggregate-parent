package com.kxtx.middleware.aggregate.example;

import com.kxtx.middleware.aggregate.example.domain.Address;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: alex
 * @Description: 这是一个数据持久化服务
 * @Date: created in 2019/1/23.
 */
@Service("addressRepository")
public class AddressRepository {

    public Address getAddressById(Integer id) {
        if (id == null) {
            return null;
        }
        return new Address(id);
    }

    public List<Address> getAddressByIds(List<Integer> ids) {
        return ids.stream().map(x -> new Address(x)).collect(Collectors.toList());
    }

    public Address getAddressById2(List<Address> addresses, Integer id) {
        if (addresses == null || addresses.isEmpty()) return null;
        return addresses.stream().filter(x -> x.getAddressId() == id).findFirst().get();
    }
}
