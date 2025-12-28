#!/bin/bash

# =================================================================
# SCRIPT DE EXECUÇÃO OTIMIZADO PARA ISH (Modo Interpretado)
# =================================================================

# 1. Configuração
# -----------------------------------------------------------------
MAIN_CLASS="App" 
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"
FILES="GUI/App.java"
OUT_DIR="classes"

# --- CONFIGURAÇÃO DE ESTABILIDADE ---
# MEM_LIMIT: Limita a RAM para não matar o processo
MEM_LIMIT="64m"

# OPT_FLAGS: -Xint força o modo interpretado (desliga o JIT).
# Isto evita o erro SIGSEGV no iSH.
OPT_FLAGS="-Xint -Xmx$MEM_LIMIT"

# 2. Limpeza
# -----------------------------------------------------------------
echo "A limpar compilações anteriores..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# 3. Compilação
# -----------------------------------------------------------------
echo "A compilar (Modo Seguro)..."

# Passamos -J-Xint para o compilador não crashar também
javac -J-Xint -J-Xmx$MEM_LIMIT -d "$OUT_DIR" \
      -sourcepath . \
      $CORE_FILES $EXCEPTION_FILES $FILES

if [ $? -ne 0 ]; then
    echo "❌ ERRO: A compilação falhou."
    exit 1
fi

# 4. Execução
# -----------------------------------------------------------------
echo "A executar a App..."
echo "------------------------------------------------"

# Usamos as flags de otimização (Xint) e memória
java $OPT_FLAGS -cp "$OUT_DIR" "$MAIN_CLASS"

# -----------------------------------------------------------------
echo ""
echo "Fim da execução."