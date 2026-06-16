package controller;

import dao.LogDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.ContainerAsset;
import model.LogAsset;
import service.DockerService;
import util.Constants;

import java.util.List;

public class DashboardController {

    // --- ABA 1: OPERACIONAL ---
    @FXML private TableView<ContainerAsset> containerTable;
    @FXML private TableColumn<ContainerAsset, String> colId;
    @FXML private TableColumn<ContainerAsset, String> colName;
    @FXML private TableColumn<ContainerAsset, String> colStatus;
    @FXML private TableColumn<ContainerAsset, String> colCpu;
    @FXML private TableColumn<ContainerAsset, String> colMem;
    @FXML private TableColumn<ContainerAsset, Void> colAcoes;
    
    // --- ABA 2: GRÁFICOS ---
    @FXML private BarChart<String, Number> cpuChart;
    @FXML private BarChart<String, Number> memChart;

    // --- ABA 3: HISTÓRICO BD ---
    @FXML private TableView<LogAsset> logTable;
    @FXML private TableColumn<LogAsset, Integer> colLogId;
    @FXML private TableColumn<LogAsset, String> colLogData;
    @FXML private TableColumn<LogAsset, String> colLogAcao;
    @FXML private TableColumn<LogAsset, String> colLogContainer;
    @FXML private TableColumn<LogAsset, String> colLogResultado;
    @FXML private TableColumn<LogAsset, String> colLogMensagem;

    // --- CONSOLE ---
    @FXML private TextArea logArea;

    private DockerService dockerService;
    private LogDAO logDAO;
    private ObservableList<ContainerAsset> containerList;
    private ObservableList<LogAsset> dbLogList;

    @FXML
    public void initialize() {
        dockerService = new DockerService();
        logDAO = new LogDAO();
        containerList = FXCollections.observableArrayList();
        dbLogList = FXCollections.observableArrayList();
        
        // Remove animações padrão para carregar os gráficos mais rapidamente
        cpuChart.setAnimated(false);
        memChart.setAnimated(false);

        configurarTabelaOperacional();
        configurarTabelaLogs();
        
        // Carrega todos os dados iniciais
        atualizarContainers();
    }

    private void configurarTabelaOperacional() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCpu.setCellValueFactory(new PropertyValueFactory<>("cpuUsage"));
        colMem.setCellValueFactory(new PropertyValueFactory<>("memoryUsage"));

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnStart = new Button("▶ Iniciar");
            private final Button btnStop = new Button("⏹ Parar");
            private final HBox pane = new HBox(10, btnStart, btnStop);

            {
                btnStart.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
                btnStop.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");

                btnStart.setOnAction(event -> {
                    ContainerAsset container = getTableView().getItems().get(getIndex());
                    iniciarContainer(container);
                });
                
                btnStop.setOnAction(event -> {
                    ContainerAsset container = getTableView().getItems().get(getIndex());
                    pararContainer(container);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ContainerAsset container = getTableView().getItems().get(getIndex());
                    btnStart.setDisable(!container.canStart());
                    btnStop.setDisable(!container.canStop());
                    setGraphic(pane);
                }
            }
        });

        containerTable.setItems(containerList);
    }

    private void configurarTabelaLogs() {
        colLogId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogData.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
        colLogAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colLogContainer.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        colLogResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        colLogMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));

        logTable.setItems(dbLogList);
    }

    @FXML
    public void atualizarContainers() {
        addLog("Sincronizando com Docker e Banco de Dados...");
        
        new Thread(() -> {
            try {
                // Busca containers no Docker
                List<ContainerAsset> containers = dockerService.listarContainers();
                // Busca histórico no Banco de Dados
                List<LogAsset> logsBD = logDAO.listarTodos();

                Platform.runLater(() -> {
                    // Atualiza Tabela 1
                    containerList.clear();
                    containerList.addAll(containers);
                    containerTable.refresh();
                    
                    // Atualiza Gráficos (Aba 2)
                    atualizarGraficos(containers);

                    // Atualiza Tabela do BD (Aba 3)
                    dbLogList.clear();
                    dbLogList.addAll(logsBD);
                    logTable.refresh();

                    addLog("Tabelas e gráficos atualizados com sucesso.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    addLog("Erro ao atualizar: " + e.getMessage());
                    logDAO.registrarLog("ALL", "UPDATE", Constants.RESULT_ERROR, e.getMessage());
                    mostrarAlertaErro("Erro", "Não foi possível comunicar com o Docker.");
                });
            }
        }).start();
    }

    private void atualizarGraficos(List<ContainerAsset> containers) {
        cpuChart.getData().clear();
        memChart.getData().clear();

        XYChart.Series<String, Number> cpuSeries = new XYChart.Series<>();
        cpuSeries.setName("Uso CPU");

        XYChart.Series<String, Number> memSeries = new XYChart.Series<>();
        memSeries.setName("Uso RAM");

        for (ContainerAsset c : containers) {
            // Apenas adiciona no gráfico os que estão rodando
            if (c.getStatus().toLowerCase().contains("up")) {
                // Remove o sinal "%" e converte para double numérico
                double cpuValue = extrairNumero(c.getCpuUsage());
                double memValue = extrairNumero(c.getMemoryUsage());

                cpuSeries.getData().add(new XYChart.Data<>(c.getName(), cpuValue));
                memSeries.getData().add(new XYChart.Data<>(c.getName(), memValue));
            }
        }

        cpuChart.getData().add(cpuSeries);
        memChart.getData().add(memSeries);
    }

    private double extrairNumero(String valorComPorcentagem) {
        try {
            return Double.parseDouble(valorComPorcentagem.replace("%", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void iniciarContainer(ContainerAsset container) {
        addLog("Comando: Iniciar -> " + container.getName());
        new Thread(() -> {
            try {
                dockerService.iniciarContainer(container.getId());
                Platform.runLater(() -> {
                    logDAO.registrarLog(container.getId(), Constants.ACTION_START, Constants.RESULT_SUCCESS, "Container iniciado");
                    atualizarContainers(); 
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    logDAO.registrarLog(container.getId(), Constants.ACTION_START, Constants.RESULT_ERROR, e.getMessage());
                    mostrarAlertaErro("Erro ao Iniciar", e.getMessage());
                    atualizarContainers(); // Atualiza logs mesmo no erro
                });
            }
        }).start();
    }

    private void pararContainer(ContainerAsset container) {
        addLog("Comando: Parar -> " + container.getName());
        new Thread(() -> {
            try {
                dockerService.pararContainer(container.getId());
                Platform.runLater(() -> {
                    logDAO.registrarLog(container.getId(), Constants.ACTION_STOP, Constants.RESULT_SUCCESS, "Container parado");
                    atualizarContainers();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    logDAO.registrarLog(container.getId(), Constants.ACTION_STOP, Constants.RESULT_ERROR, e.getMessage());
                    mostrarAlertaErro("Erro ao Parar", e.getMessage());
                    atualizarContainers();
                });
            }
        }).start();
    }

    private void addLog(String message) {
        logArea.appendText(message + "\n");
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
