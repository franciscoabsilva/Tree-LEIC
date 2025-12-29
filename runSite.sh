#!/bin/bash

# --- CONFIGURAÇÃO ---
PORT=8080
# COLA AQUI O DOMÍNIO QUE O NGROK TE DEU NO SITE:
DOMINIO_FIXO="bernice-sericultural-shawnna.ngrok-free.dev"

# Obtém o diretório onde este script está
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cleanup() {
    echo ""
    echo "🔴 A encerrar..."
    pkill -P $$ 
    exit
}
trap cleanup SIGINT EXIT

# ==========================================
# 1. INICIAR O NGROK COM DOMÍNIO FIXO
# ==========================================
echo "🌐 A iniciar Ngrok em: https://$DOMINIO_FIXO"

# O comando muda ligeiramente para usar o domínio
ngrok http --domain=$DOMINIO_FIXO $PORT > /dev/null &

sleep 3

echo "-----------------------------------------------------"
echo "🔗 O TEU SITE ESTÁ ONLINE (E FIXO) EM:"
echo "👉 https://$DOMINIO_FIXO"
echo "-----------------------------------------------------"

# ==========================================
# 2. RODAR O SITE (GRADLE)
# ==========================================
echo "☕ A iniciar o servidor Gradle..."
cd "$BASE_DIR/website/treeleicsite"

chmod +x gradlew
./gradlew bootRun