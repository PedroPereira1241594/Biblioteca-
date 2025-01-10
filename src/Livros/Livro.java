package Livros;

import java.util.ArrayList;
import java.util.Scanner;


public class Livro {
    private String nome;
    private String editora;
    private String categoria;
    private int ano;
    private String autor;
    private int ISBN;

    public Livro(String nome, String editora, String categoria, int ano, String autor) {
        this.nome = nome;
        this.editora = editora;
        this.categoria = categoria;
        this.ano = ano;
        this.autor = autor;
    }

    public String getnome() {
        return nome;
    }

    public void setnome(String nome) {
        nome = nome;
    }

    public String geteditora() {
        return editora;
    }

    public void seteditora(String editora) {
        editora = editora;
    }

    public String getcategoria() {
        return categoria;
    }

    public void setcategoria(String categoria) {
        categoria = categoria;
    }

    public int getano() {
        return ano;
    }

    public void setano(int ano) {
        ano = ano;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        autor = autor;
    }

    @Override
    public String toString() {
        return "Livros.Livro: " + nome + "\n" + "Editora: " + editora + "\n" + "Categoria :" + editora + "\n" + "Ano: " + ano + "\n" + "Autor: " + autor;
    }

    public static class Biblioteca {
        private final ArrayList<Livro> livros;

        public Biblioteca() {
            this.livros = new ArrayList<>();
        }

        // Adicionar um livro
        public void adicionarLivro(Livro livro) {
            livros.add(livro);
            System.out.println("Livros.Livro adicionado com sucesso!");
        }

        // Mostra todos os livros
        public void listarLivros() {
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro disponível na biblioteca.");
            } else {
                for (Livro livro : livros) {
                    System.out.println(livro);
                }
            }
        }
        // Editar livros
        public void editarLivro(Scanner scanner) {
            listarLivros(); // Exibir os livros disponíveis
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro para editar.");
                return;
            }


            System.out.print("Digite o número do livro a editar: ");
            int indice = scanner.nextInt() - 1;
            scanner.nextLine(); // Consumir a quebra de linha

            if (indice < 0 || indice >= livros.size()) {
                System.out.println("Número inválido. Operação cancelada.");
                return;
            }

            // Novas informações para o livro
            System.out.print("Novo Nome: ");
            String novoNome = scanner.nextLine();
            System.out.print("Nova Editora: ");
            String novaEditora = scanner.nextLine();
            System.out.print("Nova Categoria: ");
            String novaCategoria = scanner.nextLine();
            System.out.print("Novo Ano: ");
            int novoAno = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            System.out.print("Novo Autor: ");
            String novoAutor = scanner.nextLine();

            // Atualizar o livro
            Livro novoLivro = new Livro(novoNome, novaEditora, novaCategoria, novoAno, novoAutor);
            livros.set(indice, novoLivro);
            System.out.println("Livros.Livro editado com sucesso!");
        }
        // Remover livros
        public void removerLivro(Scanner scanner) {
            listarLivros(); // Exibir os livros disponíveis
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro para remover.");
                return;
            }

            System.out.print("Digite o número do livro a remover: ");
            int indice = scanner.nextInt() - 1;
            scanner.nextLine();

            if (indice < 0 || indice >= livros.size()) {
                System.out.println("Número inválido. Operação cancelada.");
                return;
            }

            livros.remove(indice);
            System.out.println("Livros.Livro removido com sucesso!");
        }
    }
}


