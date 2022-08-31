/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 地区
 * @author charles
 */
@Data
@AllArgsConstructor
public class AliYunRegions {

    private String regionId;

    private String regionEndpoint;

    private String localName;

}
