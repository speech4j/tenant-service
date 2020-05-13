package org.speech4j.tenantservice.fixture;

import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.request.UserDtoReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFixture {

    public static List<TenantDtoCreateReq> getListOfTenants(){
        List<TenantDtoCreateReq> list = new ArrayList<>();

        //entity1
        TenantDtoCreateReq tenant1 = new TenantDtoCreateReq();
        tenant1.setName("name1");
        tenant1.setDescription("Company1");
        list.add(tenant1);

        //entity2
        TenantDtoCreateReq tenant2 = new TenantDtoCreateReq();
        tenant2.setName("name2");
        tenant2.setDescription("Company2");
        list.add(tenant2);

        return list;
    }

    public static List<UserDtoReq> getListOfUsers(){
        List<UserDtoReq> list = new ArrayList<>();

        //entity1
        UserDtoReq user1 = new UserDtoReq();
        user1.setFirstName("Name1");
        user1.setLastName("Surname1");
        user1.setEmail("email1@gmail.com");
        user1.setPassword("qwertY123");
        list.add(user1);

        //entity2
        UserDtoReq user2 = new UserDtoReq();
        user2.setFirstName("Name2");
        user2.setLastName("Surname2");
        user2.setEmail("email2@gmail.com");
        user2.setPassword("qwertY123");
        list.add(user2);

        return list;
    }


    public static List<ConfigDtoReq> getListOfConfigs(){
        List<ConfigDtoReq> list = new ArrayList<>();
        Map<String,Object> credentials = new HashMap<>();
        credentials.put("username", "mslob");
        credentials.put("password", "qwerty123");

        //entity1
        ConfigDtoReq config1 = new ConfigDtoReq();
        config1.setApiName("Google Api");
        config1.setCredentials(credentials);
        list.add(config1);

        //entity2
        ConfigDtoReq config2 = new ConfigDtoReq();
        config2.setApiName("AWS Api");
        config1.setCredentials(credentials);
        list.add(config2);

        return list;
    }

}
