/*
 *
 *  * Copyright (c) 2022-2022. Alibaba Group Holding Ltd. All rights reserved.
 *
 */

package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TencentInstanceConfigsDao;
import com.example.demo.entity.TencentInstanceConfigs;
import com.example.demo.service.TencentInstanceConfigsService;
import org.springframework.stereotype.Service;

/**
 * @author charles
 */
@Service
public class TencentInstanceConfigsServiceImpl extends ServiceImpl<TencentInstanceConfigsDao, TencentInstanceConfigs>implements TencentInstanceConfigsService {
}
