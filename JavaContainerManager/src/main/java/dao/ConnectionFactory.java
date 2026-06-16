package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import util.Constants;

/**
 * Padrão Factory (Fábrica): Responsável por criar e fornecer a conexão com o banco de dados SQLite.
 * Pertence à camada DAO (Data Access Object).
 */
public class ConnectionFactory {
    
    /**
     * Tenta estabelecer a conexão com o SQLite.
     * @return Objeto Connection ativo.
     */
    public static Connection getConnection() {
        try {
            // O DriverManager gerencia o driver JDBC do SQLite
            Connection conn = DriverManager.getConnection(Constants.DB_URL);
            
            // Garante que a tabela exista antes de retornarmos a conexão
            createTableIfNotExists(conn);
            return conn;
        } catch (SQLException e) {
            // Se falhar, lançamos uma exceção não-verificada interrompendo a execução
            throw new RuntimeException("Erro ao conectar com o banco de dados SQLite", e);
        }
    }

    /**
     * Método privado (Encapsulamento) que roda um DDL (Data Definition Language)
     * criando a tabela de logs caso seja a primeira vez que o sistema rode no PC.
     */
    private static void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS logs_operacoes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "container_id TEXT," +
                     "acao TEXT," +
                     "resultado TEXT," +
                     "mensagem TEXT," +
                     "data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                     ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql); // Executa o script SQL de criação
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
