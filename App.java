
import java.util.Scanner;
import java.util.InputMismatchException;

public class App {
    public static void main(String[] args) throws InputMismatchException {
        Manager manager = new Manager();
        manager.load();

        ArvoreLEIC arvore = manager.getArvore();
        
        Scanner scanner = new Scanner(System.in);

        // caso fechem sem ser pelo menu
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {manager.save();}));

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Adicionar Pessoa");
            System.out.println("2. Adicionar Padrinho");
            System.out.println("3. Mostrar Pessoa");
            System.out.println("4. Remover Pessoa");
            System.out.println("5. Remover Padrinho");
            System.out.println("6. Mostrar Lista de Todas as Pessoas");
            System.out.println("7. Menu Gestão de Pessoa");
            System.out.println("8. Menu Gestão do Ano");
            System.out.println("9. Mostrar Arvore Genealógica Acima Pessoa");
            System.out.println("10. Mostrar Arvore Genealógica Abaixo Pessoa");
            System.out.println("11. Lista Pessoas Números");
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
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Input inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }

                    case 2:
                        try {
                            System.out.print("Digite o ID do afilhado: ");
                            int afilhadoId = scanner.nextInt();
        
                            System.out.print("Digite o ID do padrinho: ");
                            int padrinhoId = scanner.nextInt();
                            scanner.nextLine();  // Consumir nova linha após nextInt()
        
                            arvore.adicionarPadrinho(afilhadoId, padrinhoId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }

                    case 3:
                        System.out.print("Digite o ID da pessoa que deseja mostrar: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            Pessoa pessoa = arvore.getPessoa(pessoaId);
                            arvore.printPessoa(pessoa);
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
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
                            } else {
                                System.out.println("Remoção cancelada.");
                            }
                            scanner.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }
                    case 5:
                        try {
                            System.out.print("Digite o ID do afilhado: ");
                            int removerAfilhadoId = scanner.nextInt();
        
                            System.out.print("Digite o ID do padrinho: ");
                            int removePadrinhoId = scanner.nextInt();
                            scanner.nextLine();  // Consumir nova linha após nextInt()
        
                            arvore.removerPadrinho(removerAfilhadoId, removePadrinhoId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }

                    case 6:
                        for (Pessoa pessoa : arvore.getPessoas().values()) {
                            arvore.printPessoa(pessoa);
                            System.out.println();
                        }
                        break;
                    case 7:
                        System.out.print("Digite o ID da pessoa para gestão: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();

                            Pessoa pessoa = arvore.getPessoa(pessoaId);
                            if (pessoa == null) {
                                System.out.println("Pessoa não encontrada com o ID fornecido.");
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
                                        System.out.print("Digite o novo ID do Fenix: ");
                                        int novoFenixId = scanner.nextInt();
                                        scanner.nextLine();
                                        arvore.atualizarPessoaId(pessoaId, novoFenixId);
                                        System.out.println("ID do Fenix atualizado com sucesso.");
                                        break;

                                    case 2:
                                        System.out.print("Digite o novo Nome: ");
                                        String novoNome = scanner.nextLine();
                                        pessoa.setNome(novoNome);
                                        System.out.println("Nome atualizado com sucesso.");
                                        break;

                                    case 3:
                                        System.out.print("Digite a nova Alcunha: ");
                                        String novaAlcunha = scanner.nextLine();
                                        pessoa.setAlcunha(novaAlcunha);
                                        System.out.println("Alcunha atualizada com sucesso.");
                                        break;

                                    case 4:
                                        System.out.print("Digite a nova Matrícula: ");
                                        try{
                                            int novaMatricula = scanner.nextInt();
                                            scanner.nextLine();
                                            pessoa.setMatriculas(novaMatricula);
                                            System.out.println("Matrícula atualizada com sucesso.");
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Input inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        }
                                    case 5:
                                        try {
                                            System.out.print("Digite o ID do padrinho: ");
                                            int padrinhoId = scanner.nextInt();
                                            scanner.nextLine();
                                            arvore.adicionarPadrinho(pessoaId, padrinhoId);
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("ID inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        }
                                    case 6:
                                        try {
                                            System.out.print("Digite o ID do afilhado: ");
                                            int afilhadoId = scanner.nextInt();
                                            scanner.nextLine();
                                            arvore.adicionarPadrinho(afilhadoId, pessoaId);
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("ID inválido. Tente novamente.");
                                            scanner.nextLine();
                                            break;
                                        }
                                    case 0:
                                        editar = false;
                                        break;

                                    default:
                                        System.out.println("Opção inválida. Tente novamente.");
                                }
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();  // Consumir a entrada inválida
                        }
                        break;

                        case 8:
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
                                            System.out.println("Ano avançado com sucesso");
                                            break;

                                        case 2:
                                            arvore.recuarAno();
                                            System.out.println("Ano recuado com sucesso");
                                            break;

                                        case 0:
                                            gerenciarAno = false;
                                            break;

                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Input inválido. Tente novamente.");
                                    scanner.nextLine();  // Consumir a entrada inválida
                                }
                            }
                            break;
                    case 9:
                        System.out.print("Digite o ID da pessoa para mostrar a árvore genealógica: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();
                            arvore.mostrarArvoreGenealogicaCima(pessoaId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }
                    case 10:
                        System.out.print("Digite o ID da pessoa para mostrar a árvore genealógica: ");
                        try {
                            int pessoaId = scanner.nextInt();
                            scanner.nextLine();
                            arvore.mostrarArvoreGenealogicaBaixo(pessoaId);
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("ID inválido. Tente novamente.");
                            scanner.nextLine();
                            break;
                        }

                    case 11:
                    for (Pessoa pessoa : arvore.getPessoas().values()) {
                        pessoa.printPessoaId();
                    }
                    break;

                    case 0:
                        System.out.println("Saindo e salvando...");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input inválido. Tente novamente.");
                scanner.nextLine();
                continue;
            }
        }
    }
}
