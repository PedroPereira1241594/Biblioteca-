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
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros: " + e.getMessage());
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
                        boolean genero = dados[2].replace("Genero: ", "").trim().equalsIgnoreCase("M");
                        String contacto = dados[3].replace("Contacto: ", "").trim();

                        Utentes utente = new Utentes(nome, nif, genero, contacto);
                        utentes.add(utente);
                    } else {
                        System.out.println("Linha mal formatada no arquivo de utentes. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar utentes do arquivo: " + e.getMessage());
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
                        String issn = dados[0].replace("ISSN: ", "").trim();
                        String titulo = dados[1].replace("Titulo: ", "").trim();
                        String categoria = dados[2].replace("Categoria: ", "").trim();
                        String editora = dados[3].replace("Editora: ", "").trim();
                        LocalDate dataPublicacao = LocalDate.parse(dados[4].replace("Data Publicação: ", "").trim());

                        Jornal jornal = new Jornal(issn, titulo, categoria, editora, dataPublicacao);
                        jornais.add(jornal);
                    } else {
                        System.out.println("Linha mal formatada no arquivo de jornais. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jornais do arquivo: " + e.getMessage());
        }

        return jornais;
    }

    public static List<Emprestimos> carregarEmprestimos(String caminhoEmprestimo, List<Utentes> utentes, List<ItemEmprestavel> itens) {
        List<Emprestimos> emprestimos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoEmprestimo))) {
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
                    String itensEmprestadosStr = dados[2].replace("ISBN: ", "").trim();

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

                    // Processa os itens emprestados
                    List<ItemEmprestavel> itensEmprestados = new ArrayList<>();
                    String[] idsItens = itensEmprestadosStr.split(",");

                    for (String idItem : idsItens) {
                        idItem = idItem.trim();
                        ItemEmprestavel itemEncontrado = null;

                        // Procura o item pelo ISBN ou ISSN
                        for (ItemEmprestavel item : itens) {
                            if (item instanceof Livro && ((Livro) item).getIsbn().equals(idItem)) { // Corrigido para getIsbn()
                                itemEncontrado = item;
                                break;
                            } else if (item instanceof Jornal && ((Jornal) item).getISSN().equals(idItem)) {
                                itemEncontrado = item;
                                break;
                            }
                        }

                        if (itemEncontrado != null) {
                            itensEmprestados.add(itemEncontrado);
                        } else {
                            System.out.println("Item com ID " + idItem + " não encontrado. Ignorado.");
                        }
                    }

                    // Processa datas
                    LocalDate dataInicio = LocalDate.parse(dados[3].replace("DataInicio: ", "").trim());
                    LocalDate dataPrevistaDevolucao = LocalDate.parse(dados[4].replace("DataPrevistaDevolução: ", "").trim());
                    LocalDate dataEfetivaDevolucao = null;

                    String dataEfetivaStr = dados[5].replace("DataEfetivaDevolução: ", "").trim();
                    if (!dataEfetivaStr.equalsIgnoreCase("null") && !dataEfetivaStr.isEmpty()) {
                        dataEfetivaDevolucao = LocalDate.parse(dataEfetivaStr);
                    }

                    // Cria o empréstimo
                    Emprestimos emprestimo = new Emprestimos(numero, utente, itensEmprestados, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
                    emprestimos.add(emprestimo);

                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos do arquivo: " + e.getMessage());
        }

        return emprestimos;
    }



    public static List<Reserva> carregarReservas(String caminhoReserva, List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();
        int maiorId = 0;  // Inicializa com 0, assumindo que todos os IDs serão positivos

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

                    // Atualiza o maior ID encontrado
                    if (numeroReserva > maiorId) {
                        maiorId = numeroReserva;
                    }

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

        // Aqui você pode usar o maiorId conforme necessário
        System.out.println("Maior ID encontrado: " + maiorId);

        return reservas;
    }
}