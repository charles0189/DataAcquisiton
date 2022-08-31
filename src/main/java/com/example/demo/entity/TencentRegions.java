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
 * 腾讯地区
 * @author charles
 */
@Data
@AllArgsConstructor
public class TencentRegions implements Serializable {
    private String region;

    private String regionName;

    private String regionState;


}
