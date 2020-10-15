create table auth_role
(
    id              bigint auto_increment       comment '标识，主键'     primary key,
    name            varchar(100) default ''     not null comment '角色名',
    description     varchar(500) default ''     null comment '说明',
    create_time     datetime                    null comment '创建时间',
    create_user_id  bigint,
    update_time     datetime                    null comment '修改时间',
    update_user_id  bigint,
    del_flag  bit   default b'0' not null comment '是否已删除'
) comment '角色表';

create index del_flag
    on auth_role (id, del_flag);


INSERT INTO auth_role
(name, description, create_time, create_user_id, update_time, update_user_id, del_flag)
VALUES
('admin', '系统管理员', null, null, null, null, 0);
