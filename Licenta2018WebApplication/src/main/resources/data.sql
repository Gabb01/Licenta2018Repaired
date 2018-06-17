INSERT INTO request_type (id, description)
VALUES
(1, 'Autorizatie constructie'),
(2, 'Autorizatie mediu'),
(3, 'Autorizare deschidere spatiu');

INSERT INTO authority (id, role)
VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_CONSTRUCTION_AUTHORIZE'),
(3, 'ROLE_ENVIRONMENT_AUTHORIZE'),
(4, 'ROLE_INFRASTRUCTURE'),
(5, 'ROLE_SECRETARY');

INSERT INTO `user`(id, email, name, password, username, authority_id)
VALUES
(1, 'rata.gabriel@ac.tuiasi.ro','Rata Gabriel', 'pass', 'Gabi', 1),
(2, 'rata.gabriel2@ac.tuiasi.ro','Popescu Ion', 'pass', 'Ion', 4),
(3, 'rata.gabriel3@ac.tuiasi.ro','Ana Maria', 'pass', 'Ana', 3),
(4, 'rata.gabrie4l@ac.tuiasi.ro','Andrei Popescu', 'pass', 'Andrei', 2),
(5, 'rata.gabri5el@ac.tuiasi.ro','Ilinca Test', 'pass', 'Ilinca', 5);