package Model;

import java.util.ArrayList;

public class Biblioteca {
    private final ArrayList<Livro> livros;

    public Biblioteca() {
        this.livros = new ArrayList<>();
    }

    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void editarLivro(int indice, Livro novoLivro) {
        if (indice >= 0 && indice < livros.size()) {
            livros.set(indice, novoLivro);
        }
    }

    public void removerLivro(int indice) {
        if (indice >= 0 && indice < livros.size()) {
            livros.remove(indice);
        }
    }
}

