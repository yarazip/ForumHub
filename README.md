# 📌 ForumHub

**ForumHub** é uma aplicação desenvolvida em **Java Spring Boot** como parte do **Challenge Back End** do programa **Oracle Next Education (ONE)** em parceria com a **Alura**.
O projeto simula um fórum de perguntas e respostas, permitindo o cadastro de usuários, autenticação com **JWT**, criação de tópicos e respostas, além de recursos de segurança e boas práticas de API REST.

---

## 📖 História

Um **fórum** é um espaço onde os participantes de uma plataforma podem colocar suas perguntas sobre determinados assuntos.
Na **Alura**, os alunos e alunas utilizam o fórum para tirar dúvidas sobre os cursos e projetos em que participam, gerando um ambiente de **aprendizagem e colaboração** entre estudantes, professores e moderadores.

👉 Esse é o desafio do **FórumHub**: replicar esse processo no **back-end**, construindo uma **API REST** com **Spring Boot**.

### 🎯 Objetivo

A API se concentra na gestão de **tópicos**, oferecendo:

* Criar um novo tópico;
* Listar todos os tópicos criados;
* Visualizar um tópico específico;
* Atualizar um tópico existente;
* Excluir um tópico.

Em outras palavras, um **CRUD completo (Create, Read, Update, Delete)**.

### 🛠️ Requisitos do desafio

* API seguindo boas práticas do modelo REST;
* Validações baseadas nas regras de negócio;
* Base de dados relacional para persistência;
* Autenticação e autorização via **JWT** para restringir acessos.

---

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot** (Web, Security, Data JPA, Validation)
* **PostgreSQL** (banco de dados relacional)
* **Redis** (cache e rate limiting)
* **Swagger/OpenAPI** (documentação da API)
* **Maven** (gerenciador de dependências)
* **JWT (JSON Web Token)** para autenticação e autorização

---

## ⚙️ Funcionalidades

* ✅ Cadastro e autenticação de usuários
* ✅ Login com **JWT**
* ✅ CRUD de tópicos
* ✅ Respostas vinculadas a tópicos
* ✅ Paginação e ordenação
* ✅ Configuração de **CORS** para integração com frontend
* ✅ **Rate limit** para segurança extra
* ✅ Documentação interativa com **Swagger UI**

---

## 📂 Estrutura do Projeto

```
forumhub/
 ├── src/main/java/br/com/yarazip/forumhub
 │   ├── controllers/      # Controladores REST
 │   ├── dtos/             # Objetos de transferência de dados
 │   ├── models/           # Entidades JPA
 │   ├── repositories/     # Interfaces JPA Repository
 │   ├── security/         # Configurações de segurança e filtros JWT
 │   ├── services/         # Regras de negócio
 │   └── ForumhubApplication.java
 │
 ├── src/main/resources/
 │   ├── application.yml        # Configurações comuns
 │   ├── application-dev.yml    # Configurações de desenvolvimento
 │   └── application-prod.yml   # Configurações de produção
 │
 └── pom.xml
```

---

## 🛠️ Como Rodar o Projeto

### 📌 Pré-requisitos

* Java 17+
* Maven 3.9+
* PostgreSQL rodando localmente (ou via Docker)

### ▶️ Passos

```bash
# Clonar o repositório
git clone https://github.com/yarazip/forumhub.git
cd forumhub

# Rodar em dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 📖 Documentação da API

Com a aplicação rodando, acesse no navegador:

👉 **Swagger UI**:

```
http://localhost:8080/swagger-ui.html
```

---

## 👨‍🏫 Sobre o Desafio

Este projeto faz parte do **Challenge Back End – FórumHub**, proposto pelo programa **Oracle Next Education (ONE)** em parceria com a **Alura**.

---

## 📜 Licença

Este projeto é distribuído sob a licença **MIT**.

---

## ✨ Autoria

👩‍💻 Desenvolvido por **Yara Rosa**
🚀 Desafio proposto por **Oracle Next Education (ONE)** e **Alura**
