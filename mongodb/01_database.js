const nomeBanco = "logitrack";
const bancoAnterior = db.getSiblingDB(nomeBanco);

bancoAnterior.dropDatabase();

print("Banco de dados 'logitrack' limpo e recriado com sucesso.");
