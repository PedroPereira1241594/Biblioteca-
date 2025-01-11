package View;

import Controller.LivroController;
import Model.Livro;

import java.util.ArrayList;
import java.util.Scanner;

public class LivroView {

    public void exibirLivros(ArrayList<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro Registado.");
        } else {
            System.out.println("Lista de Livros:");
            for (int i = 0; i < livros.size(); i++) {
                Livro livro = livros.get(i);
                System.out.println("Título: " + livro.getNome());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("Editora: " + livro.getEditora());
                System.out.println("Categoria: " + livro.getCategoria());
                System.out.println("Ano: " + livro.getAno());
                System.out.println("----------------------");
            }
        }
    }
}

