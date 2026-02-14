Write-Host "=== Iniciando Verificação do Ecossistema OrionPay ===" -ForegroundColor Cyan

# 1. Verificar se os Pods estão estáveis
Write-Host "`n[1/4] Verificando status dos Pods..." -ForegroundColor Yellow
kubectl get pods -n orionpay

# 2. Testar a conectividade do Gateway (Health Check)
Write-Host "`n[2/4] Testando Health Check do Gateway..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/q/health/ready" -Method Get
    Write-Host "Gateway respondendo: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "Erro: Gateway inacessível. Verifique se as sondas de saúde no YAML estão na porta 8080." -ForegroundColor Red
}

# 3. Testar o fluxo de Autenticação (Identity Service via Gateway)
Write-Host "`n[3/4] Testando Autenticação (Login)..." -ForegroundColor Yellow
$loginBody = @{ username = "admin"; password = "password123" } | ConvertTo-Json
try {
    $auth = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $token = $auth.accessToken
    Write-Host "Autenticação bem-sucedida! Token gerado." -ForegroundColor Green
} catch {
    Write-Host "Erro na autenticação. Verifique se o service-identity está na porta 9090." -ForegroundColor Red
}

# 4. Testar Autorização de Pagamento (Fluxo Completo)
if ($token) {
    Write-Host "`n[4/4] Testando Autorização de Pagamento..." -ForegroundColor Yellow
    $paymentBody = @{
        externalId = "venda-test-$(Get-Date -Format 'ssmm')"
        amount = 100.00
        currency = "BRL"
        cardNumber = "4444555566667777"
        cardHolder = "FRANCISCO TESTE"
        cvv = "123"
        expirationDate = "12/30"
    } | ConvertTo-Json

    $headers = @{ Authorization = "Bearer $token" }
    try {
        $payment = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payments/authorize" -Method Post -Body $paymentBody -ContentType "application/json" -Headers $headers
        Write-Host "Pagamento Autorizado: ID $($payment.id)" -ForegroundColor Green
    } catch {
        Write-Host "Erro no processamento do pagamento. Verifique os logs do authorization-service." -ForegroundColor Red
    }
}