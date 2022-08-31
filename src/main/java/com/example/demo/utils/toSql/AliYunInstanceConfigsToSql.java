/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils.toSql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.sdk.service.ecs20140526.models.*;
import com.aliyun.sdk.service.ecs20140526.*;
import com.example.demo.service.AliYunInstanceConfigsService;
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
 * @author charles
 */
@Slf4j
@Component
public class AliYunInstanceConfigsToSql {
    @Resource
    public AliYunInstanceConfigsService aliYunInstanceConfigsService;
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;
    public List<com.example.demo.entity.AliYunInstanceConfigs> getInstanceConfigsData() throws ExecutionException, InterruptedException {
        // 创建保存数据结果的数组
        List<com.example.demo.entity.AliYunInstanceConfigs> result = new ArrayList<>();
        // 获取地域结果数组
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
            DescribeInstanceTypesRequest describeInstanceTypesRequest = DescribeInstanceTypesRequest.builder()
                    .build();

            CompletableFuture<DescribeInstanceTypesResponse> response = client.describeInstanceTypes(describeInstanceTypesRequest);
            DescribeInstanceTypesResponse resp = response.get();

            String jsonString = JSON.toJSONString(DescribeInstanceTypesResponse.toMap(resp));
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONObject bodyObject = JSONObject.parseObject(jsonObject.getString("body"));
            JSONObject instanceTypesObject = JSONObject.parseObject(bodyObject.getString("InstanceTypes"));
            JSONArray configsInfo = instanceTypesObject.getJSONArray("InstanceType");

            for (Object o : configsInfo) {
                JSONObject key = (JSONObject) o;
                String instanceTypeId = key.getString("InstanceTypeId");
                String instanceTypeFamily = key.getString("InstanceTypeFamily");
                String cpuType = "";
                String cpuCoreCount = key.getString("CpuCoreCount");

                String memorySize = key.getString("MemorySize");
                String gpuSpec = key.getString("GPUSpec");
                String gpuAmount = key.getString("GPUAmount");
                String instanceBandwidthRx = key.getString("InstanceBandwidthRx");
                String instanceBandwidthTx = key.getString("InstanceBandwidthTx");
                String instancePpsRx = key.getString("InstancePpsRx");
                String instancePpsTx = key.getString("InstancePpsTx");
                Integer cpu = Integer.valueOf(cpuCoreCount);
                Double memory = Double.valueOf(memorySize);
                Integer gpu = Integer.valueOf(gpuAmount);
                com.example.demo.entity.AliYunInstanceConfigs aliYunInstanceConfigs = new com.example.demo.entity.AliYunInstanceConfigs(aliYunRegion, instanceTypeId, instanceTypeFamily, cpuType,
                        cpu, memory, gpuSpec, gpu, instanceBandwidthRx, instanceBandwidthTx, instancePpsRx,
                        instancePpsTx);
                result.add(aliYunInstanceConfigs);

            }
        }
        return result;
    }
    public void saveData() throws ExecutionException, InterruptedException {
        List<com.example.demo.entity.AliYunInstanceConfigs> aliYunInstanceConfigs = getInstanceConfigsData();
        aliYunInstanceConfigsService.remove(null);
        aliYunInstanceConfigsService.saveBatch(aliYunInstanceConfigs);
        log.info("******成功保存AliYun InstanceConfigs数据******");
    }

    public void updateData() throws ExecutionException, InterruptedException {
        log.info("————————开始更新AliYun InstanceConfigs数据————————");
        saveData();
    }

}