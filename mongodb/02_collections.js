const banco = db.getSiblingDB("logitrack");

const schemas = {
  clientes: {
    bsonType: "object",
    required: ["_id", "nome", "email", "telefone", "tipoPessoa", "ativo", "dataCadastro"],
    properties: {
      _id: { bsonType: "objectId" },
      nome: { bsonType: "string" },
      email: { bsonType: "string" },
      telefone: { bsonType: "string" },
      tipoPessoa: { enum: ["PF", "PJ"] },
      documento: { bsonType: "string" },
      ativo: { bsonType: "bool" },
      dataCadastro: { bsonType: "date" },
      endereco: {
        bsonType: "object",
        required: ["logradouro", "numero", "cidade", "estado", "cep"],
        properties: {
          logradouro: { bsonType: "string" },
          numero: { bsonType: "string" },
          complemento: { bsonType: ["string", "null"] },
          bairro: { bsonType: "string" },
          cidade: { bsonType: "string" },
          estado: { bsonType: "string" },
          cep: { bsonType: "string" }
        }
      }
    }
  },

  produtos: {
    bsonType: "object",
    required: ["_id", "codigoProduto", "nome", "pesoKg", "fragil", "ativo"],
    properties: {
      _id: { bsonType: "objectId" },
      codigoProduto: { bsonType: "string" },
      nome: { bsonType: "string" },
      descricao: { bsonType: "string" },
      pesoKg: { bsonType: ["double", "int", "long", "decimal"] },
      fragil: { bsonType: "bool" },
      ativo: { bsonType: "bool" }
    }
  },

  pedidos: {
    bsonType: "object",
    required: ["_id", "idCliente", "situacao", "dataPedido", "origem", "destino", "volumes", "pagamento"],
    properties: {
      _id: { bsonType: "objectId" },
      idCliente: { bsonType: "objectId" },
      situacao: { bsonType: "string" },
      dataPedido: { bsonType: "date" },
      origem: {
        bsonType: "object",
        properties: {
          cidade: { bsonType: "string" },
          estado: { bsonType: "string" },
          cep: { bsonType: "string" },
          endereco: { bsonType: "string" }
        }
      },
      destino: {
        bsonType: "object",
        required: ["cidade", "estado"],
        properties: {
          cidade: { bsonType: "string" },
          estado: { bsonType: "string" },
          cep: { bsonType: "string" },
          endereco: { bsonType: "string" }
        }
      },
      volumes: {
        bsonType: "array",
        items: {
          bsonType: "object",
          required: ["idProduto", "nomeProduto", "pesoKg", "quantidade"],
          properties: {
            idProduto: { bsonType: "objectId" },
            nomeProduto: { bsonType: "string" },
            pesoKg: { bsonType: ["double", "int", "long", "decimal"] },
            quantidade: { bsonType: "int" },
            fragil: { bsonType: "bool" }
          }
        }
      },
      pagamento: {
        bsonType: "object",
        properties: {
          metodo: { bsonType: "string" },
          status: { bsonType: "string" },
          valorProdutos: { bsonType: ["decimal", "double", "int", "long"] },
          dataPagamento: { bsonType: ["date", "null"] }
        }
      }
    }
  },

  entregas: {
    bsonType: "object",
    required: ["_id", "idPedido", "idMotorista", "idVeiculo", "idTransportadora", "idCentroDistribuicao", "situacao", "atrasada", "prazoEntrega", "rota"],
    properties: {
      _id: { bsonType: "objectId" },
      idPedido: { bsonType: "objectId" },
      idMotorista: { bsonType: "objectId" },
      idVeiculo: { bsonType: "objectId" },
      idTransportadora: { bsonType: "objectId" },
      idCentroDistribuicao: { bsonType: "objectId" },
      situacao: { bsonType: "string" },
      atrasada: { bsonType: "bool" },
      prazoEntrega: { bsonType: "date" },
      dataSaida: { bsonType: ["date", "null"] },
      dataEntrega: { bsonType: ["date", "null"] },
      rota: {
        bsonType: "object",
        properties: {
          origem: { bsonType: "string" },
          destino: { bsonType: "string" },
          distanciaKm: { bsonType: ["double", "int", "long", "decimal"] },
          duracaoPrevistaMin: { bsonType: "int" },
          duracaoRealMin: { bsonType: "int" },
          etapas: {
            bsonType: "array",
            items: {
              bsonType: "object",
              properties: {
                descricao: { bsonType: "string" },
                horarioPrevisto: { bsonType: "date" },
                horarioReal: { bsonType: ["date", "null"] }
              }
            }
          }
        }
      },
      ultimoRastreio: {
        bsonType: ["object", "null"],
        properties: {
          latitude: { bsonType: ["double", "int", "long", "decimal"] },
          longitude: { bsonType: ["double", "int", "long", "decimal"] },
          dataHora: { bsonType: "date" },
          cidade: { bsonType: "string" },
          status: { bsonType: "string" }
        }
      },
      frete: { bsonType: "decimal" }
    }
  },

  transportadoras: {
    bsonType: "object",
    required: ["_id", "nome", "cnpj", "ativo", "endereco"],
    properties: {
      _id: { bsonType: "objectId" },
      nome: { bsonType: "string" },
      cnpj: { bsonType: "string" },
      telefone: { bsonType: "string" },
      ativo: { bsonType: "bool" },
      endereco: {
        bsonType: "object",
        properties: {
          logradouro: { bsonType: "string" },
          numero: { bsonType: "string" },
          cidade: { bsonType: "string" },
          estado: { bsonType: "string" },
          cep: { bsonType: "string" }
        }
      }
    }
  },

  motoristas: {
    bsonType: "object",
    required: ["_id", "nome", "cpf", "telefone", "disponivel", "ativo"],
    properties: {
      _id: { bsonType: "objectId" },
      nome: { bsonType: "string" },
      cpf: { bsonType: "string" },
      telefone: { bsonType: "string" },
      cnh: { bsonType: "string" },
      disponivel: { bsonType: "bool" },
      ativo: { bsonType: "bool" }
    }
  },

  veiculos: {
    bsonType: "object",
    required: ["_id", "placa", "tipo", "capacidadeKg", "ativo"],
    properties: {
      _id: { bsonType: "objectId" },
      placa: { bsonType: "string" },
      tipo: { bsonType: "string" },
      capacidadeKg: { bsonType: ["double", "int", "long", "decimal"] },
      ativo: { bsonType: "bool" }
    }
  },

  centrosDistribuicao: {
    bsonType: "object",
    required: ["_id", "nome", "endereco", "ativo"],
    properties: {
      _id: { bsonType: "objectId" },
      nome: { bsonType: "string" },
      endereco: {
        bsonType: "object",
        required: ["cidade", "estado"],
        properties: {
          logradouro: { bsonType: "string" },
          numero: { bsonType: "string" },
          cidade: { bsonType: "string" },
          estado: { bsonType: "string" },
          cep: { bsonType: "string" }
        }
      },
      ativo: { bsonType: "bool" }
    }
  },

  ocorrencias: {
    bsonType: "object",
    required: ["_id", "idEntrega", "tipo", "descricao", "dataHora"],
    properties: {
      _id: { bsonType: "objectId" },
      idEntrega: { bsonType: "objectId" },
      tipo: { enum: ["atraso", "avaria", "outro"] },
      descricao: { bsonType: "string" },
      dataHora: { bsonType: "date" },
      minutosAtraso: { bsonType: "int" },
      gravidade: { enum: ["baixa", "media", "alta", "critica"] }
    }
  },

  rastreamentos: {
    bsonType: "object",
    required: ["_id", "idEntrega", "latitude", "longitude", "dataHora"],
    properties: {
      _id: { bsonType: "objectId" },
      idEntrega: { bsonType: "objectId" },
      latitude: { bsonType: "double" },
      longitude: { bsonType: "double" },
      cidade: { bsonType: "string" },
      estado: { bsonType: "string" },
      status: { bsonType: "string" },
      dataHora: { bsonType: "date" }
    }
  }
};

for (const [nome, jsonSchema] of Object.entries(schemas)) {
  const existe = banco.getCollectionInfos({ name: nome }).length > 0;

  if (!existe) {
    banco.createCollection(nome, {
      validator: { $jsonSchema: jsonSchema },
      validationLevel: "strict",
      validationAction: "error"
    });
  } else {
    banco.runCommand({
      collMod: nome,
      validator: { $jsonSchema: jsonSchema },
      validationLevel: "strict",
      validationAction: "error"
    });
  }
}
