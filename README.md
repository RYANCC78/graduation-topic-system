# 毕业设计选题系统

《软件项目开发与实践2》课程作业 —— 第7组（雷彩宸 / 张肃金 / 罗洪）

## 一、技术栈

| 层次 | 技术 |
|---|---|
| 架构 | B/S，MVC 五层（表现层 JSP → 控制层 Servlet → 业务层 Service → 持久层 DAO/JDBC → 域模型 POJO） |
| 后端 | Java 8 + Servlet 4.0 + JSP |
| 数据库 | MySQL 8.0 |
| 服务器 | Tomcat 8.5+ |
| 构建 | Maven（war 包） |
| 报表导出 | Apache POI |
| 密码存储 | SHA-256 加盐前哈希 |

## 二、目录结构

```
graduation-topic-system/
├── pom.xml
├── sql/init.sql                     # 建库 + 建表 + 初始数据
└── src/main/
    ├── resources/db.properties      # 数据库连接配置
    ├── java/com/buptsse/topic/
    │   ├── model/                   # 域模型：User/Topic/Application/SystemConfig
    │   ├── dao/ + dao/impl/         # DAO 接口 + JDBC 实现
    │   ├── service/                 # 业务逻辑层
    │   ├── controller/               # Servlet 控制器
    │   ├── filter/AuthFilter.java   # 登录与角色权限拦截
    │   └── util/                    # DBUtil/PasswordUtil/ExcelUtil
    └── webapp/
        ├── login.jsp                     # 登录页（可直接访问）
        ├── css/style.css
        └── WEB-INF/jsp/                  # 业务视图（仅 Servlet 可转发，外部不可直接访问）
            ├── student/  (topicList.jsp, myApplication.jsp)
            ├── teacher/  (topicManage.jsp, reviewList.jsp)
            └── admin/    (accountManage.jsp, statistics.jsp, config.jsp)
```

## 三、部署步骤

### 1. 准备数据库
启动 MySQL 8.0，执行：
```bash
mysql -u root -p < sql/init.sql
```
该脚本会自动建库 `topic_system`、建表并插入初始数据。

### 2. 修改数据库连接（如需要）
编辑 `src/main/resources/db.properties`：
```
jdbc.url=jdbc:mysql://localhost:3306/topic_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
jdbc.username=root
jdbc.password=你的MySQL密码
```

### 3. 打包部署
```bash
mvn clean package
```
把生成的 `target/topic-system.war` 放到 Tomcat 的 `webapps/` 目录，启动 Tomcat。

或在 IDEA 中：Open 本目录为 Maven 工程，配置 Tomcat 服务器，部署 war  exploded  artifacts 后运行。

### 4. 访问
浏览器打开：`http://localhost:8080/topic-system/`，自动跳到登录页。

## 四、测试账号

| 角色 | 账号 | 密码 |
|---|---|---|
| 管理员 | admin | admin123 |
| 教师 | t001 | teacher123 |
| 教师 | t002 | teacher123 |
| 学生 | s2023001 | student123 |
| 学生 | s2023002 | student123 |
| 学生 | s2023003 | student123 |

## 五、功能对照

| 需求编号 | 功能 | 实现位置 |
|---|---|---|
| Fun_01 | 登录/登出/角色跳转 | LoginServlet / LogoutServlet / AuthFilter |
| Fun_02 | 学生课题浏览+搜索 | StudentServlet#topics / topicList.jsp |
| Fun_03 | 提交/撤销选题申请（三重校验） | ApplicationService#apply / cancel |
| Fun_04 | 教师发布/编辑/撤销课题 | TopicService#publish / edit / revoke |
| Fun_05 | 教师审核（同意扣名额/驳回填理由） | ApplicationService#review |
| Fun_06 | 管理员账号增删启停 | AccountService / accountManage.jsp |
| Fun_07 | 选题统计 + Excel 导出 | StatisticsService / ExcelUtil / AdminServlet#export |
| Fun_08 | 选题开放时间配置 | ConfigService / config.jsp |

## 六、核心业务规则

1. **学生提交申请**依次校验：选题时间在开放窗口 → 学生无待审核申请 → 学生无已通过申请 → 课题已发布 → 课题剩余名额 > 0。
2. **教师同意申请**：同时把申请状态改为 approved 并扣减课题剩余名额（`UPDATE ... WHERE remaining_quota > 0` 防超卖）。
3. **教师驳回申请**：必须填写驳回理由，学生端可见。
4. **教师撤销课题**：课题状态置为 revoked，该课题下所有 pending 申请自动置为 rejected（理由：课题被撤销）。
5. **学生撤销申请**：仅 pending 状态可撤，不占名额故无需恢复。
6. **管理员禁用账号**：仅置 status=0，不删除历史数据；登录时拦截。
7. **密码**：SHA-256 哈希后入库，明文不落盘。
