### Transportadora

Empresa que opera o transporte.

**Responsabilidade:** agrupar motoristas, veículos e entregas.

```
Transportadora
├── _id
├── nome
├── documento                 # CNPJ
├── email
├── telefone
├── endereco
└── ativo
```

### Ocorrência

Evento ou problema no transporte.

**Responsabilidade:** perguntas 5 e 6 (horário e problemas do CD).

```
Ocorrencia
├── _id
├── idEntrega
├── idCentroDistribuicao
├── idMotorista
├── tipo                      # atraso | avaria | ausente | recusa | outro
├── descricao
├── dataHora
└── cidade
```

### Rastreamento

Ponto de localização (GPS ou check-in).

**Responsabilidade:** histórico para as perguntas 1 e 5. O **último** ponto também fica em `Entrega.rastreioAtual`.

```
Rastreamento
├── _id
├── idEntrega
├── latitude
├── longitude
├── cidade
├── estado
└── registradoEm
```

### Endereço

Local de coleta, destino ou CD.

**Responsabilidade:** cidade e estado para atraso por região. **Não** vira collection.

```
Endereco
├── rua
├── numero
├── complemento
├── bairro
├── cidade
├── estado
├── cep
└── pais
```

## Relacionamentos (ligação para o código)

Os documentos se conectam por `_id` e pelos campos `id...`:

```
Cliente._id          →  Pedido.idCliente
Produto._id          →  Pedido.volumes[].idProduto
Pedido._id           →  Entrega.idPedido
Transportadora._id   →  Motorista.idTransportadora
Transportadora._id   →  Veiculo.idTransportadora
Transportadora._id   →  Entrega.idTransportadora
Motorista._id        →  Entrega.idMotorista
Veiculo._id          →  Entrega.idVeiculo
CentroDistribuicao._id → Entrega.idCentroDistribuicao
Entrega._id          →  Ocorrencia.idEntrega
Entrega._id          →  Rastreamento.idEntrega
```

```
Cliente 1 ── * Pedido
Pedido  1 ── 1 Pagamento          (dentro do pedido)
Pedido  1 ── * Volume             (dentro do pedido)
Volume  * ── 1 Produto            (idProduto)
Pedido  1 ── 1 Entrega
Entrega * ── 1 Transportadora
Entrega * ── 1 Motorista
Entrega * ── 1 Veiculo
Entrega * ── 1 Centro de Distribuição
Entrega 1 ── 1 Rota               (dentro da entrega)
Entrega 1 ── * Ocorrencia
Entrega 1 ── * Rastreamento
```

Com isso dá para, nas próximas atividades, montar as collections e buscar: pedido do cliente, entrega do pedido, motorista da entrega, ocorrências da entrega.
