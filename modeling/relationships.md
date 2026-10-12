# Modelagem e Relacionamentos - LogiTrack no MongoDB

## 1. Entidades e Mapeamento de Collections
O sistema LogiTrack possui 14 entidades em sua modelagem, que foram tratadas de maneiras diferentes dependendo de seu comportamento e ciclo de vida. Dez delas viraram *collections* independentes, enquanto quatro não possuem *collection* própria.
* **Collections Criadas:** Clientes, Produtos, Pedidos, Entregas, Transportadoras, Motoristas, Veículos, Centros de Distribuição, Ocorrências e Rastreamentos.
* **Entidades Incorporadas:** Volume, Pagamento, Rota e Endereço ficam dentro de outro documento.

## 2. Tabela de Relacionamentos e Cardinalidade

| Entidade Origem | Relacionamento | Entidade Destino | Cardinalidade | Observação |
| :--- | :--- | :--- | :--- | :--- |
| Cliente | possui | Endereço | 1:1 | Endereço embutido no cliente. |
| Pedido | pertence a | Cliente | N:1 | Pedido aponta para Cliente. |
| Pedido | possui | Pagamento | 1:1 | Pagamento embutido no pedido. |
| Pedido | contém | Volume | 1:N | Volumes embutidos no pedido. |
| Volume | aponta para | Produto | N:1 | Vários volumes apontam para um produto. |
| Entrega | possui | Rota | 1:1 | Rota embutida na entrega. |
| Entrega | alocada para | Motorista | N:1 | Entrega aponta para Motorista. |
| Rastreamento | registra passo da | Entrega | N:1 | Rastreamento aponta para Entrega. |
| Ocorrência | registrada na | Entrega | N:1 | Ocorrência aponta para Entrega. |

## 3. Estratégia de Incorporação (Embedded)
A estratégia de incorporar (embed) documentos foi utilizada para dados que são lidos juntos, que não têm vida própria, que não crescem de forma a estourar o limite de tamanho e que não precisam ser atualizados separadamente.
* **Endereço do cliente:** Fica dentro do documento do cliente pois não possui ciclo de vida independente; ao consultar o cliente, a operação lê o endereço junto.
* **Volumes do pedido:** Gravados dentro do pedido pois pertencem apenas àquela transação, são lidos de uma vez só e não crescem demais.
* **Pagamento do pedido:** Fica embutido por ser intrínseco ao pedido, lido conjuntamente para agilizar a visão comercial e sem atualizações separadas.
* **Rota da entrega:** Fica dentro da entrega, pois a rota nasce e morre com aquela entrega específica, não sendo compartilhada.

## 4. Estratégia de Referência (References)
Utilizamos referências quando as entidades possuem vida própria, dados muito grandes que crescem continuamente ou quando são cadastros reutilizados que precisam ser atualizados de maneira separada.
* **Pedido aponta para o cliente:** O cliente é um cadastro reutilizado. Seus dados têm ciclo de vida próprio e atualizações separadas.
* **Volume aponta para o produto:** O volume referencia o produto e copia seu nome e peso no momento da compra. Assim, se o produto mudar no futuro, o histórico do pedido não quebra.
* **Entrega aponta para o motorista:** Motoristas são recursos operacionais com dados próprios. Uma atualização no cadastro não deve alterar as entregas antigas.
* **Rastreamento e ocorrência apontam para a entrega:** Apontam de fora para a entrega porque são dados que crescem muito. Se ficassem embutidos, o documento da entrega ficaria pesado e lento para atualizações.

## 5. Estratégia de Identificadores (_id)
Usamos o `ObjectId` padrão do MongoDB para os identificadores das collections. Placa de veículo e código do produto continuam como texto comum. Fizemos isso porque dados do mundo real (como a placa mudar) podem sofrer alterações; garantindo a referência pelo ObjectId, nenhuma ligação quebra.

## 6. Tipos de Dados Utilizados
* **String:** Textos gerais, placas, descrições.
* **Boolean:** Flags como ativo, frágil ou atrasado.
* **Int32:** Números inteiros, como quantidade e minutos de atraso.
* **Double:** Coordenadas e peso do produto.
* **Decimal128:** Valor do frete e pagamentos (garante precisão financeira).
* **ObjectId:** Chaves primárias e estrangeiras.
* **Date:** Datas e horários dos pedidos, entregas e rastreamentos.
* **Array:** Múltiplos itens, como a lista de volumes.
* **Documento (Object):** Blocos aninhados, como endereço e destino.

## 7. Schema Flexível (Ocorrências)
Em Ocorrências, o tipo "atraso" tem a propriedade `minutosAtraso`, enquanto "avaria" tem a propriedade `gravidade`. Essa diferença é necessária e ajuda a operação, pois evita preencher campos que não fazem sentido (uma avaria não tem minutos de atraso). O restante do documento continua igual, mantendo a consistência nas buscas.


## 8. Conclusão da Modelagem
A **entrega** junta e embute tudo o que a operação lê o tempo todo (rota, destino), otimizando o trabalho logístico em tempo real. O **pedido** junta a parte puramente comercial. Entidades como **motorista, veículo, cliente e produto** ficam separadas porque são cadastros reutilizados o tempo todo por várias operações. Por fim, **rastreamento (GPS) e ocorrência** ficam fora da entrega porque crescem descontroladamente e são consultados por outros eixos (por horário ou por centro logístico).
