package org.speech4j.tenantservice.fixture;

import org.json.JSONObject;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.entity.tenant.ApiName;
import org.speech4j.tenantservice.entity.tenant.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFixture {

    public static List<TenantDtoCreateReq> getListOfTenants(){
        //entity1
        TenantDtoCreateReq tenant1 = new TenantDtoCreateReq();
        tenant1.setName("test_tenant_1");
        tenant1.setDescription("Company-1");
        //entity2
        TenantDtoCreateReq tenant2 = new TenantDtoCreateReq();
        tenant2.setName("test_tenant_2");
        tenant2.setDescription("Company-2");
        return Arrays.asList(tenant1, tenant2);
    }

    public static List<UserDtoReq> getListOfUsers(){
        //entity1
        UserDtoReq user1 = new UserDtoReq();
        user1.setFirstName("Name1");
        user1.setLastName("Surname1");
        user1.setEmail("email1@gmail.com");
        user1.setPassword("Qwerty123");
        //entity2
        UserDtoReq user2 = new UserDtoReq();
        user2.setFirstName("Name2");
        user2.setLastName("Surname2");
        user2.setEmail("email2@gmail.com");
        user2.setPassword("Qwerty123");
        return Arrays.asList(user1, user2);
    }


    public static List<Config> getListOfConfigs(){
        Map<String,Object> credentials = new HashMap<>();
        credentials.put("username", "mslob");
        credentials.put("password", "qwerty123");

        //entity1
        Config config1 = new Config();
        config1.setId("1");
        config1.setTenantId("test_tenant_1");
        config1.setApiName(ApiName.GOOGLE);
        config1.setCredentials(new JSONObject(credentials).toString());
        //entity2
        Config config2 = new Config();
        config2.setId("2");
        config2.setTenantId("test_tenant_2");
        config2.setApiName(ApiName.AWS);
        config2.setCredentials(new JSONObject(credentials).toString());

        return Arrays.asList(config1,config2);
    }

}
