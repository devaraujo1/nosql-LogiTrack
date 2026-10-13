const banco = db.getSiblingDB("logitrack");

const endereco = {
  bsonType: "object",
  required: ["rua", "numero", "bairro", "cidade", "estado", "cep", "pais"],
  properties: {
    rua: { bsonType: "string" },
    numero: { bsonType: "string" },
    complemento: { bsonType: "string" },
    bairro: { bsonType: "string" },
    cidade: { bsonType: "string" },
    estado: { bsonType: "string" },
    cep: { bsonType: "string" },
    pais: { bsonType: "string" }
  }
};

const dimensoes = {
  bsonType: "object",
  required: ["comprimentoCm", "larguraCm", "alturaCm"],
  properties: {
    comprimentoCm: { bsonType: "double" },
    larguraCm: { bsonType: "double" },
    alturaCm: { bsonType: "double" }
  }
};

function criar(nome, required, properties) {
  properties._id = { bsonType: "objectId" };
  banco.createCollection(nome, {
    validator: {
      $jsonSchema: {
        bsonType: "object",
        required: required,
        properties: properties,
        additionalProperties: false
      }
    },
    validationLevel: "strict",
    validationAction: "error"
  });
  print("Collection criada: " + nome);
}

criar("clientes", ["nome", "tipo", "documento", "email", "telefone", "endereco", "criadoEm", "atualizadoEm"], {
  nome: { bsonType: "string" },
  tipo: { enum: ["PF", "PJ"] },
  documento: { bsonType: "string" },
  email: { bsonType: "string" },
  telefone: { bsonType: "string" },
  endereco: endereco,
  criadoEm: { bsonType: "date" },
  atualizadoEm: { bsonType: "date" }
});

criar("produtos", ["nome", "codigo", "categoria", "pesoKg", "dimensoes", "fragil", "ativo"], {
  nome: { bsonType: "string" },
  codigo: { bsonType: "string" },
  categoria: { bsonType: "string" },
  pesoKg: { bsonType: "double" },
  dimensoes: dimensoes,
  fragil: { bsonType: "bool" },
  ativo: { bsonType: "bool" }
});

criar("transportadoras", ["nome", "documento", "email", "telefone", "endereco", "ativo"], {
  nome: { bsonType: "string" },
  documento: { bsonType: "string" },
  email: { bsonType: "string" },
  telefone: { bsonType: "string" },
  endereco: endereco,
  ativo: { bsonType: "bool" }
});

criar("centrosDistribuicao", ["nome", "codigo", "endereco", "ativo"], {
  nome: { bsonType: "string" },
  codigo: { bsonType: "string" },
  endereco: endereco,
  ativo: { bsonType: "bool" }
});

criar("motoristas", ["idTransportadora", "nome", "documento", "numeroCnh", "telefone", "situacao"], {
  idTransportadora: { bsonType: "objectId" },
  nome: { bsonType: "string" },
  documento: { bsonType: "string" },
  numeroCnh: { bsonType: "string" },
  telefone: { bsonType: "string" },
  situacao: { enum: ["disponivel", "em_rota", "inativo"] }
});

criar("veiculos", ["idTransportadora", "placa", "tipo", "capacidadeKg", "situacao"], {
  idTransportadora: { bsonType: "objectId" },
  placa: { bsonType: "string" },
  tipo: { enum: ["van", "caminhao", "motocicleta"] },
  capacidadeKg: { bsonType: "int" },
  situacao: { enum: ["disponivel", "em_uso", "manutencao"] }
});

criar("pedidos", ["idCliente", "situacao", "origem", "destino", "volumes", "pagamento", "criadoEm", "atualizadoEm"], {
  idCliente: { bsonType: "objectId" },
  situacao: { enum: ["criado", "pago", "em_transito", "entregue", "cancelado"] },
  origem: endereco,
  destino: endereco,
  volumes: {
    bsonType: "array",
    items: {
      bsonType: "object",
      required: ["codigo", "idProduto", "nomeProduto", "pesoKg", "dimensoes", "quantidade"],
      properties: {
        codigo: { bsonType: "string" },
        idProduto: { bsonType: "objectId" },
        nomeProduto: { bsonType: "string" },
        pesoKg: { bsonType: "double" },
        dimensoes: dimensoes,
        quantidade: { bsonType: "int" }
      }
    }
  },
  pagamento: {
    bsonType: "object",
    required: ["forma", "valor", "situacao"],
    properties: {
      forma: { enum: ["pix", "boleto", "cartao"] },
      valor: { bsonType: "decimal" },
      situacao: { enum: ["pendente", "pago"] },
      pagoEm: { bsonType: "date" }
    }
  },
  criadoEm: { bsonType: "date" },
  atualizadoEm: { bsonType: "date" }
});

criar("entregas", ["idPedido", "idTransportadora", "idMotorista", "idVeiculo", "idCentroDistribuicao", "situacao", "atrasado", "prometidoPara", "destino", "rota", "horariosEtapas", "rastreioAtual", "criadoEm", "atualizadoEm"], {
  idPedido: { bsonType: "objectId" },
  idTransportadora: { bsonType: "objectId" },
  idMotorista: { bsonType: "objectId" },
  idVeiculo: { bsonType: "objectId" },
  idCentroDistribuicao: { bsonType: "objectId" },
  situacao: { enum: ["no_cd", "em_transito", "entregue", "falhou"] },
  atrasado: { bsonType: "bool" },
  prometidoPara: { bsonType: "date" },
  destino: endereco,
  rota: {
    bsonType: "object",
    required: ["distanciaKm", "duracaoPrevistaMin", "duracaoRealMin"],
    properties: {
      distanciaKm: { bsonType: "double" },
      duracaoPrevistaMin: { bsonType: "int" },
      duracaoRealMin: { bsonType: "int" }
    }
  },
  horariosEtapas: {
    bsonType: "object",
    properties: {
      recebidoNoCdEm: { bsonType: "date" },
      saiuEm: { bsonType: "date" },
      chegouEm: { bsonType: "date" },
      entregueEm: { bsonType: "date" }
    }
  },
  rastreioAtual: {
    bsonType: "object",
    required: ["latitude", "longitude", "cidade", "estado", "registradoEm"],
    properties: {
      latitude: { bsonType: "double" },
      longitude: { bsonType: "double" },
      cidade: { bsonType: "string" },
      estado: { bsonType: "string" },
      registradoEm: { bsonType: "date" }
    }
  },
  criadoEm: { bsonType: "date" },
  atualizadoEm: { bsonType: "date" }
});

criar("ocorrencias", ["idEntrega", "idCentroDistribuicao", "idMotorista", "tipo", "descricao", "dataHora", "cidade"], {
  idEntrega: { bsonType: "objectId" },
  idCentroDistribuicao: { bsonType: "objectId" },
  idMotorista: { bsonType: "objectId" },
  tipo: { enum: ["atraso", "avaria", "ausente", "recusa", "outro"] },
  descricao: { bsonType: "string" },
  dataHora: { bsonType: "date" },
  cidade: { bsonType: "string" },
  minutosAtraso: { bsonType: "int" },
  gravidade: { bsonType: "string" }
});

criar("rastreamentos", ["idEntrega", "latitude", "longitude", "cidade", "estado", "registradoEm"], {
  idEntrega: { bsonType: "objectId" },
  latitude: { bsonType: "double" },
  longitude: { bsonType: "double" },
  cidade: { bsonType: "string" },
  estado: { bsonType: "string" },
  registradoEm: { bsonType: "date" }
});
