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
import com.example.demo.entity.AliYunZones;
import com.example.demo.service.AliYunZonesService;
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
 * 阿里云Zones数据存sql
 *
 * @author charles
 */
@Component
@Slf4j
public class AliYunZonesToSql {

    @Resource
    public AliYunZonesService aliYunZonesService;
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;


    public List<AliYunZones> getAliYunZonesData() throws ExecutionException, InterruptedException {
        List<AliYunZones> result = new ArrayList<>();
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
                    .destinationResource("Zone")
                    .regionId(aliYunRegion)
                    .build();
            CompletableFuture<DescribeAvailableResourceResponse> response = client.describeAvailableResource(describeAvailableResourceRequest);
            DescribeAvailableResourceResponse resp = response.get();

            String jsonString = JSON.toJSONString(DescribeAvailableResourceResponse.toMap(resp));
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONObject bodyObject = JSONObject.parseObject(jsonObject.getString("body"));
            JSONObject regionsObject = JSONObject.parseObject(bodyObject.getString("AvailableZones"));
            JSONArray zone = regionsObject.getJSONArray("AvailableZone");
            for (Object o : zone) {
                JSONObject key = (JSONObject) o;
                String zoneId = key.getString("ZoneId");
                String status = key.getString("Status");
                String statusCategory = key.getString("StatusCategory");
                String regionId = key.getString("RegionId");
                AliYunZones aliYunZones = new AliYunZones(zoneId, status, statusCategory, regionId);
                result.add(aliYunZones);
            }
        }
        return result;
    }
    public void saveData() throws ExecutionException, InterruptedException {

            List<AliYunZones> aliYunZones = getAliYunZonesData();
            aliYunZonesService.remove(null);
            aliYunZonesService.saveBatch(aliYunZones);
            log.info("******成功保存AliYun Zones数据******");

    }

    public void updateData(){
        log.info("————————开始更新AliYun Zones数据————————");
        try {
            saveData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
