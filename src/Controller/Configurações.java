package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe responsável por carregar e armazenar configurações do sistema a partir de um txt.
 */
public class Configurações {
    private String caminhoLivros;
    private String caminhoUtentes;
    private String caminhoJornal;
    private String caminhoEmprestimo;
    private String caminhoReserva;

    /**
     * Construtor da classe Configurações.
     *
     * @param caminhoConfig O caminho do txt da configuração.
     * @throws IOException Se ocorrer um erro ao carregar as configurações.
     */
    public Configurações(String caminhoConfig) throws IOException {
        carregarConfiguracoes(caminhoConfig);
    }

    /**
     * Carrega as configurações a partir de um txt.
     *
     * @param caminho O caminho do txt de configuração.
     * @throws IOException Se ocorrer um erro na leitura do txt.
     */
    private void carregarConfiguracoes(String caminho) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("caminhoLivros=")) {
                    caminhoLivros = linha.split("=", 2)[1].trim();
                } else if (linha.startsWith("caminhoUtentes=")) {
                    caminhoUtentes = linha.split("=", 2)[1].trim();
                } else if (linha.startsWith("caminhoJornal=")) {
                    caminhoJornal = linha.split("=", 2)[1].trim();
                } else if (linha.startsWith("caminhoEmprestimos=")) {
                    caminhoEmprestimo = linha.split("=", 2)[1].trim();
                } else if (linha.startsWith("caminhoReservas=")) {
                    caminhoReserva = linha.split("=", 2)[1].trim();
                }
            }
        }
    }

    /**
     * Obtém o caminho dos livros.
     *
     * @return O caminho do arquivo dos livros.
     */
    public String getCaminhoLivros() {
        return caminhoLivros;
    }

    /**
     * Obtém o caminho dos utentes.
     *
     * @return O caminho do arquivo dos utentes.
     */
    public String getCaminhoUtentes() {
        return caminhoUtentes;
    }

    /**
     * Obtém o caminho do jornal.
     *
     * @return O caminho do arquivo do jornal.
     */
    public String getCaminhoJornal() {
        return caminhoJornal;
    }

    /**
     * Obtém o caminho dos empréstimos.
     *
     * @return O caminho do arquivo dos empréstimos.
     */
    public String getCaminhoEmprestimo() {
        return caminhoEmprestimo;
    }

    /**
     * Obtém o caminho das reservas.
     *
     * @return O caminho do arquivo das reservas.
     */
    public String getCaminhoReserva() {
        return caminhoReserva;
    }
}
