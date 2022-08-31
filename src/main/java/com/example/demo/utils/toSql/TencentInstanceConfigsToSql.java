/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils.toSql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.TencentInstanceConfigs;
import com.example.demo.service.TencentInstanceConfigsService;
import com.example.demo.utils.GetCloudHttpConnect;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZoneInstanceConfigInfosRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZoneInstanceConfigInfosResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author charles
 */
@Component
@Slf4j
public class TencentInstanceConfigsToSql {
    @Resource
    public TencentInstanceConfigsService tencentInstanceConfigsService;
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;

    /**
     * 获得区实例配置信息
     *
     * @return {@link List}<{@link TencentInstanceConfigs}>
     */
    public List<TencentInstanceConfigs> getZoneInstanceConfigInfos(){
        List<String> region = getCloudHttpConnect.getLatestRegions();
        List<TencentInstanceConfigs> result = new ArrayList<>();
        for(int i = 0; i < region.size(); i++) {
            CvmClient client = new CvmClient(getCloudHttpConnect.getCred(), region.get(i), getCloudHttpConnect.getClientProfile());
            DescribeZoneInstanceConfigInfosRequest req = new DescribeZoneInstanceConfigInfosRequest();
            DescribeZoneInstanceConfigInfosResponse resp;
            try {
                resp = client.DescribeZoneInstanceConfigInfos(req);
            } catch (TencentCloudSDKException e) {
                throw new RuntimeException(e);
            }
            JSONObject jsonObject = JSON.parseObject(DescribeZoneInstanceConfigInfosResponse.toJsonString(resp));
            JSONArray configSet = jsonObject.getJSONArray("InstanceTypeQuotaSet");
            for (int j = 0; j < configSet.size(); j++) {
                JSONObject key = (JSONObject) configSet.get(i);
                String zone = key.getString("Zone");
                String instanceType = key.getString("InstanceType");
                String cpuType = key.getString("CpuType");
                String instanceFamily = key.getString("InstanceFamily");
                String typeName = key.getString("TypeName");
                String cpu = key.getString("Cpu");
                String memory = key.getString("Memory");
                String instanceBandwidth = key.getString("InstanceBandwidth");
                String instanceChargeType = key.getString("InstanceChargeType");
                String instancePps = key.getString("InstancePps");
                TencentInstanceConfigs tencentInstanceConfigs = new TencentInstanceConfigs(zone, instanceType, cpuType, instanceFamily, typeName, Integer.valueOf(cpu), Integer.valueOf(memory), instanceBandwidth, instancePps, instanceChargeType);
                result.add(tencentInstanceConfigs);
            }
        }
        return result;
    }

    /**
     * 保存数据
     */
    public void saveData() {
        List<TencentInstanceConfigs> tencentInstanceConfigs = getZoneInstanceConfigInfos();
        tencentInstanceConfigsService.remove(null);
        tencentInstanceConfigsService.saveBatch(tencentInstanceConfigs);
        log.info("******成功保存Tencent InstanceConfigs数据******");
    }

    /**
     * 更新日期
     */
    public void updateDate(){
        log.info("————————开始更新Tencent InstanceConfigs数据————————");
        saveData();
    }
}
