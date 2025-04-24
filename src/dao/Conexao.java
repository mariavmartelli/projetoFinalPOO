package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection conectar() throws SQLException {
            return DriverManager.getConnection(
        	"jdbc:postgresql://localhost:5432/projetoFinalPOO",  // nome banco
        	"postgres",                                 // usuario padrao
        	"root"                            // senha q usa no pgAdmin
        	);

    }
}
