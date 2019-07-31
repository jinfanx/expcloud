# Expcloud

一个前后端分离 + spring cloud + docker 的小项目，麻雀虽小，五脏俱全。

## 主页
[https://www.freej.top](https://www.freej.top)

## Features
- ElementUI (vue)
- spring cloud 
- docker-compose
- MongoDB
- elasticsearch
- OAuth2.0 （支持github登录）
- Nginx ( ssl、proxy )
- spring-session-redis
- mongo-connector ( 从mongodb同步数据到es )
- markdown

## 常见问题与解决办法
|序号|问题|解决办法|
|---|----|-------|
|1|前后端分离中的跨域问题||
|2|在https站点调用http接口||
|3|vue动态组件||
|4|vue父子组件之间互相传值||
|5|用elementui脚手架搭建的工程build报错，不支持ES6语法||
|6|spring-cloud-gateway cors配置||
|7|前后端分离session传递||
|8|vue生命周期钩子，钩子函数概念||
|9|es 9300端口外网无法访问||
|10|es启动报错，vm.max_count设置、jvm参数配置||
|11|mongo-connector从mongodb同步数据到es||
|12|mongodb单节点启用副本集||
|13|mongodb副本集配置修改||
|14|nginx ssl配置||
|15|nginx代理配置||
|16|mongodb中ObjectID转json格式化问题||
|17|spring cloud gateway自定义过滤器，全局过滤器配置||
|18|webflux session设置||

## TODO
1. 注册和登录功能
2. 在网关加入Hystrix配置
3. 日志系统
4. spring security
5. 服务间使用自签名证书配置ssl
6. 引入spring-cloud-config
7. 模拟集群，容器编排

