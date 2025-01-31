package Controller;

import Model.Emprestimos;
import Model.Reserva;
import Model.Utentes;
import View.UtenteView;

import java.util.*;

/**
 * Controlador responsável pela gestão de utentes.
 */
public class UtenteController {
    private final ArrayList<Utentes> utentes;
    private final UtenteView utenteView;
    private final List<Reserva> reservas;
    private final List<Emprestimos> emprestimos;

    /**
     * Construtor para iniciar a classe UtenteController.
     *
     * @param utentes    Lista de utentes do sistema.
     * @param utenteView Instância da view para interação com utentes.
     * @param reservas   Lista de reservas feitas pelos utentes.
     * @param emprestimos Lista de empréstimos feitos pelos utentes.
     */
    public UtenteController(ArrayList<Utentes> utentes, UtenteView utenteView, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.utentes = utentes;
        this.utenteView = utenteView;
        this.reservas = reservas;
        this.emprestimos = emprestimos;
    }

    /**
     * Adiciona um novo utente ao sistema.
     * Pede as informações necessárias para criar um utente.
     */
    public void adicionarUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("NIF: ");
        String NIF = scanner.nextLine();

        // Verifica se o utente já existe
        for (Utentes utentes1 : utentes) {
            if (utentes1.getNif().equals(NIF)) {
                System.out.println("Esse Utente Já Existe!");
                return;
            }
        }
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        Boolean Genero = null;
        while (Genero == null) {
            System.out.print("Gênero (M/F): ");
            String genero1 = scanner.nextLine().trim().toUpperCase();

            // Converte boolean para M/F (Genero)
            if (genero1.equals("M")) {
                Genero = true;
            } else if (genero1.equals("F")) {
                Genero = false;
            } else {
                System.out.println("Entrada inválida! Digite 'M' para Masculino ou 'F' para Feminino.");
            }
        }

        System.out.print("Contacto: ");
        String Contacto = scanner.nextLine();

        Utentes utente = new Utentes(nome, NIF, Genero, Contacto);
        utentes.add(utente);
        System.out.println("Utente adicionado com sucesso!\n");
    }

    /**
     * Lista todos os utentes que possuem reservas ou empréstimos ativos.
     *
     * @param utentes    Lista de utentes do sistema.
     * @param reservas   Lista de reservas realizadas.
     * @param emprestimos Lista de empréstimos efetuados.
     */
    public void listarUtentesComReservasOuEmprestimos(List<Utentes> utentes, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        Set<Utentes> utentesComReservasOuEmprestimos = new HashSet<>();

        // Adiciona utentes que possuem reservas
        for (Reserva reserva : reservas) {
            utentesComReservasOuEmprestimos.add(reserva.getUtente());
        }

        // Adiciona utentes que possuem empréstimos
        for (Emprestimos emprestimo : emprestimos) {
            utentesComReservasOuEmprestimos.add(emprestimo.getUtente());
        }

        // Verifica e mostra os utentes com reservas ou empréstimos
        if (utentesComReservasOuEmprestimos.isEmpty()) {
            System.out.println("Não há utentes com reservas ou empréstimos.");
        } else {
            System.out.println("\n=== Lista de Utentes com Reservas ou Empréstimos ===");
            System.out.printf("%-60s %-30s %-30s %-30s\n", "Nome", "NIF", "Contacto", "Género");
            System.out.println("-".repeat(133));
            for (Utentes utente : utentesComReservasOuEmprestimos) {
                String genero = (utente.getGenero() == null)
                        ? "Indefinido"
                        : (utente.getGenero() ? "Masculino" : "Feminino");

                System.out.printf("%-60s %-30s %-30s %-30s\n",
                        utente.getNome(),
                        utente.getNif(),
                        utente.getContacto(),
                        genero);
            }
        }
    }

    /**
     * Edita as informações do utente, permitindo a alteração do nome, NIF, género e contacto.
     * O utente é identificado pelo NIF.
     */
    public void editarUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIntroduza o NIF do utente a editar: ");
        String nif = scanner.nextLine();
        Utentes utente = null;
        for (Utentes Indice : utentes) {
            if (Indice.getNif().equals(nif)) {
                utente = Indice;
                break;
            }
        }
        if (utente != null) {
            System.out.println("Editando o utente: " + utente.getNome());
            System.out.print("Introduza o novo nome (ou pressione Enter para manter): ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) utente.setNome(nome);

            System.out.print("Introduza o novo NIF (ou pressione Enter para manter): ");
            String nifStr = scanner.nextLine();
            if (!nifStr.isEmpty()) utente.setNif(nifStr);

            while (true) {
                System.out.print("Insira o Gênero (M/F) (ou Pressione Enter para manter): ");
                String generoStr = scanner.nextLine().trim().toUpperCase();

                if (generoStr.isEmpty()) {
                    System.out.println("Género mantido como: " + (utente.getGenero() ? "Masculino" : "Feminino"));
                    break;
                } else if (generoStr.equals("M")) {
                    utente.setGenero(true);
                    System.out.println("Gênero atualizado para (M)");
                    break;
                } else if (generoStr.equals("F")) {
                    utente.setGenero(false);
                    System.out.println("Gênero atualizado para (F)");
                    break;
                } else {
                    System.out.println("Entrada inválida! Digite 'M' para Masculino ou 'F' para Feminino.");
                }
            }

            System.out.print("Digite o novo contacto (ou pressione Enter para manter): ");
            String contactoStr = scanner.nextLine();
            if (!contactoStr.isEmpty()) utente.setContacto(contactoStr);

            System.out.println("Utente editado com sucesso!");
        } else {
            System.out.println("Utente não encontrado");
        }
    }

    /**
     * Remove um utente do sistema pelo NIF.
     * Só remove se o utente não estiver associado a emprestimo ou reserva.
     */
    public void removerUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIntroduza o NIF do utente a remover: ");
        String nif = scanner.nextLine();
        Utentes utente = null;

        // Procura o utente pelo NIF
        for (Utentes Indice : utentes) {
            if (Indice.getNif().equals(nif)) {
                utente = Indice;
                break;
            }
        }

        if (utente == null) {
            System.out.println("NIF inválido!");
            return;
        }

        // Verifica se o utente tem reservas associadas
        boolean utenteComReserva = false;
        for (Reserva reserva : reservas) {
            if (reserva.getUtente().equals(utente)) {
                utenteComReserva = true;
                break;
            }
        }

        // Verifica se o utente tem empréstimos ativos
        boolean utenteComEmprestimo = false;
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getUtente().equals(utente)) {
                utenteComEmprestimo = true;
                break;
            }
        }

        if (utenteComReserva) {
            System.out.println("Erro: O utente '" + utente.getNome() + "' tem reservas associadas e não pode ser removido.");
        } else if (utenteComEmprestimo) {
            System.out.println("Erro: O utente '" + utente.getNome() + "' tem empréstimos ativos e não pode ser removido.");
        } else {
            utentes.remove(utente);
            System.out.println("Utente removido com sucesso!");
        }
    }

    /**
     * Procura um utente pelo NIF.
     *
     * @param nif O NIF do utente a ser procurado.
     * @return O utente correspondente ao NIF, ou null se não existir.
     */
    public Utentes procurarUtentePorNif(String nif) {
        for (Utentes utente : utentes) {
            if (utente.getNif().equals(nif)) {
                return utente;
            }
        }
        return null;
    }

    /**
     * Consulta um utente pelo seu NIF.
     *
     * @param nif O NIF do utente a ser consultado.
     * @return O utente correspondente ao NIF, ou null se não existir.
     */
    public Utentes consultarUtente(String nif) {
        for (Utentes utente : utentes) {
            if (utente.getNif().equals(nif)) {
                return utente;
            }
        }
        return null;
    }
}
