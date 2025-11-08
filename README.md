<h1 align="center">🧾 ERP System — Desafio Técnico Java</h1>

<p align="center">
  <b>API REST para gestão de Clientes, Produtos e Pedidos</b><br>
  Desenvolvido em <b>Spring Boot + PostgreSQL + JPA + JUnit</b>.<br>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-red?logo=openjdk" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?logo=springboot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql"/>
  <img src="https://img.shields.io/badge/Build-Maven-orange?logo=apachemaven"/>
  <img src="https://img.shields.io/badge/Tests-JUnit5-green?logo=junit5"/>
</p>

---

## 🧠 Visão geral

Este projeto implementa um **mini-ERP backend** com os módulos:
- 👥 **Clientes**
- 📦 **Produtos**
- 🧾 **Pedidos e Itens de Pedido**

A API permite **cadastrar, listar e gerenciar pedidos**, validando **estoque**, **cálculo de total** e garantindo **transações consistentes** com JPA e Hibernate.

---

## 🏗️ Arquitetura

```
erp-system/

├── pom.xml

├── README.md

├── docker-compose.yml

└── src/

├── main/

│ ├── java/

│ │ └── com/

│ │ └── desafio_pleno/

│ │ └── erp_system/

│ │ ├── FreireApplication.java

│ │ ├── controller/

│ │ ├── service/

│ │ ├── repository/

│ │ ├── model/

│ │ ├── dto/

│ │ └── exception/

│ └── resources/

│ └── application.yml

└── test/

└── java/

└── com/

└── desafio_pleno/

└── erp_system/

└── PedidoServiceTest.java
```

📦 **Camadas:**
- `controller` → Endpoints REST (`@RestController`)
- `service` → Regras de negócio (`@Service`, `@Transactional`)
- `repository` → Persistência com `JpaRepository`
- `model` → Entidades JPA e enums
- `dto` → Transferência de dados (entrada/saída)
- `exception` → Tratamento centralizado de erros

---

## ⚙️ Configuração do ambiente

### 🐳 Subindo o banco via Docker
```
bash
docker compose up -d

docker exec -it erp-system-postgres-1 psql -U dev -d appdb
```
docker-compose.yml
```
version: '3.8'
services:
  postgres:
    image: postgres:14
    container_name: pg-local
    environment:
      POSTGRES_USER: dev
      POSTGRES_PASSWORD: dev
      POSTGRES_DB: appdb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```
⚙️ application.yml
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/appdb
    username: dev
    password: dev
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate.format_sql: true
server:
  port: 8080
```
▶️ Como rodar o projeto

💻 Via Maven
```
bash
mvn spring-boot-run
```
💡 Ou via IntelliJ IDEA / VSCode

Abra:
```
swift
src/main/java/com/desafio_pleno/erp_system/FreireApplication.java
```
🌐 Endpoints principais

| Método | Endpoint            | Descrição                               |
| ------ | ------------------- | --------------------------------------- |
| `POST` | `/api/clientes`     | Cadastrar cliente                       |
| `GET`  | `/api/clientes`     | Listar clientes                         |
| `POST` | `/api/produtos`     | Cadastrar produto                       |
| `GET`  | `/api/produtos`     | Listar produtos                         |
| `POST` | `/api/pedidos`      | Criar pedido (com validação de estoque) |
| `GET`  | `/api/pedidos/{id}` | Detalhar pedido com itens               |

🧾 Exemplo — Criar Pedido

POST /api/pedidos
```
{
  "clienteId": 1,
  "itens": [
    { "produtoId": 2, "quantidade": 3 },
    { "produtoId": 5, "quantidade": 1 }
  ]
}
```
Resposta
```
{
  "id": 10,
  "cliente": "Maria Oliveira",
  "dataCriacao": "2025-11-08T14:32:45Z",
  "status": "CRIADO",
  "total": 185.90,
  "itens": [
    { "produto": "Teclado", "quantidade": 3, "subtotal": 120.00 },
    { "produto": "Mouse", "quantidade": 1, "subtotal": 65.90 }
  ]
}
```
🧪 Testes automatizados
```
bash
mvn test
```
Principais testes:

✅ PedidoServiceTest — valida regra de estoque e cálculo do total

✅ ProdutoRepositoryTest — operações CRUD básicas

✅ PedidoControllerTest — integração dos endpoints REST

🧩 Stack Técnica

| Categoria | Tecnologia                  |
| --------- | --------------------------- |
| Framework | Spring Boot 3               |
| ORM       | Spring Data JPA / Hibernate |
| Banco     | PostgreSQL                  |
| Testes    | JUnit 5 / Mockito           |
| Build     | Maven                       |
| Docs      | Springdoc OpenAPI           |
| Container | Docker Compose              |

- Regras de Negócio

🚫 Pedido não pode ser criado se algum produto não tiver estoque.

🧮 O total do pedido é calculado automaticamente com base nos itens.

📉 O estoque é reduzido ao salvar o pedido.

🔁 Cancelamentos (opcional) restauram o estoque.

👨‍💻 Autor

 - Guilherme Freire

   - 💼 Desenvolvedor Java • Bacharelado em Engenharia de Software • Pós-graduação em Java

   - 📧 contato: freireguilherme2@gmail.com
