package View;

import Controller.*;
import Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável por apresentar ao utilizador a interface para a gestão de utentes.
 *
 * Esta classe permite ao utilizador realizar diversas operações relacionadas
 * com utentes, como adicionar, consultar, editar, remover e listar utentes,
 * além de mostrar informações detalhadas sobre um utente específico.
 */
public class UtenteView {
    private UtenteController utenteController;
    private Scanner scanner;

    /**
     * Configura o controller responsável pelas operações de utentes.
     *
     * @param utenteController Instância de {@link UtenteController}.
     */
    public void setUtenteController(UtenteController utenteController) {
        this.utenteController = utenteController;
    }

    /**
     * Configura o scanner utilizado para capturar entradas do utilizador.
     *
     * @param scanner Instância de {@link Scanner}.
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Menu principal de gestão de utentes.
     *
     * Este método exibe um menu para que o utilizador possa:
     * - Adicionar um novo utente.
     * - Consultar detalhes de um utente existente.
     * - Editar informações de um utente.
     * - Remover um utente.
     * - Listar todos os utentes com suas reservas ou empréstimos.
     *
     * @param utentes Lista de utentes disponíveis no sistema.
     * @param reservas Lista de reservas associadas aos utentes.
     * @param emprestimos Lista de empréstimos associados aos utentes.
     */
    public void gerirUtentes(List<Utentes> utentes, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        int opcao;

        do {
            System.out.println("\n=== Gestão de Utentes ===");
            System.out.println("1. Adicionar Utente");
            System.out.println("2. Consultar Utente");
            System.out.println("3. Editar Utente");
            System.out.println("4. Remover Utente");
            System.out.println("5. Listar Utentes");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, insira um número válido.");
                scanner.next();
            }
            opcao = scanner.nextInt();
            scanner.nextLine();  

            switch (opcao) {
                case 1:
                    utenteController.adicionarUtente();
                    break;
                case 2:
                    consultarUtente();
                    break;
                case 3:
                    utenteController.editarUtente();
                    break;
                case 4:
                    utenteController.removerUtente();
                    break;
                case 5:
                    utenteController.listarUtentesComReservasOuEmprestimos(utentes, reservas, emprestimos);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    /**
     * Mostra detalhes completos de um utente.
     *
     * @param utente Instância de {@link Utentes} contendo os dados do utente.
     */
    public void exibirUtenteDetalhado(Utentes utente) {
        System.out.println("\n========== Detalhes do Utente ===========");
        System.out.println("Nome: " + utente.getNome());
        System.out.println("NIF: " + utente.getNif());
        System.out.println("Género: " + (utente.getGenero() ? "Masculino" : "Feminino"));
        System.out.println("Contacto: " + utente.getContacto());
        System.out.println("=".repeat(41));
    }

    /**
     * Permite consultar os detalhes de um utente com base no NIF fornecido.
     *
     * O método solicita ao utilizador o NIF do utente, chama o controlador para
     * procurar as informações e mostra os detalhes, caso o utente seja encontrado.
     * Caso contrário, emite uma mensagem a informar que o utente não foi encontrado.
     */
    private void consultarUtente() {
        System.out.println("\n=== Consultar Utente ===");
        System.out.print("NIF do Utente: ");
        String nif = scanner.nextLine();

        // Chama o método consultarUtente do controller
        Utentes utente = utenteController.consultarUtente(nif);

        if (utente != null) {
            exibirUtenteDetalhado(utente);
        } else {
            System.out.println("O Utente com o NIF " + nif + " não foi encontrado.");
        }
    }
}
