//package org.speech4j.tenantservice.migration.service.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.speech4j.tenantservice.entity.tenant.ApiName;
//import org.speech4j.tenantservice.entity.tenant.Config;
//import org.speech4j.tenantservice.entity.tenant.User;
//import org.speech4j.tenantservice.migration.service.SourceService;
//import org.speech4j.tenantservice.service.ConfigService;
//import org.speech4j.tenantservice.service.UserService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@Slf4j
//public class SourceServiceImpl implements SourceService {
//    @Value("${speech4j.email}")
//    private String email;
//    @Value("${speech4j.password}")
//    private String password;
//    @Value(value = "${speech4j.aws.bucketName}")
//    private String bucketName;
//    @Value(value = "${speech4j.aws.endpointUrl}")
//    private String endpointUrl;
//    @Value(value = "${speech4j.aws.accessKey}")
//    private String accessKey;
//    @Value(value = "${speech4j.aws.secretKey}")
//    private String secretKey;
//
//    private UserService userService;
//    private ConfigService configService;
//
//    public SourceServiceImpl(UserService userService, ConfigService configService) {
//        this.userService = userService;
//        this.configService = configService;
//    }
//
//    @Override
//    public void insertDefaultDataForTenant() {
//        User user = new User();
//        user.setActive(true);
//        user.setEmail(email);
//        user.setFirstName("speech4j_name");
//        user.setLastName("speech4j_surname");
//        user.setPassword(password);
//        user.setTenantId("speech4j");
//        userService.create(user);
//        log.debug("SOURCE-SERVICE: Default user was successfully set!");
//
//        //Setting of credentials for AWS S3
//        Map<String, Object> credentials = new HashMap<>();
//        credentials.put("bucket_name", bucketName);
//        credentials.put("access_key", accessKey);
//        credentials.put("secret_key", secretKey);
//        credentials.put("endpoint_url", endpointUrl);
//        JSONObject jsonCredentials = new JSONObject(credentials);
//
//        Config config = new Config();
//       // config.setApiName(ApiName.AWS);
//        config.setTenantId("speech4j");
//        //config.setCredentials(jsonCredentials);
//        configService.create(config);
//        log.debug("SOURCE-SERVICE: Default config was successfully set!");
//    }
//}
