# Arquitetura — LogiTrack

## Visão geral

A LogiTrack usa MongoDB (`logitrack`) e uma API Spring Boot. A modelagem das atividades anteriores continua valendo: as collections e as decisões de embed/referência em `modeling/` e `mongodb/` são a base do código Java.

Fluxo previsto da aplicação:

```text
Controller → Service → Repository → MongoDB
```

## Pacotes Java

Pacote base: `br.com.logi_track.aplicacao_api`

| Pacote | Responsabilidade |
| --- | --- |
| `models` | Documentos `@Document` e classes embutidas |
| `repositories` | Acesso às collections (Spring Data MongoDB) |
| `services` | Regras de negócio e validação de referências |
| `controllers` | Endpoints REST |
| `exception` | Erros da API e códigos HTTP |

As pastas já estão criadas no projeto. A classe principal é `AplicacaoApiApplication`.

## Banco

- Host: `localhost`
- Porta: `27017`
- Database: `logitrack`
- Configuração: `src/main/resources/application.properties`

Dez collections. Volume, Pagamento, Rota e Endereço ficam dentro de outros documentos (sem collection própria), como em `modeling/relationships.md`.
