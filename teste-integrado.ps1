Write-Host "=== INICIANDO SMOKE TEST ORIONPAY (SPRING BOOT) ===" -ForegroundColor Cyan

# Configurações
$baseUrl = "http://localhost:8080"
$loginUrl = "$baseUrl/api/v1/auth/login"
$paymentUrl = "$baseUrl/api/v1/payments/authorize"

# 1. Teste de Health Check do Gateway
Write-Host "`n[1/3] Verificando se o Gateway Spring está online..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$baseUrl/actuator/health/readiness" -Method Get
    Write-Host "Gateway status: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "Gateway ainda não responde em $baseUrl/actuator/health" -ForegroundColor Red
}

# 2. Obtenção do Token JWT
Write-Host "`n[2/3] Autenticando usuário 'admin'..." -ForegroundColor Yellow
$loginBody = @{ username = "admin"; password = "password123" } | ConvertTo-Json
try {
    $auth = Invoke-RestMethod -Uri $loginUrl -Method Post -Body $loginBody -ContentType "application/json"
    $token = $auth.accessToken
    Write-Host "Token JWT obtido com sucesso!" -ForegroundColor Green
} catch {
    Write-Host "Falha na autenticação. Verifique a conexão Gateway <-> Identity." -ForegroundColor Red
    return
}

# 3. Fluxo de Autorização de Venda
Write-Host "`n[3/3] Enviando transação de teste..." -ForegroundColor Yellow
$paymentBody = @{
    externalId = "venda-spring-$(Get-Date -Format 'HHmmss')"
    amount = 250.75
    currency = "BRL"
    cardNumber = "4444555566667777"
    cardHolder = "FRANCISCO TESTE"
    cvv = "123"
    expirationDate = "12/30"
} | ConvertTo-Json

$headers = @{ Authorization = "Bearer $token" }
try {
    $response = Invoke-RestMethod -Uri $paymentUrl -Method Post -Body $paymentBody -ContentType "application/json" -Headers $headers
    Write-Host "--------------------------------------" -ForegroundColor Gray
    Write-Host "VENDA PROCESSADA COM SUCESSO!" -ForegroundColor Green
    Write-Host "Transação ID: $($response.id)"
    Write-Host "Status: $($response.status)"
    Write-Host "--------------------------------------" -ForegroundColor Gray
} catch {
    Write-Host "Erro ao processar venda no Authorization Service." -ForegroundColor Red
}