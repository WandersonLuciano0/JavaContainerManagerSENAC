/**
 * Classe wrapper para inicialização segura do JavaFX.
 * Ela serve como uma ponte de entrada para evitar a checagem rigorosa de Módulos (Jigsaw) 
 * presente a partir do Java 11. Como essa classe não 'extends Application', a JVM executa o Main sem exigir o 'module-info.java'.
 */
public class App {
    public static void main(String[] args) {
        Main.main(args); // Repassa a execução para a classe principal oficial
    }
}
