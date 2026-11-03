package pedido;

import cardapio.ItemCardapio;
import java.util.ArrayList;

public class Pedido {
    private int numero;
    private String nomeCliente;
    private ArrayList<ItemCardapio> itens = new ArrayList<>();

    public Pedido(int numero, String nomeCliente) {
        this.numero = numero;
        setNomeCliente(nomeCliente);
    }

    public int getNumero() {
        return numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");
        }
        this.nomeCliente = nomeCliente;
    }

    public void adicionarItem(ItemCardapio item) {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode ser nulo.");
        }
        itens.add(item);
    }

    // Remove o item da posição indicada (começa em 0). O total reflete a lista atualizada.
    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            throw new IllegalArgumentException("Esse item não existe no pedido.");
        }
        itens.remove(indice);
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public ArrayList<ItemCardapio> getItens() {
        return itens;
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemCardapio item : itens) {
            total += item.getPreco();
        }
        return total;
    }
}