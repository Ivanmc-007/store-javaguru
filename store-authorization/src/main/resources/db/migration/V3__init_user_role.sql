insert into public.t_user(user_id, email, password)
values (1, 'admin@gmail.com', '$2y$12$qxNyKljADzjq86d3ayJYQuqCMPF6QDh9HtOFvoQ4ftw/AZlvNI5q2');

insert into public.role (role_id, name)
values (1, 'ROLE_ADMIN');
insert into public.role (role_id, name)
values (2, 'ROLE_USER');

insert into public.t_user_role (user_id, role_id)
values (1, 1);
insert into public.t_user_role (user_id, role_id)
values (1, 2);
