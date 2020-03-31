package com.speech4j.tenantservice.util;

import com.speech4j.tenantservice.dto.request.ConfigDtoReq;
import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.request.UserDtoReq;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static List<TenantDtoReq> getListOfTenants(){
        List<TenantDtoReq> list = new ArrayList<>();

        //entity1
        TenantDtoReq tenant1 = new TenantDtoReq();
        tenant1.setName("Company1");
        list.add(tenant1);

        //entity2
        TenantDtoReq tenant2 = new TenantDtoReq();
        tenant2.setName("Company2");
        list.add(tenant2);

        return list;
    }

    public static List<UserDtoReq> getListOfUsers(){
        List<UserDtoReq> list = new ArrayList<>();

        //entity1
        UserDtoReq user1 = new UserDtoReq();
        user1.setFirstName("Name1");
        user1.setLastName("Surname1");
        user1.setEmail("email@gmail.com");
        user1.setPassword("qwerty123");
        list.add(user1);

        //entity2
        UserDtoReq user2 = new UserDtoReq();
        user2.setFirstName("Name2");
        user2.setLastName("Surname2");
        user2.setEmail("email@gmail.com");
        user2.setPassword("qwerty123");
        list.add(user2);

        return list;
    }


    public static List<ConfigDtoReq> getListOfConfigs(){
        List<ConfigDtoReq> list = new ArrayList<>();

        //entity1
        ConfigDtoReq config1 = new ConfigDtoReq();
        config1.setApiName("Google Api");
        config1.setUsername("mslob");
        config1.setPassword("qwerty123");
        list.add(config1);

        //entity2
        ConfigDtoReq config2 = new ConfigDtoReq();
        config2.setApiName("AWS Api");
        config2.setUsername("speech4j");
        config2.setPassword("qwerty123");
        list.add(config2);

        return list;
    }

}
