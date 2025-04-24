CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    salario_bruto NUMERIC(10,2) NOT NULL
);


CREATE TABLE dependente (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    parentesco TEXT NOT NULL,
    id_funcionario INTEGER NOT NULL REFERENCES funcionario(id)
);

CREATE TABLE folha_pagamento (
    id SERIAL PRIMARY KEY,
    id_funcionario INTEGER NOT NULL REFERENCES funcionario(id),
    data_pagamento DATE NOT NULL,
    desconto_inss NUMERIC(10,2) NOT NULL,
    desconto_ir NUMERIC(10,2) NOT NULL,
    salario_liquido NUMERIC(10,2) NOT NULL
);

SELECT * FROM funcionario;
SELECT * FROM dependente;
SELECT * FROM folha_pagamento;
