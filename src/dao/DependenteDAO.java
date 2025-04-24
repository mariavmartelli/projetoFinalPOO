
package dao;

import inicio.Dependente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DependenteDAO {
    public void inserir(Dependente d, String cpfFuncionario) throws SQLException {
        String sql = "INSERT INTO dependente(nome, cpf, data_nascimento, parentesco, id_funcionario) " +
                     "VALUES (?, ?, ?, ?, (SELECT id FROM funcionario WHERE cpf = ?))";
        Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, d.getNome());
        stmt.setString(2, d.getCpf());
        stmt.setDate(3, java.sql.Date.valueOf(d.getDataNascimento()));
        stmt.setString(4, d.getParentesco().name());
        stmt.setString(5, cpfFuncionario);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
