package service;

import model.ContainerAsset;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Camada de Serviço (Business Logic).
 * Responsável por orquestrar e se comunicar com o motor do Docker.
 */
public class DockerService {

    /**
     * Usa o motor nativo para listar todos os containers (em execução ou parados).
     * @return Lista de Objetos ContainerAsset
     * @throws Exception Caso o Docker não esteja respondendo ou dê erro.
     */
    public List<ContainerAsset> listarContainers() throws Exception {
        List<ContainerAsset> containers = new ArrayList<>();
        
        // ProcessBuilder permite executar comandos reais do SO como se fosse no terminal
        // Comando: docker ps -a --format "{{.ID}};{{.Names}};{{.Status}}"
        ProcessBuilder builder = new ProcessBuilder("docker", "ps", "-a", "--format", "{{.ID}};{{.Names}};{{.Status}}");
        Process process = builder.start(); // Inicia o processo
        
        // Lemos a saída (resultado) do comando linha por linha
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Dividimos a string usando o ponto e vírgula, extraindo ID, NOME e STATUS
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    ContainerAsset container = new ContainerAsset(parts[0], parts[1], parts[2]);
                    
                    // Se o container estiver ligado ("Up"), tentamos pegar o uso de CPU e Memória
                    if (container.getStatus().toLowerCase().contains("up")) {
                        Map<String, String> stats = obterStats(container.getId());
                        container.setCpuUsage(stats.getOrDefault("cpu", "0%"));
                        container.setMemoryUsage(stats.getOrDefault("mem", "0%"));
                    }
                    containers.add(container);
                }
            }
        }
        
        // Esperamos o comando do terminal finalizar. Se exitCode != 0, deu erro (ex: Docker offline)
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("Falha ao listar containers. Docker está rodando?");
        }
        
        return containers; // Retorna a lista populada de containers para a interface
    }

    /**
     * Executa: docker start [ID]
     */
    public boolean iniciarContainer(String id) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("docker", "start", id);
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("Falha ao iniciar container " + id);
        }
        return true;
    }

    /**
     * Executa: docker stop [ID]
     */
    public boolean pararContainer(String id) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("docker", "stop", id);
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("Falha ao parar container " + id);
        }
        return true;
    }

    /**
     * Pega as estatísticas isoladas de um container específico.
     * Executa: docker stats --no-stream --format "{{.CPUPerc}};{{.MemPerc}}" [ID]
     */
    public Map<String, String> obterStats(String id) {
        Map<String, String> stats = new HashMap<>();
        try {
            ProcessBuilder builder = new ProcessBuilder("docker", "stats", "--no-stream", "--format", "{{.CPUPerc}};{{.MemPerc}}", id);
            Process process = builder.start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        stats.put("cpu", parts[0]); // Porcentagem CPU
                        stats.put("mem", parts[1]); // Porcentagem RAM
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            // Se der falha silenciosa (ex: erro de permissão no comando stats), retorna 0% pro usuário
            stats.put("cpu", "0%");
            stats.put("mem", "0%");
        }
        return stats;
    }
}
