import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Ponto de entrada oficial da Aplicação JavaFX.
 * Herda da classe 'Application' exigida pelo framework gráfico.
 */
public class Main extends Application {

    /**
     * Método start é onde a "Janela/Tela" primária é construída e exibida.
     * @param primaryStage É o palco principal onde a interface gráfica atuará.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega a marcação XML visual gerada pela ferramenta (FXML)
        URL fxmlLocation = getClass().getResource("/dashboard.fxml");
        if (fxmlLocation == null) {
            System.err.println("Arquivo FXML não encontrado!");
            System.exit(1);
        }
        
        // Converte o XML em objetos reais na memória (Botões, Tabela, etc)
        Parent root = FXMLLoader.load(fxmlLocation);
        
        // Define Título e Tamanho da Janela (850x600 pixels)
        primaryStage.setTitle("Java Container Manager");
        primaryStage.setScene(new Scene(root, 850, 600));
        
        // Mostra a janela para o usuário
        primaryStage.show();
    }

    public static void main(String[] args) {
        // O método estático launch() dá partida na Thread dedicada do JavaFX
        launch(args);
    }
}
