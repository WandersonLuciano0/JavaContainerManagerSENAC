package model;

/**
 * Representa uma Rede do Docker (Docker Network).
 * Exemplo prático de Herança, aproveitando id e name da classe pai.
 */
public class NetworkAsset extends DockerAsset {
    public NetworkAsset(String id, String name) {
        super(id, name);
    }
}
