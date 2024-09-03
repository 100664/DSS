USE ESIdeal;
-- Povoamento de Exemplo

-- Povoamento da tabela clientes
INSERT INTO clientes (nif, nome, morada, contacto, valorvouchers)
VALUES
    ('123456789', 'Cliente1', 'Morada1', '912345678', 100),
    ('987654321', 'Cliente2', 'Morada2', '987654321', 50),
    ('111223344', 'Cliente3', 'Morada3', '998877665', 200);

-- Povoamento da tabela funcionarios
INSERT INTO funcionarios (nome, idade, username, password, isAdmin, emTurno, posto, inicio, fim, fazerServico, carro, servico)
VALUES
    ('Funcionario1', 30, 'func1', 'senha1', 1, 1, 1, '2022-01-01 08:00:00', '2022-01-01 17:00:00', 0,NULL, NULL),
    ('Funcionario2', 25, 'func2', 'senha2', 0, 0, NULL, NULL, NULL, 0, NULL, NULL),
    ('Funcionario3', 35, 'func3', 'senha3', 0, 1, 2, '2022-01-01 09:00:00', '2022-01-01 18:00:00', 0,NULL, NULL),
    ('Funcionario4', 28, 'func4', 'senha4', 0, 0, NULL, NULL, NULL, 0, NULL, NULL),
    ('Funcionario5', 40, 'func5', 'senha5', 1, 1, 3, '2022-01-01 07:00:00', '2022-01-01 16:00:00',0,NULL, NULL),
    ('Funcionario6', 23,'func6', 'senha6', 0,1, 3, '2022-01-01 07:00:00', '2022-01-01 16:00:00', 0,NULL, NULL);

-- Povoamento da tabela postoTrabalho
INSERT INTO postoTrabalho (id) VALUES (1), (2), (3), (4), (5), (6);

-- Povoamento da tabela posto_servico
INSERT INTO posto_servico (posto_id, servico_id) VALUES
                                                     (1, 1),
                                                     (2, 2),
                                                     (2, 3),
                                                     (2, 4),
                                                     (2, 5),
                                                     (3, 6),
                                                     (3, 7),
                                                     (3, 8),
                                                     (3, 9),
                                                     (3, 10),
                                                     (3, 11),
                                                     (4, 1),
                                                     (5, 2),
                                                     (5, 3),
                                                     (5, 4),
                                                     (5, 5),
                                                     (6, 6),
                                                     (6, 7),
                                                     (6, 8),
                                                     (6, 9),
                                                     (6, 10),
                                                     (6, 11);


-- Povoamento da tabela funcionario_serviço
INSERT INTO funcionario_servico (funcionario_username, servico_id)
VALUES
    ('func1', 1),
    ('func1', 2),
    ('func1', 3),
    ('func2', 4),
    ('func2', 5),
    ('func2', 6),
    ('func3', 7),
    ('func3', 8),
    ('func3', 9),
    ('func3', 10),
    ('func3', 11),
    ('func4', 1),
    ('func4', 2),
    ('func4', 3),
    ('func5', 4),
    ('func5', 5),
    ('func5', 6),
    ('func6', 7),
    ('func6', 8),
    ('func6', 9),
    ('func6', 10),
    ('func6', 11);
-- Povoamento da tabela veiculos
INSERT INTO veiculos (type, matricula, marca, modelo, quilometragem, ano, data_revisao, nifcliente)
VALUES
    (1, 'ABC123', 'Marca1', 'Modelo1', 50000, 2019, '2022-01-01 10:00:00', '123456789'),
    (2, 'DEF456', 'Marca2', 'Modelo2', 30000, 2020, '2022-01-01 12:00:00', '111223344'),
    (1, 'XYZ789', 'Marca3', 'Modelo3', 70000, 2018, '2022-01-01 09:30:00', '987654321');

-- Povoamento da tabela servicos
INSERT INTO servicos (id, nome, duracao_media, preco)
VALUES
    (1, 'CHECKUP', 120, 20.00),
    (2, 'Trocar Pneus', 45, 80.00),
    (3, 'Reparação Motor', 120, 150.00),
    (4, 'Alinhamento Direção', 60, 90.00),
    (5, 'Verificação Travões', 40, 60.00),
    (6, 'Substituição Bateria', 50, 70.00),
    (7, 'Limpeza Filtros', 35, 55.00),
    (8, 'Reparação Suspensão', 90, 120.00),
    (9, 'Teste Bateria', 180, 200.00),
    (10, 'Substituição Correias', 75, 100.00),
    (11, 'Mudar Oleo', 30, 20.00);

INSERT INTO servicos_type (id, type)
VALUES
    (1,1),
    (1,2),
    (1,3),
    (1,4),
    (1,5),
    (2,1),
    (2,2),
    (2,3),
    (2,4),
    (2,5),
    (3,1),
    (3,2),
    (3,3),
    (3,4),
    (3,5),
    (4,1),
    (4,2),
    (4,3),
    (4,4),
    (4,5),
    (5,1),
    (5,2),
    (5,3),
    (5,4),
    (5,5),
    (6,3),
    (6,4),
    (6,5),
    (7,1),
    (7,2),
    (7,4),
    (7,5),
    (8,1),
    (8,2),
    (8,3),
    (8,4),
    (8,5),
    (9,3),
    (9,4),
    (9,5),
    (10,1),
    (10,2),
    (10,4),
    (10,5),
    (11,1),
    (11,2),
    (11,4),
    (11,5);




-- Povoamento da tabela workshops
INSERT INTO workshops (id, nome, localidade, inicio, fim)
VALUES
    (1, 'Workshop1', 'Gualtar', '2022-01-10 08:00:00', '2022-01-10 17:00:00'),
    (2, 'Workshop2', 'Guimaraes', '2022-01-15 09:00:00', '2022-01-15 18:00:00');

-- Povoamento da tabela workshops_funcionarios
INSERT INTO workshops_funcionarios (workshop_id, funcionario_id) VALUES
                                                                     (1, 'func1'), (1, 'func2'), (1, 'func3'),
                                                                     (2, 'func4'), (2, 'func5'), (2, 'func6');

-- Povoamento da tabela workshops_clientes
INSERT INTO workshops_clientes (workshop_id, cliente_nif) VALUES
                                                              (1, '123456789'), (1, '987654321'),
                                                              (2, '111223344');

-- Povoamento da tabela workshops_postos
INSERT INTO workshops_postos (workshop_id, posto_id) VALUES
                                                         (1, 1), (1, 2), (1, 3),
                                                         (2, 4), (2, 5), (2, 6);