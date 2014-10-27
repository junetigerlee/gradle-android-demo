/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample. Run it as a Java application (not
 * Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    private static final String packageName = "cn.com.incito.driver.dao";

    private static final String genPath = "../src-gen";

    private static final int DATABASE_VERSION = 5;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DATABASE_VERSION, packageName);
        Agent.addEntity(schema);
        Comment.addEntity(schema);
        OrdersAll.addEntity(schema);// 全部订单表
        OrdersObligation.addEntity(schema);// 待付款订单表
        OrdersDistribution.addEntity(schema);// 待配货订单表
        OrdersSign.addEntity(schema);// 待签收订单表
        OrdersEvaluated.addEntity(schema);// 待评价订单表
        OrdersCanceled.addEntity(schema);// 已取消订单表
        OrdersReimburse.addEntity(schema);// 退款订单表
        OrdersReceived.addEntity(schema);// 待接受订单表

        GoodsAvailable.addEntity(schema);// 可抢货源列表
        GoodsHasAvailable.addEntity(schema);// 已抢货源列表

        new DaoGenerator().generateAll(schema, genPath);
    }

}
