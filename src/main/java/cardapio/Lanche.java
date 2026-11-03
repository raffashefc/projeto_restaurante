package cardapio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lanche implements ItemCardapio {
    private String nome;
    private double preco;
    private List<String> ingredientes = new ArrayList<>();

    // Ingredientes que podem ser retirados. O "= true" é obrigatório:
    // sem ele o boolean nasceria false e o lanche viria sem nada.
    private boolean queijo = true;
    private boolean alface = true;
    private boolean tomate = true;
    private boolean cebola = true;

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

    public boolean isQueijo() {
        return queijo;
    }

    public void setQueijo(boolean queijo) {
        this.queijo = queijo;
    }

    public boolean isAlface() {
        return alface;
    }

    public void setAlface(boolean alface) {
        this.alface = alface;
    }

    public boolean isTomate() {
        return tomate;
    }

    public void setTomate(boolean tomate) {
        this.tomate = tomate;
    }

    public boolean isCebola() {
        return cebola;
    }

    public void setCebola(boolean cebola) {
        this.cebola = cebola;
    }

    // Retirar ingrediente muda a descrição, mas não o preço.
    @Override
    public String getDescricao() {
        String desc = nome;

        if (!queijo) desc += " sem queijo";
        if (!alface) desc += " sem alface";
        if (!tomate) desc += " sem tomate";
        if (!cebola) desc += " sem cebola";

        return desc;
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