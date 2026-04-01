# Soft - Serviço e Pedidos

## Sobre o Projeto
O projeto **Soft** é uma API RESTful desenvolvida para gerenciar o catálogo de produtos/serviços e o fluxo de pedidos de clientes. A aplicação foi construída com foco em escalabilidade e manutenibilidade, utilizando o ecossistema Spring Boot, padrões de projeto consistentes e estruturação de respostas navegáveis (HATEOAS). O sistema possui endpoints estruturados, validados e documentados, permitindo uma integração segura e facilitada.

## Tecnologias e Ferramentas
Desenvolvido em **Java 21** e **Spring Boot 3.x**, o projeto utiliza as seguintes dependências principais:
- **Spring WebMVC:** Para o desenvolvimento dos endpoints REST.
- **Spring Data JPA / Hibernate:** Para mapeamento objeto-relacional e abstração de persistência.
- **Spring HATEOAS:** Para adicionar hipermídia às respostas, permitindo a navegação dinâmica da API.
- **QueryDSL:** Para criação de consultas dinâmicas, tipadas e escaláveis.
- **Flyway:** Para controle de versionamento e execução das migrações de banco de dados.
- **Spring Boot Validation (Jakarta):** Garantindo validações consistentes nas requisições.
- **Springdoc OpenAPI (Swagger):** Para a documentação interativa de todos os endpoints e modelos da API.
- **Banco de Dados (Supabase):** O armazenamento dos dados corporativos é gerido por um banco de dados PostgreSQL integrado e hospedado na infraestrutura do **Supabase**.

## Funcionalidades Principais

### Produtos e Serviços
- Consulta paginada com suporte a filtros (ex: por tipo de item ou ativos).
- Cadastro, edição e deleção de itens do catálogo.
- Desativação lógica de produtos ou serviços sem perda de histórico.
- Respostas ricas com links HATEOAS.

### Pedidos
- Criação e acompanhamento de ciclo de vida do pedido.
- Listagem e busca com filtro baseado em status do pedido.
- Adição e remoção de itens (`ItemPedido`) de forma isolada e segura.
- Aplicação de descontos.
- Fechamento do pedido na conclusão das operações.

## Pré-requisitos
Para a correta execução deste código localmente:
- **Java JDK 21** ou superior configurado no ambiente de execução.
- **Maven** (A ferramenta Maven Wrapper já se encontra incluída no projeto).
- Conexão ativa com a internet para conectividade com o banco de dados no Supabase e download de artefatos.

## Como Executar
1. Acesse o diretório do projeto e utilize o terminai de sua preferência.

2. Atualize as dependências e compile o projeto (o comando abaixo fará a resolução de bibliotecas e compilação do código):
```bash
./mvnw clean install
```
*(Caso deseje ignorar a execução de testes momentaneamente, utilize `./mvnw clean install -DskipTests`)*.

3. Inicie o servidor embutido via Spring Boot:
```bash
./mvnw spring-boot:run
```

A aplicação fará a conexão automática com a base do Supabase e o Flyway avaliará o estado do schema gerenciando eventuais migrações necessárias no processo de carga de inicialização.

## Acesso à Documentação da API
Uma vez com a aplicação rodando localmente de forma ativa, toda a documentação estruturada pode ser consumida e testada via interface gráfica:
- **Swagger UI:** Pela URL mapeada comumente em `http://localhost:8080/swagger-ui.html`
- As definições em JSON podem ser obtidas em `http://localhost:8080/v3/api-docs`
