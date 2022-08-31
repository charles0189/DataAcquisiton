/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 阿里云实例配置
 * @author charles
 */
@Data
@AllArgsConstructor
public class AliYunInstanceConfigs implements Serializable {

    private String region;

    private String instanceTypeId;

    private String instanceTypeFamily;

    private String cpuType;

    private Integer cpuCoreCount;

    private Double memorySize;

    private String gpuSpec;

    private Integer gpuAmount;

    private String instanceBandwidthRx;

    private String instanceBandwidthTx;

    private String instancePpsRx;

    private String instancePpsTx;

}
