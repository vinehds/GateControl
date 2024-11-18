# GateControl

O *GateControl* é um sistema de gerenciamento de registros de entrada e saída de veículos, com controle das informações dos motoristas e veículos. A aplicação permite o gerenciamento dos registros de forma intuitiva, facilitando o controle de veículos que entram e saem de uma determinada área.

## Tabela de Conteúdos

- [Descrição](#descrição)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação](#instalação)
- [Uso](#uso)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Dados de Exemplo](#dados-de-exemplo)
- [Licença](#licença)

## Descrição

Este projeto implementa um sistema de gerenciamento de registros de veículos e motoristas, permitindo adicionar, editar, excluir e consultar entradas e saídas de veículos. O sistema é baseado em *Java, utilizando **Spring Boot* para o backend e *MySQL* para o banco de dados. A interface de usuário é construída com *HTML, **CSS* e *JavaScript*.

## Tecnologias Utilizadas

- *Java 17*
- *Spring Boot* para o backend
- *MySQL* como banco de dados
- *HTML, **CSS* e *JavaScript* para a interface de usuário
- *Maven* como ferramenta de gerenciamento de dependências

## Instalação

Para rodar o *GateControl* localmente, siga os passos abaixo:

### 1. Pré-requisitos:

Antes de rodar a aplicação, você precisará ter o *MySQL* instalado e configurado na sua máquina. Você pode obter o MySQL [aqui](https://dev.mysql.com/downloads/installer/).

### 2. Criar o Banco de Dados:

Crie um banco de dados para o *GateControl*. No MySQL, execute o seguinte comando para criar um banco de dados:

sql
CREATE DATABASE supportcrud;

### 3. Configurar a Conexão com o Banco de Dados:

O projeto utiliza o *MySQL* como banco de dados. A configuração da conexão pode ser feita no arquivo **src/main/resources/application-dev.properties**.

Dentro deste arquivo, localize ou adicione as seguintes configurações:

properties
# Configuração do banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/supportcrud -> URL DO BANCO
spring.datasource.username=SEU_USUARIO   -> seu usuario do mySQL
spring.datasource.password=SUA_SENHA     -> sua senha do mySQL
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true


*Nota*: Altere seu_usuario e sua_senha para o seu usuário e senha do MySQL.

### 4. Instalar as Dependências:

Se você ainda não tem o Maven instalado, baixe e instale a partir do [site oficial](https://maven.apache.org/download.cgi). Depois, no diretório raiz do projeto, execute o comando abaixo para instalar as dependências do projeto:

mvn clean install


### 5. Rodar o Projeto:

Para iniciar o servidor Spring Boot e o sistema de gerenciamento de registros, execute o seguinte comando:

mvn spring-boot:run


Isso vai iniciar o servidor localmente, e a aplicação estará disponível em [http://localhost:8080](http://localhost:8080).

---

## Uso

A aplicação *GateControl* permite gerenciar os registros de entrada e saída de veículos, incluindo informações sobre os motoristas e dos veículos.

### Fluxo de uso:

1. *Tela Inicial:*
   - Ao acessar o sistema, o usuário verá uma lista de registros já salvos com as informações dos veículos e motoristas.
   - É possível *editar* ou *excluir* qualquer registro da lista.
   - O usuário também pode *adicionar novos registros*, clicando na opção correspondente.

2. *Menu Lateral:*
   - No menu lateral, o usuário tem acesso a funcionalidades adicionais para gerenciar tanto os *veículos* quanto os *motoristas*.
   - Da mesma forma que na tela inicial, ele pode *editar, **excluir* ou *adicionar novos registros* de veículos e motoristas.

### Exemplo de Interação:
- Na tela inicial, o usuário pode clicar em "Adicionar Registro" para incluir um novo veículo, fornecendo as informações necessárias.
- O usuário pode também visualizar os registros existentes, clicar em um item específico para editar, ou clicar em "Excluir" para remover um registro.

---

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais informações.

---
