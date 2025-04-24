
package dao;

import inicio.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FuncionarioDAO {
    public void inserir(Funcionario f) throws SQLException {
        String sql = "INSERT INTO funcionario(nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?)";
        Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, f.getNome());
        stmt.setString(2, f.getCpf());
        stmt.setDate(3, java.sql.Date.valueOf(f.getDataNascimento()));
        stmt.setDouble(4, f.getSalarioBruto());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
