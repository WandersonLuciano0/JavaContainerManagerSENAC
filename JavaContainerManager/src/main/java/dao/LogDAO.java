package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe DAO (Data Access Object) especializada em manipular a tabela 'logs_operacoes'.
 * Separa a lógica de Banco de Dados da lógica de Negócio.
 */
public class LogDAO {

    /**
     * Salva um registro de auditoria no banco.
     * @param containerId ID do container afetado.
     * @param acao Ação executada (Iniciar, Parar, etc).
     * @param resultado Se deu Certo (SUCESSO) ou Falhou (ERRO).
     * @param mensagem Mensagem detalhada da ação ou do erro ocorrido.
     */
    public void registrarLog(String containerId, String acao, String resultado, String mensagem) {
        // Usamos ? (parâmetros de placeholder) para evitar ataques de SQL Injection
        String sql = "INSERT INTO logs_operacoes (container_id, acao, resultado, mensagem) VALUES (?, ?, ?, ?)";

        // Try-with-resources: Garante que a conexão será fechada ao final, mesmo se der erro
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Substitui os pontos de interrogação pelos valores passados no método
            pstmt.setString(1, containerId);
            pstmt.setString(2, acao);
            pstmt.setString(3, resultado);
            pstmt.setString(4, mensagem);
            
            // Executa o comando de Inserção (Insert)
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime o erro no console
        }
    }

    /**
     * Retorna todos os logs ordenados do mais recente para o mais antigo.
     * @return Lista de LogAsset
     */
    public java.util.List<model.LogAsset> listarTodos() {
        java.util.List<model.LogAsset> logs = new java.util.ArrayList<>();
        String sql = "SELECT * FROM logs_operacoes ORDER BY id DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.LogAsset log = new model.LogAsset(
                    rs.getInt("id"),
                    rs.getString("container_id"),
                    rs.getString("acao"),
                    rs.getString("resultado"),
                    rs.getString("mensagem"),
                    rs.getString("data_hora")
                );
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
