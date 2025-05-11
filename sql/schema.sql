CREATE DATABASE IF NOT EXISTS ouvidoria;
USE ouvidoria;

CREATE TABLE IF NOT EXISTS reclamacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(20),
    tipo VARCHAR(30),    
    cpf VARCHAR(11), 
    email VARCHAR(30),
    descricao text,
    criado_em timestamp
);
