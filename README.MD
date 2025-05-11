# Ouvidoria Java

Sistema CRUD completo para gerenciamento de reclamações de clientes. Permite cadastro de usuários com dados pessoais, informações de contato e categoria da ocorrência. Suporte à listagem geral e filtragem por categoria. Persistência total em banco de dados relacional utilizando JDBC.


---

## 📦 Requisitos

- Java 11+ (JDK)
- MySQL Server
- Driver JDBC MySQL (`mysql-connector-java-x.x.x.jar`)

---

## 🔧 Configuração Inicial

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/seu-usuario/ouvidoria-java.git
   cd ouvidoria-java

2. **Crie a base de dados executando o script sql/schema.sql no seu MySQL:**

   ```bash
    mysql -u root -p < sql/schema.sql

3. **Copie e edite o arquivo de configuração:**
   ```bash
   cp config.properties.example config.properties

4. **Depois edite o config.properties com suas credenciais:**
   ````propeties
   db.url=jdbc:mysql://localhost:3306/ouvidoria
   db.user=seu_usuario
   db.password=sua_senha

5. **Compile e execute o projeto (via terminal):**
   ```bash
      javac -cp lib/mysql-connector-java-x.x.x.jar src/br/com/dio/Ouvidoria.java
      java -cp "lib/mysql-connector-java-x.x.x.jar:src" br.com.dio.Ouvidoria

No Windows, use ; ao invés de : no classpath.
