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
 * 实例配置
 * @author charles
 * @since 2022-08-17
 */
@Data
@AllArgsConstructor
public class TencentInstanceConfigs implements Serializable {

    private String zone;

    private String instanceType;

    private String cpuType;

    private String instanceFamily;

    private String typeName;

    private Integer cpu;

    private Integer memory;

    private String instanceBandwidth;

    private String instancePps;

    private String instanceChargeType;

}
