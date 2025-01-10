package Controller;

import Model.Biblioteca;
import Model.Livro;
import View.LivroView;

public class LivroController {
    private final Biblioteca biblioteca;
    private final LivroView livroView;

    public LivroController(Biblioteca biblioteca, LivroView livroView) {
        this.biblioteca = biblioteca;
        this.livroView = livroView;
    }

    public void adicionarLivro() {
        Livro novoLivro = livroView.capturarDadosLivro();
        biblioteca.adicionarLivro(novoLivro);
        livroView.exibirMensagem("Livro adicionado com sucesso!");
    }

    public void listarLivros() {
        for (Livro livro : biblioteca.getLivros()) {
            livroView.exibirLivro(livro);
        }
    }

    public void editarLivro(int indice) {
        if (indice >= 0 && indice < biblioteca.getLivros().size()) {
            Livro novoLivro = livroView.capturarDadosLivro();
            biblioteca.editarLivro(indice, novoLivro);
            livroView.exibirMensagem("Livro editado com sucesso!");
        } else {
            livroView.exibirMensagem("Índice inválido!");
        }
    }

    public void removerLivro(int indice) {
        if (indice >= 0 && indice < biblioteca.getLivros().size()) {
            biblioteca.removerLivro(indice);
            livroView.exibirMensagem("Livro removido com sucesso!");
        } else {
            livroView.exibirMensagem("Índice inválido!");
        }
    }
}
