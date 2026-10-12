# LogiTrack

Plataforma inteligente de logística e entregas. Projeto da disciplina de NoSQL (MongoDB).

## Descrição

Projeto desenvolvido pela equipe ao longo da disciplina, com o objetivo de aplicar os conhecimentos adquiridos nas atividades e construir uma solução com banco de dados MongoDB.

A LogiTrack não só registra pedidos: acompanha a operação do pedido até a entrega e permite analisar desempenho — atrasos por região, motoristas, rotas, horários e centros de distribuição.

Fluxo do domínio:

Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega

## Objetivo

Desenvolver uma solução que aplique conceitos de desenvolvimento de software, organização de dados e uso do MongoDB, com boa estruturação e versionamento de código.

## Integrantes

- Murilo Vinícius Lima de Araújo
- Lismara Santos Moreira
- Ícaro Assis Oliveira Maia
- João Paulo de Oliveira Carneiro
- Enzo Cabral Lopes

## Estrutura do projeto

```text
/
├── mongodb/
│   ├── 01_database.js
│   ├── 02_collections.js
│   ├── 03_insert_data.js
│   ├── 04_queries.js
│   └── 05_relationships.js
├── modeling/
│   ├── entities.md
│   └── relationships.md
└── README.md
```

- `modeling/entities.md` — entidades levantadas na atividade anterior.
- `modeling/relationships.md` — cardinalidade, embed, referência e justificativa da modelagem.
- `mongodb/` — scripts para criar o banco, inserir os documentos de exemplo e consultar.

## Como executar

É preciso ter o MongoDB em execução na máquina e o `mongosh` instalado. Na raiz do repositório:

```text
mongosh mongodb/01_database.js
mongosh mongodb/02_collections.js
mongosh mongodb/03_insert_data.js
mongosh mongodb/04_queries.js
mongosh mongodb/05_relationships.js
```

O primeiro script recria o banco `logitrack`. A ordem importa: as collections nascem no segundo, os documentos no terceiro, e as consultas usam esses dados.
