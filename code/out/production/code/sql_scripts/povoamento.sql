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
    ('Funcionario1', 30, 'func1', 'senha1', 1, 1, 1, '2022-01-01 08:00:00', '2022-01-01 17:00:00', 1, 'ABC123', 1),
    ('Funcionario2', 25, 'func2', 'senha2', 0, 0, NULL, NULL, NULL, 0, NULL, NULL),
    ('Funcionario3', 35, 'func3', 'senha3', 0, 1, 2, '2022-01-01 09:00:00', '2022-01-01 18:00:00', 1, 'XYZ789', 3),
    ('Funcionario4', 28, 'func4', 'senha4', 0, 0, NULL, NULL, NULL, 0, NULL, NULL),
    ('Funcionario5', 40, 'func5', 'senha5', 1, 1, 3, '2022-01-01 07:00:00', '2022-01-01 16:00:00', 1, 'DEF456', 2);

-- Povoamento da tabela postoTrabalho
INSERT INTO postoTrabalho (id) VALUES (1), (2), (3);

-- Povoamento da tabela posto_servico
INSERT INTO posto_servico (posto_id, servico_id) VALUES (1, 1), (2, 2), (3, 3);

-- Povoamento da tabela veiculos
INSERT INTO veiculos (type, matricula, marca, modelo, quilometragem, ano, data_revisao, nifcliente)
VALUES
    (1, 'ABC123', 'Marca1', 'Modelo1', 50000, 2019, '2022-01-01 10:00:00', '123456789'),
    (2, 'DEF456', 'Marca2', 'Modelo2', 30000, 2020, '2022-01-01 12:00:00', '111223344'),
    (1, 'XYZ789', 'Marca3', 'Modelo3', 70000, 2018, '2022-01-01 09:30:00', '987654321');

-- Povoamento da tabela veiculos_servicos
INSERT INTO veiculos_servicos (veiculo_matricula, servico_id) VALUES ('ABC123', 1), ('DEF456', 2), ('XYZ789', 3);

-- Povoamento da tabela servicos
INSERT INTO servicos (id, nome, duracao_media, preco)
VALUES
    (1, 'Mudar Óleo', 30, 50.00),
    (2, 'Trocar Pneus', 45, 80.00),
    (3, 'Reparação Motor', 120, 150.00),
    (4, 'Alinhamento Direção', 60, 90.00),
    (5, 'Verificação Travões', 40, 60.00),
    (6, 'Substituição Bateria', 50, 70.00),
    (7, 'Limpeza Filtros', 35, 55.00),
    (8, 'Reparação Suspensão', 90, 120.00),
    (9, 'Inspeção Geral', 180, 200.00),
    (10, 'Substituição Correias', 75, 100.00);

-- Povoamento da tabela workshops
INSERT INTO workshops (id, nome, localidade, inicio, fim)
VALUES
    (1, 'Workshop1', 'Localidade1', '2022-01-10 08:00:00', '2022-01-10 17:00:00'),
    (2, 'Workshop2', 'Localidade2', '2022-01-15 09:00:00', '2022-01-15 18:00:00');

-- Povoamento da tabela workshops_funcionarios
INSERT INTO workshops_funcionarios (workshop_id, funcionario_id) VALUES
                                                                     (1, 'func1'), (1, 'func2'), (1, 'func3'), (1, 'func4'), (1, 'func5'),
                                                                     (2, 'func1'), (2, 'func2'), (2, 'func3'), (2, 'func4'), (2, 'func5');

-- Povoamento da tabela workshops_clientes
INSERT INTO workshops_clientes (workshop_id, cliente_nif) VALUES
                                                              (1, '123456789'), (1, '987654321'), (1, '111223344'),
                                                              (2, '123456789'), (2, '987654321'), (2, '111223344');

-- Povoamento da tabela workshops_postos
INSERT INTO workshops_postos (workshop_id, posto_id) VALUES
                                                         (1, 1), (1, 2), (1, 3),
                                                         (2, 1), (2, 2), (2, 3);