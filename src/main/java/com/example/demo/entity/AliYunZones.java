/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author charles
 */
@Data
@AllArgsConstructor
public class AliYunZones {

    private String zoneId;

    private String status;

    private String statusCategory;

    private String regionId;
}
