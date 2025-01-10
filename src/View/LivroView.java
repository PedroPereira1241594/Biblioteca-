package View;

import Model.Livro;
import java.util.Scanner;

public class LivroView {
    private final Scanner scanner;

    public LivroView() {
        this.scanner = new Scanner(System.in);
    }

    public Livro capturarDadosLivro() {
        System.out.println("Digite o nome do livro:");
        String nome = scanner.nextLine();
        System.out.println("Digite a editora:");
        String editora = scanner.nextLine();
        System.out.println("Digite a categoria:");
        String categoria = scanner.nextLine();
        System.out.println("Digite o ano de publicação:");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        System.out.println("Digite o autor:");
        String autor = scanner.nextLine();

        return new Livro(nome, editora, categoria, ano, autor);
    }

    public void exibirLivro(Livro livro) {
        System.out.println(livro.toString());
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
