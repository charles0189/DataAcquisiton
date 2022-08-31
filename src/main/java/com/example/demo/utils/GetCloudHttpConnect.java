/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * @author charles
 */
@ConfigurationProperties(prefix = "application.yml")
@Component
public class GetCloudHttpConnect {
    @Value("${secret.tencentId}")
    private String tencentSecretId;

    @Value("${secret.tencentKey}")
    private String tencentSecretKey;

    @Value("${secret.aliYunAccessKeyId}")
    private String aliYunAccessKeyId;

    @Value("${secret.aliYunAccessKeySecret}")
    private String aliYunAccessKeySecret;

    public Credential getCred() {
        return new Credential(tencentSecretId, tencentSecretKey);
    }

    public StaticCredentialProvider getProvider(){
        return StaticCredentialProvider.create(com.aliyun.auth.credentials.Credential
                .builder().accessKeyId(aliYunAccessKeyId).accessKeySecret(aliYunAccessKeySecret)
                .build());
    }

    public ClientProfile getClientProfile() {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cvm.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return clientProfile;
    }

    public AsyncClient getClient(){
        return AsyncClient.builder()
                .region("cn-qingdao")
                .credentialsProvider(this.getProvider())
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("ecs.cn-qingdao.aliyuncs.com")
                )
                .build();
    }

    public List<String> getLatestRegions() {
        DescribeRegionsRequest req1 = new DescribeRegionsRequest();
        Credential cred = new Credential(tencentSecretId, tencentSecretKey);
        CvmClient client1 = new CvmClient(cred, "", this.getClientProfile());
        DescribeRegionsResponse resp1;
        try {
            resp1 = client1.DescribeRegions(req1);
        } catch (
                TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = JSONObject.parseObject(DescribeRegionsResponse.toJsonString(resp1));
        JSONArray regionSet = jsonObject.getJSONArray("RegionSet");
        List<String> region = new ArrayList<>();
        for (Object o : regionSet) {
            JSONObject key = (JSONObject) o;
            String regionJson = (String) key.get("Region");
            region.add(regionJson);
        }
        return region;
    }

    public List<String> getLatestAliRegions() throws ExecutionException, InterruptedException {
        com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest describeRegionsRequest = com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest.builder()
                .build();
        AsyncClient client = this.getClient();
        CompletableFuture<com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse> response = client.describeRegions(describeRegionsRequest);
        com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse resp = response.get();
        String jsonString = JSON.toJSONString(com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse.toMap(resp));

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject bodyObject = JSONObject.parseObject(jsonObject.getString("body"));
        JSONObject regionsObject = JSONObject.parseObject(bodyObject.getString("Regions"));
        JSONArray region = regionsObject.getJSONArray("Region");
        List<String> aliRegion = new ArrayList<>();
        for (Object o : region) {
            JSONObject key = (JSONObject) o;
            String regionId = key.getString("RegionId");
            aliRegion.add(regionId);
        }
        return aliRegion;
    }
}
