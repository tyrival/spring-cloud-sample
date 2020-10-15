create table auth_user_role
(
    id              bigint auto_increment       comment '标识，主键'     primary key,
    user_id         bigint     not null comment '用户id',
    role_id         bigint     not null comment '角色id',
    create_time     datetime   null comment '创建时间',
    create_user_id  bigint,
    update_time     datetime   null comment '修改时间',
    update_user_id  bigint,
    del_flag  bit   default b'0' not null comment '是否已删除'
) comment '用户-角色关系表';

create index del_flag_role_id
    on auth_user_role (id, del_flag, role_id);

create index del_flag_user_id
    on auth_user_role (id, del_flag, user_id);

INSERT INTO auth_user_role
(user_id, role_id, create_time, create_user_id, update_time, update_user_id, del_flag)
VALUES
('1', '1', null, null, null, null, 0);
