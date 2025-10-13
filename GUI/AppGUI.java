import javax.swing.*;

import java.awt.*;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import exceptions.*;

public class AppGUI extends JFrame {

    private Manager manager;

    private ArvoreLEIC arvore;

    private JFrame frame;

    private JPanel panelPrincipal;

    private JPanel panelGestaoPessoa; // Painel de gestão de pessoa
    private Pessoa pessoaSelecionada; // Pessoa selecionada para gestão
    
    private String currentMode; // Modo atual de visualização da árvore
    private Stack<Pessoa> historico = new Stack<>(); // historico de arvores visitadas

    public AppGUI() {

        frame = new JFrame();
        
        frame.setTitle("Arvore LEIC"); // titulo da janela
        frame.setSize(800, 600); // tamanho da janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha a janela
        frame.setLocationRelativeTo(null);  // centralizar a janela
        ImageIcon icon = new ImageIcon("icon.png");
        frame.setIconImage(icon.getImage());

        manager = new Manager();
        manager.load();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {manager.save();}));
        arvore = manager.getArvore();

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(11, 1, 10, 10)); //11 linhas, 1 coluna

        initializeButtons(panelPrincipal);

        frame.add(panelPrincipal);
        frame.setVisible(true);
    }

    private void initializeButtons(JPanel panel) {
        // Criação dos botões
        JButton btnAdicionarPessoa = new JButton("1. Adicionar Pessoa");
        JButton btnAdicionarPadrinho = new JButton("2. Adicionar Padrinho");
        JButton btnMostrarPessoa = new JButton("3. Mostrar Pessoa");
        JButton btnRemoverPessoa = new JButton("4. Remover Pessoa");
        JButton btnRemoverPadrinho = new JButton("5. Remover Padrinho");
        JButton btnMostrarListaPessoas = new JButton("6. Mostrar Lista de Todas as Pessoas");
        JButton btnListaPessoasNumeros = new JButton("7. Lista Pessoas Números");
        JButton btnGestaoPessoa = new JButton("8. Menu Gestão de Pessoa");
        JButton btnGestaoAno = new JButton("9. Menu Gestão do Ano");
        JButton btnArvore = new JButton("10. Mostrar Árvore Genealógica Pessoa");
        JButton btnSair = new JButton("0. Sair");

        // Adiciona ação aos botões
        btnAdicionarPessoa.addActionListener(e -> adicionarPessoa());
        btnAdicionarPadrinho.addActionListener(e -> adicionarPadrinho());
        btnMostrarPessoa.addActionListener(e -> mostrarPessoa());
        btnRemoverPessoa.addActionListener(e -> removerPessoa());
        btnRemoverPadrinho.addActionListener(e -> removerPadrinho());
        btnMostrarListaPessoas.addActionListener(e -> mostrarListaDePessoas());
        btnListaPessoasNumeros.addActionListener(e -> mostrarListaPessoasNumeros());
        btnGestaoPessoa.addActionListener(e -> menuGestaoPessoa());
        btnGestaoAno.addActionListener(e -> menuGestaoAno());
        btnArvore.addActionListener(e -> mostrarArvoreGenealogica());
        btnSair.addActionListener(e -> sair());

        // Adiciona os botões ao painel
        panel.add(btnAdicionarPessoa);
        panel.add(btnAdicionarPadrinho);
        panel.add(btnMostrarPessoa);
        panel.add(btnRemoverPessoa);
        panel.add(btnRemoverPadrinho);
        panel.add(btnMostrarListaPessoas);
        panel.add(btnListaPessoasNumeros);
        panel.add(btnGestaoPessoa);
        panel.add(btnGestaoAno);
        panel.add(btnArvore);
        panel.add(btnSair);

    }

    private void adicionarPessoa() {
        // Criando a janela de entrada personalizada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // 5 linhas, 2 colunas
    
        // Criando os campos de entrada
        JTextField fenixIdField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField alcunhaField = new JTextField();
        JTextField matriculasField = new JTextField();
        
        // Labels para os campos
        panel.add(new JLabel("ID do Fenix:"));
        panel.add(fenixIdField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Alcunha:"));
        panel.add(alcunhaField);
        panel.add(new JLabel("Matrícula:"));
        panel.add(matriculasField);
    
        // Criando o botão de OK
        int option = JOptionPane.showConfirmDialog(this, panel, "Adicionar Pessoa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        // Se o usuário clicou em OK
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Pegando os valores dos campos
                String fenixIdStr = fenixIdField.getText();
                String nome = nomeField.getText();
                String alcunha = alcunhaField.getText();
                String matriculasStr = matriculasField.getText();
    
                // Validando o ID do Fenix e Matrícula
                if (fenixIdStr.isEmpty() || matriculasStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O ID do Fenix e Matrícula são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                int fenixId = Integer.parseInt(fenixIdStr);
                int matriculas = Integer.parseInt(matriculasStr);
    
                // Criando a nova pessoa
                Pessoa novaPessoa = new Pessoa(fenixId, nome, alcunha, matriculas);
                arvore.adicionarPessoa(novaPessoa);
    
                JOptionPane.showMessageDialog(this, "Pessoa adicionada com sucesso!");
    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do Fenix e Matrícula devem ser números inteiros.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (DuplicatePersonException e) {
                JOptionPane.showMessageDialog(this, "Já existe uma pessoa com o Fenix ID " + e.getFenixId(), "Erro: Pessoa Duplicada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void adicionarPadrinho() {
        // Painel de entrada para IDs do Afilhado e Padrinho
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField afilhadoIdField = new JTextField();
        JTextField padrinhoIdField = new JTextField();
        panel.add(new JLabel("ID do Afilhado:"));
        panel.add(afilhadoIdField);
        panel.add(new JLabel("ID do Padrinho:"));
        panel.add(padrinhoIdField);
    
        // Criar JOptionPane
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    
        // Configurar JDialog com JOptionPane
        JDialog dialog = optionPane.createDialog("Adicionar Padrinho");
        dialog.setModal(false);  // Torna o diálogo não modal
        dialog.setLocationRelativeTo(frame);  // Centraliza em relação à janela principal
    
        // Propriedade para lidar com ação do botão OK/Cancelar
        optionPane.addPropertyChangeListener(e -> {
            if (JOptionPane.VALUE_PROPERTY.equals(e.getPropertyName())) {
                int value = (int) optionPane.getValue();
    
                if (value == JOptionPane.OK_OPTION) {
                    // Executar lógica para adicionar padrinho
                    try {
                        String afilhadoIdStr = afilhadoIdField.getText();
                        String padrinhoIdStr = padrinhoIdField.getText();
    
                        if (afilhadoIdStr.isEmpty() || padrinhoIdStr.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "O ID do Afilhado e do Padrinho são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
    
                        int afilhadoId = Integer.parseInt(afilhadoIdStr);
                        int padrinhoId = Integer.parseInt(padrinhoIdStr);
    
                        if (afilhadoId == padrinhoId) {
                            JOptionPane.showMessageDialog(dialog, "Uma pessoa não pode ser padrinho de si mesma!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (padrinhoId > afilhadoId) {
                            int confirmation = JOptionPane.showConfirmDialog(dialog, 
                                "O ID do Padrinho é maior que o ID do Afilhado. Tem certeza que deseja continuar?", 
                                "Confirmação", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.WARNING_MESSAGE);
                            if (confirmation != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
    
                        arvore.adicionarPadrinho(afilhadoId, padrinhoId);
                        JOptionPane.showMessageDialog(dialog, arvore.getPessoa(afilhadoId).nomeComAlcunha() + " é afilhado/a de " + arvore.getPessoa(padrinhoId).nomeComAlcunha());
                        dialog.dispose(); // Fecha a janela após adicionar
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "ID do Afilhado e ID do Padrinho devem ser números inteiros.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    } catch (UnknownPersonException ex) {
                        JOptionPane.showMessageDialog(dialog, "Pessoa com ID " + ex.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    dialog.dispose(); // Fecha a janela se Cancelar for clicado
                }
            }
        });
    
        dialog.setVisible(true);  // Exibe o diálogo não modal
    }

    private void mostrarPessoa() {
        // Criando a janela de entrada personalizada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // 3 linhas, 2 colunas
    
        // Criando os campos de entrada
        JTextField fenixIdField = new JTextField();
    
        // Labels para os campos
        panel.add(new JLabel("ID pessoa a mostrar: "));
        panel.add(fenixIdField);
    
        // Criando o botão de OK
        int option = JOptionPane.showConfirmDialog(this, panel, "Mostrar Pessoa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        // Se o usuário clicou em OK
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Pegando os valores dos campos
                String fenixIdStr = fenixIdField.getText();
    
                // Validando o ID do Fenix e Matrícula
                if (fenixIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O ID da pessoa a mostrar é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                int fenixId = Integer.parseInt(fenixIdStr);
    
                // Chama o core para obter as informações da pessoa
                String pessoaInfo = arvore.getPessoaInfo(fenixId, 1);  // usa o novo método do core

                // Exibe as informações da pessoa em uma janela com botão OK
                JOptionPane.showMessageDialog(this, pessoaInfo, "Informações da Pessoa", JOptionPane.INFORMATION_MESSAGE);

    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do Fenix deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (UnknownPersonException e) {
                JOptionPane.showMessageDialog(this, "Pessoa " + e.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerPessoa() {
        // Criando a janela de entrada personalizada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // 3 linhas, 2 colunas
    
        // Criando os campos de entrada
        JTextField fenixIdField = new JTextField();
    
        // Labels para os campos
        panel.add(new JLabel("ID pessoa a remover: "));
        panel.add(fenixIdField);
    
        // Criando o botão de OK
        int option = JOptionPane.showConfirmDialog(this, panel, "Remover Pessoa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        // Se o usuário clicou em OK
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Pegando os valores dos campos
                String fenixIdStr = fenixIdField.getText();
    
                // Validando o ID do Fenix e Matrícula
                if (fenixIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O ID da pessoa a remover é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                int fenixId = Integer.parseInt(fenixIdStr);

                // Perguntando ao usuário se tem certeza que quer remover a pessoa
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Tem a certeza que deseja remover " + arvore.getPessoaExistente(fenixId).getFenixId() + " " + arvore.getPessoaExistente(fenixId).nomeComAlcunha() + "?",
                        "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                // Se o usuário confirmar a remoção
                if (confirmation == JOptionPane.YES_OPTION) {
                    arvore.removerPessoa(fenixId); // Remover a pessoa da árvore
                    JOptionPane.showMessageDialog(this, "Pessoa removida com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Remoção cancelada.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do Fenix deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (UnknownPersonException e) {
                JOptionPane.showMessageDialog(this, "Pessoa " + e.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerPadrinho() {
        // Criando a janela de entrada personalizada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2)); // 3 linhas, 2 colunas
    
        // Criando os campos de entrada
        JTextField afilhadoIdField = new JTextField();
        JTextField padrinhoIdField = new JTextField();
    
        // Labels para os campos
        panel.add(new JLabel("ID do Afilhado:"));
        panel.add(afilhadoIdField);
        panel.add(new JLabel("ID do Padrinho:"));
        panel.add(padrinhoIdField);
    
        // Criando o botão de OK
        int option = JOptionPane.showConfirmDialog(this, panel, "Remover Padrinho", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        // Se o usuário clicou em OK
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Pegando os valores dos campos
                String afilhadoIdStr = afilhadoIdField.getText();
                String padrinhoIdStr = padrinhoIdField.getText();
    
                // Validando o ID do Fenix e Matrícula
                if (afilhadoIdStr.isEmpty() || padrinhoIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O ID do Afilhado e ID do Padrinho são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                int afilhadoId = Integer.parseInt(afilhadoIdStr);
                int padrinhoId = Integer.parseInt(padrinhoIdStr);
    
                arvore.removerPadrinho(afilhadoId, padrinhoId);
    
                JOptionPane.showMessageDialog(this, arvore.getPessoa(afilhadoId).nomeComAlcunha() + " já não é afilhado de " + arvore.getPessoa(padrinhoId).nomeComAlcunha());
    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do Afilhado e ID do Padrinho devem ser números inteiros.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (UnknownPersonException e) {
                JOptionPane.showMessageDialog(this, "Não existe uma pessoa com o Fenix ID " + e.getFenixId(), "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
            } // TODO, FAZER ERRO DE O PADRINHO NAO SER PADRINHO DO AFILHADO, E APANHAR LÁ EM BAIXO TB
        }
    }

    private void mostrarListaDePessoas() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        
        try {
            for (int pessoaId : arvore.getPessoas().keySet()) { // método para obter todas as pessoas
                textArea.append(arvore.getPessoaInfo(pessoaId, 1) + "\n\n");
            }
        } catch (UnknownPersonException e) {
            JOptionPane.showMessageDialog(this, "Pessoa " + e.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
        }
    
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
    
        // Exibir as informações em uma nova janela
        JFrame frame = new JFrame("Lista de Todas as Pessoas");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Adiciona o painel com o scroll
        frame.add(scrollPane);
    
        // Botão de voltar ao menu principal
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        frame.add(btnVoltar, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }

    private void mostrarListaPessoasNumeros() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
    
        // Obtém todas as pessoas na árvore e exibe o ID e nome com alcunha
        for (int pessoaId : arvore.getPessoas().keySet()) { // método para obter todas as pessoas
            Pessoa pessoa = arvore.getPessoa(pessoaId);
            
            // Formato: número (ID do Fenix): nome com alcunha
            String pessoaInfo = pessoa.getFenixId() + " " + pessoa.nomeComAlcunha();
            textArea.append(pessoaInfo + "\n");
        }
    
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
    
        // Exibir as informações em uma nova janela
        JFrame frame = new JFrame("Lista Pessoas Números");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Adiciona o painel com o scroll
        frame.add(scrollPane);
    
        // Botão de voltar ao menu principal
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        frame.add(btnVoltar, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }

    private void menuGestaoPessoa() {
        String idInput = JOptionPane.showInputDialog(this, "Digite o ID da pessoa para gerir:", "Seleção de Pessoa", JOptionPane.QUESTION_MESSAGE);

        try {
            int pessoaId = Integer.parseInt(idInput);
            pessoaSelecionada = arvore.getPessoa(pessoaId);

            if (pessoaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Pessoa com o ID " + pessoaId + " não encontrada.");
                return;
            }
        
            // Cria o painel de gestão de pessoa
            panelGestaoPessoa = new JPanel();
            panelGestaoPessoa.setLayout(new BorderLayout()); // Usa BorderLayout para facilitar a adição do título

            // Criação do título com FENIX ID e nome com alcunha
            String titulo = "A gerir " + pessoaSelecionada.getFenixId() + " " + pessoaSelecionada.nomeComAlcunha();
            JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
            panelGestaoPessoa.add(lblTitulo, BorderLayout.NORTH);

            // Painel de botões para as opções de gestão
            JPanel panelBotoes = new JPanel(new GridLayout(7, 1, 10, 10)); // Organiza os botões verticalmente

            // Criação de botões para opções de gestão
            JButton btnAlterarIdFenix = new JButton("1. Alterar ID do Fenix");
            btnAlterarIdFenix.addActionListener(e -> alterarIdFenix());

            JButton btnAlterarNome = new JButton("2. Alterar Nome");
            btnAlterarNome.addActionListener(e -> alterarNome());

            JButton btnAlterarAlcunha = new JButton("3. Alterar Alcunha");
            btnAlterarAlcunha.addActionListener(e -> alterarAlcunha());

            JButton btnAlterarMatricula = new JButton("4. Alterar Matrícula");
            btnAlterarMatricula.addActionListener(e -> alterarMatricula());

            JButton btnAdicionarPadrinhoPessoal = new JButton("5. Adicionar Padrinho");
            btnAdicionarPadrinhoPessoal.addActionListener(e -> adicionarPadrinhoPessoal());

            JButton btnAdicionarAfilhadoPessoal = new JButton("6. Adicionar Afilhado");
            btnAdicionarAfilhadoPessoal.addActionListener(e -> adicionarAfilhadoPessoal());

            JButton btnVoltar = new JButton("Voltar ao Menu Principal");
            btnVoltar.addActionListener(e -> voltarAoMenuPrincipal());

            // Adiciona os botões ao painel de gestão de pessoa
            panelBotoes.add(btnAlterarIdFenix);
            panelBotoes.add(btnAlterarNome);
            panelBotoes.add(btnAlterarAlcunha);
            panelBotoes.add(btnAlterarMatricula);
            panelBotoes.add(btnAdicionarPadrinhoPessoal);
            panelBotoes.add(btnAdicionarAfilhadoPessoal);
            panelBotoes.add(btnVoltar);

            panelGestaoPessoa.add(panelBotoes, BorderLayout.CENTER);

            // Substitui o painel principal pelo painel de gestão de pessoa
            frame.setContentPane(panelGestaoPessoa);
            frame.revalidate();  // Atualiza a janela com o novo painel
            frame.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido.");
        }
    }

    private void alterarIdFenix() {
        try {
            int pessoaId = pessoaSelecionada.getFenixId();
            String novoFenixId = JOptionPane.showInputDialog(this, "Digite o novo ID do Fenix");
            arvore.atualizarPessoaId(pessoaId, Integer.parseInt(novoFenixId));
            JOptionPane.showMessageDialog(this, "ID do Fenix alterado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido.");
        } catch (DuplicatePersonException e) {
            JOptionPane.showMessageDialog(this, "Já existe uma pessoa com o Fenix ID " + e.getFenixId(), "Erro: Pessoa Duplicada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarNome() {
        String novoNome = JOptionPane.showInputDialog(this, "Digite o novo nome");
        pessoaSelecionada.setNome(novoNome);
        JOptionPane.showMessageDialog(this, "Nome alterado com sucesso!");
    }

    private void alterarAlcunha() {
        String novaAlcunha = JOptionPane.showInputDialog(this, "Digite a nova alcunha");
        pessoaSelecionada.setAlcunha(novaAlcunha);
        JOptionPane.showMessageDialog(this, "Alcunha alterada com sucesso!");
    }

    private void alterarMatricula() {
        try {
            String novaMatricula = JOptionPane.showInputDialog(this, "Digite a nova matrícula");
            pessoaSelecionada.setMatriculas(Integer.parseInt(novaMatricula));
            JOptionPane.showMessageDialog(this, "Matrícula alterada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número inteiro para a matrícula.");
        }
    }

    private void adicionarPadrinhoPessoal() {
        try {
            String padrinhoIdStr = JOptionPane.showInputDialog(this, "Digite o ID do padrinho");
            int padrinhoId = Integer.parseInt(padrinhoIdStr);
            arvore.adicionarPadrinho(pessoaSelecionada.getFenixId(), padrinhoId);
            JOptionPane.showMessageDialog(this, "Padrinho adicionado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido.");
        } catch (UnknownPersonException e) {
            JOptionPane.showMessageDialog(this, "Pessoa " + e.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
        }       
    }

    private void adicionarAfilhadoPessoal() {
        try {
            String afilhadoIdStr = JOptionPane.showInputDialog(this, "Digite o ID do afilhado");
            int afilhadoId = Integer.parseInt(afilhadoIdStr);
            arvore.adicionarPadrinho(afilhadoId, pessoaSelecionada.getFenixId());
            JOptionPane.showMessageDialog(this, "Afilhado adicionado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido.");
        } catch (UnknownPersonException e) {
            JOptionPane.showMessageDialog(this, "Pessoa " + e.getFenixId() + " não encontrada", "Erro: Pessoa Desconhecida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void menuGestaoAno() {
        // Painel de gestão do ano
        JPanel panelGestaoAno = new JPanel();
        panelGestaoAno.setLayout(new BorderLayout());
    
        // Rótulo para exibir o ano atual
        JLabel lblAnoAtual = new JLabel("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1), JLabel.CENTER);
        lblAnoAtual.setFont(new Font("Arial", Font.BOLD, 20));
        panelGestaoAno.add(lblAnoAtual, BorderLayout.NORTH);
    
        // Painel de botões
        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
    
        // Botão para avançar o ano
        JButton btnAvancarAno = new JButton("Avançar Ano");
        btnAvancarAno.addActionListener(e -> {
            arvore.avancarAno();
            lblAnoAtual.setText("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
            JOptionPane.showMessageDialog(this, "Ano avançado com sucesso!");
        });
        panelBotoes.add(btnAvancarAno);
    
        // Botão para recuar o ano
        JButton btnRecuarAno = new JButton("Recuar Ano");
        btnRecuarAno.addActionListener(e -> {
            arvore.recuarAno();
            lblAnoAtual.setText("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno() + 1));
            JOptionPane.showMessageDialog(this, "Ano recuado com sucesso!");
        });
        panelBotoes.add(btnRecuarAno);
    
        // Botão para voltar ao menu principal
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.addActionListener(e -> voltarAoMenuPrincipal());
        panelBotoes.add(btnVoltar);
    
        // Adiciona o painel de botões ao painel de gestão do ano
        panelGestaoAno.add(panelBotoes, BorderLayout.CENTER);
    
        // Configura o painel de gestão do ano como conteúdo da janela
        frame.setContentPane(panelGestaoAno);
        frame.revalidate();
        frame.repaint();
    }

    private void mostrarArvoreGenealogica() {
        // Criando a janela de entrada personalizada
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // 3 linhas, 2 colunas
    
        // Criando os campos de entrada
        JTextField fenixIdField = new JTextField();
    
        // Labels para os campos
        panel.add(new JLabel("ID pessoa a mostrar: "));
        panel.add(fenixIdField);
    
        // Criando o botão de OK
        int option = JOptionPane.showConfirmDialog(this, panel, "Mostrar Árvore Genealógica", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        // Se o usuário clicou em OK
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Pegando os valores dos campos
                String fenixIdStr = fenixIdField.getText();

                int fenixId = Integer.parseInt(fenixIdStr);

                Pessoa pessoa = arvore.getPessoa(fenixId);
                
                if (pessoa == null) {
                    JOptionPane.showMessageDialog(this, "Pessoa com ID " + fenixId + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                historico.clear();
                currentMode = null;

                // Mostra a árvore para a pessoa selecionada
                arvoreGenealogicaGUI(pessoa);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do Fenix deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void arvoreGenealogicaGUI(Pessoa pessoaRaiz) {
        
        //Histórico para o botão de voltar
        if (!historico.isEmpty()) {
            Pessoa pessoaAnterior = historico.peek();
            if (pessoaAnterior != pessoaRaiz) {
                historico.push(pessoaRaiz);
            }
        } else {
            historico.push(pessoaRaiz);
        }


        // Menu inicial para escolher modo de visualização
        if (currentMode == null) {
            JDialog modeDialog = new JDialog(frame, "Modo de Visualização", true);
            modeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            modeDialog.setSize(400, 200);
            modeDialog.setLocationRelativeTo(frame);
            modeDialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 1));

            // Botão "Padrinho"
            JButton btnPadrinho = new JButton("Padrinhos");
            btnPadrinho.addActionListener(e -> {
                currentMode = "Padrinhos";
                modeDialog.dispose();
                arvoreGenealogicaGUI(pessoaRaiz);
            });

            // Botão "Afilhado"
            JButton btnAfilhado = new JButton("Afilhados");
            btnAfilhado.addActionListener(e -> {
                currentMode = "Afilhados";
                modeDialog.dispose();
                arvoreGenealogicaGUI(pessoaRaiz);
            });

            panel.add(btnPadrinho);
            panel.add(btnAfilhado);

            modeDialog.add(panel, BorderLayout.CENTER);
            modeDialog.setVisible(true);

            return;
        }

        frame.setTitle("Árvore Genealógica de " + currentMode + " de " + pessoaRaiz.nomeComAlcunha()); 

        // Adiciona o painel principal que desenha a árvore genealógica
        JScrollPane scrollPane = new JScrollPane(desenhaArvore(pessoaRaiz, new HashSet<>(), currentMode));
        JPanel contentPanel = butoesArvore(scrollPane);
        frame.setContentPane(contentPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel desenhaArvore(Pessoa pessoa, Set<Pessoa> visitados, String mode) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Painel para afilhados
        if (mode.equals("Afilhados")) {
            if (!visitados.contains(pessoa)) {
                Map<Integer, Pessoa> afilhados = pessoa.getAfilhados();
                if (!afilhados.isEmpty()) {
                    JPanel panelAfilhados = new JPanel();
                    panelAfilhados.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

                    for (Pessoa afilhado : afilhados.values()) {
                        panelAfilhados.add(desenhaArvore(afilhado, visitados, "Afilhados"));
                    }

                    panel.add(panelAfilhados, BorderLayout.CENTER);
                }
            }
        }

        if (mode.equals("Padrinhos")) {
            // Painel para padrinhos
            if (!visitados.contains(pessoa)) {
                Map<Integer, Pessoa> padrinhos = pessoa.getPadrinhos();
                if (!padrinhos.isEmpty()) {
                    JPanel panelPadrinhos = new JPanel();
                    panelPadrinhos.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

                    for (Pessoa padrinho : padrinhos.values()) {
                        panelPadrinhos.add(desenhaArvore(padrinho, visitados, "Padrinhos"));
                    }

                    panel.add(panelPadrinhos, BorderLayout.CENTER);
                }
            }
        }

        // Cria o painel do topo para a pessoa principal
        if (visitados.contains(pessoa)) {
            JButton btnPessoa = new JButton(pessoa.nomeComAlcunha() + " (repeated)");
            btnPessoa.setFont(new Font("Arial", Font.BOLD, 14));
            btnPessoa.setEnabled(true);
            btnPessoa.addActionListener(e -> arvoreGenealogicaGUI(pessoa));
            panel.add(btnPessoa, BorderLayout.NORTH);
        } else {
            JButton btnPessoa = new JButton(pessoa.nomeComAlcunha());
            btnPessoa.setFont(new Font("Arial", Font.BOLD, 14));
            btnPessoa.setEnabled(true);
            btnPessoa.addActionListener(e -> arvoreGenealogicaGUI(pessoa));
            panel.add(btnPessoa, BorderLayout.NORTH);
            visitados.add(pessoa);
        }
        return panel;
    }
    
    private JPanel butoesArvore(JScrollPane scrollPane) { 
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new BoxLayout(panelBotoes, BoxLayout.X_AXIS)); // Alinha os botões na vertical
        
        // Painel com botões Menu Principal e Voltar
        JPanel panelMenuVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Botão "Menu Principal"
        JButton btnMenuPrincipal = new JButton("Menu Principal");
        btnMenuPrincipal.setPreferredSize(new Dimension(180, 50));
        btnMenuPrincipal.setFont(new Font("Arial", Font.BOLD, 16)); 
        btnMenuPrincipal.addActionListener(e -> voltarAoMenuPrincipal());
        panelMenuVoltar.add(btnMenuPrincipal);
    
        // Botão "Voltar"
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(180, 50)); 
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 16)); 
        btnVoltar.addActionListener(e -> voltarArvore());
        panelMenuVoltar.add(btnVoltar);
    
        panelBotoes.add(panelMenuVoltar);  // Adiciona os dois botões no painel principal

        // Painel com botões Padrinhos e Afilhados
        JPanel panelPadrinhosAfilhados = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Botão "Padrinhos"
        JButton btnPadrinhos = new JButton("Padrinhos");
        btnPadrinhos.setPreferredSize(new Dimension(180, 50));
        btnPadrinhos.setFont(new Font("Arial", Font.BOLD, 16));
        btnPadrinhos.addActionListener(e -> {
            currentMode = "Padrinhos";
            arvoreGenealogicaGUI(historico.peek());
        });
        panelPadrinhosAfilhados.add(btnPadrinhos);
    
        // Botão "Afilhados"
        JButton btnAfilhados = new JButton("Afilhados");
        btnAfilhados.setPreferredSize(new Dimension(180, 50));
        btnAfilhados.setFont(new Font("Arial", Font.BOLD, 16));
        btnAfilhados.addActionListener(e -> {
            currentMode = "Afilhados";
            arvoreGenealogicaGUI(historico.peek());
        });
        panelPadrinhosAfilhados.add(btnAfilhados);
    
        panelBotoes.add(panelPadrinhosAfilhados);
        
        mainPanel.add(panelBotoes, BorderLayout.SOUTH);
    
        return mainPanel;
    }

    private void voltarArvore() {
        if (historico.size() > 1) {
            historico.pop();
            Pessoa pessoaAnterior = historico.peek();
            arvoreGenealogicaGUI(pessoaAnterior);
        }
    }

    private void sair() {
        System.exit(0);
    }

    private void voltarAoMenuPrincipal() {
        // Voltar para o menu principal
        frame.setTitle("Arvore LEIC");
        frame.setContentPane(panelPrincipal);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new AppGUI();
    }

}