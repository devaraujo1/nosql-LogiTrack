// LogiTrack — Atividade 3
// Responsável: João Paulo de Oliveira Carneiro
//
// Documentos de exemplo da operação na Bahia.
// ObjectId fixo para os outros scripts apontarem para o mesmo documento.
// insertOne e insertMany aparecem de propósito.
// Ocorrência de atraso leva minutosAtraso; ocorrência de avaria leva gravidade.

const banco = db.getSiblingDB("logitrack");

const idTransportadoraNorte = ObjectId("64f100000000000000000001");
const idTransportadoraBahia = ObjectId("64f100000000000000000002");

const idCdFeira = ObjectId("64f200000000000000000001");
const idCdSalvador = ObjectId("64f200000000000000000002");

const idMotoristaCarlos = ObjectId("64f300000000000000000001");
const idMotoristaAna = ObjectId("64f300000000000000000002");
const idMotoristaPedro = ObjectId("64f300000000000000000003");

const idVeiculoVan = ObjectId("64f400000000000000000001");
const idVeiculoCaminhao = ObjectId("64f400000000000000000002");
const idVeiculoMoto = ObjectId("64f400000000000000000003");

const idClienteMaria = ObjectId("64f500000000000000000001");
const idClienteOliveira = ObjectId("64f500000000000000000002");
const idClienteJoao = ObjectId("64f500000000000000000003");

const idProdutoNotebook = ObjectId("64f600000000000000000001");
const idProdutoMouse = ObjectId("64f600000000000000000002");
const idProdutoCadeira = ObjectId("64f600000000000000000003");
const idProdutoPerfume = ObjectId("64f600000000000000000004");

const idPedidoMariaSalvador = ObjectId("64f700000000000000000001");
const idPedidoOliveiraFeira = ObjectId("64f700000000000000000002");
const idPedidoJoaoSerrinha = ObjectId("64f700000000000000000003");
const idPedidoMariaIlheus = ObjectId("64f700000000000000000004");

const idEntregaSalvador = ObjectId("64f800000000000000000001");
const idEntregaFeira = ObjectId("64f800000000000000000002");
const idEntregaSerrinha = ObjectId("64f800000000000000000003");
const idEntregaIlheus = ObjectId("64f800000000000000000004");

function endereco(rua, numero, complemento, bairro, cidade, estado, cep) {
  return {
    rua: rua,
    numero: numero,
    complemento: complemento,
    bairro: bairro,
    cidade: cidade,
    estado: estado,
    cep: cep,
    pais: "Brasil"
  };
}

function dimensoes(comprimentoCm, larguraCm, alturaCm) {
  return {
    comprimentoCm: Double(comprimentoCm),
    larguraCm: Double(larguraCm),
    alturaCm: Double(alturaCm)
  };
}

function ponto(latitude, longitude, cidade, estado, registradoEm) {
  return {
    latitude: Double(latitude),
    longitude: Double(longitude),
    cidade: cidade,
    estado: estado,
    registradoEm: registradoEm
  };
}

banco.transportadoras.deleteMany({});
banco.centrosDistribuicao.deleteMany({});
banco.motoristas.deleteMany({});
banco.veiculos.deleteMany({});
banco.clientes.deleteMany({});
banco.produtos.deleteMany({});
banco.pedidos.deleteMany({});
banco.entregas.deleteMany({});
banco.ocorrencias.deleteMany({});
banco.rastreamentos.deleteMany({});

banco.transportadoras.insertOne({
  _id: idTransportadoraNorte,
  nome: "Rota Norte Logística",
  documento: "12.345.678/0001-90",
  email: "contato@rotanorte.example",
  telefone: "7532101000",
  endereco: endereco("Avenida Getúlio Vargas", "1500", "Galpão 2", "Centro", "Feira de Santana", "BA", "44001-000"),
  ativo: true
});

banco.transportadoras.insertMany([
  {
    _id: idTransportadoraBahia,
    nome: "Bahia Express",
    documento: "98.765.432/0001-10",
    email: "operacao@bahiaexpress.example",
    telefone: "7133332000",
    endereco: endereco("Rua Chile", "40", "", "Comércio", "Salvador", "BA", "40020-000"),
    ativo: true
  }
]);

banco.centrosDistribuicao.insertMany([
  {
    _id: idCdFeira,
    nome: "CD Feira de Santana",
    codigo: "CD-FSA",
    endereco: endereco("Rodovia BR-116", "Km 8", "Bloco A", "Tomba", "Feira de Santana", "BA", "44090-000"),
    ativo: true
  },
  {
    _id: idCdSalvador,
    nome: "CD Salvador",
    codigo: "CD-SSA",
    endereco: endereco("Avenida Luís Viana", "3200", "", "Paralela", "Salvador", "BA", "41730-101"),
    ativo: true
  }
]);

banco.motoristas.insertMany([
  {
    _id: idMotoristaCarlos,
    idTransportadora: idTransportadoraNorte,
    nome: "Carlos Lima",
    documento: "111.222.333-44",
    numeroCnh: "BA100200300",
    telefone: "75988881001",
    situacao: "em_rota"
  },
  {
    _id: idMotoristaAna,
    idTransportadora: idTransportadoraNorte,
    nome: "Ana Souza",
    documento: "222.333.444-55",
    numeroCnh: "BA100200301",
    telefone: "75988881002",
    situacao: "disponivel"
  },
  {
    _id: idMotoristaPedro,
    idTransportadora: idTransportadoraBahia,
    nome: "Pedro Almeida",
    documento: "333.444.555-66",
    numeroCnh: "BA100200302",
    telefone: "71988881003",
    situacao: "em_rota"
  }
]);

banco.veiculos.insertMany([
  {
    _id: idVeiculoVan,
    idTransportadora: idTransportadoraNorte,
    placa: "ABC1D23",
    tipo: "van",
    capacidadeKg: NumberInt(800),
    situacao: "em_uso"
  },
  {
    _id: idVeiculoCaminhao,
    idTransportadora: idTransportadoraNorte,
    placa: "DEF2E34",
    tipo: "caminhao",
    capacidadeKg: NumberInt(3000),
    situacao: "disponivel"
  },
  {
    _id: idVeiculoMoto,
    idTransportadora: idTransportadoraBahia,
    placa: "GHI3F45",
    tipo: "motocicleta",
    capacidadeKg: NumberInt(30),
    situacao: "em_uso"
  }
]);

banco.clientes.insertOne({
  _id: idClienteMaria,
  nome: "Maria Silva",
  tipo: "PF",
  documento: "123.456.789-00",
  email: "maria@email.com",
  telefone: "75999990000",
  endereco: endereco("Rua A", "100", "", "Centro", "Feira de Santana", "BA", "44000-000"),
  criadoEm: ISODate("2026-09-01T11:00:00Z"),
  atualizadoEm: ISODate("2026-09-10T11:00:00Z")
});

banco.clientes.insertMany([
  {
    _id: idClienteOliveira,
    nome: "Comercial Oliveira LTDA",
    tipo: "PJ",
    documento: "45.678.901/0001-22",
    email: "compras@oliveira.example",
    telefone: "7130304040",
    endereco: endereco("Rua Chile", "80", "Sala 3", "Comércio", "Salvador", "BA", "40020-010"),
    criadoEm: ISODate("2026-08-15T14:00:00Z"),
    atualizadoEm: ISODate("2026-09-12T14:00:00Z")
  },
  {
    _id: idClienteJoao,
    nome: "João Santos",
    tipo: "PF",
    documento: "987.654.321-00",
    email: "joao.santos@email.com",
    telefone: "75988887777",
    endereco: endereco("Rua B", "45", "Apto 2", "Kalilândia", "Feira de Santana", "BA", "44050-000"),
    criadoEm: ISODate("2026-09-05T09:30:00Z"),
    atualizadoEm: ISODate("2026-09-05T09:30:00Z")
  }
]);

banco.produtos.insertMany([
  {
    _id: idProdutoNotebook,
    nome: "Notebook",
    codigo: "PROD-001",
    categoria: "Eletrônicos",
    pesoKg: Double(2.1),
    dimensoes: dimensoes(36, 25, 3),
    fragil: true,
    ativo: true
  },
  {
    _id: idProdutoMouse,
    nome: "Mouse",
    codigo: "PROD-002",
    categoria: "Eletrônicos",
    pesoKg: Double(0.12),
    dimensoes: dimensoes(12, 7, 4),
    fragil: false,
    ativo: true
  },
  {
    _id: idProdutoCadeira,
    nome: "Cadeira de escritório",
    codigo: "PROD-003",
    categoria: "Móveis",
    pesoKg: Double(12.5),
    dimensoes: dimensoes(60, 60, 110),
    fragil: false,
    ativo: true
  },
  {
    _id: idProdutoPerfume,
    nome: "Perfume",
    codigo: "PROD-004",
    categoria: "Cosméticos",
    pesoKg: Double(0.4),
    dimensoes: dimensoes(8, 8, 15),
    fragil: true,
    ativo: true
  }
]);

banco.pedidos.insertMany([
  {
    _id: idPedidoMariaSalvador,
    idCliente: idClienteMaria,
    situacao: "em_transito",
    origem: endereco("Rua A", "100", "", "Centro", "Feira de Santana", "BA", "44000-000"),
    destino: endereco("Rua da Graça", "200", "Apto 11", "Graça", "Salvador", "BA", "40150-000"),
    volumes: [
      {
        codigo: "VOL-1001",
        idProduto: idProdutoNotebook,
        nomeProduto: "Notebook",
        pesoKg: Double(2.1),
        dimensoes: dimensoes(36, 25, 3),
        quantidade: NumberInt(1)
      },
      {
        codigo: "VOL-1002",
        idProduto: idProdutoMouse,
        nomeProduto: "Mouse",
        pesoKg: Double(0.12),
        dimensoes: dimensoes(12, 7, 4),
        quantidade: NumberInt(2)
      }
    ],
    pagamento: {
      forma: "pix",
      valor: NumberDecimal("189.90"),
      situacao: "pago",
      pagoEm: ISODate("2026-09-16T11:20:00Z")
    },
    criadoEm: ISODate("2026-09-16T11:00:00Z"),
    atualizadoEm: ISODate("2026-09-16T15:00:00Z")
  },
  {
    _id: idPedidoOliveiraFeira,
    idCliente: idClienteOliveira,
    situacao: "em_transito",
    origem: endereco("Rua Chile", "80", "Sala 3", "Comércio", "Salvador", "BA", "40020-010"),
    destino: endereco("Avenida Senhor dos Passos", "900", "", "Centro", "Feira de Santana", "BA", "44002-000"),
    volumes: [
      {
        codigo: "VOL-2001",
        idProduto: idProdutoCadeira,
        nomeProduto: "Cadeira de escritório",
        pesoKg: Double(12.5),
        dimensoes: dimensoes(60, 60, 110),
        quantidade: NumberInt(1)
      },
      {
        codigo: "VOL-2002",
        idProduto: idProdutoPerfume,
        nomeProduto: "Perfume",
        pesoKg: Double(0.4),
        dimensoes: dimensoes(8, 8, 15),
        quantidade: NumberInt(3)
      }
    ],
    pagamento: {
      forma: "boleto",
      valor: NumberDecimal("240.00"),
      situacao: "pago",
      pagoEm: ISODate("2026-09-17T13:00:00Z")
    },
    criadoEm: ISODate("2026-09-17T12:10:00Z"),
    atualizadoEm: ISODate("2026-09-17T16:00:00Z")
  },
  {
    _id: idPedidoJoaoSerrinha,
    idCliente: idClienteJoao,
    situacao: "entregue",
    origem: endereco("Rua B", "45", "Apto 2", "Kalilândia", "Feira de Santana", "BA", "44050-000"),
    destino: endereco("Praça da Matriz", "10", "", "Centro", "Serrinha", "BA", "48700-000"),
    volumes: [
      {
        codigo: "VOL-3001",
        idProduto: idProdutoMouse,
        nomeProduto: "Mouse",
        pesoKg: Double(0.12),
        dimensoes: dimensoes(12, 7, 4),
        quantidade: NumberInt(1)
      }
    ],
    pagamento: {
      forma: "pix",
      valor: NumberDecimal("35.50"),
      situacao: "pago",
      pagoEm: ISODate("2026-09-15T10:05:00Z")
    },
    criadoEm: ISODate("2026-09-15T10:00:00Z"),
    atualizadoEm: ISODate("2026-09-15T16:40:00Z")
  },
  {
    _id: idPedidoMariaIlheus,
    idCliente: idClienteMaria,
    situacao: "em_transito",
    origem: endereco("Rua A", "100", "", "Centro", "Feira de Santana", "BA", "44000-000"),
    destino: endereco("Avenida Soares Lopes", "500", "", "Centro", "Ilhéus", "BA", "45653-000"),
    volumes: [
      {
        codigo: "VOL-4001",
        idProduto: idProdutoNotebook,
        nomeProduto: "Notebook",
        pesoKg: Double(2.1),
        dimensoes: dimensoes(36, 25, 3),
        quantidade: NumberInt(1)
      },
      {
        codigo: "VOL-4002",
        idProduto: idProdutoCadeira,
        nomeProduto: "Cadeira de escritório",
        pesoKg: Double(12.5),
        dimensoes: dimensoes(60, 60, 110),
        quantidade: NumberInt(1)
      }
    ],
    pagamento: {
      forma: "cartao",
      valor: NumberDecimal("310.00"),
      situacao: "pago",
      pagoEm: ISODate("2026-09-17T08:15:00Z")
    },
    criadoEm: ISODate("2026-09-17T08:00:00Z"),
    atualizadoEm: ISODate("2026-09-17T11:00:00Z")
  }
]);

banco.entregas.insertMany([
  {
    _id: idEntregaSalvador,
    idPedido: idPedidoMariaSalvador,
    idTransportadora: idTransportadoraNorte,
    idMotorista: idMotoristaCarlos,
    idVeiculo: idVeiculoVan,
    idCentroDistribuicao: idCdFeira,
    situacao: "em_transito",
    atrasado: true,
    prometidoPara: ISODate("2026-09-16T21:00:00Z"),
    destino: endereco("Rua da Graça", "200", "Apto 11", "Graça", "Salvador", "BA", "40150-000"),
    rota: {
      distanciaKm: Double(108.5),
      duracaoPrevistaMin: NumberInt(120),
      duracaoRealMin: NumberInt(240)
    },
    horariosEtapas: {
      recebidoNoCdEm: ISODate("2026-09-16T12:00:00Z"),
      saiuEm: ISODate("2026-09-16T15:00:00Z")
    },
    rastreioAtual: ponto(-12.95, -38.51, "Salvador", "BA", ISODate("2026-09-16T22:30:00Z")),
    criadoEm: ISODate("2026-09-16T12:00:00Z"),
    atualizadoEm: ISODate("2026-09-16T22:30:00Z")
  },
  {
    _id: idEntregaFeira,
    idPedido: idPedidoOliveiraFeira,
    idTransportadora: idTransportadoraBahia,
    idMotorista: idMotoristaPedro,
    idVeiculo: idVeiculoMoto,
    idCentroDistribuicao: idCdSalvador,
    situacao: "em_transito",
    atrasado: false,
    prometidoPara: ISODate("2026-09-17T22:00:00Z"),
    destino: endereco("Avenida Senhor dos Passos", "900", "", "Centro", "Feira de Santana", "BA", "44002-000"),
    rota: {
      distanciaKm: Double(112),
      duracaoPrevistaMin: NumberInt(90),
      duracaoRealMin: NumberInt(80)
    },
    horariosEtapas: {
      recebidoNoCdEm: ISODate("2026-09-17T15:00:00Z"),
      saiuEm: ISODate("2026-09-17T16:10:00Z")
    },
    rastreioAtual: ponto(-12.4, -38.85, "Conceição do Jacuípe", "BA", ISODate("2026-09-17T17:20:00Z")),
    criadoEm: ISODate("2026-09-17T15:00:00Z"),
    atualizadoEm: ISODate("2026-09-17T17:20:00Z")
  },
  {
    _id: idEntregaSerrinha,
    idPedido: idPedidoJoaoSerrinha,
    idTransportadora: idTransportadoraNorte,
    idMotorista: idMotoristaAna,
    idVeiculo: idVeiculoCaminhao,
    idCentroDistribuicao: idCdFeira,
    situacao: "entregue",
    atrasado: false,
    prometidoPara: ISODate("2026-09-15T18:00:00Z"),
    destino: endereco("Praça da Matriz", "10", "", "Centro", "Serrinha", "BA", "48700-000"),
    rota: {
      distanciaKm: Double(78.2),
      duracaoPrevistaMin: NumberInt(60),
      duracaoRealMin: NumberInt(55)
    },
    horariosEtapas: {
      recebidoNoCdEm: ISODate("2026-09-15T12:00:00Z"),
      saiuEm: ISODate("2026-09-15T13:10:00Z"),
      chegouEm: ISODate("2026-09-15T14:05:00Z"),
      entregueEm: ISODate("2026-09-15T14:12:00Z")
    },
    rastreioAtual: ponto(-11.664, -39.007, "Serrinha", "BA", ISODate("2026-09-15T14:12:00Z")),
    criadoEm: ISODate("2026-09-15T12:00:00Z"),
    atualizadoEm: ISODate("2026-09-15T14:12:00Z")
  },
  {
    _id: idEntregaIlheus,
    idPedido: idPedidoMariaIlheus,
    idTransportadora: idTransportadoraNorte,
    idMotorista: idMotoristaCarlos,
    idVeiculo: idVeiculoVan,
    idCentroDistribuicao: idCdFeira,
    situacao: "em_transito",
    atrasado: true,
    prometidoPara: ISODate("2026-09-17T15:00:00Z"),
    destino: endereco("Avenida Soares Lopes", "500", "", "Centro", "Ilhéus", "BA", "45653-000"),
    rota: {
      distanciaKm: Double(430),
      duracaoPrevistaMin: NumberInt(300),
      duracaoRealMin: NumberInt(380)
    },
    horariosEtapas: {
      recebidoNoCdEm: ISODate("2026-09-17T08:40:00Z"),
      saiuEm: ISODate("2026-09-17T11:05:00Z")
    },
    rastreioAtual: ponto(-14.788, -39.278, "Itabuna", "BA", ISODate("2026-09-17T18:30:00Z")),
    criadoEm: ISODate("2026-09-17T08:40:00Z"),
    atualizadoEm: ISODate("2026-09-17T18:30:00Z")
  }
]);

banco.ocorrencias.insertMany([
  {
    _id: ObjectId("64f900000000000000000001"),
    idEntrega: idEntregaSalvador,
    idCentroDistribuicao: idCdFeira,
    idMotorista: idMotoristaCarlos,
    tipo: "atraso",
    descricao: "Congestionamento na BR-324 depois do horário prometido.",
    dataHora: ISODate("2026-09-16T22:10:00Z"),
    cidade: "Salvador",
    minutosAtraso: NumberInt(95)
  },
  {
    _id: ObjectId("64f900000000000000000002"),
    idEntrega: idEntregaIlheus,
    idCentroDistribuicao: idCdFeira,
    idMotorista: idMotoristaCarlos,
    tipo: "atraso",
    descricao: "Saída do CD depois da janela da manhã.",
    dataHora: ISODate("2026-09-17T08:40:00Z"),
    cidade: "Feira de Santana",
    minutosAtraso: NumberInt(40)
  }
]);

banco.ocorrencias.insertOne({
  _id: ObjectId("64f900000000000000000003"),
  idEntrega: idEntregaIlheus,
  idCentroDistribuicao: idCdFeira,
  idMotorista: idMotoristaCarlos,
  tipo: "avaria",
  descricao: "Embalagem da cadeira amassada na carroceria.",
  dataHora: ISODate("2026-09-17T16:05:00Z"),
  cidade: "Itabuna",
  gravidade: "media"
});

banco.rastreamentos.insertMany([
  {
    _id: ObjectId("64fa00000000000000000001"),
    idEntrega: idEntregaSalvador,
    latitude: Double(-12.266),
    longitude: Double(-38.966),
    cidade: "Feira de Santana",
    estado: "BA",
    registradoEm: ISODate("2026-09-16T15:00:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000002"),
    idEntrega: idEntregaSalvador,
    latitude: Double(-12.55),
    longitude: Double(-38.7),
    cidade: "Amélia Rodrigues",
    estado: "BA",
    registradoEm: ISODate("2026-09-16T18:40:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000003"),
    idEntrega: idEntregaSalvador,
    latitude: Double(-12.95),
    longitude: Double(-38.51),
    cidade: "Salvador",
    estado: "BA",
    registradoEm: ISODate("2026-09-16T22:30:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000004"),
    idEntrega: idEntregaFeira,
    latitude: Double(-12.977),
    longitude: Double(-38.501),
    cidade: "Salvador",
    estado: "BA",
    registradoEm: ISODate("2026-09-17T16:10:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000005"),
    idEntrega: idEntregaFeira,
    latitude: Double(-12.4),
    longitude: Double(-38.85),
    cidade: "Conceição do Jacuípe",
    estado: "BA",
    registradoEm: ISODate("2026-09-17T17:20:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000006"),
    idEntrega: idEntregaSerrinha,
    latitude: Double(-12.1),
    longitude: Double(-38.99),
    cidade: "Feira de Santana",
    estado: "BA",
    registradoEm: ISODate("2026-09-15T13:10:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000007"),
    idEntrega: idEntregaSerrinha,
    latitude: Double(-11.664),
    longitude: Double(-39.007),
    cidade: "Serrinha",
    estado: "BA",
    registradoEm: ISODate("2026-09-15T14:12:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000008"),
    idEntrega: idEntregaIlheus,
    latitude: Double(-12.266),
    longitude: Double(-38.966),
    cidade: "Feira de Santana",
    estado: "BA",
    registradoEm: ISODate("2026-09-17T11:05:00Z")
  },
  {
    _id: ObjectId("64fa00000000000000000009"),
    idEntrega: idEntregaIlheus,
    latitude: Double(-14.788),
    longitude: Double(-39.278),
    cidade: "Itabuna",
    estado: "BA",
    registradoEm: ISODate("2026-09-17T18:30:00Z")
  }
]);

print("Documentos inseridos em logitrack.");
print("transportadoras: " + banco.transportadoras.countDocuments());
print("centrosDistribuicao: " + banco.centrosDistribuicao.countDocuments());
print("motoristas: " + banco.motoristas.countDocuments());
print("veiculos: " + banco.veiculos.countDocuments());
print("clientes: " + banco.clientes.countDocuments());
print("produtos: " + banco.produtos.countDocuments());
print("pedidos: " + banco.pedidos.countDocuments());
print("entregas: " + banco.entregas.countDocuments());
print("ocorrencias: " + banco.ocorrencias.countDocuments());
print("rastreamentos: " + banco.rastreamentos.countDocuments());
