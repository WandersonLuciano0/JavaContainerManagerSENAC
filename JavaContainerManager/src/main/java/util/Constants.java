package util;

/**
 * Classe utilitária para centralizar as Constantes do sistema.
 * Boas práticas de programação: evitar "Magic Strings" espalhadas pelo código.
 */
public class Constants {
    // URL de conexão com o banco de dados SQLite local
    public static final String DB_URL = "jdbc:sqlite:containermanager.db";
    
    // Constantes de Ação e Resultado para padronizar os Logs
    public static final String ACTION_START = "INICIAR";
    public static final String ACTION_STOP = "PARAR";
    public static final String RESULT_SUCCESS = "SUCESSO";
    public static final String RESULT_ERROR = "ERRO";
}
