#!/bin/bash

# =================================================================
# SCRIPT DE COMPILAÇÃO E EXECUÇÃO DO PROJETO JAVAFX (Tree-LEIC)
# =================================================================

# 1. Variável de Configuração
# -----------------------------------------------------------------
# Certifique-se que o nome do SDK e o caminho estão corretos.
PATH_TO_FX="./javafx-sdk-17.0.16/lib"
MAIN_CLASS="AppFX"

# Ficheiros Core da sua lógica
CORE_FILES="core/ArvoreLEIC.java core/Pessoa.java core/Manager.java"

# Ficheiros de Exceção
EXCEPTION_FILES="exceptions/DuplicatePersonException.java exceptions/UnknownPersonException.java"


# Ficheiros da nova GUI JavaFX (no subdiretório AppFX/)
FX_FILES="AppFX/AppFX.java AppFX/ArvoreController.java"

# Pasta de saída dos ficheiros compilados (.class)
OUT_DIR="classes"

# 2. Verifica se o SDK do JavaFX existe
# -----------------------------------------------------------------
if [ ! -d "$PATH_TO_FX" ]; then
    echo "ERRO: O SDK do JavaFX não foi encontrado em: $PATH_TO_FX"
    echo "Por favor, descompacte o SDK (ex: javafx-sdk-25) na pasta Tree-LEIC."
    exit 1
fi

# 3. Limpa Compilações Anteriores
# -----------------------------------------------------------------
echo "A limpar compilações anteriores..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# 4. Copia de recursos FXML e Ícones
# -----------------------------------------------------------------
echo "A copiar recursos FXML e Ícones..."
# Cria a pasta do pacote dentro do diretório de classes
# Copia os ficheiros necessários
cp AppFX/ArvoreView.fxml "$OUT_DIR"
cp icon.png "$OUT_DIR"
# -----------------------------------------------------------------

# 5. Compilação
# -----------------------------------------------------------------
echo "A compilar os ficheiros Java com o JavaFX..."
javac -d "$OUT_DIR" \
      --module-path "$PATH_TO_FX" \
      --add-modules javafx.controls,javafx.fxml,javafx.graphics \
      -sourcepath . $CORE_FILES $FX_FILES $EXCEPTION_FILES

# Verifica se a compilação foi bem sucedida
if [ $? -ne 0 ]; then
    echo "ERRO: A compilação falhou. Verifique as mensagens de erro acima."
    exit 1
fi

# 6. Execução
# -----------------------------------------------------------------
echo "A executar a aplicação JavaFX..."
java -cp "$OUT_DIR" \
     --module-path "$PATH_TO_FX" \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics \
     "$MAIN_CLASS"

# -----------------------------------------------------------------
echo "Fim da execução."

