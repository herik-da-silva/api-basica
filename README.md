# Gerenciamento de Produtos API

Este é um projeto básico de uma API RESTful desenvolvido em **Java 21** utilizando **Spring Boot 3.3.4**. O objetivo principal deste projeto é aprimorar conhecimentos em **segurança** e **boas práticas de desenvolvimento**, além de revisitar conceitos fundamentais que, com o tempo, podem se tornar automáticos e pouco intuitivos.

---

## 🚀 Objetivos do Projeto

1. Aprimorar habilidades relacionadas a:
   - **Segurança** em APIs.
   - Implementação de **boas práticas** no desenvolvimento.
2. Revisitar conceitos básicos de:
   - **CRUD** (Create, Read, Update, Delete).
   - Estrutura e design de APIs RESTful.
   - Validação de dados e gestão de erros.
3. Consolidar o uso de ferramentas e frameworks modernos com o Spring Boot.

---

## ⚙️ Tecnologias Utilizadas

- **Java**: versão 21.
- **Spring Boot**: versão 3.3.4.
  - **Spring Web**: para construção da API REST.
  - **Spring Data JPA**: para integração com banco de dados.
  - **Spring Security**: para controle de autenticação e autorização.
- **Banco de Dados**: Oracle
- **Maven**: para gerenciamento de dependências.

---

## 🛠️ Funcionalidades

- Gerenciamento de Produtos:
  - Cadastro de novos produtos.
  - Listagem de produtos (com filtros e paginação*).
  - Atualização de informações dos produtos.
  - Exclusão de produtos.
- Validação de dados de entrada.
- Tratamento centralizado de erros.
- Padrões de segurança básicos, incluindo autenticação e autorização.

---

## 🏁 Como Executar

### Pré-requisitos

- **Java 21** instalado.
- **Maven** configurado.
- Banco de dados configurado*.

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/herik-da-silva/api-basica.git
   cd api-basica
   ```

2. Instale as dependências:
   ```bash
   mvn clean install
   ```

3. Configure o banco de dados no arquivo `application.properties` ou utilize o H2 (padrão)*.

4. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

5. Acesse a aplicação:
   - API: `http://localhost:8080/api/v1/produtos`
   - Console H2 (se habilitado): `http://localhost:8080/h2-console`

---

## 🔒 Segurança

O projeto implementa as seguintes medidas básicas de segurança:

- **Autenticação JWT** (JSON Web Token).
- **Autorização baseada em perfis de usuário**.
- Configuração de **CORS** (Cross-Origin Resource Sharing).

---

## 🔧 Melhorias Futuras

- Adicionar testes unitários e de integração.
- Implementar documentação com **Swagger/OpenAPI**.
- Criar um pipeline de CI/CD.
- Melhorar a cobertura de casos de uso.

---

## 📝 Contribuições

Este é um projeto pessoal e educativo. Sugestões, melhorias e críticas construtivas são sempre bem-vindas!

---
