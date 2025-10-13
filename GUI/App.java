import java.util.Scanner;

import java.util.InputMismatchException;

import exceptions.*;

public class App {
    public static void main(String[] args) throws InputMismatchException {
        Manager manager = new Manager();
        manager.load();

        ArvoreLEIC arvore = manager.getArvore();
        
        Scanner scanner = new Scanner(System.in);

        // caso fechem sem ser pelo menu TODO FAZER O SAVE DAR PRINTS NO APP E NAO NO CORE
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {manager.save();}));

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Adicionar Pessoa");
            System.out.println("2. Adicionar Padrinho");
            System.out.println("3. Mostrar Pessoa");
            System.out.println("4. Remover Pessoa");
            System.out.println("5. Remover Padrinho");
            System.out.println("6. Mostrar Lista de Todas as Pessoas");
            System.out.println("7. Lista Pessoas Números");
            System.out.println("8. Menu Gestão de Pessoa");
            System.out.println("9. Menu Gestão do Ano");
            System.out.println("10. Mostrar Arvore Genealógica Acima Pessoa");
            System.out.println("11. Mostrar Arvore Genealógica Abaixo Pessoa");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            try {
                int opcao = scanner.nextInt();
                switch (opcao) {
                    case 1:
                        try {
                            System.out.print("Digite o ID do Fenix: ");
                            int fenixId = scanner.nextInt();
                            scanner.nextLine();  // Consumir nova linha após nextInt()
        
                            System.out.print("Digite o Nome: ");
                            String nome = scanner.nextLine();
        
                            System.out.print("Digite a Alcunha: ");
                            String alcunha = scanner.nextLine();
        
                            System.out.print("Digite a Matrícula: ");
                            int matriculas = scanner.nextInt();
        
                            Pessoa novaPessoa = new Pessoa(fenixId, nome, alcunha, matriculas);
                            arvore.adicionarPessoa(novaPessoa);
                            System.out.println("\nPessoa adicionada com sucesso!");
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nInput inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (DuplicatePersonException e) {
                            System.out.println("\nJá existe uma pessoa com o Fenix ID " + e.getFenixId());
                            break;
                        }

                    case 2:
                        try {
                            System.out.print("Digite o ID do afilhado: ");
                            int afilhadoId = scanner.nextInt();
        
                            System.out.print("Digite o ID do padrinho: ");
                            int padrinhoId = scanner.nextInt();
                            scanner.nextLine();
        
                            arvore.adicionarPadrinho(afilhadoId, padrinhoId);
                            System.out.println("\nPadrinho adicionado com sucesso! " + arvore.getPessoa(afilhadoId).getNome() + " é afilhado/a de " + arvore.getPessoa(padrinhoId).getNome());
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }

                    case 3: // TODO: FAZER O MOSTRAR PESSOA NA APP E NAO NO CORE, O CORE SO DEVOLVE AS INFORMAÇÕES
                        System.out.print("Digite o ID da pessoa que deseja mostrar: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            System.out.println();
                            arvore.printPessoa(pessoaId);
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("Pessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }

                    case 4:
                        System.out.print("Digite o ID do Fenix da pessoa a remover: ");
                        try {
                            int removeFenixId = scanner.nextInt();
                            System.out.print("Tem a certeza que quer remover a pessoa? (s/n): ");
                            String confirmation = scanner.next();
                            if (confirmation.equalsIgnoreCase("s")) {
                                arvore.removerPessoa(removeFenixId);
                                System.out.println("\nPessoa removida com sucesso!");
                            } else {
                                System.out.println("\nRemoção cancelada.");
                            }
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }
                    case 5:
                        try {
                            System.out.print("Digite o ID do afilhado: ");
                            int removerAfilhadoId = scanner.nextInt();
        
                            System.out.print("Digite o ID do padrinho: ");
                            int removePadrinhoId = scanner.nextInt();
                            scanner.nextLine();
        
                            arvore.removerPadrinho(removerAfilhadoId, removePadrinhoId);
                            System.out.println("\nPadrinho removido com sucesso!");
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }

                    case 6: // TODO: FAZER O MOSTRAR PESSOA NA APP E NAO NO CORE, O CORE SO DEVOLVE AS INFORMAÇÕES
                        try {
                            System.out.println();
                            for (int pessoaId : arvore.getPessoas().keySet()) {
                                arvore.printPessoa(pessoaId);
                                System.out.println();
                            }
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }
                    case 7: // TODO METER PRINTS NA APP
                        for (Pessoa pessoa : arvore.getPessoas().values()) {
                            pessoa.printPessoaId();
                        }
                        break;
                    case 8:
                        System.out.print("Digite o ID da pessoa para gestão: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();

                            Pessoa pessoa = arvore.getPessoa(pessoaId);
                            if (pessoa == null) {
                                System.out.println("\nPessoa não encontrada com o ID fornecido.");
                                break;
                            }

                            boolean editar = true;
                            while (editar) {
                                System.out.println("\n--- Gestão de Pessoa ---");
                                System.out.println("1. Alterar ID do Fenix");
                                System.out.println("2. Alterar Nome");
                                System.out.println("3. Alterar Alcunha");
                                System.out.println("4. Alterar Matrícula");
                                System.out.println("5. Adicionar Padrinho");
                                System.out.println("6. Adicionar Afilhado");
                                System.out.println("0. Voltar ao Menu Principal");
                                System.out.print("Escolha uma opção: ");
                                int opcaoEdicao = scanner.nextInt();
                                scanner.nextLine();  // Consumir nova linha após nextInt()

                                switch (opcaoEdicao) {
                                    case 1:
                                        try {
                                            System.out.print("Digite o novo ID do Fenix: ");
                                            int novoFenixId = scanner.nextInt();
                                            scanner.nextLine();
                                            arvore.atualizarPessoaId(pessoaId, novoFenixId);
                                            System.out.println("\nID do Fenix atualizado com sucesso.");
                                            break;
                                        } catch (DuplicatePersonException e) {
                                            System.out.println("\nJá existe uma pessoa com o Fenix ID " + e.getFenixId());
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nInput inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        }

                                    case 2:
                                        System.out.print("Digite o novo Nome: ");
                                        String novoNome = scanner.nextLine();
                                        pessoa.setNome(novoNome);
                                        System.out.println("\nNome atualizado com sucesso.");
                                        break;

                                    case 3:
                                        System.out.print("Digite a nova Alcunha: ");
                                        String novaAlcunha = scanner.nextLine();
                                        pessoa.setAlcunha(novaAlcunha);
                                        System.out.println("\nAlcunha atualizada com sucesso.");
                                        break;

                                    case 4:
                                        System.out.print("Digite a nova Matrícula: ");
                                        try{
                                            int novaMatricula = scanner.nextInt();
                                            scanner.nextLine();
                                            pessoa.setMatriculas(novaMatricula);
                                            System.out.println("\nMatrícula atualizada com sucesso.");
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nInput inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        }
                                    case 5:
                                        try {
                                            System.out.print("Digite o ID do padrinho: ");
                                            int padrinhoId = scanner.nextInt();
                                            scanner.nextLine();
                                            arvore.adicionarPadrinho(pessoaId, padrinhoId);
                                            System.out.println("\nPadrinho adicionado com sucesso! " + arvore.getPessoa(pessoaId).getNome() + " é afilhado/a de " + arvore.getPessoa(padrinhoId).getNome());
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nID inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        } catch (UnknownPersonException e) {
                                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                                            break;
                                        }
                                    case 6:
                                        try {
                                            System.out.print("Digite o ID do afilhado: ");
                                            int afilhadoId = scanner.nextInt();
                                            scanner.nextLine();
                                            arvore.adicionarPadrinho(afilhadoId, pessoaId);
                                            System.out.println("\nPadrinho adicionado com sucesso! " + arvore.getPessoa(afilhadoId).getNome() + " é afilhado/a de " + arvore.getPessoa(pessoaId).getNome());
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nID inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        } catch (UnknownPersonException e) {
                                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                                            break;
                                        }
                                    case 0:
                                        editar = false;
                                        break;

                                    default:
                                        System.out.println("\nOpção inválida. Tente novamente.");
                                }
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                        }
                        break;

                    case 9:
                        boolean gerenciarAno = true;
                        while (gerenciarAno) {
                            System.out.println("\n--- Gestão do Ano ---");
                            System.out.println("Ano atual: " + arvore.getAno() + "/" + (arvore.getAno()+1));
                            System.out.println("1. Avançar Ano");
                            System.out.println("2. Recuar Ano");
                            System.out.println("0. Voltar ao Menu Principal");
                            System.out.print("Escolha uma opção: ");

                            try {
                                int opcaoAno = scanner.nextInt();
                                scanner.nextLine();  // Consumir nova linha após nextInt()

                                switch (opcaoAno) {
                                    case 1:
                                        arvore.avancarAno();
                                        System.out.println("\nAno avançado com sucesso");
                                        break;

                                    case 2:
                                        arvore.recuarAno();
                                        System.out.println("\nAno recuado com sucesso");
                                        break;

                                    case 0:
                                        gerenciarAno = false;
                                        break;

                                    default:
                                        System.out.println("\nOpção inválida. Tente novamente.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("\nInput inválido. Tente novamente.");
                                scanner.nextLine();
                            }
                        }
                        break;
                    case 10:
                        System.out.print("Digite o ID da pessoa para mostrar a árvore genealógica: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();
                            arvore.mostrarArvoreGenealogicaCima(pessoaId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }
                    case 11:
                        System.out.print("Digite o ID da pessoa para mostrar a árvore genealógica: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();
                            arvore.mostrarArvoreGenealogicaBaixo(pessoaId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("\nID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        } catch (UnknownPersonException e) {
                            System.out.println("\nPessoa " + e.getFenixId() + " não encontrada");
                            break;
                        }

                    case 0:
                        System.out.println("Saindo e salvando...");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInput inválido. Tente novamente.");
                scanner.nextLine();
                continue;
            }
        }
    }
}
