package Controller;

import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportarDados {

    public static List<Livro> carregarLivros(String caminhoLivros) {
        List<Livro> livros = new ArrayList<>();
        int countLinhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoLivros))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty()) continue;

                String[] dados = linha.split(";");

                String isbn = dados[0].replace("ISBN: ", "").trim();
                String nome = dados[1].replace("Nome: ", "").trim();
                String editora = dados[2].replace("Editora: ", "").trim();
                String categoria = dados[3].replace("Categoria: ", "").trim();
                int ano = Integer.parseInt(dados[4].replace("Ano: ", "").trim());
                String autor = dados[5].replace("Autor: ", "").trim();

                Livro livro = new Livro(nome, editora, categoria, ano, autor, isbn); // Certifique-se de passar o ISBN aqui
                livros.add(livro);
                countLinhas++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros: " + e.getMessage());
        }
        System.out.println("Total de livros lidos: " + countLinhas);
        return livros;
    }


    public static List<Utentes> carregarUtentes(String caminhoUtentes) {
        List<Utentes> utentes = new ArrayList<>();
        int countLinhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoUtentes))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 4) {
                        String nome = dados[0].replace("Nome: ", "").trim();
                        String nif = dados[1].replace("NIF: ", "").trim();
                        boolean genero = dados[2].replace("Genero: ", "").trim().equalsIgnoreCase("M");
                        String contacto = dados[3].replace("Contacto: ", "").trim();

                        Utentes utente = new Utentes(nome, nif, genero, contacto);
                        utentes.add(utente);
                        countLinhas++;
                    } else {
                        System.out.println("Linha mal formatada no arquivo de utentes. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar utentes do arquivo: " + e.getMessage());
        }
        System.out.println("Total de utentes lidos: " + countLinhas);
        return utentes;
    }

    public static List<Jornal> carregarJornais(String caminhoJornal) {
        List<Jornal> jornais = new ArrayList<>();
        int countLinhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoJornal))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 5) {
                        String issn = dados[0].replace("ISSN: ", "").trim();
                        String titulo = dados[1].replace("Titulo: ", "").trim();
                        String categoria = dados[2].replace("Categoria: ", "").trim();
                        String editora = dados[3].replace("Editora: ", "").trim();
                        LocalDate dataPublicacao = LocalDate.parse(dados[4].replace("Data Publicação: ", "").trim());

                        Jornal jornal = new Jornal(issn, titulo, categoria, editora, dataPublicacao);
                        jornais.add(jornal);
                        countLinhas++;
                    } else {
                        System.out.println("Linha mal formatada no arquivo de jornais. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jornais do arquivo: " + e.getMessage());
        }
        System.out.println("Total de jornais/revistas lidos: " + countLinhas);
        return jornais;
    }

    public static List<Emprestimos> carregarEmprestimos(String caminhoEmprestimo, List<Utentes> utentes, List<ItemEmprestavel> itens) {
        List<Emprestimos> emprestimos = new ArrayList<>();
        int countLinhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoEmprestimo))) {
            String linha;
            int linhaNumero = 0;

            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim();

                if (linha.isEmpty()) continue; // Ignora linhas vazias

                String[] dados = linha.split(";");

                // Verifica se a linha possui todos os campos necessários
                if (dados.length < 7) { // Ajustado para 7 colunas no mínimo
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    // Extrai os dados da linha
                    int numero = Integer.parseInt(dados[0].replace("ID: ", "").trim());
                    String nomeUtente = dados[1].replace("Nome: ", "").trim();
                    String issnStr = dados[2].replace("ISSN: ", "").trim();
                    String isbnStr = dados[3].replace("ISBN: ", "").trim();

                    // Processa ISSNs e ISBNs em listas
                    List<ItemEmprestavel> itensEmprestados = new ArrayList<>();

                    // Processa ISSNs
                    if (!issnStr.isEmpty()) {
                        String[] issns = issnStr.split(",");
                        for (String issn : issns) {
                            issn = issn.trim();
                            for (ItemEmprestavel item : itens) {
                                if (item instanceof Jornal && ((Jornal) item).getIssn().equalsIgnoreCase(issn)) {
                                    itensEmprestados.add(item);
                                    break;
                                }
                            }
                        }
                    }

                    // Processa ISBNs
                    if (!isbnStr.isEmpty()) {
                        String[] isbns = isbnStr.split(",");
                        for (String isbn : isbns) {
                            isbn = isbn.trim();
                            for (ItemEmprestavel item : itens) {
                                if (item instanceof Livro && ((Livro) item).getIsbn().equalsIgnoreCase(isbn)) {
                                    itensEmprestados.add(item);
                                    break;
                                }
                            }
                        }
                    }

                    // Procura o utente pelo nome
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente)) {
                            utente = u;
                            break;
                        }
                    }

                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa datas
                    LocalDate dataInicio = LocalDate.parse(dados[4].replace("DataInicio: ", "").trim());
                    LocalDate dataPrevistaDevolucao = LocalDate.parse(dados[5].replace("DataPrevistaDevolução: ", "").trim());
                    LocalDate dataEfetivaDevolucao = null;

                    String dataEfetivaStr = dados[6].replace("DataEfetivaDevolução: ", "").trim();
                    if (!dataEfetivaStr.equalsIgnoreCase("null") && !dataEfetivaStr.isEmpty()) {
                        dataEfetivaDevolucao = LocalDate.parse(dataEfetivaStr);
                    }

                    // Cria o empréstimo
                    Emprestimos emprestimo = new Emprestimos(numero, utente, itensEmprestados, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
                    emprestimos.add(emprestimo);
                    countLinhas++;

                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos do arquivo: " + e.getMessage());
        }
        System.out.println("Total de empréstimos lidos: " + countLinhas);
        return emprestimos;
    }


    public static List<Reserva> carregarReservas(String caminhoReserva, List<Utentes> utentes, List<ItemEmprestavel> itensDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();
        int countLinhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoReserva))) {
            String linha;
            int linhaNumero = 0;

            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim();

                if (linha.isEmpty()) continue; // Ignora linhas vazias

                String[] dados = linha.split(";");

                // Verifica se a linha possui todos os campos necessários
                if (dados.length < 6) {
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    // Extrai os dados da linha
                    int numero = Integer.parseInt(dados[0].replace("ID: ", "").trim());
                    String nomeUtente = dados[1].replace("Nome: ", "").trim();
                    String issnStr = dados[2].replace("ISSN: ", "").trim();
                    String isbnStr = dados[3].replace("ISBN: ", "").trim();

                    // Processa ISSNs e ISBNs em listas
                    List<ItemEmprestavel> itensReservados = new ArrayList<>();

                    // Processa ISSNs (Jornais)
                    if (!issnStr.isEmpty()) {
                        String[] issns = issnStr.split(",");
                        for (String issn : issns) {
                            issn = issn.trim();
                            for (ItemEmprestavel item : itensDisponiveis) {
                                if (item instanceof Jornal && ((Jornal) item).getIssn().equalsIgnoreCase(issn)) {
                                    itensReservados.add(item);
                                    break;
                                }
                            }
                        }
                    }

                    // Processa ISBNs (Livros)
                    if (!isbnStr.isEmpty()) {
                        String[] isbns = isbnStr.split(",");
                        for (String isbn : isbns) {
                            isbn = isbn.trim();
                            for (ItemEmprestavel item : itensDisponiveis) {
                                if (item instanceof Livro && ((Livro) item).getIsbn().equalsIgnoreCase(isbn)) {
                                    itensReservados.add(item);
                                    break;
                                }
                            }
                        }
                    }

                    // Procura o utente pelo nome
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente)) {
                            utente = u;
                            break;
                        }
                    }

                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa datas
                    LocalDate dataRegisto = LocalDate.parse(dados[4].replace("DataRegisto: ", "").trim());
                    LocalDate dataInicio = LocalDate.parse(dados[5].replace("DataInicioReserva: ", "").trim());
                    LocalDate dataFim = LocalDate.parse(dados[6].replace("DataFimReserva: ", "").trim());

                    // Cria a reserva e adiciona à lista
                    Reserva reserva = new Reserva(numero, utente, itensReservados, dataRegisto, dataInicio, dataFim);
                    reservas.add(reserva);
                    countLinhas++;
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar reservas do arquivo: " + e.getMessage());
        }
        System.out.println("Total de reservas lidas: " + countLinhas);
        return reservas;
    }

}