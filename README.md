# Sistema de Agendamento

API de agendamento de reservas em horários, construída como projeto de estudo de **backend Java moderno**, com foco em **Clean Architecture**, **DDD pragmático** e, principalmente, em **controle de concorrência com lock pessimista**.

## Sumário

- [Descrição do Projeto](#descrição-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
  - [Camadas](#camadas)
  - [Estrutura de um módulo](#estrutura-de-um-módulo)
  - [Fluxo de uma requisição](#fluxo-de-uma-requisição)
  - [Módulos de negócio](#módulos-de-negócio)
  - [Banco de dados e soft delete](#banco-de-dados-e-soft-delete)
  - [Segurança](#segurança)
  - [Controle de concorrência (foco do projeto)](#controle-de-concorrência-foco-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Como Executar](#como-executar)
  - [Pré-requisitos](#pré-requisitos)
  - [Configuração do Ambiente](#configuração-do-ambiente)
  - [Executando a Aplicação](#executando-a-aplicação)
  - [Documentação da API](#documentação-da-api-openapiswagger)
  - [Executando os Testes](#executando-os-testes)

---

## Descrição do Projeto

O **Sistema de Agendamento** é uma API REST que permite gerenciar agendas (`Calendar`), janelas de horário (`TimeSlot`) e reservas (`Reservation`) feitas por usuários autenticados.

O coração do projeto é o fluxo de **criação de reservas**, que precisa garantir uma invariante crítica: **não pode existir mais de uma reserva confirmada para o mesmo horário na mesma data**. Em um cenário de múltiplas requisições simultâneas, isso configura uma clássica **race condition**, e é justamente o problema que o projeto resolve de forma intencional, utilizando **lock pessimista** dentro de uma transação.

Além disso, o projeto explora:

- **Clean Architecture** organizada por módulos e em **quatro camadas** (`domain`, `application`, `infrastructure`, `presentation`) + módulo transversal `shared`.
- **DDD pragmático**: entidades de domínio ricas e puras (sem framework) e Value Objects (ex.: `Email`).
- **Spring Security stateless** com autenticação via **JWT (Bearer token)**.
- **Soft delete** reforçado no banco com índices únicos parciais.
- Versionamento de schema com **Flyway** (Hibernate em modo `validate`).
- **Testes unitários** (use cases, entidades e Value Objects) e **testes de integração** com **Testcontainers**, incluindo um teste real de concorrência.
- Documentação de API com **OpenAPI / Swagger** desacoplada dos controllers.

---

## Tecnologias Utilizadas

![Java](https://img.shields.io/badge/Java%2021-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger%2FOpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Testcontainers](https://img.shields.io/badge/Testcontainers-291A38?style=for-the-badge&logo=testcontainers&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

| Categoria | Tecnologias |
|-----------|-------------|
| **Linguagem & Runtime** | Java 21 |
| **Framework** | Spring Boot 3.5.x (`web`, `data-jpa`, `security`, `validation`) |
| **Autenticação** | Spring Security (stateless) + JWT (`jjwt` 0.13.0) |
| **Persistência** | Spring Data JPA / Hibernate, PostgreSQL |
| **Migrations** | Flyway (`flyway-core` + `flyway-database-postgresql`) |
| **Documentação** | springdoc-openapi (Swagger UI) |
| **Testes** | JUnit 5, Spring Boot Test, Spring Security Test, Testcontainers (PostgreSQL) |
| **Produtividade** | Lombok |
| **Build** | Maven |
| **Infraestrutura** | Docker, Docker Compose (PostgreSQL) |

---

## Arquitetura

O projeto segue a **Clean Architecture** organizada **por módulos de negócio** (`user`, `calendar`, `timeslot`, `reservation`) e um módulo transversal `shared`. Cada módulo é dividido em **quatro camadas**, garantindo baixo acoplamento e independência do domínio em relação a frameworks.

### Camadas

```text
domain          → regras de negócio puras (entidades, Value Objects, enums, contratos de repositório)
application     → casos de uso, DTOs internos (Input/Output), mappers e exceções de aplicação
infrastructure  → adaptadores técnicos (persistência JPA, segurança, configs)
presentation    → camada web (controllers, DTOs HTTP, mappers HTTP, docs OpenAPI)
shared          → base comum reutilizada por todos os módulos
```

### Estrutura de um módulo

Exemplo com o módulo `reservation` (todos os módulos seguem o mesmo padrão):

```text
reservation
├── application
│   ├── dto            → CreateReservationInput, ReservationOutput
│   ├── exception      → ReservationConflictException, ReservationNotFoundException
│   ├── mapper         → ReservationMapper (domínio ↔ Output)
│   └── usecase        → CreateReservationUseCase, CancelReservationUseCase, ...
│
├── domain
│   ├── entity         → Reservation (entidade rica, sem framework)
│   ├── enums          → ReservationStatus
│   ├── exception      → InvalidReservationException, ReservationAlreadyCancelledException
│   └── repository     → ReservationRepository (interface / porta)
│
├── infrastructure
│   └── persistence
│       ├── entity     → ReservationJpaEntity (@Entity)
│       ├── mapper     → ReservationPersistenceMapper (domínio ↔ JPA)
│       ├── repository → SpringDataReservationRepository (interface Spring Data)
│       └── ReservationRepositoryImp  → adapta Spring Data à porta de domínio
│
└── presentation
    ├── controller     → ReservationController
    ├── docs           → ReservationControllerDocs, ... (anotações OpenAPI)
    ├── dto
    │   ├── request    → CreateReservationRequest
    │   └── response   → ReservationResponse
    └── mapper         → ReservationApiMapper (request/Input ↔ Output/response)
```

> As configurações globais (`SecurityConfig`, `OpenApiConfig`, `JwtProperties`) ficam em `shared/infrastructure/config`. Há **três níveis de mapper** por módulo (aplicação, persistência e apresentação) e os DTOs são separados em dois níveis: **aplicação** (`Input`/`Output`) e **apresentação** (`Request`/`Response`).

### Fluxo de uma requisição

```text
HTTP Request
  → Controller (presentation)
  → ApiMapper: Request → Input
  → UseCase (application)
       usa AuthenticatedUserProvider / Repository (portas do domínio)
       opera sobre a Entidade de Domínio (regras de negócio)
  → RepositoryImp (infrastructure) ↔ PersistenceMapper ↔ JPA ↔ PostgreSQL
  → Output
  → ApiMapper: Output → Response
  → ApiResponse<T> (envelope padrão)
HTTP Response
```

### Módulos de negócio

| Módulo | Responsabilidade | Pontos principais |
|--------|------------------|-------------------|
| **User** | Usuários e autenticação | `email` como Value Object (`Email`), senha com BCrypt, roles `USER`/`ADMIN`, soft delete via `deactivate()` |
| **Calendar** | Agendas | `name` único entre calendários ativos, soft delete via `archive()` |
| **TimeSlot** | Janelas de horário (`LocalTime`) de um calendário | `startTime < endTime`, validação de sobreposição de horários, soft delete via `archive()` |
| **Reservation** | Reservas de horários | Status `CONFIRMED`/`CANCELLED`, ciclo de vida controlado por status (sem soft delete), invariante de no máximo 1 reserva confirmada por slot/data |

### Banco de dados e soft delete

- **PostgreSQL** com Spring Data JPA / Hibernate.
- O schema é gerenciado por **Flyway** (`src/main/resources/db/migration`, V1–V6). O Hibernate roda em `ddl-auto=validate`, ou seja, **não cria nem altera tabelas**, apenas valida o mapeamento contra o schema versionado.
- **Soft delete** aplicado automaticamente nas entidades JPA com `@SQLRestriction("deleted_at IS NULL")` e reforçado no banco com **índices únicos parciais**:

```sql
CREATE UNIQUE INDEX uk_users_email_active
    ON users (email)
    WHERE deleted_at IS NULL;
```

> **Exceção intencional:** a entidade `Reservation` **não** usa soft delete. Seu ciclo de vida é controlado exclusivamente pelo `status` (`CONFIRMED` → `CANCELLED`); cancelar não "apaga" a reserva, apenas muda seu estado.

### Segurança

Autenticação **stateless** baseada em **JWT (Bearer token)**:

- `SecurityConfig` — sessão `STATELESS`, CSRF desabilitado, `@EnableMethodSecurity`.
- `JwtAuthenticationFilter` — lê o header `Authorization` e popula o `SecurityContext`.
- `JwtService` — emite e valida tokens (`jjwt`).
- Rotas públicas: `/auth/**` (login e registro) e `/swagger-ui/**`, `/v3/api-docs/**`. Todas as demais exigem autenticação.
- **Autorização** combina `@PreAuthorize` (ex.: `hasRole('ADMIN')`) nos controllers com regras de negócio dentro dos use cases.

O domínio **não conhece o Spring Security**, depende apenas da porta `AuthenticatedUserProvider`, implementada na camada de infraestrutura:

```java
public interface AuthenticatedUserProvider {
    UUID getUserId();
    String getRole();
    boolean hasRole(String role);
}
```

### Controle de concorrência (foco do projeto)

A criação de reservas precisa garantir que **não existam duas reservas `CONFIRMED` para o mesmo `TimeSlot` na mesma data**. Sem proteção, esse fluxo sofre uma **race condition** clássica:

```text
Thread A verifica reserva → não encontra
Thread B verifica reserva → não encontra
A cria
B cria
Resultado: 2 reservas CONFIRMED para o mesmo slot/data
```

#### Solução: lock pessimista no `TimeSlot`

A solução adotada (em `CreateReservationUseCase`, dentro de uma transação `@Transactional`) é adquirir um **lock pessimista de escrita sobre a linha do `TimeSlot`** logo no início do fluxo, **antes** da verificação de conflito:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select ts from TimeSlotJpaEntity ts where ts.id = :id")
Optional<TimeSlotJpaEntity> findByIdForUpdate(UUID id);
```

Ordem de execução no caso de uso:

```text
1. timeSlotRepository.findByIdForUpdate(timeSlotId)   ← adquire o lock (ou 404 se não existir)
2. existsConfirmedReservation(timeSlotId, data)        ← checa conflito já protegido pelo lock
3. resolve o userId (regra USER/ADMIN)
4. se houver conflito → ReservationConflictException
5. caso contrário → cria e salva a reserva
```

#### Decisão arquitetural: por que travar o `TimeSlot` e não a tabela `reservations`?

- A criação **já precisa buscar o `TimeSlot`** para validar sua existência (404 imediato se não existir). Como esse é o **primeiro passo do fluxo**, é o ponto natural para adquirir o lock.
- A partir desse lock, dentro da transação, **todo o restante do fluxo tem exclusão mútua**: nenhuma outra requisição para o **mesmo `TimeSlot`** prossegue até a transação atual terminar (commit/rollback).
- Travar a tabela `reservations` **não faria sentido**: o objetivo é impedir a criação de **duas reservas simultâneas**. Duas requisições no mesmo instante não conseguiriam "enxergar" a reserva uma da outra (que ainda não foi commitada), então **não haveria linha de reserva para travar**. O `TimeSlot`, por já existir, funciona como o **lock natural** que serializa a disputa.

No PostgreSQL, o lock gera um `... FOR NO KEY UPDATE`, garantindo exclusão mútua por linha de `TimeSlot` e mantendo a invariante de **no máximo uma reserva `CONFIRMED` por slot/data**.

Esse comportamento é validado por um teste real de concorrência (`ReservationConcurrencyIT`) usando `ExecutorService`, `CountDownLatch` e `Future`, que dispara múltiplas threads contra o mesmo slot/data e verifica que apenas **uma** reserva é confirmada.

---

## Endpoints da API

> Todas as rotas (exceto `/auth/**` e a documentação) exigem autenticação via header `Authorization: Bearer <token>`. As respostas usam o envelope padrão `ApiResponse<T>`.

### Autenticação — `/auth`

| Método | Rota | Descrição | Acesso |
|--------|------|-----------|--------|
| `POST` | `/auth/register` | Registra um usuário e retorna um JWT | Público |
| `POST` | `/auth/login` | Autentica e retorna um JWT | Público |

### Usuários — `/user`

| Método | Rota | Descrição | Acesso |
|--------|------|-----------|--------|
| `GET` | `/user` | Lista usuários | ADMIN |
| `GET` | `/user/{id}` | Busca usuário por id | Dono ou ADMIN |
| `POST` | `/user` | Cria usuário | ADMIN |
| `DELETE` | `/user/{id}` | Desativa (soft delete) usuário | Dono ou ADMIN |

### Calendários — `/calendars`

| Método | Rota | Descrição | Acesso |
|--------|------|-----------|--------|
| `GET` | `/calendars` | Lista calendários | ADMIN |
| `GET` | `/calendars/{id}` | Busca calendário por id | ADMIN |
| `POST` | `/calendars` | Cria calendário | ADMIN |
| `DELETE` | `/calendars/{id}` | Arquiva (soft delete) calendário | ADMIN |

> Todo o controller de calendários é restrito a **ADMIN** (`@PreAuthorize("hasRole('ADMIN')")` em nível de classe).

### Horários — `/timeslots`

| Método | Rota | Descrição | Acesso |
|--------|------|-----------|--------|
| `GET` | `/timeslots` | Lista horários | Autenticado |
| `GET` | `/timeslots/calendar/{calendarId}` | Lista horários de um calendário | Autenticado |
| `GET` | `/timeslots/{id}` | Busca horário por id | Autenticado |
| `POST` | `/timeslots` | Cria horário | ADMIN |
| `DELETE` | `/timeslots/{id}` | Arquiva (soft delete) horário | ADMIN |

### Reservas — `/reservations`

| Método | Rota | Descrição | Acesso |
|--------|------|-----------|--------|
| `GET` | `/reservations` | Lista todas as reservas | ADMIN |
| `GET` | `/reservations/user/{userId}` | Lista reservas de um usuário | Dono ou ADMIN |
| `GET` | `/reservations/timeslot/{timeSlotId}` | Lista reservas de um horário | ADMIN |
| `GET` | `/reservations/{id}` | Busca reserva por id | Dono ou ADMIN |
| `POST` | `/reservations` | Cria reserva (protegida por lock pessimista) | Autenticado |
| `DELETE` | `/reservations/{id}` | Cancela reserva | Dono ou ADMIN |

> Na criação, um **USER** sempre cria a reserva para si mesmo (o `userId` enviado no corpo é ignorado e usa-se o usuário autenticado). Um **ADMIN** pode criar reserva para qualquer usuário informando o `userId`. Acesso "Dono ou ADMIN" significa que o próprio dono do recurso ou um ADMIN podem acessar; caso contrário retorna `403 Forbidden`.

---

## Como Executar

### Pré-requisitos

- **Java 21**
- **Maven** (ou use o wrapper `./mvnw` incluído no projeto)
- **Docker** e **Docker Compose** (para subir o PostgreSQL e para os testes de integração com Testcontainers)

> Não é necessário ter um PostgreSQL instalado localmente: o banco sobe via Docker Compose.

### Configuração do Ambiente

1. **Clone o repositório:**

```bash
git clone https://github.com/Tauan-Ray/booking-system-concurrency.git
cd booking-system-concurrency
```

2. **Crie o arquivo `.env`** a partir do exemplo (usado pelo Docker Compose para subir o banco):

```bash
cp .env.example .env
```

Variáveis disponíveis no `.env`:

```properties
JWT_SECRET=troque-por-um-segredo-base64-forte
JWT_EXPIRATION=86400000

POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=agendamento
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

3. **Crie o arquivo de configuração da aplicação** a partir do exemplo:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

> Mantenha os valores de `application.properties` (banco `agendamento`, usuário/senha `postgres`, porta `5432`) alinhados com o que estiver no `.env`. As tabelas e o schema são criados automaticamente pelo **Flyway** na primeira execução.

### Subindo o Banco de Dados (Docker)

Na raiz do projeto, suba o PostgreSQL em segundo plano:

```bash
docker compose up -d
```

Esse comando inicializa um container PostgreSQL (`agendamento_db`) já configurado com as variáveis do `.env`, com volume persistente e healthcheck.

Para acompanhar os logs ou encerrar o banco:

```bash
docker compose logs -f postgres-agendamento   # acompanhar logs
docker compose down                            # encerrar (mantém o volume de dados)
```

### Executando a Aplicação

Com o banco no ar, na raiz do projeto execute:

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em `http://localhost:8080`. Ao iniciar, o Flyway aplica todas as migrations (V1–V6) e o Hibernate valida o schema.

### Documentação da API (OpenAPI/Swagger)

Com a aplicação rodando, a documentação interativa fica disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

A especificação OpenAPI em JSON:

```text
http://localhost:8080/v3/api-docs
```

> A documentação é gerada automaticamente a partir das anotações OpenAPI, que ficam concentradas em interfaces `*ControllerDocs` para manter os controllers enxutos. Use o esquema de autenticação `bearerAuth` para informar o JWT no Swagger UI.

### Executando os Testes

O projeto possui testes unitários (use cases, entidades e Value Objects) e testes de integração com **Testcontainers** (que sobem um PostgreSQL real, por isso o Docker é necessário para essa parte).

```bash
./mvnw test
```

Destaques da suíte de integração:

- `CreateReservationIT` — criação de reservas e regras de permissão.
- `CancelReservationIT` — cancelamento e liberação de horário.
- `ReservationConcurrencyIT` — teste real de concorrência, validando que apenas **uma** reserva é confirmada para o mesmo slot/data sob disputa de múltiplas threads.
