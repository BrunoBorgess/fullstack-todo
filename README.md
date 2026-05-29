# fullstack-todo

Aplicação full stack de gerenciamento de tarefas com autenticação de usuários.

## Tecnologias

**Backend**
- Kotlin + Spring Boot
- Spring Security + JWT
- SQLite
- JUnit 5 (testes unitários)

**Frontend**
- Astro + React
- Tailwind CSS
- Axios

---

## Pré-requisitos

- Java 17+
- Node.js 22+
- npm

---

## Como rodar

### Backend

```bash
cd backend
./gradlew bootRun
```

> O servidor inicia em `http://localhost:8080`

O banco de dados SQLite (`todo.db`) é criado automaticamente na primeira execução.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

> O servidor inicia em `http://localhost:4321`

---

## Funcionalidades

- Cadastro e login de usuários com JWT
- Cada usuário vê e gerencia apenas suas próprias tarefas
- CRUD completo de tarefas (criar, listar, editar, concluir, excluir)
- Rotas protegidas no frontend e no backend

---

## Estrutura do projeto

```
fullstack-todo/
├── backend/
│   └── src/main/kotlin/com/todo/backend/
│       ├── controller/       # AuthController, TaskController
│       ├── service/          # AuthService, TaskService
│       ├── model/            # User, Task
│       ├── repository/       # Repositories
│       ├── security/         # JwtFilter, JwtService, SecurityConfig
│       └── dto/              # Request/Response DTOs
└── frontend/
    └── src/
        ├── pages/            # index.astro, tasks.astro
        ├── components/       # Login.tsx, TaskList.tsx, api.ts
        ├── layouts/          # Layout.astro
        └── styles/           # global.css
```

---

## Endpoints da API

### Auth

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/register` | Cadastro de usuário |
| POST | `/api/auth/login` | Login e geração do token JWT |

### Tarefas (requer autenticação)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/tasks` | Listar tarefas do usuário |
| POST | `/api/tasks` | Criar tarefa |
| PUT | `/api/tasks/{id}` | Editar tarefa |
| DELETE | `/api/tasks/{id}` | Excluir tarefa |

---

## Testes

```bash
cd backend
./gradlew test
```

Testes unitários cobrem:
- Cadastro e autenticação de usuários
- CRUD de tarefas
