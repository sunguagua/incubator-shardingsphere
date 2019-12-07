/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export default {
  common: {
    menuData: [
      {
        title: '数据治理',
        child: [
          {
            title: '注册配置中心',
            href: '/registry-center'
          },
          {
            title: '配置管理',
            href: '/rule-config'
          },
          {
            title: '运行状态',
            href: '/runtime-status'
          }
        ]
      }
    ],
    connect: '已连接',
    connection: '连接',
    del: '删除',
    notify: {
      title: '提示',
      conSucMessage: '连接成功',
      conFailMessage: '连接失败',
      delSucMessage: '删除成功',
      delFailMessage: '删除失败',
      updateCompletedMessage: '更新成功',
      updateFaildMessage: '更新失败'
    },
    loginOut: '退出登陆'
  },
  login: {
    btnTxt: '登陆',
    labelUserName: '用户名',
    labelPassword: '密码'
  },
  btn: {
    submit: '提交',
    reset: '重置',
    cancel: '取消'
  },
  input: {
    pUserName: '请输入用户名',
    pPaasword: '请输入密码'
  },
  index: {
    btnTxt: '添加',
    registDialog: {
      title: '添加注册中心',
      name: '注册中心名称',
      centerType: '注册中心类型',
      address: '注册中心地址',
      orchestrationName: '数据治理实例',
      namespaces: '命名空间',
      digest: '登录凭证',
      btnConfirmTxt: '确定',
      btnCancelTxt: '取消'
    },
    table: {
      operate: '操作',
      operateConnect: '连接',
      operateConnected: '已激活',
      operateDel: '删除'
    },
    rules: {
      name: '请输入注册中心名称',
      centerType: '请选择注册中心类型',
      namespaces: '请输入命名空间',
      address: '请选输入注册中心地址',
      orchestrationName: '请输入数据治理实例名称',
      digest: '请输入登录凭证'
    }
  },
  orchestration: {
    serviceNode: '服务节点',
    slaveDataSourceName: '从库信息',
    dataSource: {
      schema: '逻辑库名',
      masterDataSourceName: '主库名',
      slaveDataSourceName: '从库名'
    },
    instance: {
      instanceId: '节点标识',
      serverIp: '服务ip'
    }
  },
  configManage: {
    schema: {
      name: '名称',
      ruleConfig: '分片配置规则',
      dataSourceConfig: '据源配置规则'
    },
    schemaRules: {
      name: '请输入名称',
      ruleConfig: '请输入数据分片配置规则',
      dataSourceConfig: '请输入数据源配置规则'
    }
  }
}
