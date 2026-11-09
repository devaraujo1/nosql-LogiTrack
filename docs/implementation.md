# Decisões de implementação

## Identificadores

`_id` como `String` no Java, correspondente ao `ObjectId` do MongoDB. IDs de negócio (placa, código de produto, código de volume) permanecem em campos separados, como na modelagem.

## Tipos Java

| Domínio | Tipo Java |
| --- | --- |
| Texto | `String` |
| Contagens / minutos / capacidade | `Integer` (não `int`) |
| Peso / GPS / distância | `Double` |
| Valor monetário | `BigDecimal` |
| Datas | `LocalDateTime` |
| Flags | `Boolean` |
| Listas | `List<...>` |

## Embedded Documents

Implementados como classes POJO sem `@Document`:

- `Endereco` — Cliente, Transportadora, Centro, origem/destino do Pedido, destino da Entrega
- `Volume` + `Dimensoes` — dentro de `Pedido.volumes`
- `Pagamento` — dentro de `Pedido.pagamento`
- `Rota`, `HorariosEtapas`, `RastreioAtual` — dentro de `Entrega`

## References

Campos `String idX` (ex.: `idCliente`, `idMotorista`), no mesmo espírito dos scripts da Atividade 3. A aplicação valida existência no service antes de gravar. Endpoints `.../com-cliente` e `.../relacionados` combinam resultados de vários repositories.

## Regras de negócio

1. **E-mail único de cliente** — evita duplicidade indevida (409 Conflict).
2. **Pedido com volumes** — impede pedido comercial vazio (400 Bad Request).
3. **Entrega com pedido e motorista válidos** — o MongoDB não garante integridade referencial; a checagem é feita na aplicação (também transportadora, veículo e CD).

## Integridade dos dados

| Risco | Como a aplicação evita |
| --- | --- |
| Dados inválidos / campos obrigatórios | Validação nos services antes do `save` |
| Referência a documento inexistente | `existsById` nos repositories relacionados |
| Duplicidade de e-mail | `existsByEmail` / `findByEmail` |
| Atualizações inconsistentes | Atualização carrega o documento existente, aplica campos e grava de uma vez; `atualizadoEm` é renovado |

## Alterações em relação à modelagem

Nenhuma mudança estrutural de collection ou de estratégia embed/referência. A implementação segue `modeling/entities.md` e `modeling/relationships.md`.
