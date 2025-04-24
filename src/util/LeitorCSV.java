package util;

import exception.CpfDuplicadoException;
import exception.DependenteException;
import inicio.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LeitorCSV {
    public static List<Funcionario> carregarFuncionarios(String caminhoArquivo) throws IOException, DependenteException, CpfDuplicadoException {
        List<Funcionario> funcionarios = new ArrayList<>();
        Set<String> cpfsUsados = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
        String linha;
        Funcionario funcionario = null;

        while ((linha = br.readLine()) != null) {
            if (linha.trim().isEmpty()) {
                if (funcionario != null) {
                    funcionarios.add(funcionario);
                    funcionario = null;
                }
                continue;
            }

            String[] campos = linha.split(";");

            if (campos.length == 4 && funcionario == null) {
                // Leitura de funcionário
                String nome = campos[0];
                String cpf = campos[1];
                LocalDate dataNasc = LocalDate.parse(campos[2], formatter);
                double salario = Double.parseDouble(campos[3]);

                if (!cpfsUsados.add(cpf)) {
                    throw new CpfDuplicadoException("CPF de funcionário duplicado: " + cpf);
                }

                funcionario = new Funcionario(nome, cpf, dataNasc, salario);

            } else if (campos.length == 4) {
                // Leitura de dependente
                String nome = campos[0];
                String cpf = campos[1];
                LocalDate dataNasc = LocalDate.parse(campos[2], formatter);
                Parentesco parentesco = Parentesco.valueOf(campos[3]);

                if (!cpfsUsados.add(cpf)) {
                    throw new CpfDuplicadoException("CPF de dependente duplicado: " + cpf);
                }

                if (!dataNasc.isAfter(LocalDate.now().minusYears(18))) {
                    throw new DependenteException("Dependente maior de idade: " + nome);
                }

                Dependente d = new Dependente(nome, cpf, dataNasc, parentesco);
                funcionario.adicionarDependente(d);
            }
        }

        if (funcionario != null) {
            funcionarios.add(funcionario);
        }

        br.close();
        return funcionarios;
    }
}
