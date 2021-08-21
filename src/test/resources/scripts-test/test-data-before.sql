-- insert into users (id, email, name, password, phone) values (1, 'yayota1045@naymio.com', 'Pol', '$2a$10$YUVtwcUqbTp5jNxpgwFAJ.Pnm2428efPTXIFCyKAqTVRiD9Mwj4RC', '1231231231');

insert into users (id, hash, reg_time, balance, name) values (5005, 'e4e09ed6ee8ca9c5cc773081f7ef0736781d1e9805cb13a3f436a6a1d7bf051d', '2020-10-17 14:53:44', 9632, 'Rob Coast');

insert into user_contact (id, user_id, type, approved, code, code_trials, code_time, contact) values (425, 5005, 'EMAIL', 1, '371 997', 0, '2021-09-24 16:37:54', 'bogoke1616@eyeremind.com');
insert into user_contact (id, user_id, type, approved, code, code_trials, code_time, contact) values (426, 5005, 'PHONE', 1, '371 997', 0, '2021-09-26 16:37:54', '+7 (956) 545-85-69');
