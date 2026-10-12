```javascript
// =====================================================================
// 05_relationships.js — LogiTrack: navegação entre relacionamentos
// Executar depois de 03_insert_data.js:
//   mongosh --file mongodb/05_relationships.js
// =====================================================================

db = db.getSiblingDB("logitrack");

// Mesma função e mesmos ObjectId do script 03_insert_data.js.
const oid = (n) => ObjectId("65" + "0".repeat(20) + n);

// A entrega 73 corresponde à entrega atrasada com destino
// a Vitória da Conquista.
const idEntrega = oid("73");

// ---------------------------------------------------------------------
// 1. ENTREGA PRINCIPAL
// ---------------------------------------------------------------------

print("\n==================================================");
print("1. DOCUMENTO PRINCIPAL: ENTREGA");
print("==================================================");

const entrega = db.entregas.findOne({
  _id: idEntrega
});

if (!entrega) {
  print("Entrega não encontrada. Execute 03_insert_data.js primeiro.");
} else {

  print("\n1.1 - Documento completo da entrega");
  printjson(entrega);

  // A entrega é o ponto inicial da navegação.
  // Os campos idPedido, idMotorista, idVeiculo,
  // idTransportadora e idCentroDistribuicao são referências.
  // Os campos destino, rota, horariosEtapas e rastreioAtual
  // estão incorporados no próprio documento.

  // -------------------------------------------------------------------
  // 2. PEDIDO — REFERÊNCIA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("2. PEDIDO REFERENCIADO PELA ENTREGA");
  print("==================================================");

  const pedido = db.pedidos.findOne({
    _id: entrega.idPedido
  });

  printjson(pedido);

  // O pedido é uma referência porque está na coleção pedidos.
  // A entrega guarda somente idPedido, evitando duplicar o pedido inteiro.

  if (pedido) {

    // ---------------------------------------------------------------
    // 3. CLIENTE — REFERÊNCIA DO PEDIDO
    // ---------------------------------------------------------------

    print("\n==================================================");
    print("3. CLIENTE REFERENCIADO PELO PEDIDO");
    print("==================================================");

    const cliente = db.clientes.findOne({
      _id: pedido.idCliente
    });

    printjson(cliente);

    // O cliente é uma referência por idCliente.
    // O cadastro possui identidade própria e pode ser utilizado
    // por vários pedidos, sem duplicar seus dados cadastrais.

    // ---------------------------------------------------------------
    // 4. PRODUTOS — REFERÊNCIAS DOS VOLUMES
    // ---------------------------------------------------------------

    print("\n==================================================");
    print("4. PRODUTOS REFERENCIADOS PELOS VOLUMES");
    print("==================================================");

    if (pedido.volumes && pedido.volumes.length > 0) {
      pedido.volumes.forEach((volume, indice) => {

        print("\nProduto do volume " + (indice + 1));
        print("Código do volume: " + volume.codigo);

        const produto = db.produtos.findOne({
          _id: volume.idProduto
        });

        printjson(produto);

        // idProduto é uma referência à coleção produtos.
        // O produto contém o cadastro oficial do catálogo.
        // nomeProduto, pesoKg e dimensoes também são copiados
        // para o volume, preservando o histórico do pedido.
      });
    }

    // ---------------------------------------------------------------
    // 5. DADOS INCORPORADOS NO PEDIDO
    // ---------------------------------------------------------------

    print("\n==================================================");
    print("5. DADOS INCORPORADOS NO PEDIDO");
    print("==================================================");

    print("\n5.1 - Origem");
    printjson(pedido.origem);

    // A origem está incorporada porque faz parte do pedido
    // e é necessária para conhecer o local de partida.

    print("\n5.2 - Destino");
    printjson(pedido.destino);

    // O destino está incorporado para manter os dados do endereço
    // associados ao pedido, sem depender de outra consulta.

    print("\n5.3 - Volumes");
    printjson(pedido.volumes);

    // volumes é um array incorporado porque os volumes pertencem
    // ao pedido e são consultados junto com ele.

    print("\n5.4 - Pagamento");
    printjson(pedido.pagamento);

    // pagamento é um documento incorporado porque reúne
    // forma, valor, situação e data de pagamento do pedido.
  }

  // -------------------------------------------------------------------
  // 6. MOTORISTA — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("6. MOTORISTA REFERENCIADO PELA ENTREGA");
  print("==================================================");

  const motorista = db.motoristas.findOne({
    _id: entrega.idMotorista
  });

  printjson(motorista);

  // idMotorista é uma referência porque o motorista possui
  // cadastro próprio na coleção motoristas e pode realizar
  // diferentes entregas ao longo do tempo.

  // -------------------------------------------------------------------
  // 7. VEÍCULO — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("7. VEÍCULO REFERENCIADO PELA ENTREGA");
  print("==================================================");

  const veiculo = db.veiculos.findOne({
    _id: entrega.idVeiculo
  });

  printjson(veiculo);

  // idVeiculo é uma referência ao cadastro do veículo.
  // A placa, o tipo e a capacidade ficam na coleção veiculos,
  // evitando repetir o cadastro completo em cada entrega.

  // -------------------------------------------------------------------
  // 8. TRANSPORTADORA — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("8. TRANSPORTADORA REFERENCIADA PELA ENTREGA");
  print("==================================================");

  const transportadora = db.transportadoras.findOne({
    _id: entrega.idTransportadora
  });

  printjson(transportadora);

  // idTransportadora é uma referência ao cadastro da empresa.
  // O endereço da transportadora, por sua vez, está incorporado
  // no documento da própria transportadora.

  // -------------------------------------------------------------------
  // 9. CENTRO DE DISTRIBUIÇÃO — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("9. CENTRO DE DISTRIBUIÇÃO REFERENCIADO");
  print("==================================================");

  const centro = db.centrosDistribuicao.findOne({
    _id: entrega.idCentroDistribuicao
  });

  printjson(centro);

  // idCentroDistribuicao é uma referência porque o centro possui
  // cadastro próprio, que pode ser compartilhado por várias entregas.

  // -------------------------------------------------------------------
  // 10. OCORRÊNCIAS — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("10. OCORRÊNCIAS ASSOCIADAS À ENTREGA");
  print("==================================================");

  const ocorrencias = db.ocorrencias.find({
    idEntrega: entrega._id
  }).sort({
    dataHora: -1
  }).toArray();

  printjson(ocorrencias);

  // As ocorrências ficam em coleção separada porque uma entrega
  // pode ter nenhuma, uma ou várias ocorrências.
  // Cada documento utiliza idEntrega como referência.
  // Isso permite consultar o histórico sem aumentar indefinidamente
  // o tamanho do documento da entrega.

  // -------------------------------------------------------------------
  // 11. RASTREAMENTOS — REFERÊNCIA DA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("11. HISTÓRICO DE RASTREAMENTOS DA ENTREGA");
  print("==================================================");

  const rastreamentos = db.rastreamentos.find({
    idEntrega: entrega._id
  }).sort({
    registradoEm: 1
  }).toArray();

  printjson(rastreamentos);

  // Os rastreamentos ficam em coleção separada porque cada entrega
  // pode acumular muitos registros de GPS ao longo do trajeto.
  // idEntrega conecta cada ponto ao documento correspondente.
  // A ordenação crescente mostra o percurso do mais antigo ao recente.

  // -------------------------------------------------------------------
  // 12. DADOS INCORPORADOS NA ENTREGA
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("12. DADOS INCORPORADOS NA ENTREGA");
  print("==================================================");

  print("\n12.1 - Destino");
  printjson(entrega.destino);

  // destino é incorporado à entrega porque contém o endereço
  // de destino utilizado durante o transporte.

  print("\n12.2 - Rota");
  printjson(entrega.rota);

  // rota é incorporada porque distância e duração prevista/real
  // são informações diretamente relacionadas àquela entrega.
  // duracaoRealMin pode não existir enquanto a entrega está em andamento.

  print("\n12.3 - Último registro de GPS");
  printjson(entrega.rastreioAtual);

  // rastreioAtual é incorporado para recuperar rapidamente
  // a posição mais recente sem consultar todo o histórico.
  // O histórico completo permanece na coleção rastreamentos.

  print("\n12.4 - Horários das etapas");
  printjson(entrega.horariosEtapas);

  // horariosEtapas é incorporado porque reúne os horários
  // relevantes do ciclo de vida da entrega.

  // -------------------------------------------------------------------
  // 13. RESUMO DO PERCURSO
  // -------------------------------------------------------------------

  print("\n==================================================");
  print("13. RESUMO DOS RELACIONAMENTOS");
  print("==================================================");

  print("Entrega: " + entrega._id);
  print("Pedido: " + entrega.idPedido);
  print("Motorista: " + entrega.idMotorista);
  print("Veículo: " + entrega.idVeiculo);
  print("Transportadora: " + entrega.idTransportadora);
  print("Centro de distribuição: " + entrega.idCentroDistribuicao);

  print("Quantidade de ocorrências: " + ocorrencias.length);
  print("Quantidade de rastreamentos: " + rastreamentos.length);

  print("\nRelacionamentos consultados com sucesso.");
}

print("\nScript 05_relationships.js finalizado.");
```
