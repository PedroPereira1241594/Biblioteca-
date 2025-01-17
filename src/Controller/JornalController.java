package Controller;

import Model.Jornal;
import java.util.ArrayList;

public class JornalController {
    private ArrayList<Jornal> jornais; // Lista para armazenar os jornais

    // Construtor
    public JornalController() {
        this.jornais = new ArrayList<>();
    }

    // Criar um novo jornal
    public void criarJornal(String titulo, String editora, String categoria, String issn, String dataPublicacao) {
        Jornal novoJornal = new Jornal(titulo, editora, categoria, issn, dataPublicacao);
        jornais.add(novoJornal);
        System.out.println("Jornal/revista adicionado com sucesso!");
    }

    // Método para listar todos os jornais
    public void listarJornais() {
        if (jornais.isEmpty()) {
            System.out.println("Nenhum jornal ou revista cadastrado.");
            return;
        }

        System.out.println("Lista de jornais/revistas:");
        for (Jornal jornal : jornais) {
            System.out.println(jornal);
        }
    }

    // Método para atualizar um jornal
    public void atualizarJornal(String issn, String novoTitulo, String novaEditora, String novaCategoria, String novoIssn, String novaDataPublicacao) {
        Jornal jornalEncontrado = null;

        // Buscando o jornal pelo ISSN
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                jornalEncontrado = jornal;
                break;
            }
        }

        if (jornalEncontrado == null) {
            System.out.println("Nenhum jornal/revista encontrado com o ISSN fornecido.");
            return;
        }

        // Atualizando os dados do jornal encontrado
        if (!novoTitulo.isEmpty()) jornalEncontrado.setTitulo(novoTitulo);
        if (!novaEditora.isEmpty()) jornalEncontrado.setEditora(novaEditora);
        if (!novaCategoria.isEmpty()) jornalEncontrado.setCategoria(novaCategoria);
        if (!novoIssn.isEmpty()) jornalEncontrado.setIssn(novoIssn);
        if (!novaDataPublicacao.isEmpty()) jornalEncontrado.setDataPublicacao(novaDataPublicacao);

        System.out.println("Jornal/revista atualizado com sucesso!");
    }

    // Método para deletar um jornal
    public void deletarJornal(String issn) {
        Jornal jornalEncontrado = null;

        // Buscando o jornal pelo ISSN
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                jornalEncontrado = jornal;
                break;
            }
        }

        if (jornalEncontrado == null) {
            System.out.println("Nenhum jornal/revista encontrado com o ISSN fornecido.");
            return;
        }

        // Deletando o jornal encontrado
        System.out.println("Deletando o jornal/revista: " + jornalEncontrado);
        jornais.remove(jornalEncontrado);
        System.out.println("Jornal/revista removido com sucesso!");
    }

    // Método para buscar um jornal pelo ISSN
    public Jornal buscarPorIssn(String issn) {
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                return jornal;
            }
        }
        return null;
    }
}



