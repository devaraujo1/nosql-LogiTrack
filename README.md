# LogiTrack — Atividade 4

API REST em Java com Spring Boot e MongoDB para a plataforma LogiTrack (logística e entregas).

## Integrantes

- Murilo Vinícius Lima de Araújo
- Lismara Santos Moreira
- Ícaro Assis Oliveira Maia
- João Paulo de Oliveira Carneiro
- Enzo Cabral Lopes

## Objetivo

Transformar a modelagem das atividades anteriores em uma aplicação Java funcional, com persistência MongoDB, regras de negócio e API REST, mantendo a coerência com embeds e referências já definidos.

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring Data MongoDB
- Spring Web
- Maven
- MongoDB

## Como abrir no IntelliJ e ligar

1. Abra o IntelliJ IDEA.
2. **File → Open** e selecione a pasta `atividade-4-completa` (detecta o Maven pelo `pom.xml`).
3. Aguarde o IntelliJ baixar as dependências.
4. Suba o MongoDB local na porta `27017`.
5. (Opcional) Carregue os dados de exemplo:

```text
mongosh mongodb/01_database.js
mongosh mongodb/02_collections.js
mongosh mongodb/03_insert_data.js
```

6. Execute a classe `AplicacaoApiApplication`.
7. A API fica em `http://localhost:8080`.

## Configuração do MongoDB

Arquivo `src/main/resources/application.properties`:

```properties
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=logitrack
```

Não publique senhas ou credenciais no repositório.

## Principais entidades implementadas

Collections (`@Document`): Cliente, Pedido, Produto, Entrega, CentroDistribuicao, Motorista, Veiculo, Transportadora, Ocorrencia, Rastreamento.

Documentos incorporados: Endereco, Volume, Pagamento, Dimensoes, Rota, HorariosEtapas, RastreioAtual.

## Estratégias Embedded e References

- **Embedded:** `Cliente.endereco`, `Pedido.volumes`, `Pedido.pagamento`, `Entrega.rota`, `Entrega.rastreioAtual`.
- **References (campos id):** `Pedido.idCliente`, `Volume.idProduto`, `Entrega.idPedido`, `Entrega.idMotorista`, `Entrega.idVeiculo`, `Entrega.idTransportadora`, `Entrega.idCentroDistribuicao`.

A recuperação dos relacionados é feita nos services (consultas aos repositories + combinação do resultado).

## Endpoints (CRUD completo)

| Recurso | Base |
| --- | --- |
| Clientes | `/api/clientes` |
| Pedidos | `/api/pedidos` |
| Entregas | `/api/entregas` |

Para cada um: POST (criar), GET (listar), GET `/{id}`, PUT `/{id}`, DELETE `/{id}`.

Extras:

- `GET /api/pedidos/{id}/com-cliente`
- `GET /api/entregas/{id}/relacionados`
- `GET /api/entregas?atrasadas=true`
- `GET /api/pedidos?idCliente=...`

Detalhes e exemplos: [docs/api.md](docs/api.md).

## Regras de negócio

1. Não cadastrar cliente com e-mail duplicado (409).
2. Não cadastrar pedido sem volumes (400).
3. Não criar entrega com pedido ou motorista inexistente (400).

## Estrutura do projeto

```text
atividade-4-completa/
├── src/main/java/br/com/logi_track/aplicacao_api/
│   ├── controllers/
│   ├── services/
│   ├── repositories/
│   ├── models/
│   ├── exception/
│   └── AplicacaoApiApplication.java
├── src/main/resources/application.properties
├── modeling/
├── mongodb/
├── docs/
├── pom.xml
└── README.md
```

## Documentação

- [docs/architecture.md](docs/architecture.md)
- [docs/implementation.md](docs/implementation.md)
- [docs/api.md](docs/api.md)
- [modeling/entities.md](modeling/entities.md)
- [modeling/relationships.md](modeling/relationships.md)
