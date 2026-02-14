# OrionPay - Script de Inicialização Completa
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   INICIANDO RESET E START DO ORIONPAY MVP     " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

# 1. Limpeza Radical
#Write-Host "[1/4] Removendo ambiente antigo para evitar travas de volume..." -ForegroundColor Yellow
#minikube delete

# 2. Inicialização do Cluster
Write-Host "[2/4] Iniciando Minikube com 8GB RAM e 4 CPUs..." -ForegroundColor Yellow
minikube start --memory=8192 --cpus=4 --driver=docker

# 3. Preparação da Infraestrutura
Write-Host "[3/4] Criando Namespace e preparando o terreno..." -ForegroundColor Yellow
kubectl apply -f k8s-manifests/01-infra-namespace.yaml

# 4. Orquestração com Skaffold
Write-Host "[4/4] Iniciando build Java e Deploy dos Microserviços..." -ForegroundColor Yellow
Write-Host "Aguarde o Spring Boot subir. Use Ctrl+C para encerrar." -ForegroundColor Green
skaffold dev --port-forward

# Altere a última linha do seu script para esta:
skaffold dev --port-forward --status-check=false --cleanup=false