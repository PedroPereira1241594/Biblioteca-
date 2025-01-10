package View;

import Model.Utentes;
import java.util.Scanner;

public class UtenteView {
    private final Scanner scanner;

    public UtenteView() {
        this.scanner = new Scanner(System.in);
    }

    public Utentes capturarDadosUtente() {
        System.out.println("Digite o nome do utente:");
        String nome = scanner.nextLine();
        System.out.println("Digite o NIF:");
        int nif = scanner.nextInt();
        System.out.println("Digite o género (1 para Masculino, 0 para Feminino):");
        boolean genero = scanner.nextInt() == 1;
        System.out.println("Digite o contacto:");
        int contacto = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        return new Utentes(nome, nif, genero, contacto);
    }

    public void exibirUtente(Utentes utente) {
        System.out.println(utente.toString());
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
