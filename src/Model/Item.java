package Model;

public abstract class Item {
    private String nome;       // Nome do item (pode ser título do livro ou jornal)
    private String tipo;       // Tipo do item (ex: "Livro", "Jornal")
    private String identificador; // Pode ser ISBN ou ISSN, dependendo do tipo

    // Construtor
    public Item(String nome, String tipo, String identificador) {
        this.nome = nome;
        this.tipo = tipo;
        this.identificador = identificador;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    // Método abstrato para exibir detalhes específicos
    public abstract void exibirDetalhes();

    @Override
    public String toString() {
        return "Item: " + nome + "\nTipo: " + tipo + "\nIdentificador: " + identificador;
    }
}

