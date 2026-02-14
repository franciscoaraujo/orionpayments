# Obter Token de Acesso (Client Credentials Flow) - PowerShell
# Este fluxo é usado para comunicação máquina-a-máquina (ex: Gateway -> Identity Service)

$headers = @{
    "Content-Type" = "application/x-www-form-urlencoded"
    "Authorization" = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("orionpay-merchant-client:secret"))
}

$body = @{
    grant_type = "client_credentials"
    scope = "payments.authorize"
}

Invoke-RestMethod -Uri "http://localhost:9090/oauth2/token" -Method Post -Headers $headers -Body $body
