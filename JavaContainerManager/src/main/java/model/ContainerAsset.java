package model;

/**
 * Representa um Container Docker.
 * Herda (extends) as características da classe base DockerAsset.
 */
public class ContainerAsset extends DockerAsset {
    // Atributos específicos de um container
    private String status;
    private String cpuUsage;
    private String memoryUsage;

    /**
     * Construtor da classe Container.
     */
    public ContainerAsset(String id, String name, String status) {
        super(id, name); // Chama o construtor da classe pai (DockerAsset)
        this.status = status;
        this.cpuUsage = "0%";
        this.memoryUsage = "0%";
    }

    // Getters e Setters (Encapsulamento)
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(String cpuUsage) { this.cpuUsage = cpuUsage; }

    public String getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(String memoryUsage) { this.memoryUsage = memoryUsage; }

    /**
     * Regra de negócio: Um container só pode ser INICIADO se ele não estiver rodando (Up).
     * @return true se o botão "Iniciar" puder ser clicado.
     */
    public boolean canStart() {
        return status != null && !status.toLowerCase().contains("up");
    }

    /**
     * Regra de negócio: Um container só pode ser PARADO se ele estiver rodando (Up).
     * @return true se o botão "Parar" puder ser clicado.
     */
    public boolean canStop() {
        return status != null && status.toLowerCase().contains("up");
    }
}
