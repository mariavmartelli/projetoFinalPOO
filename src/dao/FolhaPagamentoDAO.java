
package dao;

import inicio.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class FolhaPagamentoDAO {
    public void inserir(Funcionario f, LocalDate data) throws SQLException {
        String sql = "INSERT INTO folha_pagamento(id_funcionario, data_pagamento, desconto_inss, desconto_ir, salario_liquido) " +
                     "VALUES ((SELECT id FROM funcionario WHERE cpf = ?), ?, ?, ?, ?)";
        Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, f.getCpf());
        stmt.setDate(2, java.sql.Date.valueOf(data));
        stmt.setDouble(3, f.getDescontoINSS());
        stmt.setDouble(4, f.getDescontoIR());
        stmt.setDouble(5, f.calcularSalarioLiquido());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
