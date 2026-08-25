 LogiTrack — Levantamento de entidades

## Como os dados serão utilizados pela aplicação?

A LogiTrack acompanha:

Pedido → Centro de distribuição → Veículo → Motorista → Rota → Eventos → Entrega

A aplicação precisa responder:

1. Onde estão as entregas?
2. Quais regiões atrasam mais?
3. Qual motorista tem melhor desempenho?
4. Quais rotas são mais eficientes?
5. Quais horários geram atraso?
6. Qual centro de distribuição apresenta problemas?
7. Quanto tempo cada etapa demora?

| Pergunta                                 | Onde está a resposta                                      |
| ---------------------------------------- | --------------------------------------------------------- |
| 1. Onde estão as entregas?               | Entrega: `situacao` + último rastreio                     |
| 2. Quais regiões atrasam mais?           | Entrega: `destino.cidade` / `destino.estado` + `atrasado` |
| 3. Qual motorista tem melhor desempenho? | Entrega: `idMotorista` + se atrasou                       |
| 4. Quais rotas são mais eficientes?      | Entrega: tempos da `rota`                                 |
| 5. Quais horários geram atraso?          | Ocorrência e Rastreamento: `dataHora`                     |
| 6. Qual CD apresenta problemas?          | Entrega e Ocorrência: `idCentroDistribuicao`              |
| 7. Quanto tempo cada etapa demora?       | Entrega: `horariosEtapas`                                 |

---

## 1. Lista das entidades

14 entidades. São do domínio de **logística**.

| Entidade               | Descrição                                  | Responsabilidade no sistema                             |
| ---------------------- | ------------------------------------------ | ------------------------------------------------------- |
| Cliente                | Pessoa ou empresa que solicita uma entrega | Fazer pedidos e acompanhar envios                       |
| Pedido                 | Solicitação de envio feita pelo cliente    | Registrar carga, origem, destino e pagamento            |
| Produto                | Item que pode ser transportado (catálogo)  | Informar nome, peso e tamanho da mercadoria             |
| Volume                 | Pacote, caixa ou palete do pedido          | Descrever cada unidade da carga                         |
| Pagamento              | Cobrança do serviço de transporte          | Registrar valor, forma e se foi pago                    |
| Entrega                | Transporte do pedido até o destino         | Controlar situação, prazos, motorista, veículo e etapas |
| Rota                   | Percurso da entrega                        | Guardar distância e tempos previsto e real              |
| Centro de Distribuição | Local de armazenagem e despacho            | Receber e expedir cargas                                |
| Motorista              | Quem realiza a entrega                     | Executar a rota e medir desempenho                      |
| Veículo                | Meio de transporte                         | Informar placa, tipo e capacidade                       |
| Transportadora         | Empresa que faz o transporte               | Agrupar motoristas e veículos                           |
| Ocorrência             | Problema ou evento no caminho              | Registrar atraso, avaria, recusa etc.                   |
| Rastreamento           | Ponto de localização no tempo              | Mostrar onde a carga passou                             |
| Endereço               | Local de coleta, destino ou CD             | Guardar rua, cidade, estado e CEP                       |

Volume, Pagamento e Endereço **existem no domínio**, mas ficam **dentro** de outros documentos (não têm collection própria).

---

## 2. Atributos

Todos os endereços usam os **mesmos campos**. Os IDs seguem o padrão `id` + nome da entidade (`idCliente`, `idPedido`, …).

Endereço padrão (repetido onde aparecer `endereco`, `origem` ou `destino`):

```
endereco
├── rua
├── numero
├── complemento
├── bairro
├── cidade
├── estado
├── cep
└── pais
```

### Cliente

Pessoa física ou jurídica que solicita o envio.

**Responsabilidade:** guardar contato e identificar quem fez o pedido.

```
Cliente
├── _id
├── nome
├── tipo                      # PF | PJ
├── documento                 # CPF ou CNPJ
├── email
├── telefone
├── endereco
├── criadoEm
└── atualizadoEm
```

Exemplo de documento incorporado (como na aula):

```json
{
  "nome": "Maria Silva",
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

Solicitação de envio.

**Responsabilidade:** registrar a carga, de onde sai, para onde vai e o pagamento.

```
Pedido
├── _id
├── idCliente
├── situacao                  # criado | pago | em_transito | entregue | cancelado
├── origem                    # endereço
├── destino                   # endereço
├── volumes[]                 # Volume incorporado
│     ├── codigo
│     ├── idProduto
│     ├── nomeProduto
│     ├── pesoKg
│     ├── dimensoes
│     │     ├── comprimentoCm
│     │     ├── larguraCm
│     │     └── alturaCm
│     └── quantidade
├── pagamento                 # Pagamento incorporado
│     ├── forma
│     ├── valor
│     ├── situacao            # pendente | pago
│     └── pagoEm
├── criadoEm
└── atualizadoEm
```

### Produto

Item do catálogo.

**Responsabilidade:** dados da mercadoria. No pedido copiamos `nomeProduto` e `pesoKg`, para o histórico não mudar se o catálogo mudar.

```
Produto
├── _id
├── nome
├── codigo
├── categoria
├── pesoKg
├── dimensoes
│     ├── comprimentoCm
│     ├── larguraCm
│     └── alturaCm
├── fragil
└── ativo
```

### Volume

Unidade física da carga.

**Responsabilidade:** detalhar cada pacote. **Não** tem collection: fica em `Pedido.volumes`.

```
Volume
├── codigo
├── idProduto
├── nomeProduto
├── pesoKg
├── dimensoes
│     ├── comprimentoCm
│     ├── larguraCm
│     └── alturaCm
└── quantidade
```

### Pagamento

Cobrança do frete.

**Responsabilidade:** dizer se o pedido foi pago. **Não** tem collection: fica em `Pedido.pagamento`.

```
Pagamento
├── forma
├── valor
├── situacao                  # pendente | pago
└── pagoEm
```
