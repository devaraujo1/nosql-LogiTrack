# LogiTrack

Plataforma inteligente de logística e entregas. Projeto da disciplina de NoSQL (MongoDB).

## O que é o projeto

A LogiTrack acompanha a operação do **pedido até a entrega** e permite analisar desempenho: atrasos por região, motoristas, rotas, horários e centros de distribuição.

Fluxo do domínio:

```text
Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega
```

Perguntas que o sistema precisa responder:

1. Onde estão as entregas?
2. Quais regiões atrasam mais?
3. Qual motorista tem melhor desempenho?
4. Quais rotas são mais eficientes?
5. Quais horários geram atraso?
6. Qual centro de distribuição apresenta problemas?
7. Quanto tempo cada etapa demora?

## Objetivo

Construir, ao longo da disciplina, uma solução completa com MongoDB: levantamento de entidades, modelagem (embed vs referência), scripts de banco e, por fim, uma API Java com Spring Boot alinhada a essa modelagem.

## Integrantes

- Murilo Vinícius Lima de Araújo
- Lismara Santos Moreira
- Ícaro Assis Oliveira Maia
- João Paulo de Oliveira Carneiro
- Enzo Cabral Lopes

## Como o repositório está organizado

O projeto cresceu em etapas. Cada pasta tem um papel:

| Pasta / área | Papel |
| --- | --- |
| `modeling/` | Documentação das entidades e dos relacionamentos (embed × reference) |
| `mongodb/` | Scripts `mongosh` para criar o banco, collections, dados e consultas |
| `src/` | Aplicação Spring Boot (models, repositories, services, controllers) |
| `docs/` | Arquitetura, decisões de implementação e exemplos da API |
| `pom.xml` | Projeto Maven (`aplicacao-api`) |

```text
nosql-LogiTrack/
├── modeling/
│   ├── entities.md
│   └── relationships.md
├── mongodb/
│   ├── 01_database.js
│   ├── 02_collections.js
│   ├── 03_insert_data.js
│   ├── 04_queries.js
│   └── 05_relationships.js
├── docs/
│   ├── architecture.md
│   ├── implementation.md
│   └── api.md
├── src/
│   ├── main/java/br/com/logi_track/aplicacao_api/
│   │   ├── models/
│   │   ├── repositories/
│   │   ├── services/
│   │   ├── controllers/
│   │   ├── exception/
│   │   └── AplicacaoApiApplication.java
│   └── main/resources/application.properties
├── pom.xml
├── .gitignore
└── README.md
```

## Evolução do projeto

1. **Modelagem** — 14 entidades do domínio; 10 viram collection; Volume, Pagamento, Rota e Endereço ficam incorporados. Detalhes em [`modeling/entities.md`](modeling/entities.md) e [`modeling/relationships.md`](modeling/relationships.md).
2. **Scripts MongoDB** — criação do banco `logitrack`, validators, inserts de exemplo, consultas e percurso de relacionamentos em `mongodb/`.
3. **API Spring Boot** — as mesmas decisões de modelagem viram classes Java, repositories, regras de negócio e endpoints REST.

## Tecnologias

- MongoDB
- mongosh
- Java 25
- Spring Boot 4.1.2
- Spring Data MongoDB
- Spring Web
- Maven

## Dados no MongoDB

### Collections

`clientes`, `pedidos`, `produtos`, `entregas`, `centrosDistribuicao`, `motoristas`, `veiculos`, `transportadoras`, `ocorrencias`, `rastreamentos`

### Documentos incorporados (embedded)

Endereço, Volume, Pagamento, Dimensões, Rota, horários das etapas, rastreio atual — guardados **dentro** do documento pai.

### Referências

Campos `idCliente`, `idProduto`, `idPedido`, `idMotorista`, `idVeiculo`, `idTransportadora`, `idCentroDistribuicao`, `idEntrega`, etc. A integridade referencial é tratada na aplicação (services), não automaticamente pelo MongoDB.

## Como preparar o banco (scripts)

Com o MongoDB rodando e o `mongosh` instalado, na raiz do repositório:

```text
mongosh mongodb/01_database.js
mongosh mongodb/02_collections.js
mongosh mongodb/03_insert_data.js
mongosh mongodb/04_queries.js
mongosh mongodb/05_relationships.js
```

A ordem importa: o primeiro recria o banco `logitrack`; o segundo cria as collections; o terceiro insere exemplos; os dois últimos consultam.

## Como rodar a API (Spring Boot)

1. IntelliJ → **File → Open** → pasta deste repositório (`nosql-LogiTrack`).
2. Aguarde o Maven baixar as dependências.
3. MongoDB local na porta `27017` (banco `logitrack`).
4. (Opcional) rode os scripts `01`–`03` acima para ter dados de exemplo.
5. Execute `AplicacaoApiApplication`.
6. API em `http://localhost:8080`.

Configuração em `src/main/resources/application.properties`:

```properties
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=logitrack
```

Não publique senhas ou credenciais no repositório.

## API REST (visão geral)

CRUD completo para:

| Recurso | Base |
| --- | --- |
| Clientes | `/api/clientes` |
| Pedidos | `/api/pedidos` |
| Entregas | `/api/entregas` |

Operações: criar (POST), listar (GET), buscar por id (GET), atualizar (PUT), excluir (DELETE).

Extras úteis:

- `GET /api/pedidos/{id}/com-cliente`
- `GET /api/entregas/{id}/relacionados`
- `GET /api/entregas?atrasadas=true`
- `GET /api/pedidos?idCliente=...`

Exemplos de body e códigos HTTP: [`docs/api.md`](docs/api.md).

## Regras de negócio (aplicação)

1. Não cadastrar cliente com e-mail duplicado (409).
2. Não cadastrar pedido sem volumes (400).
3. Não criar entrega com pedido ou motorista inexistente (400).

## Documentação

| Arquivo | Conteúdo |
| --- | --- |
| [`modeling/entities.md`](modeling/entities.md) | Entidades, atributos e responsabilidade no domínio |
| [`modeling/relationships.md`](modeling/relationships.md) | Cardinalidade, embed × reference e justificativas |
| [`docs/architecture.md`](docs/architecture.md) | Camadas da API |
| [`docs/implementation.md`](docs/implementation.md) | Decisões técnicas e integridade |
| [`docs/api.md`](docs/api.md) | Endpoints e exemplos (Postman / Insomnia) |
