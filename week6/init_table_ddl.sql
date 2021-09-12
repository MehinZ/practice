-- 用户表
create table zhmtest.tbl_user
(
    account_id      bigint auto_increment comment '用户ID'
        primary key,
    user_name       varchar(60)  not null comment '用户名',
    password        varchar(256) not null comment '用户加密密码',
    alias_user_name varchar(60)  not null comment '用户别名账号（预留字段）',
    email           varchar(100) null comment '用户电子邮箱',
    phone_num       varchar(20)  null comment '用户手机号码'
);

create index tbl_user_alias_user_name_index
    on zhmtest.tbl_user (alias_user_name);

create index tbl_user_email_index
    on zhmtest.tbl_user (email);

create index tbl_user_phone_num_index
    on zhmtest.tbl_user (phone_num);

create index tbl_user_user_name_index
    on zhmtest.tbl_user (user_name);


-- 订单表
create table zhmtest.tbl_order
(
    order_id      varchar(50) collate utf8_bin default '' not null comment '订单id'
        primary key,
    payment       varchar(50) collate utf8_bin            null comment '实付金额',
    payment_type  int(2)                                  null comment '支付类型',
    post_fee      varchar(50) collate utf8_bin            null comment '运费',
    status        int(10)                                 null comment '订单状态',
    create_time   datetime                                null comment '订单创建时间',
    update_time   datetime                                null comment '订单更新时间',
    payment_time  datetime                                null comment '付款时间',
    consign_time  datetime                                null comment '发货时间',
    finished_time datetime                                null comment '交易完成时间',
    closed_time   datetime                                null comment '交易关闭时间',
    shipping_name varchar(20) collate utf8_bin            null comment '物流名称',
    shipping_code varchar(20) collate utf8_bin            null comment '物流单号',
    user_id       bigint                                  null comment '用户id'
) charset = utf8mb4;

create index create_time
    on zhmtest.tbl_order (create_time);

create index payment_type
    on zhmtest.tbl_order (payment_type);

create index status
    on zhmtest.tbl_order (status);

-- 商品表
create table zhmtest.tbl_product
(
    product_id bigint            not null comment '商品id',
    title      varchar(100)      not null comment '商品标题',
    brief      varchar(500)      null comment '商品简介',
    price      bigint            not null comment '商品价格',
    inventory  int(10)           not null comment '库存数量',
    image      blob              null comment '商品图片',
    status     tinyint default 1 not null comment '商品状态',
    created    datetime          not null comment '创建时间',
    updated    datetime          not null comment '更新时间'
)
    charset = utf8mb4;

create index status
    on zhmtest.tbl_product (status);

create fulltext index title
    on zhmtest.tbl_product (title);

create index updated
    on zhmtest.tbl_product (updated);