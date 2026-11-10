# LogiTrack

Projeto da disciplina de NoSQL — plataforma de logística e entregas com MongoDB e Spring Boot.

## Integrantes

- Murilo Vinícius Lima de Araújo
- Lismara Santos Moreira
- Ícaro Assis Oliveira Maia
- João Paulo de Oliveira Carneiro
- Enzo Cabral Lopes

## Sobre o projeto

A ideia da LogiTrack é acompanhar o pedido desde a criação até a entrega final, e também permitir análises do tipo: onde está a entrega, quais regiões atrasam mais, desempenho de motoristas, rotas e centros de distribuição.

Fluxo que usamos no domínio:

```text
Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega
```

## Objetivo

Transformar a modelagem das atividades anteriores em uma aplicação Java funcional (Spring Boot + MongoDB), mantendo a coerência entre entidades, relacionamentos e regras de negócio.

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring Data MongoDB
- Spring Web
- MongoDB / mongosh
- Maven

## Estrutura do repositório

```text
nosql-LogiTrack/
├── modeling/          # entidades e relacionamentos (Atividade 3)
├── mongodb/           # scripts do banco (criar, inserir, consultar)
├── docs/              # arquitetura, API e decisões de implementação
├── src/main/java/...  # models, repositories, services, controllers, exception
├── src/test/java/...  # testes das regras de negócio
├── pom.xml
└── README.md
```

Detalhes da modelagem: [`modeling/entities.md`](modeling/entities.md) e [`modeling/relationships.md`](modeling/relationships.md).

## Como subir o MongoDB

Com o MongoDB rodando, na raiz do projeto:

```text
mongosh mongodb/01_database.js
mongosh mongodb/02_collections.js
mongosh mongodb/03_insert_data.js
mongosh mongodb/04_queries.js
mongosh mongodb/05_relationships.js
```

O script `01` recria o banco `logitrack`. Depois cria as collections, insere exemplos e roda as consultas.

## Como rodar a aplicação

1. Abrir a pasta do projeto no IntelliJ (`File → Open`)
2. Esperar o Maven baixar as dependências
3. Ter o MongoDB local na porta `27017` (banco `logitrack`)
4. (Opcional) rodar os scripts `01` a `03` para ter dados de exemplo
5. Executar a classe `AplicacaoApiApplication`
6. A API sobe em `http://localhost:8080`

Configuração em `src/main/resources/application.properties`:

```properties
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=logitrack
```

Não colocar senha ou credencial no repositório.

## Entidades e modelagem

Temos **10 collections**:

`clientes`, `pedidos`, `produtos`, `entregas`, `centrosDistribuicao`, `motoristas`, `veiculos`, `transportadoras`, `ocorrencias`, `rastreamentos`

**Embedded (dentro do documento pai):** Endereco, Volume, Pagamento, Dimensoes, Rota, HorariosEtapas, RastreioAtual.

**References (id em outra collection):** `idCliente`, `idProduto`, `idPedido`, `idMotorista`, `idVeiculo`, `idTransportadora`, `idCentroDistribuicao`, `idEntrega`, etc.

Como o MongoDB não garante integridade referencial sozinho, a validação fica nos services (por exemplo, não criar entrega com pedido ou motorista que não existe).

## API REST

CRUD completo em três recursos:

| Recurso  | Base             |
| -------- | ---------------- |
| Clientes | `/api/clientes`  |
| Pedidos  | `/api/pedidos`   |
| Entregas | `/api/entregas`  |

Em cada um: POST (criar), GET (listar / buscar por id), PUT (atualizar), DELETE (excluir).

Alguns endpoints extras que fizemos:

- `GET /api/pedidos/{id}/com-cliente`
- `GET /api/entregas/{id}/relacionados`
- `GET /api/entregas?atrasadas=true`
- `GET /api/pedidos?idCliente=...`

Exemplos de request/response e códigos HTTP estão em [`docs/api.md`](docs/api.md).

Dá para testar no Postman ou no Insomnia usando esses exemplos.

## Regras de negócio

1. Não cadastrar cliente com e-mail duplicado → **409**
2. Não cadastrar pedido sem volumes → **400**
3. Não criar entrega se o pedido ou o motorista não existir → **400**

Mais detalhes sobre integridade e decisões: [`docs/implementation.md`](docs/implementation.md).

## Documentação

- [`docs/architecture.md`](docs/architecture.md) — camadas e pacotes
- [`docs/implementation.md`](docs/implementation.md) — decisões da implementação
- [`docs/api.md`](docs/api.md) — endpoints e exemplos
- [`modeling/entities.md`](modeling/entities.md) — entidades
- [`modeling/relationships.md`](modeling/relationships.md) — embed vs reference
