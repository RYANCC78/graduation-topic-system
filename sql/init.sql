DROP DATABASE IF EXISTS topic_system;
CREATE DATABASE topic_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE topic_system;

CREATE TABLE user (
    user_id    INT PRIMARY KEY AUTO_INCREMENT,
    account    VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    name       VARCHAR(50)  NOT NULL,
    phone      VARCHAR(20),
    email      VARCHAR(50),
    status     TINYINT      NOT NULL DEFAULT 1,
    create_time DATETIME    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE topic (
    topic_id         INT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(100) NOT NULL,
    description      TEXT,
    required_ability VARCHAR(200),
    quota            INT          NOT NULL,
    remaining_quota  INT          NOT NULL,
    teacher_id       INT          NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    create_time      DATETIME     NOT NULL,
    CONSTRAINT fk_topic_teacher FOREIGN KEY (teacher_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE application (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id     INT        NOT NULL,
    topic_id       INT        NOT NULL,
    status         VARCHAR(20) NOT NULL,
    reject_reason  VARCHAR(200),
    apply_time     DATETIME   NOT NULL,
    review_time    DATETIME,
    CONSTRAINT fk_app_student FOREIGN KEY (student_id) REFERENCES user(user_id),
    CONSTRAINT fk_app_topic   FOREIGN KEY (topic_id)   REFERENCES topic(topic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE system_config (
    config_id   INT PRIMARY KEY AUTO_INCREMENT,
    config_key  VARCHAR(50)  NOT NULL UNIQUE,
    config_value VARCHAR(200) NOT NULL,
    description VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO user(account,password,role,name,phone,email,status,create_time) VALUES
('admin',  '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9','admin',   '系统管理员','13800000000','admin@buptsse.edu.cn',1,NOW()),
('t001',   'cde383eee8ee7a4400adf7a15f716f179a2eb97646b37e089eb8d6d04e663416','teacher', '张老师',    '13800000001','zhang@buptsse.edu.cn',1,NOW()),
('t002',   'cde383eee8ee7a4400adf7a15f716f179a2eb97646b37e089eb8d6d04e663416','teacher', '李老师',    '13800000002','li@buptsse.edu.cn',   1,NOW()),
('s2023001','703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b','student','雷彩宸',    '13900000001','lei@buptsse.edu.cn',  1,NOW()),
('s2023002','703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b','student','张肃金',    '13900000002','zhang2@buptsse.edu.cn',1,NOW()),
('s2023003','703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b','student','罗洪',      '13900000003','luo@buptsse.edu.cn',  1,NOW());

INSERT INTO topic(title,description,required_ability,quota,remaining_quota,teacher_id,status,create_time) VALUES
('基于深度学习的图像语义分割研究','面向医学影像与街景图像的U-Net变体改进研究，需实现数据集训练、评估与对比实验。','熟悉Python、PyTorch、OpenCV',3,3,2,'published',NOW()),
('Web系统性能测试与自动化回归平台设计','基于JMeter与Selenium构建接口/UI一体化性能回归平台，输出可视化报告。','熟悉Java、JMeter、Selenium',2,2,2,'published',NOW()),
('高校实验室设备借用管理系统设计与实现','B/S架构，支持设备预约、审批、归还、逾期提醒全流程。','熟悉Java Web、MySQL',2,2,3,'published',NOW()),
('基于图神经网络的社交推荐算法研究','构建用户-物品异构图，对比GCN/GAT等模型在公开数据集上的推荐效果。','熟悉Python、TensorFlow',1,1,3,'published',NOW());

INSERT INTO system_config(config_key,config_value,description) VALUES
('selection_start_time','2026-10-01 00:00:00','选题开放开始时间'),
('selection_end_time','2026-12-31 23:59:59','选题开放结束时间');
