# Sistema de Votação

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

## Agendamentos

O sistema possui uma rotina de atualização programada para sessões de votação que já expiraram. Esta rotina calcula os resultados e utiliza o RabbitMQ para enviar mensagens com os resultados das votações.

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

## Testes

Foram realizados testes unitários e testes de integração para garantir a qualidade e funcionalidade do serviço.

## Capturas de tela

### Swagger UI

![Captura de tela da Swagger UI](src/main/resources/static/img/swagger.png)

### Relatório de Cobertura Jacoco

![Captura de tela do Relatório Jacoco](src/main/resources/static/img/jacoco.png)
