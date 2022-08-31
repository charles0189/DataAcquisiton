/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils.toSql;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.sdk.service.ecs20140526.AsyncClient;
import com.aliyun.sdk.service.ecs20140526.models.DescribeAvailableResourceRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeAvailableResourceResponse;
import com.example.demo.entity.AliYunInstances;
import com.example.demo.service.AliYunInstancesService;
import com.example.demo.utils.GetCloudHttpConnect;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 阿里云实例sql
 * @author charles
 */
@Slf4j
@Component
public class AliYunInstancesToSql {
    @Resource
    public AliYunInstancesService aliYunInstancesService;
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;

    public List<AliYunInstances> getInstanceData() throws ExecutionException, InterruptedException {
        List<AliYunInstances> result = new ArrayList<>();
        List<String> aliYunRegions = getCloudHttpConnect.getLatestAliRegions();
        for (String aliYunRegion : aliYunRegions) {
            AsyncClient client = AsyncClient.builder()
                    .region(aliYunRegion)
                    .credentialsProvider(getCloudHttpConnect.getProvider())
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("ecs." + aliYunRegion + ".aliyuncs.com")
                    )
                    .build();

            DescribeAvailableResourceRequest describeAvailableResourceRequest = DescribeAvailableResourceRequest.builder()
                    .destinationResource("InstanceType")
                    .regionId(aliYunRegion)
                    .build();

            CompletableFuture<DescribeAvailableResourceResponse> response = client.describeAvailableResource(describeAvailableResourceRequest);
            DescribeAvailableResourceResponse resp = response.get();

            String jsonString = JSON.toJSONString(DescribeAvailableResourceResponse.toMap(resp));
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONObject bodyObject = JSONObject.parseObject(jsonObject.getString("body"));
            JSONObject regionsObject = JSONObject.parseObject(bodyObject.getString("AvailableZones"));
            JSONArray zoneInfo = regionsObject.getJSONArray("AvailableZone");
            for (Object o : zoneInfo) {
                JSONObject key = (JSONObject) o;
                String zoneId = key.getString("ZoneId");
                String status = key.getString("Status");
                String regionId = key.getString("RegionId");
                String statusCategory = key.getString("StatusCategory");
                JSONObject avaReJson = JSONObject.parseObject(key.getString("AvailableResources"));
                JSONArray avaReJsonArray = avaReJson.getJSONArray("AvailableResource");
                for (Object value : avaReJsonArray) {
                    JSONObject key1 = (JSONObject) value;
                    JSONObject supportedResources = JSONObject.parseObject(key1.getString("SupportedResources"));
                    JSONArray instanceInfoArray = supportedResources.getJSONArray("SupportedResource");
                    for (Object item : instanceInfoArray) {
                        JSONObject key2 = (JSONObject) item;
                        String instanceType = key2.getString("Value");
                        AliYunInstances aliYunInstances = new AliYunInstances(zoneId, status, regionId, statusCategory, instanceType);
                        result.add(aliYunInstances);
                    }
                }
            }

        }
        return result;
    }

    public void saveData() throws ExecutionException, InterruptedException {
        List<AliYunInstances> aliYunInstances = getInstanceData();
        aliYunInstancesService.remove(null);
        aliYunInstancesService.saveBatch(aliYunInstances);
        log.info("******成功保存AliYun Instances数据******");
    }

    public void updateData() throws ExecutionException, InterruptedException {
        log.info("————————开始更新AliYun Instances数据————————");
        saveData();
    }
}