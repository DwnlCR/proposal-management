# Proposal Management

Plataforma de gerenciamento de propostas desenvolvida com Java 21 e Spring Boot 4.1.0. Sistema com autenticação via sessão HTTP, persistência em MySQL e controle de acesso baseado em roles.

## Visão Geral

O Proposal Management é uma aplicação backend que gerencia propostas entre dois tipos de usuários:

- INFLUENCERS: Criam e gerenciam suas próprias propostas
- BRANDS: Visualizam todas as propostas disponíveis no sistema

A plataforma implementa segurança com Spring Security, persistência de dados com Spring Data JPA e padrão Strategy para filtrar propostas por role.

## Stack Tecnológico

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.1.0 | Framework web |
| Spring Security | 7.0.8 | Autenticação e autorização |
| Spring Data JPA | 4.1.0 | Persistência com Hibernate |
| MySQL | 8.0 | Banco de dados |
| Lombok | 1.18 | Redução de boilerplate |
| Docker Compose | latest | Orquestração |

## Estrutura do Projeto

```
proposal-management/
├── src/main/java/br/com/dwnl/proposalmanagement/
│   ├── auth/
│   │   ├── domain/
│   │   │   └── UserRole.java
│   │   ├── infrastructure/
│   │   │   ├── http/
│   │   │   │   └── Controller.java
│   │   │   ├── persistence/
│   │   │   │   ├── entity/
│   │   │   │   │   └── User.java
│   │   │   │   └── repository/
│   │   │   │       └── UserRepository.java
│   │   │   └── security/
│   │   │       ├── SecurityConfig.java
│   │   │       ├── JpaUserDetailsService.java
│   │   │       └── RestUsernamePasswordAuthenticationFilter.java
│   │
│   ├── proposal/
│   │   ├── domain/
│   │   │   ├── Proposal.java
│   │   │   ├── ProposalId.java
│   │   │   ├── Owner.java
│   │   │   ├── OwnerId.java
│   │   │   └── ProposalRepository.java
│   │   ├── application/
│   │   │   ├── CreateProposalUseCase.java
│   │   │   ├── ListProposalUseCase.java
│   │   │   ├── input/
│   │   │   │   └── CreateProposalInput.java
│   │   │   ├── output/
│   │   │   │   └── ProposalOutput.java
│   │   │   └── list/
│   │   │       ├── Strategy.java
│   │   │       ├── AccessScope.java
│   │   │       ├── OwnStrategy.java
│   │   │       ├── AllStrategy.java
│   │   │       └── Factory.java
│   │   └── infrastructure/
│   │       ├── persistence/
│   │       │   ├── entity/
│   │       │   │   └── ProposalEntity.java
│   │       │   └── repository/
│   │       │       ├── ProposalEntityRepository.java
│   │       │       └── JpaProposalRepository.java
│   │       └── https/
│   │           ├── ProposalController.java
│   │           ├── request/
│   │           │   └── CreateProposalRequest.java
│   │           └── response/
│   │               └── ProposalResponse.java
│   │
│   └── ProposalManagementApplication.java
│
├── src/main/resources/
│   └── application.properties
├── src/test/java/
│   └── ProposalManagementApplicationTests.java
│
├── compose.yml
├── build.gradle
└── README.md
```

## Autenticação e Autorização

Autenticação via sessão HTTP usando Spring Security.

Usuários padrão criados automaticamente:

| Username | Senha | Role |
|----------|-------|------|
| fitness_vibe | password | ROLE_INFLUENCER |
| tech_guru | password | ROLE_INFLUENCER |
| logistics | password | ROLE_BRAND |

Fluxo de login:

1. POST /api/auth/login com credenciais JSON
2. RestUsernamePasswordAuthenticationFilter valida credenciais
3. Se válido, Spring cria cookie JSESSIONID
4. Cliente usa cookie para acessar endpoints protegidos

Controle de acesso:

- POST /proposals: apenas ROLE_INFLUENCER pode criar
- GET /proposals: ROLE_INFLUENCER vê suas propostas, ROLE_BRAND vê todas

## Padrão Strategy

O padrão Strategy é usado para filtrar propostas conforme o role:

- OwnStrategy: retorna apenas propostas do usuário autenticado
- AllStrategy: retorna todas as propostas
- Factory: resolve qual strategy usar baseado em AccessScope

Fluxo:

1. ListProposalUseCase recebe AccessScope (OWN ou ALL)
2. Factory resolve qual Strategy usar
3. Strategy busca propostas do repositório
4. Propostas são convertidas para DTO de resposta

## Endpoints

Login:

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "fitness_vibe",
  "password": "password"
}
```

Resposta: 200 OK com cookie JSESSIONID

Criar proposta (INFLUENCER):

```http
POST /proposals
Content-Type: application/json
Cookie: JSESSIONID=...

{
  "title": "Campanha de Moda",
  "description": "Promoção de verão 2026"
}
```

Resposta:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Campanha de Moda",
  "description": "Promoção de verão 2026",
  "response": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "fitness_vibe"
  }
}
```

Listar propostas:

```http
GET /proposals
Cookie: JSESSIONID=...
```

Se INFLUENCER: retorna suas propostas
Se BRAND: retorna todas as propostas

## Banco de Dados

MySQL 8.0 com duas tabelas criadas automaticamente:

Tabela user:
- id (UUID binary)
- username (varchar, unique)
- password (varchar)
- role (enum)

Tabela proposal:
- id (UUID binary)
- title (varchar)
- description (varchar)
- owner_id (UUID binary)
- owner_name (varchar)

## Como Executar

Pré-requisitos:

- Java 21
- Docker e Docker Compose
- Git

Passos:

```bash
# Clone o repositório
git clone https://github.com/DwnlCR/proposal-management.git
cd proposal-management

# Inicie o MySQL
docker compose up -d

# Compile
./gradlew clean build

# Execute
./gradlew bootRun
```

Aplicação estará em http://localhost:8080

## Docker Compose

```yaml
services:
  database:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: proposals
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: app
      MYSQL_PASSWORD: app
    ports:
      - "3309:3306"
    volumes:
      - data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uapp", "-papp"]
      interval: 5s
      timeout: 5s
      retries: 5

volumes:
  data:
```

## Configuração

application.properties:

```properties
spring.application.name=proposal-management
spring.datasource.url=jdbc:mysql://localhost:3309/proposals
spring.datasource.username=app
spring.datasource.password=app
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.docker.compose.lifecycle-management=start-only
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
```

## Troubleshooting

Porta 3309 já em uso:

```bash
lsof -i :3309
kill -9 <PID>
```

Container MySQL não inicia:

```bash
docker compose down -v
docker compose up -d
```

Erro de autenticação:

Verifique se o banco criou a tabela user com os dados de teste. SecurityConfig.initDatabase() executa na startup e cria os usuários.
