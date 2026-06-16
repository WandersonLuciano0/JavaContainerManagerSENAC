package model;

/**
 * Classe abstrata base para qualquer recurso do Docker.
 * Isso demonstra o conceito de Herança e Abstração na Orientação a Objetos.
 */
public abstract class DockerAsset {
    // Atributos protegidos para serem acessíveis pelas classes filhas (herança)
    protected String id;
    protected String name;

    /**
     * Construtor da classe base.
     * @param id Identificador único gerado pelo Docker.
     * @param name Nome do recurso no Docker.
     */
    public DockerAsset(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters e Setters demonstram o princípio do Encapsulamento
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
