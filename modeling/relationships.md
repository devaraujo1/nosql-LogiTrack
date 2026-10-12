# LogiTrack — Relacionamentos e modelagem

Este arquivo explica por que os dados da LogiTrack ficam desta forma no MongoDB. A decisão usa as perguntas que a aplicação precisa responder, não a regra de “uma entidade, uma collection”.

O fluxo continua o mesmo do levantamento:

Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega

## 1. Entidades

São 14 entidades. Dez viram collection. Volume, Pagamento, Rota e Endereço existem no domínio e ficam dentro de outro documento.

| Entidade | Collection | Papel |
| --- | --- | --- |
| Cliente | `clientes` | Quem solicita o envio |
| Pedido | `pedidos` | Carga, origem, destino e pagamento |
| Produto | `produtos` | Catálogo da mercadoria |
| Volume | não tem | Pacote dentro de `Pedido.volumes` |
| Pagamento | não tem | Cobrança dentro de `Pedido.pagamento` |
| Entrega | `entregas` | Operação: situação, atraso, prazos e último GPS |
| Rota | não tem | Tempos dentro de `Entrega.rota` |
| Centro de Distribuição | `centrosDistribuicao` | Hub de recebimento e despacho |
| Motorista | `motoristas` | Quem executa a rota |
| Veículo | `veiculos` | Placa, tipo e capacidade |
| Transportadora | `transportadoras` | Empresa que agrupa motorista e veículo |
| Ocorrência | `ocorrencias` | Problema ou evento no caminho |
| Rastreamento | `rastreamentos` | Histórico de pontos |
| Endereço | não tem | Rua, cidade, estado e CEP dentro de quem o usa |

## 2. Cardinalidade

| Entidade A | Relação | Entidade B | Cardinalidade |
| --- | --- | --- | --- |
| Cliente | realiza | Pedido | 1:N |
| Pedido | tem | Pagamento | 1:1 |
| Pedido | contém | Volume | 1:N |
| Volume | refere-se a | Produto | N:1 |
| Pedido | gera | Entrega | 1:1 |
| Transportadora | emprega | Motorista | 1:N |
| Transportadora | possui | Veículo | 1:N |
| Transportadora | opera | Entrega | 1:N |
| Motorista | conduz | Entrega | 1:N |
| Veículo | transporta | Entrega | 1:N |
| Centro de Distribuição | despacha | Entrega | 1:N |
| Entrega | percorre | Rota | 1:1 |
| Entrega | registra | Ocorrência | 1:N |
| Entrega | registra | Rastreamento | 1:N |
| Cliente | mora em | Endereço | 1:1 |
| Pedido | sai de / vai para | Endereço | 1:1 e 1:1 |
| Centro de Distribuição | fica em | Endereço | 1:1 |
| Transportadora | fica em | Endereço | 1:1 |

Não há N:N direto. O volume cita um produto, e o mesmo produto aparece em vários pedidos. Isso é N:1 do volume para o produto, resolvido com `idProduto` mais uma cópia do nome e do peso.

## 3. Estratégia de `_id`

Todo documento de collection usa `ObjectId` no `_id`.

O ObjectId cabe aqui porque a chave liga collections (`idCliente`, `idPedido`, `idMotorista` e as demais). Ela não precisa de um gerador central, e o valor não muda quando um dado de negócio muda.

Placa, código do produto (`PROD-001`), código do volume (`VOL-1001`) e código do centro (`CD-FSA`) continuam em campos `String`. Se a placa for corrigida, o `_id` do veículo permanece e as entregas antigas não quebram.

Um `_id` personalizado, como `"PED-001"`, misturaria a regra comercial com a chave. O código de negócio pode mudar de formato. A referência entre documentos não deve depender disso.

Nos scripts de exemplo o `ObjectId` é fixo (`64f100000000000000000001` e os seguintes) só para os arquivos de consulta apontarem para os mesmos documentos. Em produção o MongoDB pode gerar o `ObjectId` sozinho.

## 4. Embedded ou Reference

| Relacionamento | Estratégia | Justificativa |
| --- | --- | --- |
| Cliente → Endereço | Embedded | O cadastro do cliente sempre mostra o endereço. Esse endereço não é compartilhado com outro cliente. Não cresce. |
| Transportadora → Endereço | Embedded | Mesma leitura do cadastro. Um endereço por empresa. |
| Centro de Distribuição → Endereço | Embedded | O CD é lido junto com o lugar onde ele fica. |
| Pedido → origem e destino | Embedded | São a cópia do momento do pedido. Se o cliente mudar o cadastro depois, o pedido antigo não pode mudar. A tela do pedido lê os dois endereços junto. |
| Pedido → Volumes | Embedded | Um pedido tem poucos pacotes. A tela comercial precisa da carga inteira numa leitura. O array não cresce sem limite. |
| Pedido → Pagamento | Embedded | Há um pagamento por pedido. Valor e situação são lidos no detalhe do pedido. Não há vida independente. |
| Volume → Produto | Reference (`idProduto`) e cópia de `nomeProduto` e `pesoKg` | O catálogo é compartilhado e é atualizado sem reescrever pedidos. O histórico do envio não pode mudar se o produto for renomeado ou se o peso do catálogo mudar. |
| Pedido → Cliente | Reference (`idCliente`) | O cliente tem vida própria, é compartilhado por vários pedidos e é atualizado sem abrir cada pedido. |
| Pedido → Entrega | Reference (`Entrega.idPedido`), collections separadas | O pedido é o lado comercial. A entrega é a operação, consultada o tempo todo pelas perguntas de atraso, região, motorista e etapa. Atualizar o GPS não deve reescrever o pedido. |
| Entrega → Motorista, Veículo, Transportadora, Centro | Reference | São cadastros reutilizados. O mesmo motorista aparece em várias entregas. A situação do motorista muda sem copiar essa mudança para cada entrega passada. |
| Entrega → Rota | Embedded | A rota é desta entrega. Distância e tempos previsto e real respondem qual rota foi mais eficiente, e são lidos junto com o atraso. |
| Entrega → horários das etapas | Embedded | São quatro instantes da mesma entrega. A pergunta “quanto tempo cada etapa demorou” lê esse bloco junto. |
| Entrega → último rastreio | Embedded (`rastreioAtual`) | “Onde está a entrega?” precisa de um ponto só. Esse ponto cabe no documento e evita abrir o histórico. |
| Entrega → destino | Embedded | Cópia da cidade e do estado para atraso por região, lida com a entrega. |
| Entrega → histórico de rastreio | Reference (`rastreamentos.idEntrega`) | O GPS gera muitos pontos. Um array sem limite estoura o documento e deixa a leitura da entrega pesada. O histórico é consultado à parte. |
| Entrega → Ocorrência | Reference (`ocorrencias.idEntrega`) | Ocorrências nascem ao longo da viagem, são consultadas por horário e por centro, e não cabem todas dentro da entrega. |

### Por que incorporar

Estes quatro casos mostram o critério. Os dados são lidos juntos, não são compartilhados, não têm vida fora do pai e o tamanho fica limitado.

1. `Cliente.endereco` — o cadastro abre nome, contato e endereço de uma vez.
2. `Pedido.volumes` — a carga do pedido é um array pequeno, com `dimensoes` dentro de cada volume.
3. `Pedido.pagamento` — um frete, um valor, uma situação.
4. `Entrega.rota` — três números daquela viagem. `Entrega.rastreioAtual` segue o mesmo raciocínio para a pergunta de localização.

### Por que referenciar

Estes quatro casos mostram o outro lado. O dado é compartilhado, muda sozinho ou cresce sem teto.

1. `Pedido.idCliente` — vários pedidos do mesmo cliente.
2. `Pedido.volumes[].idProduto` — o catálogo muda; o pedido guarda a cópia do que foi enviado.
3. `Entrega.idMotorista` — o desempenho do motorista junta várias entregas pelo mesmo identificador.
4. `Rastreamento.idEntrega` e `Ocorrencia.idEntrega` — listas que crescem e são filtradas por tempo e por centro.

## 5. Formato dos documentos

Os exemplos abaixo são os mesmos dados carregados em `mongodb/03_insert_data.js`.

### Cliente

`_id` ObjectId. `endereco` é documento incorporado. `criadoEm` é Date. `tipo` é String (`PF` ou `PJ`).

```json
{
  "_id": "64f500000000000000000001",
  "nome": "Maria Silva",
  "tipo": "PF",
  "documento": "123.456.789-00",
  "email": "maria@email.com",
  "telefone": "75999990000",
  "endereco": {
    "rua": "Rua A",
    "numero": "100",
    "complemento": "",
    "bairro": "Centro",
    "cidade": "Feira de Santana",
    "estado": "BA",
    "cep": "44000-000",
    "pais": "Brasil"
  }
}
```

### Pedido

`idCliente` referencia `clientes`. `volumes` é array de documentos. `pagamento.valor` é Decimal128. `quantidade` é Int32.

```json
{
  "_id": "64f700000000000000000001",
  "idCliente": "64f500000000000000000001",
  "situacao": "em_transito",
  "destino": { "cidade": "Salvador", "estado": "BA" },
  "volumes": [
    {
      "codigo": "VOL-1001",
      "idProduto": "64f600000000000000000001",
      "nomeProduto": "Notebook",
      "pesoKg": 2.1,
      "quantidade": 1
    }
  ],
  "pagamento": {
    "forma": "pix",
    "valor": "189.90",
    "situacao": "pago"
  }
}
```

### Entrega

As referências ficam na raiz. Rota, etapas, destino e último ponto ficam dentro.

```json
{
  "_id": "64f800000000000000000001",
  "idPedido": "64f700000000000000000001",
  "idMotorista": "64f300000000000000000001",
  "idVeiculo": "64f400000000000000000001",
  "idCentroDistribuicao": "64f200000000000000000001",
  "situacao": "em_transito",
  "atrasado": true,
  "rota": {
    "distanciaKm": 108.5,
    "duracaoPrevistaMin": 120,
    "duracaoRealMin": 240
  },
  "rastreioAtual": {
    "cidade": "Salvador",
    "estado": "BA"
  }
}
```

### Ocorrência e rastreamento

```json
{
  "_id": "64f900000000000000000001",
  "idEntrega": "64f800000000000000000001",
  "tipo": "atraso",
  "dataHora": "2026-09-16T22:10:00Z",
  "minutosAtraso": 95
}
```

```json
{
  "_id": "64fa00000000000000000001",
  "idEntrega": "64f800000000000000000001",
  "latitude": -12.95,
  "longitude": -38.51,
  "cidade": "Salvador",
  "registradoEm": "2026-09-16T22:30:00Z"
}
```

## 6. Tipos de dados

O valor não fica todo em String.

| Tipo | Onde aparece |
| --- | --- |
| String | `nome`, `email`, `placa`, `cidade` |
| Boolean | `atrasado`, `fragil`, `ativo` |
| Int32 | `quantidade`, `capacidadeKg`, `duracaoPrevistaMin`, `minutosAtraso` |
| Double | `pesoKg`, `distanciaKm`, `latitude`, `longitude` |
| Decimal128 | `pagamento.valor` (dinheiro não usa Double) |
| ObjectId | `_id` e toda referência `id...` |
| Date | `criadoEm`, `prometidoPara`, `dataHora`, `pagoEm` |
| Array | `Pedido.volumes` |
| Documento incorporado | `endereco`, `pagamento`, `rota`, `rastreioAtual`, `dimensoes` |

## 7. Schema flexível

Na collection `ocorrencias` o núcleo é o mesmo: `idEntrega`, `tipo`, `descricao`, `dataHora` e `cidade`.

O atraso traz um campo a mais:

```json
{
  "tipo": "atraso",
  "descricao": "Congestionamento na BR-324 depois do horário prometido.",
  "minutosAtraso": 95
}
```

A avaria traz outro:

```json
{
  "tipo": "avaria",
  "descricao": "Embalagem da cadeira amassada na carroceria.",
  "gravidade": "media"
}
```

Essa diferença é necessária. Minutos de atraso não existem numa avaria, e gravidade não existe num atraso. Guardar `null` nos dois campos em todo documento só fingiria uma estrutura única.

A flexibilidade ajuda porque o fato extra depende do tipo, e as consultas de horário e de centro continuam nos campos comuns. Ela prejudicaria se cada ocorrência pudesse omitir `idEntrega` ou `dataHora`: a lista por centro e por hora quebraria.

O núcleo fica obrigatório. Só o complemento do tipo é opcional. Não vale uma collection diferente para cada tipo de evento, porque a tela de problemas do CD lê todos os tipos juntos.

## 8. Por que a modelagem ficou assim

A LogiTrack foi modelada pelo acesso, não pelo nome da entidade.

A operação abre a entrega o tempo todo. Por isso situação, atraso, destino, tempos da rota, horários das etapas e o último GPS estão no mesmo documento. Uma leitura responde onde a carga está, se atrasou, qual região, quanto a rota demorou e quanto cada etapa levou.

O pedido junta o lado comercial. Origem, destino, volumes e pagamento aparecem na mesma tela, o array de volumes é pequeno e o pagamento não vive sem o pedido. Incorporar evita uma ida a mais ao banco nesse detalhe.

Cliente, produto, motorista, veículo, transportadora e centro ficam de fora porque são cadastros compartilhados. Várias entregas apontam para o mesmo motorista. Atualizar o telefone dele não pode reescrever o histórico das viagens. O produto segue a mesma lógica, com a cópia do nome e do peso no volume para o passado não mudar.

Rastreamento e ocorrência ficam de fora pelo tamanho e pela consulta. O GPS não tem teto. A ocorrência é filtrada por hora e por centro, muitas vezes sem abrir a entrega inteira. O último ponto, que a tela de mapa precisa sempre, continua dentro da entrega.

Essa separação segue frequência de leitura, frequência de escrita, tamanho do documento, cardinalidade, compartilhamento e atualização independente. Não é “embedded porque o MongoDB usa documento” nem “reference porque são entidades diferentes”.

