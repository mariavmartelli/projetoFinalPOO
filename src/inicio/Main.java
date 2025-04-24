package inicio;

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
        try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Informe o nome do arquivo de entrada: ");
			String entrada = sc.nextLine();
			System.out.print("Informe o nome do arquivo de saída: ");
			String saida = sc.nextLine();

			Set<String> cpfsUsados = new HashSet<>();
			List<Funcionario> funcionarios = new ArrayList<>();
			List<String> rejeitados = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

			try {
			    List<String[]> linhas = CsvUtil.lerArquivo(entrada);
			    Funcionario funcionario = null;

			    for (String[] linha : linhas) {
			        if (linha.length == 1 && linha[0].isEmpty()) {
			            if (funcionario != null) {
			                funcionarios.add(funcionario);
			                funcionario = null;
			            }
			            continue;
			        }

			        try {
			            if (linha.length == 4 && funcionario == null) {
			                String nome = linha[0];
			                String cpf = linha[1];
			                LocalDate nasc = LocalDate.parse(linha[2], formatter);
			                double salario = Double.parseDouble(linha[3]);

			                if (!cpfsUsados.add(cpf)) {
			                    throw new CpfDuplicadoException("CPF duplicado: " + cpf);
			                }

			                funcionario = new Funcionario(nome, cpf, nasc, salario);

			            } else if (linha.length == 4) {
			                String nome = linha[0];
			                String cpf = linha[1];
			                LocalDate nasc = LocalDate.parse(linha[2], formatter);
			                Parentesco parentesco = Parentesco.valueOf(linha[3]);

			                if (!cpfsUsados.add(cpf)) {
			                    throw new CpfDuplicadoException("CPF duplicado: " + cpf);
			                }

			                if (!nasc.isAfter(LocalDate.now().minusYears(18))) {
			                    throw new DependenteException("Dependente maior de idade: " + nome);
			                }

			                Dependente d = new Dependente(nome, cpf, nasc, parentesco);
			                funcionario.adicionarDependente(d);
			            }
			        } catch (Exception e) {
			            rejeitados.add(Arrays.toString(linha) + ";" + e.getMessage());
			        }
			    }

			    if (funcionario != null) {
			        funcionarios.add(funcionario);
			    }

			    List<String> saidaFinal = new ArrayList<>();
			    FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			    DependenteDAO dependenteDAO = new DependenteDAO();
			    FolhaPagamentoDAO folhaDAO = new FolhaPagamentoDAO();

			    for (Funcionario f : funcionarios) {
			        f.calcularSalarioLiquido();
			        saidaFinal.add(f.toString());

			        try {
			            funcionarioDAO.inserir(f);
			            for (Dependente d : f.getDependentes()) {
			                dependenteDAO.inserir(d, f.getCpf());
			            }
			            folhaDAO.inserir(f, LocalDate.now());
			        } catch (Exception e) {
			            rejeitados.add(f.getCpf() + ";Erro ao inserir no banco: " + e.getMessage());
			        }
			    }

			    CsvUtil.escreverArquivo(saida, saidaFinal);
			    if (!rejeitados.isEmpty()) {
			        CsvUtil.escreverArquivo("rejeitados.csv", rejeitados);
			    }

			    System.out.println("Processamento concluído.");
			} catch (IOException e) {
			    System.out.println("Erro ao ler arquivo: " + e.getMessage());
			}
		}
    }
}

