# API REST — exemplos (Postman / Insomnia)

Base: `http://localhost:8080`

## Clientes

### Criar — `POST /api/clientes`

```json
{
  "nome": "Maria Silva",
  "tipo": "PF",
  "documento": "123.456.789-00",
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

Resposta esperada: **201 Created** com o documento incluindo `id`.

### Listar — `GET /api/clientes` → **200 OK**

### Buscar — `GET /api/clientes/{id}` → **200** ou **404**

### Atualizar — `PUT /api/clientes/{id}` → **200**

### Excluir — `DELETE /api/clientes/{id}` → **204 No Content**

### Conflito de e-mail → **409 Conflict**

## Pedidos

### Criar — `POST /api/pedidos`

```json
{
  "idCliente": "SUBSTITUA_PELO_ID_DO_CLIENTE",
  "situacao": "criado",
  "origem": {
    "rua": "Rua B",
    "numero": "50",
    "complemento": "",
    "bairro": "Centro",
    "cidade": "Feira de Santana",
    "estado": "BA",
    "cep": "44000-000",
    "pais": "Brasil"
  },
  "destino": {
    "rua": "Av. C",
    "numero": "200",
    "complemento": "Apto 1",
    "bairro": "Pituba",
    "cidade": "Salvador",
    "estado": "BA",
    "cep": "41810-000",
    "pais": "Brasil"
  },
  "volumes": [
    {
      "codigo": "VOL-1001",
      "idProduto": "SUBSTITUA_PELO_ID_DO_PRODUTO",
      "nomeProduto": "Notebook",
      "pesoKg": 2.1,
      "dimensoes": {
        "comprimentoCm": 40.0,
        "larguraCm": 30.0,
        "alturaCm": 10.0
      },
      "quantidade": 1
    }
  ],
  "pagamento": {
    "forma": "pix",
    "valor": 59.90,
    "situacao": "pendente"
  }
}
```

Pedido sem `volumes` → **400 Bad Request**.

### Com cliente — `GET /api/pedidos/{id}/com-cliente` → **200**

### CRUD restante

- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `PUT /api/pedidos/{id}`
- `DELETE /api/pedidos/{id}` → **204**

## Entregas

### Criar — `POST /api/entregas`

Informe `idPedido`, `idTransportadora`, `idMotorista`, `idVeiculo`, `idCentroDistribuicao` existentes, além de `destino`, `rota`, `horariosEtapas` e `rastreioAtual` conforme a modelagem.

Referência inexistente → **400 Bad Request**.

### Relacionados — `GET /api/entregas/{id}/relacionados`

Retorna entrega + pedido + motorista.

### Consultas de domínio

- `GET /api/entregas?atrasadas=true`
- `GET /api/entregas?idMotorista={id}`

### CRUD restante

- `GET /api/entregas`
- `GET /api/entregas/{id}`
- `PUT /api/entregas/{id}`
- `DELETE /api/entregas/{id}` → **204**

## Códigos HTTP usados

| Código | Uso |
| --- | --- |
| 200 | Leitura / atualização OK |
| 201 | Criação OK |
| 204 | Exclusão OK |
| 400 | Validação / regra de negócio |
| 404 | Identificador inexistente |
| 409 | Conflito (e-mail duplicado) |
