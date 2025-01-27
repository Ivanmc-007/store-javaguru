create table t_user_role (
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id)
);

alter table t_user_role
add constraint fk_user_id foreign key (user_id) references public.t_user(user_id);

alter table t_user_role
add constraint fk_role_id foreign key (role_id) references public.role(role_id);
