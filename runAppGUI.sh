#!/bin/bash

# =================================================================
# SCRIPT DE COMPILAÇÃO E EXECUÇÃO DO PROJETO SWING (Tree-LEIC)
# Este script compila e executa o AppGUI (a versão Swing).
# =================================================================

# 1. Variável de Configuração
# -----------------------------------------------------------------

# Ficheiros Core da sua lógica
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"

# Ficheiros de Exceção
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"
# Adicione outras classes de exceção se existirem.


# PARA RODAR O APP OU O APPGUI2 BASTA MUDAR AQUI
GUI_FILES="GUI/AppGUI.java" 
MAIN_CLASS_GUI="AppGUI" 


# Pasta de saída dos ficheiros compilados (.class)
OUT_DIR="classes"

# 2. Limpa Compilações Anteriores
# -----------------------------------------------------------------
echo "A limpar ficheiros .class antigos na pasta classes/..."
# Remove apenas as classes para não afetar os recursos do JavaFX se existirem
rm -rf "$OUT_DIR/core" "$OUT_DIR/exceptions" "$OUT_DIR/GUI" 
mkdir -p "$OUT_DIR"

# 3. Compilação
# -----------------------------------------------------------------
echo "A compilar os ficheiros Java (Core, Exceptions, GUI)..."

# Compila TODAS as classes para o diretório de classes (-d classes), 
# garantindo que o compilador resolve todos os pacotes.
javac -d "$OUT_DIR" \
      -sourcepath . $CORE_FILES $EXCEPTION_FILES $GUI_FILES

# Verifica se a compilação foi bem sucedida
if [ $? -ne 0 ]; then
    echo "ERRO: A compilação SWING falhou. Verifique as mensagens de erro acima."
    exit 1
fi

# 4. Execução
# -----------------------------------------------------------------
echo "A executar a aplicação Swing ($MAIN_CLASS_GUI)..."

# Usa o Classpath (-cp) apontando para a pasta 'classes' para encontrar todos os pacotes (.class)
java -cp "$OUT_DIR" "$MAIN_CLASS_GUI"

# -----------------------------------------------------------------
echo "Fim da execução."