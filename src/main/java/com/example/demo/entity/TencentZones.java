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
 * 腾讯区
 * @author charles
 */
@Data
@AllArgsConstructor
public class TencentZones implements Serializable {

    private String zone;

    private String zoneName;

    private int zoneId;

    private String zoneState;

}
