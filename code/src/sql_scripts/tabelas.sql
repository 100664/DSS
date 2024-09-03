CREATE DATABASE IF NOT EXISTS ESIdeal;
USE ESIdeal;

CREATE USER IF NOT EXISTS 'grupo48'@'localhost' IDENTIFIED BY 'grupo48-dss';
GRANT ALL PRIVILEGES ON * . * TO 'grupo48'@'localhost';
FLUSH PRIVILEGES;

SELECT * FROM clientes;
SELECT * FROM funcionarios;
SELECT * FROM veiculos;
SELECT * FROM servicos;
SELECT * FROM workshops;

CREATE TABLE IF NOT EXISTS clientes ( /**/
    nif VARCHAR(9) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    morada VARCHAR(150) NOT NULL,
    contacto VARCHAR(12) NOT NULL,
    valorvouchers INT NOT NULL,
    PRIMARY KEY (nif)
);

CREATE TABLE IF NOT EXISTS funcionarios ( /**/
    nome VARCHAR(150) NOT NULL,
    idade INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    isAdmin INT NOT NULL,
    emTurno INT NOT NULL,
    posto INT,
    inicio DATETIME,
    fim DATETIME,
    fazerServico INT NOT NULL ,
    carro VARCHAR(8),
    servico INT,
    FOREIGN KEY (carro) REFERENCES veiculos (matricula),
    FOREIGN KEY (servico) REFERENCES servicos (id),
    FOREIGN KEY (posto) REFERENCES postoTrabalho(id),
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS postoTrabalho ( /**/
     id INT NOT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS posto_servico ( /**/
    posto_id INT NOT NULL,
    servico_id INT NOT NULL,
    PRIMARY KEY (posto_id, servico_id),
    FOREIGN KEY (posto_id) REFERENCES postoTrabalho(id),
    FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

CREATE TABLE IF NOT EXISTS posto_veiculo ( /**/
    posto_id INT NOT NULL,
    veiculo_id VARCHAR(8) NOT NULL,
    PRIMARY KEY (posto_id, veiculo_id),
    FOREIGN KEY (posto_id) REFERENCES postoTrabalho(id),
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(matricula)
);

CREATE TABLE IF NOT EXISTS funcionario_servico ( /**/
    funcionario_username VARCHAR(50) NOT NULL,
    servico_id INT NOT NULL,
    PRIMARY KEY (funcionario_username, servico_id),
    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username),
    FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

CREATE TABLE IF NOT EXISTS funcionario_postoTrabalho ( /**/
    funcionario_username VARCHAR(50) NOT NULL,
    posto_id INT NOT NULL,
    inicio DATETIME NOT NULL,
    fim DATETIME NOT NULL,
    PRIMARY KEY (funcionario_username, posto_id),
    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username),
    FOREIGN KEY (posto_id) REFERENCES servicos(id)
);

CREATE TABLE IF NOT EXISTS veiculos ( /**/
    type INT NOT NULL,
    matricula VARCHAR(8) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    quilometragem INT NOT NULL,
    ano INT NOT NULL,
    data_revisao DATETIME NOT NULL,
    nifcliente VARCHAR(9),
    FOREIGN KEY (nifcliente) REFERENCES clientes (nif),
    PRIMARY KEY (matricula)
);

CREATE TABLE IF NOT EXISTS veiculos_servicos ( /**/
    veiculo_matricula VARCHAR(8) NOT NULL,
    servico_id INT NOT NULL,
    PRIMARY KEY (veiculo_matricula,servico_id),
    FOREIGN KEY (veiculo_matricula) REFERENCES veiculos(matricula),
    FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

CREATE TABLE IF NOT EXISTS servicosPrestados ( /**/
    id INT NOT NULL AUTO_INCREMENT,
    funcionario_username VARCHAR(50) NOT NULL,
    servico_id INT NOT NULL,
    inicio DATETIME NOT NULL,
    fim DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username),
    FOREIGN KEY (servico_id) REFERENCES servicos (id)
);

CREATE TABLE IF NOT EXISTS veiculos_servicosPrestados ( /**/
    veiculo_matricula VARCHAR(8) NOT NULL,
    servicosPrestados_id INT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (veiculo_matricula, servicosPrestados_id),
    FOREIGN KEY (veiculo_matricula) REFERENCES veiculos(matricula),
    FOREIGN KEY (servicosPrestados_id) REFERENCES servicosPrestados(id)

);

CREATE TABLE IF NOT EXISTS servicos_type ( /**/
    id INT NOT NULL,
    type INT NOT NULL,
    PRIMARY KEY (id, type),
    FOREIGN KEY (id) REFERENCES servicos (id)
);

CREATE TABLE IF NOT EXISTS servicos ( /**/
    id INT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    duracao_media INT NOT NULL,
    preco DECIMAL(8,2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS workshops_clientes ( /**/
    workshop_id INT NOT NULL,
    cliente_nif VARCHAR(9) NOT NULL,
    PRIMARY KEY (workshop_id, cliente_nif),
    FOREIGN KEY (workshop_id) REFERENCES workshops (id),
    FOREIGN KEY (cliente_nif) REFERENCES clientes (nif)
);

CREATE TABLE IF NOT EXISTS workshops_funcionarios ( /**/
     workshop_id INT NOT NULL,
     funcionario_id VARCHAR(50) NOT NULL,
     PRIMARY KEY (workshop_id, funcionario_id),
     FOREIGN KEY (workshop_id) REFERENCES workshops (id),
     FOREIGN KEY (funcionario_id) REFERENCES funcionarios (username)
);

CREATE TABLE IF NOT EXISTS workshops_postos ( /**/
     workshop_id INT NOT NULL,
     posto_id INT NOT NULL,
     PRIMARY KEY (workshop_id, posto_id),
     FOREIGN KEY (workshop_id) REFERENCES workshops (id),
     FOREIGN KEY (posto_id) REFERENCES postoTrabalho (id)
);


CREATE TABLE IF NOT EXISTS workshops ( /**/
    id INT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    localidade VARCHAR(50) NOT NULL,
    inicio DATETIME NOT NULL,
    fim DATETIME NOT NULL,
    PRIMARY KEY (id)
);
