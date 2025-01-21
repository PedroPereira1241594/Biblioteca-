package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Configurações {
    private String caminhoLivros;
    private String caminhoUtentes;
    private String caminhoJornal;
    private String caminhoEmprestimo;
    private String caminhoReserva;

    public Configurações(String caminhoConfig) throws IOException {
        carregarConfiguracoes(caminhoConfig);
    }

    private void carregarConfiguracoes(String Caminho) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(Caminho))) {
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

    public String getCaminhoLivros() {
        return caminhoLivros;
    }

    public String getCaminhoUtentes() {
        return caminhoUtentes;
    }

    public String getCaminhoJornal() {
        return caminhoJornal;
    }

    public String getCaminhoEmprestimo() {
        return caminhoEmprestimo;
    }

    public String getCaminhoReserva() { return caminhoReserva; }

}
