# Spring Security API

API de estudo com Spring Boot 4, Spring Security, persistência de usuários com Spring Data JPA e MySQL. O login recebe JSON por um filtro de autenticação customizado; as demais rotas exigem autenticação e algumas verificam papéis.

## Requisitos

- Java 25
- Maven
- Docker com Docker Compose

## Executar

No diretório `springSecurity`, execute:

```powershell
mvn spring-boot:run
```

A integração Docker Compose do Spring Boot inicia o serviço `database` definido em `compose.yml`. O MySQL fica disponível na porta `3307` do host, com o banco `proposals`.

## Login

Envie `POST /api/auth/login` com `Content-Type: application/json`:

```json
{
  "username": "fitness_vibe",
  "password": "password"
}
```

Quando a autenticação é bem-sucedida, a aplicação responde `200 OK` e mantém a autenticação em uma sessão HTTP. A resposta não contém um token JSON.

No PowerShell, `curl.exe` permite guardar e reutilizar o cookie de sessão:

```powershell
curl.exe -i -c session.txt -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"fitness_vibe","password":"password"}'

curl.exe -b session.txt http://localhost:8080/
```

## Usuários de demonstração

Na primeira inicialização, se o banco estiver vazio, a aplicação cria estes usuários. A senha de todos é `password`.

| Usuário | Papel armazenado | Acesso por papel configurado |
| --- | --- | --- |
| `fitness_vibe` | `ROLE_USERNAME` | `/user` (há uma divergência descrita abaixo) |
| `tech_guru` | `ROLE_USERNAME` | `/user` (há uma divergência descrita abaixo) |
| `logistics` | `ROLE_BRAND` | `/brand` |

## Rotas

| Método | Rota | Autenticação e autorização |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Pública; autentica com o JSON `username` e `password` |
| `GET` | `/` | Requer sessão autenticada; responde com uma saudação ao usuário |
| `GET` | `/user` | Requer `ROLE_roles` pela regra atual de `@PreAuthorize` |
| `GET` | `/brand` | Requer `ROLE_BRAND` |

**Atenção:** os usuários comuns são criados com `ROLE_USERNAME`, mas a regra atual de `/user` usa `hasRole('roles')`, que procura por `ROLE_roles`. Por isso, os usuários seed não passam nessa autorização e recebem `403 Forbidden` nessa rota. A regra deverá ser alinhada com o papel desejado no código.

## Banco de dados

O `compose.yml` define MySQL 9.6, banco `proposals`, usuário `app` e senha `app`. O Spring Boot detecta o Compose e fornece a conexão à aplicação.

Em `application.properties`, `spring.jpa.hibernate.ddl-auto=create` recria o schema quando a aplicação inicia. **Isso apaga os dados existentes**, inclusive usuários; altere essa configuração antes de usar dados que precisem ser preservados.
