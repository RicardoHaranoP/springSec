# Spring Security Proposal API

Este projeto é uma API REST em Spring Boot para gerenciamento de propostas, com autenticação e autorização via Spring Security.

## Visão geral

A aplicação foi estruturada em duas grandes partes:

- `auth`: autenticação, usuários e segurança
- `proposal`: domínio de propostas, casos de uso e endpoints HTTP

O fluxo principal é:

1. o usuário faz login em `/api/auth/login`
2. o Spring Security autentica o usuário com `username` e `password`
3. a sessão HTTP mantém o usuário autenticado
4. o controller de propostas valida as roles e executa as regras de negócio

## Requisitos

- Java 25
- Maven
- Docker Desktop ou Docker Engine

## Executar o projeto

1. Inicie o banco MySQL:

```bash
docker compose up -d
```

2. Rode a aplicação:

```bash
mvn spring-boot:run
```

A aplicação fica disponível em:

```text
http://localhost:8080
```

## Banco de dados

O arquivo `compose.yml` define um container MySQL com:

- banco: `proposals`
- usuário: `app`
- senha: `app`
- porta local: `3307`

O Spring Boot usa esse container automaticamente ao iniciar a aplicação.

## Usuários de teste

Na primeira execução, se o banco estiver vazio, o sistema cria usuários de demonstração:

| Usuário | Senha | Papel |
| --- | --- | --- |
| `fitness_vibe` | `password` | `ROLE_USERNAME` |
| `tech_guru` | `password` | `ROLE_USERNAME` |
| `logistics` | `password` | `ROLE_BRAND` |

## Autenticação

Este projeto usa autenticação customizada, sem JWT. O login é feito por um filtro REST e a autenticação é mantida pela sessão HTTP do Spring Security.

### Endpoint de login

```http
POST /api/auth/login
Content-Type: application/json
```

### JSON de login

```json
{
  "username": "logistics",
  "password": "password"
}
```

### Exemplo com curl

```bash
curl -c cookies.txt -H "Content-Type: application/json" \
  -d '{"username":"logistics","password":"password"}' \
  http://localhost:8080/api/auth/login
```

> O arquivo `cookies.txt` guarda o cookie da sessão. Você precisa reutilizá-lo nas chamadas autenticadas.

## Rotas da API

### 1) Criar proposta

```http
POST /api/proposals
```

Regras:

- exige autenticação
- exige role `ROLE_USERNAME`

Body exemplo:

```json
{
  "title": "Campanha de verão",
  "description": "Quero divulgar um pacote para influenciadores de fitness."
}
```

Exemplo:

```bash
curl -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
      "title": "Campanha de verão",
      "description": "Quero divulgar um pacote para influenciadores de fitness."
    }' \
  http://localhost:8080/api/proposals
```

### 2) Listar propostas

```http
GET /api/proposals
```

Regras:

- exige autenticação
- aceita `ROLE_USERNAME` e `ROLE_BRAND`

Exemplo:

```bash
curl -b cookies.txt http://localhost:8080/api/proposals
```

## Como testar no Postman

1. Faça login em:

```http
POST http://localhost:8080/api/auth/login
```

2. Envie o JSON:

```json
{
  "username": "logistics",
  "password": "password"
}
```

3. O Postman manterá o cookie da sessão automaticamente se você usar a aba "Cookie" ou "Session".

4. Depois, chame:

```http
GET http://localhost:8080/api/proposals
```

ou

```http
POST http://localhost:8080/api/proposals
```

## Exemplo de resposta

Uma proposta pode responder algo assim:

```json
{
  "id": "...",
  "title": "Campanha de verão",
  "description": "Quero divulgar um pacote para influenciadores de fitness.",
  "owner": {
    "id": "...",
    "name": "fitness_vibe"
  }
}
```

## Permissões

- `ROLE_USERNAME`: pode criar propostas e consultar as próprias propostas
- `ROLE_BRAND`: pode consultar todas as propostas

Se você receber `403 Forbidden`, normalmente significa que:

- o usuário não fez login
- o cookie de sessão não foi enviado
- a role do usuário não bate com a autorização da rota

## Estrutura principal

```text
src/
  main/
    java/
      org/springSec/
        auth/
        proposal/
```

## Observações finais

- O projeto usa autenticação baseada em sessão do Spring Security.
- Não há JWT implementado neste momento.
- O banco é recriado ao iniciar em alguns cenários de desenvolvimento, então os dados de teste podem ser perdidos entre reinicializações.

