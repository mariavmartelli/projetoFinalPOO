package inicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Pessoa implements Calculavel {
    private double salarioBruto;
    private double descontoINSS;
    private double descontoIR;
    private List<Dependente> dependentes;

    public Funcionario(String nome, String cpf, LocalDate dataNascimento, double salarioBruto) {
        super(nome, cpf, dataNascimento);
        this.salarioBruto = salarioBruto;
        this.dependentes = new ArrayList<>();
    }

    public void adicionarDependente(Dependente d) {
        dependentes.add(d);
    }

    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public double getDescontoINSS() {
        return descontoINSS;
    }

    public double getDescontoIR() {
        return descontoIR;
    }

    @Override
    public double calcularSalarioLiquido() {
        double inss = salarioBruto > 8157.41 ? 8157.41 * 0.14 : salarioBruto * 0.14;
        this.descontoINSS = inss;

        double valorDependentes = dependentes.size() * 189.59;
        double baseIR = salarioBruto - inss - valorDependentes;

        double aliq = 0.0;
        double deducao = 0.0;

        if (baseIR <= 2259.00) {
            aliq = 0.0; deducao = 0.0;
        } else if (baseIR <= 2826.65) {
            aliq = 0.075; deducao = 169.44;
        } else if (baseIR <= 3751.05) {
            aliq = 0.15; deducao = 381.44;
        } else if (baseIR <= 4664.68) {
            aliq = 0.225; deducao = 662.77;
        } else {
            aliq = 0.275; deducao = 896.00;
        }

        double ir = (baseIR * aliq) - deducao;
        this.descontoIR = ir;

        return salarioBruto - inss - ir;
    }

    @Override
    public String toString() {
        return nome + ";" + cpf + ";" +
               String.format("%.2f", descontoINSS) + ";" +
               String.format("%.2f", descontoIR) + ";" +
               String.format("%.2f", calcularSalarioLiquido());
    }
}
