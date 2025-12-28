#!/bin/bash

# =================================================================
# SCRIPT DE EXECUÇÃO - VERSÃO IPHONE (Sem JavaFX)
# =================================================================

# 1. Configuração
# -----------------------------------------------------------------
# Nome da classe principal (aquela que tem o public static void main)
# IMPORTANTE: Confirma se o nome é exatamente este.
MAIN_CLASS="App" 

# Ficheiros Core (Lógica)
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"

# Ficheiros de Exceção
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"

# Ficheiros da App Iphone
# IMPORTANTE: Ajusta o caminho se o teu ficheiro estiver noutra pasta (ex: AppIphone/AppIphone.java ou apenas AppIphone.java)
FILES="GUI/App.java"

# Pasta de saída dos ficheiros compilados (.class)
OUT_DIR="classes"

# 2. Limpeza
# -----------------------------------------------------------------
echo "A limpar compilações anteriores..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# 3. Compilação
# -----------------------------------------------------------------
echo "A compilar..."

# O truque está aqui: passamos TODOS os ficheiros de uma vez.
# O javac vai ler todos e, como não têm 'package', vai criar os .class
# todos na raiz da pasta classes_iphone.
javac -d "$OUT_DIR" \
      -sourcepath . \
      $CORE_FILES $EXCEPTION_FILES $FILES

# Verifica erros
if [ $? -ne 0 ]; then
    echo "❌ ERRO: A compilação falhou."
    exit 1
fi

# 4. Execução
# -----------------------------------------------------------------
echo "A executar a App Iphone..."
echo "------------------------------------------------"

# Executa apenas com o classpath apontado para a pasta de saída
java -cp "$OUT_DIR" "$MAIN_CLASS"

# -----------------------------------------------------------------
echo ""
echo "Fim da execução."