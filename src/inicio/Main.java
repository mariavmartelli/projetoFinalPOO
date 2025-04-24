package inicio;

import dao.Conexao;
import dao.FuncionarioDAO;
import dao.DependenteDAO;
import dao.FolhaPagamentoDAO;

import exception.CpfDuplicadoException;
import exception.DependenteException;
import util.CsvUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        System.out.print("Informe o nome do arquivo de entrada: ");
        String entrada = sc.nextLine();

        System.out.print("Informe o nome do arquivo de saída: ");
        String saida = sc.nextLine();

        Set<String> cpfsUsados = new HashSet<>();
        List<Funcionario> funcionarios = new ArrayList<>();
        List<String> rejeitados = new ArrayList<>();

        try {
            List<String[]> linhas = CsvUtil.lerArquivo(entrada);
            Funcionario f = null;

            for (String[] linha : linhas) {
                try {
                    if (linha.length == 1 && linha[0].isEmpty()) {
                        if (f != null) {
                            funcionarios.add(f);
                            f = null;
                        }
                        continue;
                    }

                    if (linha.length == 4 && f == null) {
                        String nome = linha[0];
                        String cpf = linha[1];
                        LocalDate nasc = LocalDate.parse(linha[2], formatter);
                        double salario = Double.parseDouble(linha[3]);

                        if (!cpfsUsados.add(cpf)) {
                            rejeitados.add(cpf + ";CPF duplicado de funcionário");
                            continue;
                        }

                        f = new Funcionario(nome, cpf, nasc, salario);

                    } else if (linha.length == 4 && f != null) {
                        String nome = linha[0];
                        String cpf = linha[1];
                        LocalDate nasc = LocalDate.parse(linha[2], formatter);
                        Parentesco p = Parentesco.valueOf(linha[3]);

                        if (!cpfsUsados.add(cpf)) {
                            rejeitados.add(cpf + ";CPF duplicado de dependente");
                            continue;
                        }

                        if (!nasc.isAfter(LocalDate.now().minusYears(18))) {
                            rejeitados.add(cpf + ";Dependente maior de idade");
                            continue;
                        }

                        f.adicionarDependente(new Dependente(nome, cpf, nasc, p));
                    }

                } catch (Exception e) {
                    rejeitados.add("Erro inesperado: " + Arrays.toString(linha) + " - " + e.getMessage());
                }
            }

            if (f != null) funcionarios.add(f);

            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            DependenteDAO dependenteDAO = new DependenteDAO();
            FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO();

            List<String> linhasSaida = new ArrayList<>();

            for (Funcionario func : funcionarios) {
                func.calcularSalarioLiquido();

                try {
                    funcionarioDAO.inserir(func);
                    for (Dependente d : func.getDependentes()) {
                        dependenteDAO.inserir(d, func.getCpf());
                    }
                    folhaDAO.inserir(func, LocalDate.now());

                    linhasSaida.add(func.toString());
                } catch (Exception e) {
                    rejeitados.add(func.getCpf() + ";Erro ao inserir no banco: " + e.getMessage());
                }
            }

            CsvUtil.escreverArquivo(saida, linhasSaida);
            System.out.println("Arquivo gerado com sucesso!");

            if (!rejeitados.isEmpty()) {
                CsvUtil.escreverArquivo("rejeitados.csv", rejeitados);
                System.out.println("Arquivo rejeitados.csv criado.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
