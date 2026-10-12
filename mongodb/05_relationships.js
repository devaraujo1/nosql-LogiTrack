// LogiTrack — Atividade 3
// Responsável: Enzo Cabral Lopes
//
// Percorre uma entrega atrasada para Salvador.
// Cadastros compartilhados são lidos pelo _id guardado na entrega.
// Rota, último GPS, destino e volumes saem do próprio documento,
// porque a tela lê esses dados junto com o pai.

const banco = db.getSiblingDB("logitrack");

const idEntregaSalvador = ObjectId("64f800000000000000000001");

print("\n=== Entrega (documento da operação) ===");
const entrega = banco.entregas.findOne({ _id: idEntregaSalvador });
printjson(entrega);

print("\n=== Incorporado: rota desta entrega ===");
print("A rota não é reutilizada por outra entrega. A pergunta de eficiência lê estes três números junto com o atraso.");
printjson(entrega.rota);

print("\n=== Incorporado: último rastreio ===");
print("A pergunta 'onde está a entrega?' usa só o ponto mais recente, já copiado na entrega.");
printjson(entrega.rastreioAtual);

print("\n=== Incorporado: destino ===");
print("Cidade e estado do momento do transporte. Não acompanham uma alteração posterior no cadastro do cliente.");
printjson(entrega.destino);

print("\n=== Referência: pedido ===");
const pedido = banco.pedidos.findOne({ _id: entrega.idPedido });
printjson(pedido);

print("\n=== Incorporado no pedido: volumes ===");
print("O pedido tem poucos pacotes e a tela comercial precisa da carga inteira.");
printjson(pedido.volumes);

print("\n=== Incorporado no pedido: pagamento ===");
print("Há um pagamento por pedido e ele é lido no mesmo detalhe.");
printjson(pedido.pagamento);

print("\n=== Referência: cliente do pedido ===");
print("O cliente é um cadastro. Vários pedidos apontam para a mesma pessoa.");
const cliente = banco.clientes.findOne({ _id: pedido.idCliente });
printjson(cliente);

print("\n=== Referência: produto do primeiro volume ===");
print("O catálogo continua em produtos. O volume guardou nome e peso da época do pedido.");
const produto = banco.produtos.findOne({ _id: pedido.volumes[0].idProduto });
printjson(produto);

print("\n=== Referência: motorista ===");
const motorista = banco.motoristas.findOne({ _id: entrega.idMotorista });
printjson(motorista);

print("\n=== Referência: veículo ===");
const veiculo = banco.veiculos.findOne({ _id: entrega.idVeiculo });
printjson(veiculo);

print("\n=== Referência: transportadora ===");
const transportadora = banco.transportadoras.findOne({ _id: entrega.idTransportadora });
printjson(transportadora);

print("\n=== Referência: centro de distribuição ===");
const centro = banco.centrosDistribuicao.findOne({ _id: entrega.idCentroDistribuicao });
printjson(centro);

print("\n=== Referência: ocorrências desta entrega ===");
print("Ocorrência cresce ao longo da viagem e é consultada por horário e por centro.");
printjson(banco.ocorrencias.find({ idEntrega: entrega._id }).sort({ dataHora: 1 }).toArray());

print("\n=== Referência: histórico de rastreamento ===");
print("O GPS gera vários pontos. Só o último ficou dentro da entrega.");
printjson(banco.rastreamentos.find({ idEntrega: entrega._id }).sort({ registradoEm: 1 }).toArray());
