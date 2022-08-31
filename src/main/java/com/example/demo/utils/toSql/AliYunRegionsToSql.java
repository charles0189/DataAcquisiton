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
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.sdk.service.ecs20140526.models.DescribeRegionsResponse;
import com.example.demo.entity.AliYunRegions;
import com.example.demo.service.AliYunRegionsService;
import com.example.demo.utils.GetCloudHttpConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 描述区域
 * @author charles
 */
@Slf4j
@Component
public class AliYunRegionsToSql {
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;

    @Resource
    public AliYunRegionsService aliYunRegionsService;

    public List<AliYunRegions> getAliYunRegionsData() throws ExecutionException, InterruptedException {
        List<AliYunRegions> result = new ArrayList<>();
        DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.builder()
                .build();
        AsyncClient client = getCloudHttpConnect.getClient();
        CompletableFuture<DescribeRegionsResponse> response = client.describeRegions(describeRegionsRequest);
        DescribeRegionsResponse resp = response.get();
        String jsonString = JSON.toJSONString(DescribeRegionsResponse.toMap(resp));

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject bodyObject = JSONObject.parseObject(jsonObject.getString("body"));
        JSONObject regionsObject = JSONObject.parseObject(bodyObject.getString("Regions"));
        JSONArray region = regionsObject.getJSONArray("Region");
        for (Object o : region) {
            JSONObject key = (JSONObject) o;
            String regionId = key.getString("RegionId");
            String regionEndpoint = key.getString("RegionEndpoint");
            String localName = key.getString("LocalName");
            AliYunRegions aliyunRegions = new AliYunRegions(regionId, regionEndpoint, localName);
            result.add(aliyunRegions);
        }
        client.close();
        return result;
    }

    public void saveData() throws ExecutionException, InterruptedException {
        List<AliYunRegions> aliyunRegions = getAliYunRegionsData();
        aliYunRegionsService.remove(null);
        aliYunRegionsService.saveBatch(aliyunRegions);
        log.info("******成功保存AliYun Regions数据******");
    }

    public void updateData(){
        log.info("————————开始更新AliYun Regions数据————————");
        try {
            saveData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}