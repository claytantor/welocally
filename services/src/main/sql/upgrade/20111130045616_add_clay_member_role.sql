INSERT INTO `role`
(
`version`,
`role`,
`role_group`,
`principal_id`)
VALUES
(
0,
'ROLE_MEMBER',
'ROLE_MEMBER',
1
);

delete from publisher where publisher.id in (62,63,64,65,66,67,68,69,70,71,72,73,74);
