/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils.toSql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.TencentRegions;
import com.example.demo.service.TencentRegionsService;
import com.example.demo.utils.GetCloudHttpConnect;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
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
public class TencentRegionsToSql {
    @Resource
    public TencentRegionsService tencentRegionsService;
    @Resource
    public GetCloudHttpConnect getCloudHttpConnect;
    /**
     * 得到区域数据
     *
     * @return {@link List}<{@link TencentRegions}>
     */
    public List<TencentRegions> getRegionsData() {
        List<TencentRegions> result = new ArrayList<>();
        CvmClient client = new CvmClient(getCloudHttpConnect.getCred(), "", getCloudHttpConnect.getClientProfile());
        DescribeRegionsRequest req = new DescribeRegionsRequest();
        DescribeRegionsResponse resp;
        try {
            resp = client.DescribeRegions(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        // 将其转为对象
        JSONObject jsonObject = JSONObject.parseObject(DescribeRegionsResponse.toJsonString(resp));
        JSONArray regionSet = jsonObject.getJSONArray("RegionSet");
        for (Object o : regionSet) {
            JSONObject key = (JSONObject) o;
            String region = (String) key.get("Region");
            String regionName = (String) key.get("RegionName");
            String regionState = (String) key.get("RegionState");
            TencentRegions tencentRegions = new TencentRegions(region, regionName, regionState);
            result.add(tencentRegions);
        }

        return result;
    }

    /**
     * 保存数据
     */
    public void saveData(){
        List<TencentRegions> regions = getRegionsData();
        tencentRegionsService.remove(null);
        tencentRegionsService.saveBatch(regions);
        log.info("******成功保存Tencent Regions数据******");
    }

    /**
     * 更新数据
     */
    public void updateData(){
        log.info("————————开始更新Tencent Regions数据————————");
        saveData();
    }
}
