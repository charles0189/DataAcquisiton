/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.utils;

import com.example.demo.utils.toSql.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 实例配置sql
 * 项目初始化加载时
 * @author charles
 */
@Component
@Slf4j
public class FetchData {
    @Resource
    public TencentRegionsToSql tencentRegionsToSql;
    @Resource
    public AliYunRegionsToSql aliYunRegionsToSql;
    @Resource
    public TencentZonesToSql tencentZonesToSql;
    @Resource
    public AliYunZonesToSql aliYunZonesToSql;
    @Resource
    public TencentInstanceConfigsToSql tencentInstanceConfigsToSql;
    @Resource
    public AliYunInstancesToSql aliYunInstancesToSql;
    @Resource
    public AliYunInstanceConfigsToSql aliYunInstanceConfigsToSql;

    @PostConstruct
    @Async
    @Scheduled(fixedRate = 300000)
    public void updateData() throws ExecutionException, InterruptedException {
        log.info("当前线程为:" + Thread.currentThread());
        log.info("------开始定时更新数据:");
        tencentRegionsToSql.updateData();
        aliYunRegionsToSql.updateData();
        tencentZonesToSql.updateDate();
        aliYunZonesToSql.updateData();
        aliYunInstancesToSql.updateData();
        tencentInstanceConfigsToSql.updateDate();
        aliYunInstanceConfigsToSql.updateData();
        log.info("------本轮更新结束------");
    }
}
