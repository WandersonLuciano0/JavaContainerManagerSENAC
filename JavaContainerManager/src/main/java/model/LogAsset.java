package model;

/**
 * Representa uma linha (registro) da tabela logs_operacoes no Banco de Dados.
 * Facilita a exibição dos dados na interface gráfica (TableView).
 */
public class LogAsset {
    private int id;
    private String containerId;
    private String acao;
    private String resultado;
    private String mensagem;
    private String dataHora;

    public LogAsset(int id, String containerId, String acao, String resultado, String mensagem, String dataHora) {
        this.id = id;
        this.containerId = containerId;
        this.acao = acao;
        this.resultado = resultado;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
    }

    // Getters
    public int getId() { return id; }
    public String getContainerId() { return containerId; }
    public String getAcao() { return acao; }
    public String getResultado() { return resultado; }
    public String getMensagem() { return mensagem; }
    public String getDataHora() { return dataHora; }
}
