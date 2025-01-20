package Controller;

import Model.Emprestimos;
import Model.Reserva;
import Model.Utentes;
import View.UtenteView;

import java.util.*;

public class UtenteController {
    private final ArrayList<Utentes> utentes;
    private final UtenteView utenteView;

    public UtenteController(ArrayList<Utentes> utentes, UtenteView utenteView) {
        this.utentes = utentes;
        this.utenteView = utenteView;
    }

    public void adicionarUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("NIF: ");
        String NIF = scanner.nextLine();
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

        // Exibe os utentes com reservas ou empréstimos
        if (utentesComReservasOuEmprestimos.isEmpty()) {
            System.out.println("Não há utentes com reservas ou empréstimos.");
        } else {
            System.out.println("\n=== Lista de Utentes com Reservas ou Empréstimos ===");
            for (Utentes utente : utentesComReservasOuEmprestimos) {
                System.out.println("Nome: " + utente.getNome() + " | NIF: " + utente.getNif() + " | Tel: " + utente.getContacto() + " | " + utente.getGenero());
            }
        }
    }


    public void editarUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduza o NIF do utente a editar: ");
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

    public void removerUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduza o NIF do utente a remover: ");
        String nif = scanner.nextLine();
        Utentes utente = null;
        for (Utentes Indice : utentes) {
            if (Indice.getNif().equals(nif)) {
                utente = Indice;
                break;
            }
        }
        if (utente != null) {
            utentes.remove(utente);
            System.out.println("Utente removido com sucesso!");
        } else {
            System.out.println("NIF inválido!");
        }
    }
    public Utentes buscarUtentePorNif(String nif) {
        for (Utentes utente : utentes) {
            if (utente.getNif().equals(nif)) {
                return utente;
            }
        }
        return null;
    }

}
