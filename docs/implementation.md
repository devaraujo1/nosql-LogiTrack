# Decisões de implementação

## Identificadores

No Java o `_id` é `String`. No MongoDB continua ObjectId.

As referências (`idCliente`, `idPedido`, etc.) também vão como ObjectId no banco, igual aos scripts da atividade anterior. No código usamos `@DocumentReference` ou `@Field(targetType = OBJECT_ID)`.

## Tipos

- texto → `String`
- quantidade / minutos → `Integer` (sem `int`)
- peso / GPS → `Double`
- dinheiro → `BigDecimal`
- data/hora → `LocalDateTime`
- flags → `Boolean`
- listas → `List`

## Embedded

Classes sem `@Document`, dentro do documento pai:

- `Endereco`
- `Volume` + `Dimensoes`
- `Pagamento`
- `Rota`, `HorariosEtapas`, `RastreioAtual`

## References

Usamos `@DocumentReference` nestes casos:

- Pedido → Cliente (`idCliente`)
- Entrega → Pedido (`idPedido`)
- Entrega → Motorista (`idMotorista`)

Os outros ids (`idProduto`, `idVeiculo`, `idTransportadora`, `idCentroDistribuicao`) ficam como campo com `OBJECT_ID`.

Para montar os dados relacionados:

- `GET /api/pedidos/{id}/com-cliente`
- `GET /api/entregas/{id}/relacionados`

## Validação

Tem `@Valid` nos controllers e anotações nos models (`@NotBlank`, `@Email`, etc.).  
As regras mais importantes ficam no service.

## Regras de negócio

1. e-mail de cliente não pode repetir → 409
2. pedido precisa ter volumes → 400
3. entrega só com pedido e motorista existentes → 400
4. estoque com reserva no produto:
   - criar pedido reserva (`quantidadeReservada += qtd`) se disponível >= qtd; senão 400
   - cancelar/excluir no CD libera a reserva
   - sair do CD (`em_transito`) baixa estoque e reserva
   - repor estoque (PUT produto) só altera `quantidadeEstoque`

## Integridade

O MongoDB não checa referência sozinho. Antes de salvar a gente confere se o id existe (`existsById`).  
Também evitamos o campo `_class` do Spring (`MongoConfig`), porque o validator das collections não aceita campo extra.

## Mudança em relação à modelagem

Não criamos collection de estoque. Em `produtos` ficam `quantidadeEstoque` e `quantidadeReservada`. Embed/reference das outras entidades permanecem iguais.
