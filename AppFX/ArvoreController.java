import exceptions.DuplicatePersonException;
import exceptions.UnknownPersonException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea; 
import javafx.scene.layout.BorderPane; 
import javafx.scene.control.ScrollPane; 
import javafx.stage.Modality;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.ArrayList;

public class ArvoreController implements Initializable {

    private Manager manager;
    private ArvoreLEIC arvore;

    private Stage currentStage;
    private Pessoa pessoaSelecionada; 
    private String currentMode; 
    private Stack<Pessoa> historico = new Stack<>(); 

    private Scene menuPrincipalScene; 

    @FXML private VBox panelPrincipal; 

    @FXML private Button btnAdicionarPessoa;
    @FXML private Button btnAdicionarPadrinho;
    @FXML private Button btnMostrarPessoa;
    @FXML private Button btnRemoverPessoa;
    @FXML private Button btnRemoverPadrinho;
    @FXML private Button btnMostrarListaPessoas;
    @FXML private Button btnListaPessoasNumeros;
    @FXML private Button btnGestaoPessoa;
    @FXML private Button btnGestaoAno;
    @FXML private Button btnArvore;
    @FXML private Button btnSair;


    public void setCurrentStage(Stage stage) {
        this.currentStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manager = new Manager();
        manager.load();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {manager.save();}));
        
        arvore = manager.getArvore();
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMenuPrincipalScene(Scene scene) {
        this.menuPrincipalScene = scene;
    }

    private Optional<ButtonType> showAlertConfirm(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        // Mudar o botão OK para YES e o Cancelar para NO (opcional, mas claro)
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        
        // Usamos 'OK_DONE' como o retorno se for confirmado
        return alert.showAndWait();
    }
    
    // 1. Adicionar Pessoa
    @FXML
    private void adicionarPessoa() {
        
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Adicionar Pessoa");
        dialog.setHeaderText("Introduza os dados da nova pessoa:");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField fenixIdField = new TextField();
        TextField nomeField = new TextField();
        TextField alcunhaField = new TextField();
        TextField matriculasField = new TextField();
        
        grid.addRow(0, new javafx.scene.control.Label("ID do Fenix:"), fenixIdField);
        grid.addRow(1, new javafx.scene.control.Label("Nome:"), nomeField);
        grid.addRow(2, new javafx.scene.control.Label("Alcunha:"), alcunhaField);
        grid.addRow(3, new javafx.scene.control.Label("Matrícula:"), matriculasField);
        
        dialog.getDialogPane().setContent(grid);
        
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String fenixIdStr = fenixIdField.getText();
                String matriculasStr = matriculasField.getText();

                if (fenixIdStr.isEmpty() || matriculasStr.isEmpty()) {
                    showAlert(AlertType.ERROR, "Erro", "O ID do Fenix e Matrícula são obrigatórios!");
                    return;
                }

                int fenixId = Integer.parseInt(fenixIdStr);
                int matriculas = Integer.parseInt(matriculasStr);
                String nome = nomeField.getText();
                String alcunha = alcunhaField.getText();

                Pessoa novaPessoa = new Pessoa(fenixId, nome, alcunha, matriculas);
                arvore.adicionarPessoa(novaPessoa);

                showAlert(AlertType.INFORMATION, "Sucesso", "Pessoa adicionada com sucesso!");

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "ID do Fenix e Matrícula devem ser números inteiros.");
            } catch (exceptions.DuplicatePersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Duplicada", "Já existe uma pessoa com o Fenix ID " + e.getFenixId());
            }
        }
    }

    // 2. Adicionar Padrinho
    @FXML
    private void adicionarPadrinho() {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Adicionar Padrinho");
        dialog.setHeaderText("Introduza os IDs do afilhado e do padrinho:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // Nota: O Insets não está importado. Remova se não tiver a importação.
        // grid.setPadding(new Insets(20, 150, 10, 10)); 

        TextField afilhadoIdField = new TextField();
        TextField padrinhoIdField = new TextField();

        // Use a classe Label completa (javafx.scene.control.Label)
        grid.addRow(0, new javafx.scene.control.Label("ID do Afilhado:"), afilhadoIdField);
        grid.addRow(1, new javafx.scene.control.Label("ID do Padrinho:"), padrinhoIdField);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String afilhadoIdStr = afilhadoIdField.getText();
                String padrinhoIdStr = padrinhoIdField.getText();
                
                if (afilhadoIdStr.isEmpty() || padrinhoIdStr.isEmpty()) {
                    showAlert(AlertType.ERROR, "Erro", "Ambos os IDs do Fenix são obrigatórios!");
                    return;
                }

                int afilhadoId = Integer.parseInt(afilhadoIdStr);
                int padrinhoId = Integer.parseInt(padrinhoIdStr);

                // 1. Não pode ser padrinho de si mesmo
                if (afilhadoId == padrinhoId) {
                    showAlert(AlertType.ERROR, "Erro", "Uma pessoa não pode ser padrinho de si mesma!");
                    return;
                }

                // 2. Confirmação se Padrinho ID > Afilhado ID
                if (padrinhoId > afilhadoId) {
                    Optional<ButtonType> confirmResult = showAlertConfirm(
                        "Confirmação", 
                        "O ID do Padrinho é maior que o ID do Afilhado. Tem certeza que deseja continuar?"
                    );
                    
                    if (confirmResult.isPresent() && confirmResult.get() != ButtonType.OK) {
                        return; // Retorna se o utilizador não confirmar
                    }
                }

                arvore.adicionarPadrinho(padrinhoId, afilhadoId);

                // Confirmação de Sucesso com Nomes (melhor para o utilizador)
                showAlert(AlertType.INFORMATION, "Sucesso", 
                        arvore.getPessoa(afilhadoId).nomeComAlcunha() + " é afilhado/a de " + 
                        arvore.getPessoa(padrinhoId).nomeComAlcunha());
                
                //  POSSO SALVAR NO FINAL DE CADA FUNCAO MAS EU PREFIRO QUANDO A APP É FECHADA
                //manager.save();

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Os IDs do Fenix devem ser números inteiros.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Não foi encontrada a pessoa com o ID Fenix " + e.getFenixId() + ".");
            }
        }
    }

    // 3. Mostrar Pessoa
    @FXML
    private void mostrarPessoa() {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Mostrar Pessoa");
        dialog.setHeaderText("Introduza o ID do Fenix da pessoa a mostrar:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // grid.setPadding(new Insets(20, 150, 10, 10)); // Opcional

        TextField fenixIdField = new TextField();

        grid.addRow(0, new javafx.scene.control.Label("ID do Fenix:"), fenixIdField);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String fenixIdStr = fenixIdField.getText();
                
                if (fenixIdStr.isEmpty()) {
                    showAlert(AlertType.ERROR, "Erro", "O ID do Fenix é obrigatório!");
                    return;
                }

                int fenixId = Integer.parseInt(fenixIdStr);

                String info = arvore.getPessoaInfo(fenixId, 2);
                showAlert(AlertType.INFORMATION, "Informações da Pessoa", info);

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "O ID do Fenix deve ser um número inteiro.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Não foi encontrada a pessoa com o ID Fenix " + e.getFenixId() + ".");
            }
        }
    }

    // 4. Remover Pessoa
    @FXML
    private void removerPessoa() {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Remover Pessoa");
        dialog.setHeaderText("Introduza o ID do Fenix da pessoa a remover:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fenixIdField = new TextField();
        grid.addRow(0, new Label("ID pessoa a remover:"), fenixIdField);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String fenixIdStr = fenixIdField.getText();

                if (fenixIdStr.isEmpty()) {
                    showAlert(AlertType.ERROR, "Erro", "O ID da pessoa a remover é obrigatório!");
                    return;
                }

                int fenixId = Integer.parseInt(fenixIdStr);

                // 1. Obter a Pessoa antes de pedir confirmação
                Pessoa pessoaARemover = arvore.getPessoaExistente(fenixId);

                // 2. Pedido de Confirmação (Usa a função showAlertConfirm)
                Optional<ButtonType> confirmation = showAlertConfirm(
                    "Confirmar Remoção",
                    "Tem a certeza que deseja remover " + pessoaARemover.getFenixId() + " " + pessoaARemover.nomeComAlcunha() + "?"
                );

                // Verifica se o utilizador clicou em ButtonType.YES (que definimos no showAlertConfirm)
                if (confirmation.isPresent() && confirmation.get() == ButtonType.YES) {
                    arvore.removerPessoa(fenixId);
                    showAlert(AlertType.INFORMATION, "Sucesso", "Pessoa removida com sucesso!");
                } else {
                    showAlert(AlertType.INFORMATION, "Remoção Cancelada", "Remoção cancelada pelo utilizador.");
                }

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "ID do Fenix deve ser um número inteiro.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Pessoa " + e.getFenixId() + " não encontrada");
            }
        }
    }

    // 5. Remover Padrinho
    @FXML
    private void removerPadrinho() {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Remover Padrinho");
        dialog.setHeaderText("Introduza os IDs do afilhado e do padrinho a remover:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField afilhadoIdField = new TextField();
        TextField padrinhoIdField = new TextField();

        grid.addRow(0, new Label("ID do Afilhado:"), afilhadoIdField);
        grid.addRow(1, new Label("ID do Padrinho:"), padrinhoIdField);
        
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String afilhadoIdStr = afilhadoIdField.getText();
                String padrinhoIdStr = padrinhoIdField.getText();

                if (afilhadoIdStr.isEmpty() || padrinhoIdStr.isEmpty()) {
                    showAlert(AlertType.ERROR, "Erro", "O ID do Afilhado e ID do Padrinho são obrigatórios!");
                    return;
                }

                int afilhadoId = Integer.parseInt(afilhadoIdStr);
                int padrinhoId = Integer.parseInt(padrinhoIdStr);
                
                // Chamada do método de remoção
                arvore.removerPadrinho(afilhadoId, padrinhoId);
    
                // Confirmação de Sucesso
                showAlert(AlertType.INFORMATION, "Sucesso", 
                    arvore.getPessoa(afilhadoId).nomeComAlcunha() + " já não é afilhado de " + arvore.getPessoa(padrinhoId).nomeComAlcunha()
                );
    
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "ID do Afilhado e ID do Padrinho devem ser números inteiros.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Não existe uma pessoa com o Fenix ID " + e.getFenixId() + ".");
            }
            // TODO: FAZER ERRO DE O PADRINHO NAO SER PADRINHO DO AFILHADO, E APANHAR LÁ EM BAIXO TB
        }
    }

    // 6. Mostrar Lista de Pessoas
    @FXML
    private void mostrarListaDePessoas() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-family: monospace; -fx-font-size: 14pt;"); // Estilo para melhor leitura

        // Construir o texto da lista
        StringBuilder sb = new StringBuilder();
        try {
            // Assumimos que arvore.getPessoas() devolve um Map<Integer, Pessoa> ou similar
            for (int pessoaId : arvore.getPessoas().keySet()) {
                // arvore.getPessoaInfo(pessoaId) devolve o formato completo da informação
                sb.append(arvore.getPessoaInfo(pessoaId, 2)).append("\n\n");
            }
        } catch (UnknownPersonException e) {
            // Este catch é teoricamente desnecessário se getPessoas for robusto, 
            // mas mantido por segurança.
            showAlert(AlertType.ERROR, "Erro", "Pessoa " + e.getFenixId() + " não encontrada ao listar.");
            return;
        }

        if (sb.length() == 0) {
            sb.append("A Árvore de Pessoas está vazia.");
        }
        textArea.setText(sb.toString());

        // Criar o ScrollPane para envolver o TextArea
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); 
        scrollPane.setFitToHeight(true);

        // Botão para fechar a janela
        Button btnVoltar = new Button("Voltar ao Menu Principal");
        btnVoltar.setOnAction(e -> {
            ((Stage)btnVoltar.getScene().getWindow()).close();
        });
        
        // Layout principal
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        VBox bottomContainer = new VBox(btnVoltar);
        bottomContainer.setPadding(new Insets(10));
        bottomContainer.setSpacing(10);
        root.setBottom(bottomContainer);
        BorderPane.setMargin(bottomContainer, new Insets(0, 0, 10, 0));


        // Configurar a nova janela (Stage)
        Stage stage = new Stage();
        stage.setTitle("Lista de Todas as Pessoas (Informação Completa)");
        stage.setScene(new Scene(root, 700, 500));
        stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        stage.show();
    }

    // 7. Mostrar Lista de Pessoas (ID + Nome/Alcunha)
    @FXML
    private void mostrarListaPessoasNumeros() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-family: monospace; -fx-font-size: 14pt;"); // Estilo para melhor leitura

        // Construir o texto da lista
        StringBuilder sb = new StringBuilder();

        // Obtém todas as pessoas na árvore e exibe o ID e nome com alcunha
        for (int pessoaId : arvore.getPessoas().keySet()) { 
            Pessoa pessoa = arvore.getPessoa(pessoaId);
            
            // Formato: ID Fenix: Nome (Alcunha)
            String pessoaInfo = String.format("%-2d: %s", pessoa.getFenixId(), pessoa.nomeComAlcunha());
            sb.append(pessoaInfo).append("\n");
        }
    
        if (sb.length() == 0) {
            sb.append("A Árvore de Pessoas está vazia.");
        }
        textArea.setText(sb.toString());

        // Criar o ScrollPane para envolver o TextArea
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); 
        scrollPane.setFitToHeight(true);
    
        // Botão para fechar a janela
        Button btnVoltar = new Button("Voltar ao Menu Principal");
        btnVoltar.setOnAction(e -> {
            ((Stage)btnVoltar.getScene().getWindow()).close();
        });
    
        // Layout principal
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        VBox bottomContainer = new VBox(btnVoltar);
        bottomContainer.setPadding(new Insets(10));
        bottomContainer.setSpacing(10);
        root.setBottom(bottomContainer);
        BorderPane.setMargin(bottomContainer, new Insets(0, 0, 10, 0));

        // Exibir as informações em uma nova janela
        Stage stage = new Stage();
        stage.setTitle("Lista de Pessoas (IDs e Nomes)");
        stage.setScene(new Scene(root, 700, 500));
        stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        stage.show();
    }

     // =========================================================================
    // 8. GESTÃO DE PESSOA E SUB-MÉTODOS (Implementação JavaFX com Troca de Scene)
    // =========================================================================

    @FXML
    private void menuGestaoPessoa() {
        // 1. INPUT DIALOG (Substitui JOptionPane.showInputDialog)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gestão de Pessoa");
        dialog.setHeaderText("Seleção de Pessoa");
        dialog.setContentText("Digite o ID da pessoa para gerir:");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int pessoaId = Integer.parseInt(result.get());
                pessoaSelecionada = arvore.getPessoaExistente(pessoaId);
                
                // Pessoa encontrada, cria e mostra o menu de gestão
                mostrarMenuGestao();

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um ID válido (número inteiro).");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro", "Pessoa com o ID " + e.getFenixId() + " não encontrada.");
            }
        }
    }
    
    // Método auxiliar para criar e exibir o menu de gestão
    private void mostrarMenuGestao() {
        // Criação do título
        String titulo = "A gerir " + pessoaSelecionada.getFenixId() + " " + pessoaSelecionada.nomeComAlcunha();
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
        
        // Painel de botões para as opções de gestão
        VBox painelBotoes = new VBox(10); // Espaçamento de 10
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.setPadding(new Insets(20));

        // Criação de botões para opções de gestão
        Button btnAlterarIdFenix = new Button("1. Alterar ID do Fenix");
        btnAlterarIdFenix.setOnAction(e -> alterarIdFenix());
        
        Button btnAlterarNome = new Button("2. Alterar Nome");
        btnAlterarNome.setOnAction(e -> alterarNome());
        
        Button btnAlterarAlcunha = new Button("3. Alterar Alcunha");
        btnAlterarAlcunha.setOnAction(e -> alterarAlcunha());
        
        Button btnAlterarMatricula = new Button("4. Alterar Matrícula");
        btnAlterarMatricula.setOnAction(e -> alterarMatricula());

        Button btnAdicionarPadrinhoPessoal = new Button("5. Adicionar Padrinho");
        btnAdicionarPadrinhoPessoal.setOnAction(e -> adicionarPadrinhoPessoal());

        Button btnAdicionarAfilhadoPessoal = new Button("6. Adicionar Afilhado");
        btnAdicionarAfilhadoPessoal.setOnAction(e -> adicionarAfilhadoPessoal());

        Button btnVoltar = new Button("Voltar ao Menu Principal");
        btnVoltar.setOnAction(e -> voltarAoMenuPrincipal());
        
        // Adicionar botões
        painelBotoes.getChildren().addAll(
            btnAlterarIdFenix, btnAlterarNome, btnAlterarAlcunha, 
            btnAlterarMatricula, btnAdicionarPadrinhoPessoal, 
            btnAdicionarAfilhadoPessoal, new Label(""), btnVoltar
        );

        // Estilizar e centrar todos os botões no VBox
        painelBotoes.getChildren().stream().filter(node -> node instanceof Button).forEach(node -> {
            ((Button)node).setMaxWidth(Double.MAX_VALUE); // Faz os botões terem a mesma largura
        });

        // VBox contentor para o novo menu (Título + Botões)
        VBox painelGestao = new VBox(20); // Espaçamento entre título e botões
        painelGestao.setPadding(new Insets(30));
        painelGestao.setAlignment(Pos.CENTER);
        painelGestao.getChildren().addAll(lblTitulo, painelBotoes);
        
        // 2. TROCAR A SCENE NA JANELA PRINCIPAL
        currentStage.setScene(new Scene(painelGestao, currentStage.getWidth(), currentStage.getHeight()));
        currentStage.setTitle("Gestão de Pessoa: " + pessoaSelecionada.nomeComAlcunha());
    }

    private void alterarIdFenix() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(pessoaSelecionada.getFenixId()));
        dialog.setTitle("Alterar ID do Fenix");
        dialog.setHeaderText("Pessoa: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite o novo ID do Fenix:");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int novoFenixId = Integer.parseInt(result.get());
                arvore.atualizarPessoaId(pessoaSelecionada.getFenixId(), novoFenixId);
                
                // O método 'atualizarPessoaId' deve atualizar a referência interna no Manager e na Árvore.
                // Atualizamos a referência da pessoaSelecionada (só o ID Fenix mudou).
                pessoaSelecionada = arvore.getPessoa(novoFenixId); 
                
                showAlert(AlertType.INFORMATION, "Sucesso", "ID do Fenix alterado com sucesso para " + novoFenixId + "!");
                mostrarMenuGestao(); // Recarrega o menu com o novo título

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um ID válido.");
            } catch (DuplicatePersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Duplicada", "Já existe uma pessoa com o Fenix ID " + e.getFenixId());
            }
        }
    }

    private void alterarNome() {
        TextInputDialog dialog = new TextInputDialog(pessoaSelecionada.getNome());
        dialog.setTitle("Alterar Nome");
        dialog.setHeaderText("Pessoa: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite o novo nome:");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            String novoNome = result.get();
            pessoaSelecionada.setNome(novoNome);
            showAlert(AlertType.INFORMATION, "Sucesso", "Nome alterado com sucesso para " + novoNome + "!");
            mostrarMenuGestao(); // Recarrega o menu com o novo título
        }
    }

    private void alterarAlcunha() {
        TextInputDialog dialog = new TextInputDialog(pessoaSelecionada.getAlcunha());
        dialog.setTitle("Alterar Alcunha");
        dialog.setHeaderText("Pessoa: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite a nova alcunha:");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) { // Alcunha pode ser vazia
            String novaAlcunha = result.get();
            pessoaSelecionada.setAlcunha(novaAlcunha);
            showAlert(AlertType.INFORMATION, "Sucesso", "Alcunha alterada com sucesso para " + (novaAlcunha.isEmpty() ? "(vazio)" : novaAlcunha) + "!");
            mostrarMenuGestao(); // Recarrega o menu com o novo título
        }
    }

    private void alterarMatricula() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(pessoaSelecionada.getMatriculas()));
        dialog.setTitle("Alterar Matrícula");
        dialog.setHeaderText("Pessoa: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite a nova matrícula (ano):");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int novaMatricula = Integer.parseInt(result.get());
                pessoaSelecionada.setMatriculas(novaMatricula);
                showAlert(AlertType.INFORMATION, "Sucesso", "Matrícula alterada com sucesso para " + novaMatricula + "!");
                mostrarMenuGestao(); // Recarrega o menu com o novo título
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um número inteiro para a matrícula (ano).");
            }
        }
    }

    private void adicionarPadrinhoPessoal() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Padrinho");
        dialog.setHeaderText("Afilhado: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite o ID do padrinho a adicionar:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int padrinhoId = Integer.parseInt(result.get());
                arvore.adicionarPadrinho(pessoaSelecionada.getFenixId(), padrinhoId);
                showAlert(AlertType.INFORMATION, "Sucesso", "Padrinho adicionado com sucesso!");
                mostrarMenuGestao(); // Recarrega o menu

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um ID válido.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Pessoa " + e.getFenixId() + " não encontrada");
            }
        }
    }

    private void adicionarAfilhadoPessoal() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Afilhado");
        dialog.setHeaderText("Padrinho: " + pessoaSelecionada.nomeComAlcunha());
        dialog.setContentText("Digite o ID do afilhado a adicionar:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int afilhadoId = Integer.parseInt(result.get());
                arvore.adicionarPadrinho(afilhadoId, pessoaSelecionada.getFenixId()); // O padrinho é a pessoaSelecionada
                showAlert(AlertType.INFORMATION, "Sucesso", "Afilhado adicionado com sucesso!");
                mostrarMenuGestao(); // Recarrega o menu

            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um ID válido.");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro: Pessoa Desconhecida", "Pessoa " + e.getFenixId() + " não encontrada");
            }
        }
    }

    // ============================================================================
    // 9. GESTÃO DE ANO (Implementação JavaFX com Troca de Scene)
    // ============================================================================
    @FXML
    private void menuGestaoAno() {
        // Rótulo para exibir o ano atual (será atualizado pelas ações)
        Label lblAnoAtual = new Label("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
        lblAnoAtual.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold;");
        
        // VBox para os botões e layout
        VBox painelBotoes = new VBox(15); // Espaçamento entre botões
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.setPadding(new Insets(30));
        
        // Botão Avançar Ano
        Button btnAvancarAno = new Button("Avançar Ano");
        btnAvancarAno.setMaxWidth(Double.MAX_VALUE);
        btnAvancarAno.setOnAction(e -> {
            arvore.avancarAno();
            // Atualiza o Label após a ação
            lblAnoAtual.setText("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
            // TODO POSSO BUÉ TIRAR AS CONFIRMATION BOXES PQ SAO CHATAS
            showAlert(AlertType.INFORMATION, "Sucesso", "Ano avançado com sucesso!");
        });
        
        // Botão Recuar Ano
        Button btnRecuarAno = new Button("Recuar Ano");
        btnRecuarAno.setMaxWidth(Double.MAX_VALUE);
        btnRecuarAno.setOnAction(e -> {
            // Nota: Se arvore.recuarAno() lançar exceção ao atingir o limite, deve ser apanhada aqui.
            // Assumimos que a validação está na classe ArvoreLEIC.
            arvore.recuarAno();
            // Atualiza o Label após a ação
            lblAnoAtual.setText("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
            // TODO POSSO BUÉ TIRAR AS CONFIRMATION BOXES PQ SAO CHATAS
            showAlert(AlertType.INFORMATION, "Sucesso", "Ano recuado com sucesso!");
        });
        
        // Botão Voltar ao Menu Principal
        Button btnVoltar = new Button("Voltar ao Menu Principal");
        btnVoltar.setMaxWidth(Double.MAX_VALUE);
        btnVoltar.setOnAction(e -> voltarAoMenuPrincipal());
        
        // Adiciona todos os elementos ao VBox
        painelBotoes.getChildren().addAll(lblAnoAtual, new Label(""), btnAvancarAno, btnRecuarAno, new Label(""), btnVoltar);
        
        // Configura o VBox principal
        VBox painelGestaoAno = new VBox(20);
        painelGestaoAno.setPadding(new Insets(50));
        painelGestaoAno.setAlignment(Pos.CENTER);
        painelGestaoAno.getChildren().addAll(painelBotoes);
        
        // TROCAR A SCENE NA JANELA PRINCIPAL
        currentStage.setScene(new Scene(painelGestaoAno, currentStage.getWidth(), currentStage.getHeight()));
        currentStage.setTitle("Gestão de Ano - Ano Atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
    }

    // =========================================================================
    // 10. MOSTRAR ÁRVORE GENEALÓGICA (Implementação JavaFX)
    // =========================================================================

    @FXML 
    private void mostrarArvoreGenealogica() {
        // 1. INPUT DIALOG para obter o ID da Pessoa Raiz
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mostrar Árvore Genealógica");
        dialog.setHeaderText("Seleção de Pessoa");
        dialog.setContentText("Digite o ID da pessoa para iniciar a árvore:");
        
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            try {
                int fenixId = Integer.parseInt(result.get());
                Pessoa pessoaRaiz = arvore.getPessoaExistente(fenixId);
                
                // Limpa o histórico e define a pessoa raiz
                historico.clear();
                historico.push(pessoaRaiz);
                
                // Mostrar o diálogo de seleção de modo (Padrinhos/Afilhados)
                mostrarModoSelecao(pessoaRaiz);
                
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Erro de Entrada", "Por favor, insira um ID válido (número inteiro).");
            } catch (UnknownPersonException e) {
                showAlert(AlertType.ERROR, "Erro", "Pessoa com o ID " + e.getFenixId() + " não encontrada.");
            }
        }
    }
    
    // Mostra o diálogo para o utilizador escolher o modo (Padrinhos ou Afilhados)
    private void mostrarModoSelecao(Pessoa pessoaRaiz) {
        Stage modeStage = new Stage();
        modeStage.initModality(Modality.APPLICATION_MODAL);
        modeStage.setTitle("Modo de Visualização");
        
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        
        Label instruction = new Label("Escolha o modo de visualização para " + pessoaRaiz.nomeComAlcunha() + ":");
        instruction.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");
        
        Button btnPadrinho = new Button("Padrinhos");
        btnPadrinho.setMaxWidth(Double.MAX_VALUE);
        btnPadrinho.setOnAction(e -> {
            modeStage.close();
            currentMode = "Padrinhos";
            mostrarArvoreView(pessoaRaiz, currentMode);
        });
        
        Button btnAfilhado = new Button("Afilhados");
        btnAfilhado.setMaxWidth(Double.MAX_VALUE);
        btnAfilhado.setOnAction(e -> {
            modeStage.close();
            currentMode = "Afilhados";
            mostrarArvoreView(pessoaRaiz, currentMode);
        });
        
        root.getChildren().addAll(instruction, btnPadrinho, btnAfilhado);
        
        Scene modeScene = new Scene(root, 570, 300);
        modeStage.setScene(modeScene);
        modeStage.setMinWidth(600);
        modeStage.setMinHeight(300);
        modeStage.showAndWait();
    }
    
    // Método principal para desenhar e exibir a árvore
    private void mostrarArvoreView(Pessoa pessoaRaiz, String mode) {
        // O BorderPane principal para a vista da árvore
        BorderPane arvoreRoot = new BorderPane();
        arvoreRoot.setPadding(new Insets(10));
        
        // 1. Desenha a Árvore (Centro)
        // A visualização é construída recursivamente usando VBox/HBox para estrutura hierárquica
        VBox arvoreViz = buildTreeVisualization(pessoaRaiz, mode, new HashSet<Pessoa>());
        arvoreViz.setAlignment(Pos.TOP_CENTER);
        
        ScrollPane scrollPane = new ScrollPane(arvoreViz);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        arvoreRoot.setCenter(scrollPane);
        
        // 2. Título (Topo)
        Label lblTitulo = new Label("Árvore Genealógica - " + mode + " de " + pessoaRaiz.nomeComAlcunha());
        lblTitulo.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        BorderPane.setAlignment(lblTitulo, Pos.CENTER);
        arvoreRoot.setTop(lblTitulo);
        
        // 3. Painel de Botões (Fundo)
        HBox panelBotoes = createArvoreButtons(pessoaRaiz);
        arvoreRoot.setBottom(panelBotoes);

        // Troca a Scene principal
        currentStage.setScene(new Scene(arvoreRoot, currentStage.getWidth(), currentStage.getHeight()));
        currentStage.setTitle("Árvore Genealógica: " + pessoaRaiz.nomeComAlcunha());
    }
    
    // Método para criar o Node da Pessoa (Botão)
    private VBox createPersonNode(Pessoa p) {
        Button btnPessoa = new Button(p.nomeComAlcunha() + " (" + p.getFenixId() + ")");
        // TODO MUDAR A COR NAO GOSTO DO AZUL
        btnPessoa.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;");
        btnPessoa.setOnAction(e -> {
            // Se clicar no botão, a pessoa torna-se a nova raiz (se for diferente da atual).
            if (!historico.peek().equals(p)) {
                 historico.push(p);
            }
            // Recarrega a vista com a nova raiz (mantendo o modo atual)
            mostrarArvoreView(p, currentMode);
        });

        btnPessoa.setMinWidth(Region.USE_PREF_SIZE); 

        VBox node = new VBox(btnPessoa);
        node.setAlignment(Pos.CENTER);
        return node;
    }
    
    // Método recursivo para construir a visualização da árvore
    private VBox buildTreeVisualization(Pessoa pessoa, String mode, Set<Pessoa> visited) {
        VBox rootBox = new VBox(5); // Espaçamento vertical entre nós
        rootBox.setAlignment(Pos.TOP_CENTER);

        // 1. Cria o botão para a pessoa atual
        VBox personNode = createPersonNode(pessoa);
        rootBox.getChildren().add(personNode);

        // Evita ciclos (ex: A → B → A)
        if (visited.contains(pessoa)) {
            personNode.getChildren().add(new Label("... Ciclo Detectado ..."));
            return rootBox;
        }

        // Marca a pessoa como visitada
        visited.add(pessoa);

        // Determina próximos nós (Padrinhos ou Afilhados)
        Collection<Pessoa> proximos;
        if ("Padrinhos".equals(mode)) {
            proximos = pessoa.getPadrinhos().values();
        } else if ("Afilhados".equals(mode)) {
            proximos = pessoa.getAfilhados().values();
        } else {
            proximos = Collections.emptyList();
        }

        // 2. Se existirem próximos, cria as subárvores
        if (!proximos.isEmpty()) {
            HBox childrenBox = new HBox(30); // Espaçamento horizontal entre sub-árvores
            childrenBox.setAlignment(Pos.CENTER);

            // Pane usado para desenhar as linhas de ligação
            Pane connectionPane = new Pane();
            connectionPane.setPrefHeight(50);
            connectionPane.setMinHeight(50);

            // Cria e adiciona subárvores recursivamente
            for (Pessoa proximo : proximos) {
                VBox subTree = buildTreeVisualization(proximo, mode, new HashSet<>(visited));
                childrenBox.getChildren().add(subTree);
            }

            // Adiciona o painel das linhas e os filhos
            rootBox.getChildren().addAll(connectionPane, childrenBox);

            // Desenha as linhas depois que o layout estiver pronto
            javafx.application.Platform.runLater(() -> {
                // Coordenadas do nó do pai
                double parentX = personNode.localToScene(personNode.getBoundsInLocal()).getMinX()
                                + personNode.getBoundsInLocal().getWidth() / 2;
                double parentY = personNode.localToScene(personNode.getBoundsInLocal()).getMaxY();

                // Calcula coordenadas dos filhos
                List<Point2D> filhosCoords = new ArrayList<>();
                for (javafx.scene.Node subTree : childrenBox.getChildren()) {
                    javafx.geometry.Bounds childBounds = subTree.localToScene(subTree.getBoundsInLocal());
                    double childX = childBounds.getMinX() + childBounds.getWidth() / 2;
                    double childY = childBounds.getMinY();
                    filhosCoords.add(new Point2D(childX, childY));
                }

                if (filhosCoords.isEmpty()) return;

                // Converte coordenadas para o sistema local do Pane
                Point2D startInPane = connectionPane.sceneToLocal(parentX, parentY);
                double midY = startInPane.getY() + 20; // altura da linha horizontal

                // Linha vertical do pai até a barra horizontal
                javafx.scene.shape.Line verticalDown = new javafx.scene.shape.Line(
                    startInPane.getX(), startInPane.getY(),
                    startInPane.getX(), midY
                );
                verticalDown.setStrokeWidth(2);
                verticalDown.setStroke(javafx.scene.paint.Color.GRAY);
                connectionPane.getChildren().add(verticalDown);

                // Calcula o início e o fim da barra horizontal
                double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
                for (Point2D p : filhosCoords) {
                    Point2D local = connectionPane.sceneToLocal(p);
                    minX = Math.min(minX, local.getX());
                    maxX = Math.max(maxX, local.getX());
                }

                // Linha horizontal que liga todos os filhos
                javafx.scene.shape.Line horizontal = new javafx.scene.shape.Line(minX, midY, maxX, midY);
                horizontal.setStrokeWidth(2);
                horizontal.setStroke(javafx.scene.paint.Color.GRAY);
                connectionPane.getChildren().add(horizontal);

                // Linhas verticais de cada filho até a barra horizontal
                for (Point2D child : filhosCoords) {
                    Point2D endInPane = connectionPane.sceneToLocal(child);
                    javafx.scene.shape.Line down = new javafx.scene.shape.Line(
                        endInPane.getX(), midY,
                        endInPane.getX(), endInPane.getY()
                    );
                    down.setStrokeWidth(2);
                    down.setStroke(javafx.scene.paint.Color.GRAY);
                    connectionPane.getChildren().add(down);
                }
            });
        }

        return rootBox;
    }

    // Método para criar os botões de navegação
    private HBox createArvoreButtons(Pessoa pessoaRaiz) {
        HBox panelBotoes = new HBox(20);
        panelBotoes.setAlignment(Pos.CENTER);
        panelBotoes.setPadding(new Insets(10));
        panelBotoes.setStyle("-fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");
        
        // Botão "Menu Principal"
        Button btnMenuPrincipal = new Button("Voltar ao Menu Principal");
        btnMenuPrincipal.setOnAction(e -> voltarAoMenuPrincipal());
        
        // Botão "Voltar" (Histórico)
        Button btnVoltar = new Button("Voltar (Pessoa Anterior)");
        btnVoltar.setDisable(historico.size() <= 1); // Desativa se só houver 1 pessoa no histórico
        btnVoltar.setOnAction(e -> voltarArvore());
        
        // Botão "Trocar Modo"
        Button btnTrocarModo = new Button("Trocar para " + ("Padrinhos".equals(currentMode) ? "Afilhados" : "Padrinhos"));
        btnTrocarModo.setOnAction(e -> {
            currentMode = "Padrinhos".equals(currentMode) ? "Afilhados" : "Padrinhos";
            mostrarArvoreView(pessoaRaiz, currentMode);
        });

        panelBotoes.getChildren().addAll(btnMenuPrincipal, btnVoltar, btnTrocarModo);
        
        return panelBotoes;
    }
    
    // Lógica para voltar no histórico
    private void voltarArvore() {
        if (historico.size() > 1) {
            historico.pop(); // Remove a pessoa atual
            Pessoa pessoaAnterior = historico.peek(); // Pega a pessoa anterior
            mostrarArvoreView(pessoaAnterior, currentMode); // Recarrega a vista
        }
    }

    // 0. Sair
    @FXML
    private void sair() {
        System.exit(0);
    }
    
    // O método de voltar ao menu principal será necessário mais tarde
    public void voltarAoMenuPrincipal() {
        if (menuPrincipalScene != null) {
            currentStage.setScene(menuPrincipalScene);
            currentStage.setTitle("Menu Principal");
            // Limpa a pessoa selecionada ao sair do modo de gestão
            pessoaSelecionada = null; 
        } else {
             showAlert(AlertType.ERROR, "Erro", "Não foi possível retornar. A Scene principal não foi carregada corretamente.");
        }
    }
}