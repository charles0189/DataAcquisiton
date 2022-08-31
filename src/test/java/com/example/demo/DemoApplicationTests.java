/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo;

import com.example.demo.dao.TencentRegionsDao;
import com.example.demo.service.TencentInstanceConfigsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private TencentInstanceConfigsService tencentInstanceConfigsService;


    @Autowired
    public TencentRegionsDao tencentRegionsDao;

    @Test
    void contextLoads() {


    }


}
