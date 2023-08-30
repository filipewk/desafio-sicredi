# Sistema de Votação

Para rodar o projeto, é necessário executar o seguinte comando:
```bash 
docker-compose up -d
```
Este projeto é um serviço RESTful dedicado a gerenciar tópicos de votação, registrar votos e exibir os resultados das sessões de votação.

## Controladores

### Controlador de Tópicos (`TopicControllerImpl`)

- **Endpoint:** `/topics`
- **Operações:**
  - **POST**: Cria um novo tópico de votação.
    - **Payload**:
      ```json
      {
        "id": 0,
        "description": "string"
      }
      ```
    - **Response**:
      ```json
      {
        "error": true,
        "status": 0,
        "message": "string",
        "content": {
          "id": 0,
          "description": "string"
        }
      }
      ```

### Controlador de Votos (`VoteControllerImpl`)

- **Endpoint:** `/votes`
- **Operações:**
  - **POST**: Registra um voto em uma sessão de votação.
    - **Payload**:
      ```json
      {
        "votingSessionId": 0,
        "memberId": 0,
        "voteValue": "YES"
      }
      ```
    - **Responses**:
      ```json
      {
        "error": true,
        "status": 0,
        "message": "string",
        "content": {
          "votingSessionId": 0,
          "memberId": 0,
          "voteValue": "YES"
        }
      }
      ```

### Controlador de Sessões de Votação (`VotingSessionControllerImpl`)

- **Endpoint:** `/sessions/topic/{topicId}`
  - **param:** topicId
  - **query string:** durationMinutes (Default value: 1)
  - **Response**:
    ```json
    {
      "error": true,
      "status": 0,
      "message": "string",
      "content": {
        "id": 0,
        "topic": {
          "id": 0,
          "description": "string"
        },
        "openingDate": "2023-08-29T22:03:22.232Z",
        "durationMinutes": 0
      }
    }
    ```

- **Endpoint:** `/sessions/{sessionId}/result`
  - **Response**:
    ```json
    {
      "error": true,
      "status": 0,
      "message": "string",
      "content": {
        "topicId": 0,
        "topicDescription": "string",
        "sessionId": 0,
        "totalVotes": 0,
        "yesVotes": 0,
        "noVotes": 0,
        "result": "YES_WINS",
        "votingSessionStatus": "VOTING_OPEN"
      }
    }
    ```

## Entidades

- **Pauta (`Topic`)**: Representa uma pauta de votação.
- **Voto (`Vote`)**: Representa um voto.
- **Sessão de Votação (`VotingSession`)**: Representa uma sessão de votação.

## Agendamentos e Uso do RabbitMQ

O sistema possui uma rotina programada de atualização para sessões de votação que já expiraram. Após calcular os resultados, ele usa o RabbitMQ para enviar mensagens contendo esses resultados.

O RabbitMQ funciona como um broker de mensagens, permitindo que sistemas se comuniquem entre si através de mensagens. No contexto deste sistema:

- **Exchange (`vote.topic`)**: É o mecanismo que recebe as mensagens e as distribui para as filas apropriadas com base em regras de roteamento. Neste caso, estamos usando uma `exchange` do tipo `topic`, identificada pelo nome `vote.topic`.

- **Queue (`vote-update.queue`)**: É onde as mensagens são mantidas até serem processadas por um consumidor. Neste sistema, a fila para mensagens de atualização de votação é nomeada como `vote-update.queue`.

- **Routing Key (`vote-update.routingKey`)**: A chave de roteamento é usada pela `exchange` para decidir para qual fila a mensagem deve ser enviada. Aqui, a chave usada é `vote-update.routingKey`.

Deste modo, quando uma sessão de votação expira, uma mensagem é enviada para a `exchange` `vote.topic`, que por sua vez, com base na chave de roteamento, direciona a mensagem para a fila `vote-update.queue` onde pode ser consumida por um componente ou sistema subsequente.


## Gerenciamento e Versionamento de Banco de Dados com Flyway

O Flyway é uma ferramenta que nos permite gerenciar, versionar e aplicar migrações de banco de dados de uma forma controlada e sistemática. Com o Flyway, mantemos uma sequência de scripts SQL (migrações) em uma pasta específica, onde cada script altera o banco de dados de alguma maneira. Cada script é versionado, o que nos permite rastrear quais migrações foram aplicadas e quando.

A estrutura padrão que o Flyway utiliza é a pasta `classpath:db/migration` para armazenar os scripts.

### Exemplo de Script de Migração:

Localizado em `db/migration/V1__create_topic_table.sql`, este script cria a tabela `TOPIC`:

```sql
CREATE TABLE TOPIC (
  ID BIGSERIAL PRIMARY KEY,
  DESCRIPTION VARCHAR(255) NOT NULL
);
```
Nesse exemplo, o prefixo V1__ indica a versão da migração. O Flyway garantirá que os scripts sejam executados em ordem e apenas uma vez. Conforme o banco de dados evolui, novos scripts são adicionados a esse diretório e, quando o Flyway é executado, ele aplica as migrações em sequência.

## Logs do Sistema

O sistema implementa um mecanismo de registro de logs robusto para garantir transparência e rastreabilidade das operações. Abaixo, temos uma descrição de como os logs são apresentados no sistema:

### Estrutura do Log

Os logs são estruturados da seguinte maneira:

- **app_name**: O nome da aplicação.
- **uuid_request**: O UUID gerado para cada solicitação, permitindo rastrear uma solicitação específica ao longo de todo o seu ciclo de vida.
- **method**: Método HTTP da solicitação (por exemplo, GET, POST).
- **date_begin** e **date_end**: Marcas de tempo indicando o início e o fim da solicitação.
- **message**: Uma mensagem descritiva associada ao log, como "Iniciando Requisição" ou "Finalizando Requisição".
- **URI**: O endpoint da API que está sendo acessado.
- **payload** (se aplicável): O corpo da solicitação, especialmente útil para solicitações POST.

### Exemplos de Logs:

```plaintext
2023-08-29T22:11:39.117-03:00  INFO 95247 --- [nio-8080-exec-6] b.c.s.c.interceptor.LoggingInterceptor   : app_name=CoopVote | uuid_request=a1f8c226-378d-4e6d-a651-0d21f6dcf623 | method=POST | date_begin=29/08/2023 22:11:39.117 | message=Initiating Request | URI=/api/topics
2023-08-29T22:11:39.118-03:00  INFO 95247 --- [nio-8080-exec-6] .c.s.c.i.LoggingPayloadAdviceInterceptor : app_name=CoopVote | uuid_request=a1f8c226-378d-4e6d-a651-0d21f6dcf623 | method=POST | payload={"id":null,"description":"Pauta de Votacao"} | URI=/api/topics
2023-08-29T22:11:39.127-03:00  INFO 95247 --- [nio-8080-exec-6] b.c.s.c.interceptor.LoggingInterceptor   : app_name=CoopVote | uuid_request=a1f8c226-378d-4e6d-a651-0d21f6dcf623 | method=POST | date_end=29/08/2023 22:11:39.127 | message=Finalizing Request | URI=/api/topics
2023-08-29T22:11:42.782-03:00  INFO 95247 --- [nio-8080-exec-7] b.c.s.c.interceptor.LoggingInterceptor   : app_name=CoopVote | uuid_request=5f48ee57-e3d0-4fc7-a392-1ed6670f5723 | method=POST | date_begin=29/08/2023 22:11:42.782 | message=Initiating Request | URI=/api/sessions/topic/3
2023-08-29T22:11:42.795-03:00  INFO 95247 --- [nio-8080-exec-7] b.c.s.c.interceptor.LoggingInterceptor   : app_name=CoopVote | uuid_request=5f48ee57-e3d0-4fc7-a392-1ed6670f5723 | method=POST | date_end=29/08/2023 22:11:42.794 | message=Finalizing Request | URI=/api/sessions/topic/3
2023-08-29T22:13:22.644-03:00  INFO 95247 --- [   scheduling-1] b.c.s.c.s.MessageSenderServiceImpl       : Send message: {"topicId":3,"topicDescription":"Pauta de Votacao","sessionId":2,"totalVotes":0,"yesVotes":0,"noVotes":0,"result":"TIED","votingSessionStatus":"VOTING_CLOSED"}
2023-08-29T22:13:22.645-03:00  INFO 95247 --- [   scheduling-1] b.c.s.c.s.MessageSenderServiceImpl       : Message was sent successfully.
```

## Documentação API

A documentação da API, gerada pelo Swagger, pode ser acessada através da URL: `http://localhost:8080/api/swagger-ui.html`

## Tecnologias e ferramentas

- **Spring Boot**: Framework principal.
- **Swagger/OpenAPI**: Documentação e definição da API.
- **google-java-formatter**: Lint para manter um padrão de código.
- **jacoco**: Relatórios de cobertura de código.
- **RabbitMQ**: Sistema de mensageria utilizado para envio de atualizações sobre as sessões de votação.
- **JUnit**: Framework utilizado para a criação de testes unitários.
- **Mockito**: Framework utilizado para criar mocks em testes unitários.

# Testes do Sistema

O sistema emprega diversas abordagens de teste para garantir qualidade, eficiência e robustez.

## 1. Testes de Arquitetura

Os testes de arquitetura verificam se o código segue padrões e normas arquiteturais estabelecidos. Por exemplo, a classe `ControllerTest` assegura que todos os controladores tenham a anotação `@RestController` e residam no pacote correto. Semelhantemente, a classe `EntityTest` confirma que todas as classes de domínio estejam anotadas com `@Entity`.

## 2. Testes Unitários

Estes testes focam em unidades individuais do software, garantindo que elas funcionem como esperado em isolamento. Por exemplo, `MessageSenderServiceImplTest` testa a lógica de envio de mensagens, usando mocks para simular interações com componentes externos.

## 3. Testes Integrados

Os testes integrados visam garantir que diferentes partes do sistema trabalhem juntas de maneira correta. Utilizamos o banco de dados em memória H2 para simular interações reais com o banco de dados. A classe `TopicControllerImplTest`, por exemplo, é um teste que valida a criação de tópicos através de requisições HTTP, interagindo com o banco de dados H2 em memória para simular o ambiente real.

---

Além destes, o sistema possui várias outras classes e métodos de teste que trabalham juntos para garantir que tudo funcione como esperado, abrangendo diferentes cenários e situações.


## Capturas de tela

### Swagger UI

![Captura de tela da Swagger UI](src/main/resources/static/img/swagger.png)

### Relatório de Cobertura Jacoco

![Captura de tela do Relatório Jacoco](src/main/resources/static/img/jacoco.png)
