package Controller;

import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivroLoader {

    public static List<Livro> carregarLivros(String caminhoLivros) {
        List<Livro> livros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoLivros))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 6) {
                        String isbn = dados[0].replace("ISBN: ", "").trim();
                        String nome = dados[1].replace("Nome: ", "").trim();
                        String editora = dados[2].replace("Editora: ", "").trim();
                        String categoria = dados[3].replace("Categoria: ", "").trim();
                        int ano = Integer.parseInt(dados[4].replace("Ano: ", "").trim());
                        String autor = dados[5].replace("Autor: ", "").trim();

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

    public static List<Utentes> carregarUtentes(String caminhoUtentes) {
        List<Utentes> utentes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoUtentes))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 4) {
                        String nome = dados[0].replace("Nome: ", "").trim();
                        String nif = dados[1].replace("NIF: ", "").trim();
                        Boolean genero = dados[2].replace("Genero: ", "").trim().equals("M") ? true : false;
                        String contacto = dados[3].replace("Contacto: ", "").trim();

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

    public static List<Jornal> carregarJornais(String caminhoJornal) {
        List<Jornal> jornais = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoJornal))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");

                    if (dados.length == 5) {
                        String titulo = dados[0].replace("Titulo: ", "").trim();
                        String editora = dados[1].replace("Editora: ", "").trim();
                        String categoria = dados[2].replace("Categoria: ", "").trim();
                        String issn = dados[3].replace("ISSN: ", "").trim();
                        String dataPublicacao = dados[4].replace("Data Publicação: ", "").trim();

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

    public static List<Emprestimos> carregarEmprestimos(String caminhoEmprestimo, List<Utentes> utentes, List<Livro> livrosDisponiveis) throws IOException {
        List<Emprestimos> emprestimos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoEmprestimo))) {
            String linha;
            int linhaNumero = 0;
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim();

                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 6) {
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    int numero = Integer.parseInt(dados[0].replace("ID: ", "").trim()); // Remover "ID: " e converter para inteiro
                    String nomeUtente = dados[1].replace("Nome: ", "").trim(); // Remover "Nome: " e pegar o nome do utente
                    String livrosEmprestadosStr = dados[2].replace("ISBN: ", "").trim(); // Livros emprestados no formato "Livro: [Nome] (ISBN: [ISBN])"
                    String[] titulosLivros = livrosEmprestadosStr.split(","); // Separa os livros por vírgula

                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente.trim())) {
                            utente = u;
                            break;
                        }
                    }

                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    List<Livro> livrosEmprestados = new ArrayList<>();
                    for (String titulo : titulosLivros) {
                        String[] livroDados = titulo.split("\\(");
                        if (livroDados.length == 2) {
                            String nomeLivro = livroDados[0].replace("Livro: ", "").trim(); // Remove o prefixo "Livro: " e espaços
                            String isbnLivro = livroDados[1].replace("ISBN: ", "").replace(")", "").trim(); // Extrai o ISBN

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

                    LocalDate dataInicio = LocalDate.parse(dados[3].replace("DataInicio: ", "").trim());
                    LocalDate dataPrevistaDevolucao = LocalDate.parse(dados[4].replace("DataPrevistaDevolução: ", "").trim());
                    LocalDate dataEfetivaDevolucao = null;
                    if (!dados[5].equalsIgnoreCase("null") && !dados[5].isEmpty()) {
                        dataEfetivaDevolucao = LocalDate.parse(dados[5].replace("DataEfetivaDevolução: ", "").trim());
                    }

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

    public static List<Reserva> carregarReservas(String caminhoReserva, List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoReserva))) {
            String linha;
            int linhaNumero = 0;
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim();

                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 6) {
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    int numeroReserva = Integer.parseInt(dados[0].replace("ID: ", "").trim()); // Remover "ID: " e converter para inteiro
                    String nomeUtente = dados[1].replace("Nome: ", "").trim(); // Remover "Nome: " e pegar o nome do utente
                    String livrosReservadosStr = dados[2].replace("ISBN: ", "").trim(); // Livros reservados no formato de ISBN
                    String[] isbnLivros = livrosReservadosStr.split(","); // Separa os ISBNs por vírgula

                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNome().trim().equalsIgnoreCase(nomeUtente.trim())) {
                            utente = u;
                            break;
                        }
                    }

                    if (utente == null) {
                        System.out.println("Utente com nome " + nomeUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    List<Livro> livrosReservados = new ArrayList<>();
                    for (String isbn : isbnLivros) {

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

                    LocalDate dataRegisto = LocalDate.parse(dados[3].replace("DataRegisto: ", "").trim());
                    LocalDate dataInicio = LocalDate.parse(dados[4].replace("DataInicioReserva: ", "").trim());
                    LocalDate dataFim = LocalDate.parse(dados[5].replace("DataFimReserva: ", "").trim());

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