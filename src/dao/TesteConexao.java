package dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        try {
            Connection conn = Conexao.conectar();
            if (conn != null) {
                System.out.println("Conexão com o banco de dados bem-sucedida!");
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco: " + e.getMessage());
        }
    }
}
