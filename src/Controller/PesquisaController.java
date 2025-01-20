package Controller;

import Model.Jornal;
import Model.Livro;

import java.util.ArrayList;
import java.util.List;

public class PesquisaController {
    private List<Livro> livros; // Lista de livros a ser pesquisada.
    private List<Jornal> jornals; // Lista de livros a ser pesquisada.

    // Construtor que inicializa a lista de livros
    public PesquisaController(List<Livro> livros, List<Jornal> jornals) {
        this.livros = livros;
        this.jornals = jornals;
    }

    // Método para pesquisar por ISBN
    public Livro pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equals(ISBN)) { // Verifica se o ISBN coincide.
                return livro; // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }
    // Método para pesquisar por ISSN
    public Jornal pesquisaISSN(String ISSN) {
        for (Jornal jornal : jornals) { // Agora iterando sobre a lista de jornais
            if (jornal.getIssn().equals(ISSN)) { // Verifica se o ISSN coincide
                return jornal; // Retorna o jornal encontrado
            }
        }
        return null; // Retorna null se nenhum jornal for encontrado
    }



}
