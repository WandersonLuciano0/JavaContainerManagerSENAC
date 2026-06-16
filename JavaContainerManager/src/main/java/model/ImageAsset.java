package model;

/**
 * Representa uma Imagem Docker (ex: ubuntu, nginx).
 * Exemplo prático de Herança, aproveitando id e name da classe pai.
 */
public class ImageAsset extends DockerAsset {
    public ImageAsset(String id, String name) {
        super(id, name);
    }
}
