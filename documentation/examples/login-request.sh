# Obter Token de Acesso (Client Credentials Flow)
# Este fluxo é usado para comunicação máquina-a-máquina (ex: Gateway -> Identity Service)

curl -X POST http://localhost:9090/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -u "orionpay-merchant-client:secret" \
  -d "grant_type=client_credentials" \
  -d "scope=payments.authorize"
