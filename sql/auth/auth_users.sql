create table auth_user
(
    id              bigint auto_increment       comment '标识，主键'     primary key,
    account         varchar(255) default ''     not null comment '账号',
    password        varchar(255) default ''     not null comment '密码',
    name            varchar(100) default ''     not null comment '姓名',
    phone           varchar(100) default ''     not null comment '电话',
    email           varchar(255) default ''     not null comment '电子邮箱',
    alipay_account  varchar(500) default ''     not null comment '支付宝账号',
    wechat_account  varchar(500) default ''     not null comment '微信账号',
    state           int          default 1      not null comment '账号状态',
    user_type       int          default 1      not null comment '用户类型',
    create_time     datetime                    null comment '创建时间',
    create_user_id  bigint,
    update_time     datetime                    null comment '修改时间',
    update_user_id  bigint,
    del_flag  bit   default b'0' not null comment '是否已删除'
) comment '用户表';

create index del_flag
    on auth_user (id, del_flag);


INSERT INTO auth_user
(account, password, name, phone, email, alipay_account, wechat_account, state, user_type, create_time, create_user_id, update_time,
update_user_id, del_flag)
VALUES
('admin', '82ug05b311fs6kb56afa3vqsbr5tnfnf', '系统管理员', '', '', '', '', 0, null, null, null, null, 0);

