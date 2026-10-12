// LogiTrack — Atividade 3
// Responsável: Enzo Cabral Lopes
//
// Consultas da operação. Cada bloco indica a tela ou a regra da atividade.
// Os ObjectId são os mesmos inseridos em 03_insert_data.js.

const banco = db.getSiblingDB("logitrack");

const idClienteMaria = ObjectId("64f500000000000000000001");
const idMotoristaCarlos = ObjectId("64f300000000000000000001");

print("\n=== 1. Consulta simples: clientes pessoa jurídica ===");
printjson(banco.clientes.find({ tipo: "PJ" }).toArray());

print("\n=== 2. Consulta simples: entregas atrasadas ===");
printjson(banco.entregas.find({ atrasado: true }).toArray());

print("\n=== 3. Consulta simples: motoristas disponíveis ===");
printjson(banco.motoristas.find({ situacao: "disponivel" }).toArray());

print("\n=== 4. Várias condições: atraso em Ilhéus, na Bahia ===");
printjson(banco.entregas.find({
  atrasado: true,
  "destino.estado": "BA",
  "destino.cidade": "Ilhéus"
}).toArray());

print("\n=== 5. Várias condições: atrasos registrados a partir das 20h UTC de 16/09 ===");
printjson(banco.ocorrencias.find({
  tipo: "atraso",
  dataHora: { $gte: ISODate("2026-09-16T20:00:00Z") }
}).toArray());

print("\n=== 6. Projeção — lista de clientes: nome, e-mail e telefone ===");
printjson(banco.clientes.find(
  {},
  { _id: 0, nome: 1, email: 1, telefone: 1 }
).toArray());

print("\n=== 7. Projeção — detalhe do pedido: situação, destino, valor e volumes ===");
printjson(banco.pedidos.find(
  {},
  {
    situacao: 1,
    "destino.cidade": 1,
    "destino.estado": 1,
    "pagamento.valor": 1,
    "pagamento.situacao": 1,
    volumes: 1
  }
).toArray());

print("\n=== 8. Projeção — lista de entregas: situação, atraso, prazo e cidade ===");
printjson(banco.entregas.find(
  {},
  {
    _id: 0,
    situacao: 1,
    atrasado: 1,
    prometidoPara: 1,
    "destino.cidade": 1
  }
).toArray());

print("\n=== 9. Referência: pedidos da cliente Maria Silva ===");
printjson(banco.pedidos.find({ idCliente: idClienteMaria }).toArray());

print("\n=== 10. Referência: entregas do motorista Carlos Lima ===");
printjson(banco.entregas.find({ idMotorista: idMotoristaCarlos }).toArray());

print("\n=== 11. Documento incorporado: pedidos com destino em Salvador ===");
printjson(banco.pedidos.find({ "destino.cidade": "Salvador" }).toArray());

print("\n=== 12. Documento incorporado: rotas com duração real acima de 180 minutos ===");
printjson(banco.entregas.find({
  "rota.duracaoRealMin": { $gt: NumberInt(180) }
}).toArray());

print("\n=== 13. Array: pedidos que contêm Notebook ===");
printjson(banco.pedidos.find({ "volumes.nomeProduto": "Notebook" }).toArray());

print("\n=== 14. Array: volumes com mais de 10 kg ===");
printjson(banco.pedidos.find({
  volumes: { $elemMatch: { pesoKg: { $gt: Double(10) } } }
}).toArray());

print("\n=== 15. Ordenação: ocorrências da mais recente para a mais antiga ===");
printjson(banco.ocorrencias.find(
  {},
  { _id: 0, tipo: 1, cidade: 1, dataHora: 1, minutosAtraso: 1, gravidade: 1 }
).sort({ dataHora: -1 }).toArray());
