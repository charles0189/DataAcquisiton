/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils.toSql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.TencentZones;
import com.example.demo.service.TencentZonesService;
import com.example.demo.utils.GetCloudHttpConnect;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
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
public class TencentZonesToSql {
    @Resource
    public TencentZonesService tencentZonesService;

    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;

    public List<TencentZones> getZonesData() {
        List<TencentZones> result = new ArrayList<>();
        List<String> region = getCloudHttpConnect.getLatestRegions();
        for (String s : region) {
            CvmClient client = new CvmClient(getCloudHttpConnect.getCred(), s, getCloudHttpConnect.getClientProfile());
            DescribeZonesRequest req = new DescribeZonesRequest();
            DescribeZonesResponse resp;
            try {
                resp = client.DescribeZones(req);
            } catch (TencentCloudSDKException e) {
                throw new RuntimeException(e);
            }
            JSONObject jsonObject1 = JSON.parseObject(DescribeZonesResponse.toJsonString(resp));
            JSONArray zoneSet1 = jsonObject1.getJSONArray("ZoneSet");
            for (Object o : zoneSet1) {
                JSONObject key = (JSONObject) o;
                String zone = key.getString("Zone");
                String zoneName = key.getString("ZoneName");
                String zoneId = key.getString("ZoneId");
                String zoneState = (String) key.get("ZoneState");
                TencentZones tencentZones = new TencentZones(zone, zoneName, Integer.parseInt(zoneId), zoneState);
                result.add(tencentZones);
            }
        }
        return result;
    }
    public void saveData(){
        List<TencentZones> zones = getZonesData();
        tencentZonesService.remove(null);
        tencentZonesService.saveBatch(zones);
        log.info("******成功保存Tencent Zones数据******");
    }
    public void updateDate(){
        log.info("————————开始更新Tencent Zones数据————————");
        saveData();
    }

}
