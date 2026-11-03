package cardapio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lanche implements ItemCardapio {
    private String nome;
    private double preco;
    private List<String> ingredientes = new ArrayList<>();

    public Lanche(String nome, double preco) {
        setNome(nome);
        setPreco(preco);
    }

    public Lanche(String nome, double preco, List<String> ingredientes) {
        this(nome, preco);
        if (ingredientes == null) {
            throw new IllegalArgumentException("A lista de ingredientes não pode ser nula.");
        }
        // Cópia: cada Lanche tem a sua própria lista, nunca compartilhada.
        this.ingredientes = new ArrayList<>(ingredientes);
    }

    public List<String> getIngredientes() {
        return Collections.unmodifiableList(ingredientes);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do lanche não pode ser nulo nem vazio.");
        }
        this.nome = nome;
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço do lanche não pode ser negativo.");
        }
        this.preco = preco;
    }
}