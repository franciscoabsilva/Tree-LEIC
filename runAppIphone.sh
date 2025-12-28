#!/bin/bash

# =================================================================
# SCRIPT INTELIGENTE - BUILD & RUN
# =================================================================

# 1. Configuração
# -----------------------------------------------------------------
MAIN_CLASS="App" 
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"
FILES="GUI/App.java"
OUT_DIR="classes"

# Memória (Podes tentar 128m, se o iSH fechar volta a 64m)
MEM_LIMIT="128m" 
OPT_FLAGS="-Xint -Xmx$MEM_LIMIT"

# 2. Verifica se o utilizador pediu para compilar
# -----------------------------------------------------------------
# Se escreveres "./runAppIphone.sh build", ele entra aqui.
if [ "$1" == "build" ]; then
    echo "🔨 A limpar e compilar (Modo Seguro)..."
    rm -rf "$OUT_DIR"
    mkdir -p "$OUT_DIR"

    # Compilação lenta, mas necessária quando mudas código
    javac -J-Xint -J-Xmx$MEM_LIMIT -d "$OUT_DIR" \
          -sourcepath . \
          $CORE_FILES $EXCEPTION_FILES $FILES

    if [ $? -ne 0 ]; then
        echo "❌ ERRO: A compilação falhou."
        exit 1
    fi
    echo "✅ Compilação concluída."
else
    # Se não pedires build, ele salta a compilação.
    echo "⏩ A saltar compilação (use './runAppIphone.sh build' para recompilar)."
fi

# 3. Verifica se as classes existem antes de correr
# -----------------------------------------------------------------
if [ ! -d "$OUT_DIR" ]; then
    echo "⚠️  Não encontrei ficheiros compilados."
    echo "Por favor corre a primeira vez com: ./runAppIphone.sh build"
    exit 1
fi

# 4. Execução
# -----------------------------------------------------------------
echo "🚀 A executar a App..."
echo "------------------------------------------------"

java $OPT_FLAGS -cp "$OUT_DIR" "$MAIN_CLASS"

# -----------------------------------------------------------------
echo ""
echo "Fim."