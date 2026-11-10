# LogiTrack

Projeto da disciplina de NoSQL — logística e entregas com MongoDB e Spring Boot.

## Integrantes

- Murilo Vinícius Lima de Araújo
- Lismara Santos Moreira
- Ícaro Assis Oliveira Maia
- João Paulo de Oliveira Carneiro
- Enzo Cabral Lopes

## Objetivo

Implementar a modelagem das atividades anteriores em uma API Java com Spring Boot e MongoDB.

Fluxo do domínio:

```text
Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega
```

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring Data MongoDB
- Spring Web / Validation
- MongoDB
- Maven

## Como subir o MongoDB

Com o MongoDB rodando:

```text
mongosh mongodb/01_database.js
mongosh mongodb/02_collections.js
mongosh mongodb/03_insert_data.js
mongosh mongodb/04_queries.js
mongosh mongodb/05_relationships.js
```

## Como rodar a aplicação

1. Abrir o projeto no IntelliJ
2. Esperar o Maven baixar as dependências
3. MongoDB em `localhost:27017` (banco `logitrack`)
4. Rodar os scripts 01 a 03
5. Executar `AplicacaoApiApplication`
6. API em `http://localhost:8080`

```properties
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=logitrack
```

## Entidades

10 collections: `clientes`, `pedidos`, `produtos`, `entregas`, `centrosDistribuicao`, `motoristas`, `veiculos`, `transportadoras`, `ocorrencias`, `rastreamentos`.

**Embedded:** Endereco, Volume, Pagamento, Dimensoes, Rota, HorariosEtapas, RastreioAtual.

**References:** `@DocumentReference` (Pedido→Cliente, Entrega→Pedido, Entrega→Motorista) e ids ObjectId nos demais relacionamentos.

## API

CRUD em:

| Recurso  | Base             |
| -------- | ---------------- |
| Clientes | `/api/clientes`  |
| Pedidos  | `/api/pedidos`   |
| Entregas | `/api/entregas`  |

Extras:

- `GET /api/pedidos/{id}/com-cliente`
- `GET /api/entregas/{id}/relacionados`
- `GET /api/entregas?atrasadas=true`
- `GET /api/pedidos?idCliente=...`
- `GET /api/entregas?idMotorista=...`

Exemplos em [`docs/api.md`](docs/api.md).  
Collection do Insomnia: [`docs/logitrack-insomnia.json`](docs/logitrack-insomnia.json).

## Regras de negócio

1. e-mail duplicado → 409
2. pedido sem volumes → 400
3. entrega com pedido/motorista inexistente → 400

Detalhes: [`docs/implementation.md`](docs/implementation.md).

## Docs

- [`docs/architecture.md`](docs/architecture.md)
- [`docs/implementation.md`](docs/implementation.md)
- [`docs/api.md`](docs/api.md)
- [`modeling/entities.md`](modeling/entities.md)
- [`modeling/relationships.md`](modeling/relationships.md)
