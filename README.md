# Projeto Final - Folha de Pagamento em Java

Sistema desenvolvido na disciplina de **Programação Orientada a Objetos (2025.1)**, com foco em cálculo de salário líquido de funcionários com base em INSS, IR e dependentes.

## ✨ Funcionalidades

- Leitura de dados a partir de um arquivo `.csv`
- Cálculo automático de:
  - INSS (14%)
  - Imposto de Renda (IR)
  - Abatimento por dependente (R$189,59)
- Geração de dois arquivos:
  - `saida.csv` com os dados calculados
  - `rejeitados.csv` com erros (CPF duplicado, dependente inválido)
- Persistência dos dados no banco de dados PostgreSQL

## ⚙️ Estrutura do Projeto

### Pacote `inicio`
- `Pessoa` (abstrata), `Funcionario`, `Dependente`, `Main`
- `Parentesco` (enum)
- `Calculavel` (interface)

### Pacote `dao`
- `FuncionarioDAO`, `DependenteDAO`, `FolhaPagamentoDAO`, `Conexao`

### Pacote `exception`
- `CpfDuplicadoException`, `DependenteException`

### Pacote `util`
- `LeitorCsv` (leitura/escrita de arquivos)

## 📄 Banco de Dados

- PostgreSQL com 3 tabelas: `funcionario`, `dependente`, `folha_pagamento`
- Script SQL disponível na pasta `/bancoDeDados`

## ▶️ Como Executar

1. Configure a conexão em `Conexao.java`
2. Execute o `Main.java`
3. Informe os nomes dos arquivos ao rodar
4. Verifique os resultados no `.csv` e no banco via pgAdmin

## ✅ Requisitos Atendidos

- Herança, classes abstratas, interfaces
- Exceções personalizadas
- Enum
- Manipulação de arquivos e coleções
- Conexão com banco de dados

---

## 👥 Integrantes

- Caroline Brand  
- João Vitor Amaral  
- Juliana Périco  
- Maria Vitória Martelli  
- Patrícia Sanches 