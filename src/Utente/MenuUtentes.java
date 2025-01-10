package Utente;

import java.util.Scanner;

public class MenuUtentes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ModificarUtentes modificarUtentes = new ModificarUtentes();

        while (true) {
            System.out.println("\n--- Gestão de Utentes ---");
            System.out.println("1. Adicionar Utente");
            System.out.println("2. Listar Utentes");
            System.out.println("3. Modificar Utente");
            System.out.println("4. Remover Utente");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("NIF: ");
                    int NIF = scanner.nextInt();
                    Boolean Genero = null;
                    while (Genero == null) {
                        System.out.print("Gênero (M/F): ");
                        String genero1 = scanner.next().trim().toUpperCase();

                        if (genero1.equals("M")) {
                            Genero = true;
                        } else if (genero1.equals("F")) {
                            Genero = false;
                        } else {
                            System.out.println("Entrada inválida! Digite 'M' para Masculino ou 'F' para Feminino.");
                        }
                    }

                    System.out.print("Contacto: ");
                    int Contacto = scanner.nextInt();
                    scanner.nextLine();

                    Utentes utentes = new Utentes(nome, NIF, Genero, Contacto);
                    modificarUtentes.adicionarUtente(utentes);
                    break;

                case 2:
                    modificarUtentes.mostrarUtentes();
                    break;

                case 3:
                    modificarUtentes.atualizarUtente(scanner);
                    break;

                case 4:
                    modificarUtentes.removerUtente(scanner);
                    break;

                case 5:
                    System.out.println("Encerrando o programa.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}

