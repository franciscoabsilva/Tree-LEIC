#!/bin/bash

# =================================================================
# SCRIPT DE EXECUÇÃO OTIMIZADO PARA ISH (Memória Limitada)
# =================================================================

# 1. Configuração
# -----------------------------------------------------------------
MAIN_CLASS="App" 
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"
FILES="GUI/App.java"
OUT_DIR="classes"

# --- CONFIGURAÇÃO DE MEMÓRIA ---
# Define o máximo de RAM que o Java pode usar. 
# Se o iSH fechar a app sozinho, tenta baixar para "64m" ou "32m".
MEM_LIMIT="128m"

# 2. Limpeza
# -----------------------------------------------------------------
echo "A limpar compilações anteriores..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# 3. Compilação
# -----------------------------------------------------------------
echo "A compilar..."

# Adicionámos a flag -J-Xmx... para limitar a memória do COMPILADOR
javac -J-Xmx$MEM_LIMIT -d "$OUT_DIR" \
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

# Adicionámos a flag -Xmx... para limitar a memória da EXECUÇÃO
java -Xmx$MEM_LIMIT -cp "$OUT_DIR" "$MAIN_CLASS"

# -----------------------------------------------------------------
echo ""
echo "Fim da execução."