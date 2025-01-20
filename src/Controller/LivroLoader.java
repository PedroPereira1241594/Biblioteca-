package Controller;

import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivroLoader {

    private static final String FICHEIRO_LIVROS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\livros.txt"; // Nome do ficheiro

    // Método para carregar livros do ficheiro
    public static List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_LIVROS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 6) { // Verifica se há exatamente 6 campos
                        // Remover os prefixos antes de cada valor
                        String isbn = dados[0].replace("ISBN: ", "").trim();
                        String nome = dados[1].replace("Nome: ", "").trim();
                        String editora = dados[2].replace("Editora: ", "").trim();
                        String categoria = dados[3].replace("Categoria: ", "").trim();
                        int ano = Integer.parseInt(dados[4].replace("Ano: ", "").trim());
                        String autor = dados[5].replace("Autor: ", "").trim();

                        // Cria o objeto Livro
                        Livro livro = new Livro(nome, editora, categoria, ano, autor, isbn);
                        livros.add(livro);
                    } else {
                        System.out.println("Linha mal formatada. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros do ficheiro: " + e.getMessage());
        }
        return livros;
    }

    private static final String FICHEIRO_UTENTES = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\utentes.txt"; // Nome do ficheiro

    public static List<Utentes> carregarUtentes() {
        List<Utentes> utentes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_UTENTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 4) { // Verifica se há exatamente 4 campos
                        // Remover os prefixos antes de cada valor
                        String nome = dados[0].replace("Nome: ", "").trim();
                        String nif = dados[1].replace("NIF: ", "").trim();
                        Boolean genero = Boolean.parseBoolean(dados[2].replace("Genero: ", "").trim()); // "M" -> true, "F" -> false
                        String contacto = dados[3].replace("Contacto: ", "").trim();

                        // Cria o objeto Utentes
                        Utentes utente = new Utentes(nome, nif, genero, contacto);
                        utentes.add(utente);
                    } else {
                        System.out.println("Linha mal formatada. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar utentes do ficheiro: " + e.getMessage());
        }

        return utentes;
    }


    private static final String FICHEIRO_JORNAIS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\jornal.txt"; // Nome do ficheiro

    // Método para carregar jornais do ficheiro
    public static List<Jornal> carregarJornais() {
        List<Jornal> jornais = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_JORNAIS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 5) { // Verifica se há exatamente 5 campos
                        // Remover os prefixos antes de cada valor
                        String titulo = dados[0].replace("Titulo: ", "").trim();
                        String editora = dados[1].replace("Editora: ", "").trim();
                        String categoria = dados[2].replace("Categoria: ", "").trim();
                        String issn = dados[3].replace("ISSN: ", "").trim();
                        String dataPublicacao = dados[4].replace("Data Publicação: ", "").trim();

                        // Cria o objeto Jornal
                        Jornal jornal = new Jornal(titulo, editora, categoria, issn, dataPublicacao);
                        jornais.add(jornal);
                    } else {
                        System.out.println("Linha mal formatada. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jornais do ficheiro: " + e.getMessage());
        }

        return jornais;
    }

    private static final String FICHEIRO_EMPRESTIMOS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\emprestimos.txt";

    public static List<Emprestimos> carregarEmprestimos(List<Utentes> utentes, List<Livro> livrosDisponiveis) throws IOException {
        List<Emprestimos> emprestimos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_EMPRESTIMOS))) {
            String linha;
            int linhaNumero = 0; // Para rastrear o número da linha
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim(); // Remove espaços em branco no início e no final da linha

                // Ignorar linhas vazias
                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 6) {
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    // Processa os dados de cada empréstimo
                    int numero = Integer.parseInt(dados[0].replace("ID: ", "").trim()); // Remover "ID: " e converter para inteiro
                    String nomeUtente = dados[1].replace("Nome: ", "").trim(); // Remover "Nome: " e pegar o nome do utente
                    String livrosEmprestadosStr = dados[2].replace("ISBN: ", "").trim(); // Livros emprestados no formato "Livro: [Nome] (ISBN: [ISBN])"
                    String[] titulosLivros = livrosEmprestadosStr.split(","); // Separa os livros por vírgula

                    // Busca o utente correspondente pelo nome
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        // Remover espaços extras e comparar de forma insensível a maiúsculas/minúsculas
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente.trim())) {
                            utente = u;
                            break;
                        }
                    }

                    // Se o utente não for encontrado, ignora a linha
                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa os livros emprestados
                    List<Livro> livrosEmprestados = new ArrayList<>();
                    for (String titulo : titulosLivros) {
                        // Extrai o nome e o ISBN de cada livro
                        String[] livroDados = titulo.split("\\("); // Divide pelo parêntese
                        if (livroDados.length == 2) {
                            String nomeLivro = livroDados[0].replace("Livro: ", "").trim(); // Remove o prefixo "Livro: " e espaços
                            String isbnLivro = livroDados[1].replace("ISBN: ", "").replace(")", "").trim(); // Extrai o ISBN

                            // Busca o livro correspondente pelo ISBN
                            Livro livro = null;
                            for (Livro l : livrosDisponiveis) {
                                if (l.getIsbn().equals(isbnLivro)) {
                                    livro = l;
                                    break;
                                }
                            }

                            if (livro != null) {
                                livrosEmprestados.add(livro);
                            } else {
                                System.out.println("Livro com ISBN " + isbnLivro + " não encontrado. Ignorado.");
                            }
                        }
                    }

                    // Processa as datas
                    LocalDate dataInicio = LocalDate.parse(dados[3].replace("DataInicio: ", "").trim());
                    LocalDate dataPrevistaDevolucao = LocalDate.parse(dados[4].replace("DataPrevistaDevolução: ", "").trim());
                    LocalDate dataEfetivaDevolucao = null;
                    if (!dados[5].equalsIgnoreCase("null") && !dados[5].isEmpty()) {
                        dataEfetivaDevolucao = LocalDate.parse(dados[5].replace("DataEfetivaDevolução: ", "").trim());
                    }

                    // Cria o objeto Emprestimos e adiciona à lista
                    Emprestimos emprestimo = new Emprestimos(numero, utente, livrosEmprestados, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
                    emprestimos.add(emprestimo);
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos do ficheiro: " + e.getMessage());
        }

        return emprestimos;
    }

    private static final String FICHEIRO_RESERVAS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\reservas.txt";  // Nome do ficheiro de reservas

    // Método para carregar reservas do ficheiro
    public static List<Reserva> carregarReservas(List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_RESERVAS))) {
            String linha;
            int linhaNumero = 0; // Para rastrear o número da linha
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim(); // Remove espaços em branco no início e no final da linha

                // Ignorar linhas vazias
                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 6) { // Certifica-se de que existem pelo menos 6 campos
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    // Processa os dados da reserva
                    int numeroReserva = Integer.parseInt(dados[0].replace("ID: ", "").trim()); // Remover "ID: " e converter para inteiro
                    String nomeUtente = dados[1].replace("Nome: ", "").trim(); // Remover "Nome: " e pegar o nome do utente
                    String livrosReservadosStr = dados[2].replace("ISBN: ", "").trim(); // Livros reservados no formato de ISBN
                    String[] isbnLivros = livrosReservadosStr.split(","); // Separa os ISBNs por vírgula

                    // Busca o utente correspondente pelo nome
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        // Comparar de forma insensível a maiúsculas/minúsculas
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente.trim())) {
                            utente = u;
                            break;
                        }
                    }

                    // Se o utente não for encontrado, ignora a linha
                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa os livros reservados
                    List<Livro> livrosReservados = new ArrayList<>();
                    for (String isbn : isbnLivros) {
                        // Busca o livro correspondente pelo ISBN
                        Livro livro = null;
                        for (Livro l : livrosDisponiveis) {
                            if (l.getIsbn().equals(isbn.trim())) {
                                livro = l;
                                break;
                            }
                        }

                        if (livro != null) {
                            livrosReservados.add(livro);
                        } else {
                            System.out.println("Livro com ISBN " + isbn.trim() + " não encontrado. Ignorado.");
                        }
                    }

                    // Processa as datas de registro, início e fim
                    LocalDate dataRegisto = LocalDate.parse(dados[3].replace("DataRegisto: ", "").trim());
                    LocalDate dataInicio = LocalDate.parse(dados[4].replace("DataInicioReserva: ", "").trim());
                    LocalDate dataFim = LocalDate.parse(dados[5].replace("DataFimReserva: ", "").trim());

                    // Cria o objeto Reserva e adiciona à lista de reservas
                    Reserva reserva = new Reserva(numeroReserva, utente, livrosReservados, dataRegisto, dataInicio, dataFim);
                    reservas.add(reserva);

                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar reservas do ficheiro: " + e.getMessage());
        }

        return reservas;
    }

}