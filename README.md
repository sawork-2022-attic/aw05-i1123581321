# RESTful microPoS 


请参考spring-petclinic-rest/spring-petclinic-microserivces 将aw04的webpos项目改为rest风格的微服务架构
（至少包含产品管理服务pos-products和购物车管理服务pos-carts以及discovery/gateway等微服务架构下需要的基础设施服务）。具体要求包括：

1. 请使用OpenAPI的定义每个服务的rest接口（参考pos-products）
2. 请使用ehcache管理缓存；
3. 请注意使用断路器等机制；
4. 有兴趣的同学可自学一些reactjs或vuejs等为microPoS开发一个前端。

## 模块划分

整个 pos 被划分为四个部分，其中 product 和 cart 分别提供产品目录查询和购物车管理的服务，discovery 提供服务的注册和发现服务，gateway 作为网关提供路由服务

### product

采取与上次作业类似的方法，从京东获取数据。这里使用了 cache 机制以避免多次访问京东，商品目录的 ttl 设置为 1 分钟。

接口定义参见 `api.yml`

### cart

控制购物车的相关逻辑，在这个模块对请求进行了简单的校验。在添加商品进入购物车时会先检查商品目录有没有该商品，此处对 product 服务进行请求，并且添加了一个断路器，当 product 服务宕机时会返回一个预先构造好的商品列表

接口定义参见 `api.yml`

### discovery

实现了 eureka server，需要注意的是 server 可以把自身作为客户端的寻求注册的功能关闭，可以少输出一些报错日志

### gateway

微服务中网关主要用于整合 api，路由，实行中介策略，分析日志等。本次作业中架构较为简单，网关主要用来提供一个统一的服务接入点，以及进行路由。