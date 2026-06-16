# Java Container Manager

Uma aplicação desktop desenvolvida em Java para o gerenciamento de ativos relacionados a containers (como Containers, Imagens, Redes e Logs). O projeto conta com uma interface gráfica construída em JavaFX e utiliza um banco de dados SQLite para persistência local das informações.

## 🚀 Funcionalidades

- **Gestão de Containers:** Criação, leitura, atualização e exclusão de registros de containers.
- **Gestão de Imagens e Redes:** Controle dos ativos de imagens e configurações de rede.
- **Registro de Logs:** Histórico e monitoramento das atividades.
- **Interface Gráfica (GUI):** Dashboard intuitivo construído com JavaFX e FXML.
- **Persistência de Dados:** Uso de banco de dados embutido (SQLite) para armazenamento seguro e leve das informações.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java (JDK 25)
- **Interface Gráfica:** JavaFX (versão 21)
- **Banco de Dados:** SQLite (via JDBC)
- **Gerenciador de Dependências:** Maven
- **Arquitetura:** Padrão MVC com camadas divididas em `model`, `controller`, `dao` e `service`.

## 📁 Estrutura do Projeto

O projeto segue uma estrutura bem definida e modularizada:

- `src/main/java/model/`: Classes de modelo e entidades do sistema (ContainerAsset, DockerAsset, ImageAsset, etc).
- `src/main/java/controller/`: Controladores do JavaFX para as telas.
- `src/main/java/dao/`: Objetos de Acesso a Dados (Data Access Objects) para manipulação do SQLite.
- `src/main/java/service/`: Lógica de negócios.
- `src/main/resources/`: Arquivos `.fxml` (ex: `dashboard.fxml`) para a interface gráfica.

## ⚙️ Como Executar

### Pré-requisitos
- Ter o **Java Development Kit (JDK) 25** instalado.
- Ter o **Maven** instalado e configurado no seu sistema.

### Passos para rodar a aplicação:

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/JavaContainerManager.git
   ```
2. Acesse a pasta do projeto:
   ```bash
   cd JavaContainerManager
   ```
3. Compile e rode o projeto usando o Maven:
   ```bash
   mvn clean javafx:run
   ```

## 🎓 Sobre o Projeto Acadêmico

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de **Programação Orientada a Objetos (POO) em Java**.

**Desenvolvido por:**
- Wanderson Luciano
- Sarah Moura
- Renan Cavalcante
- Kauan Nicolas
- Julia Lima

## 📄 Licença

Este projeto é desenvolvido para fins de estudo e trabalho acadêmico. Sinta-se à vontade para fazer um fork e contribuir.
