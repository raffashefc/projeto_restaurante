package pedido;

import java.util.Collection;
import java.util.HashMap;

public class GerenciadorPedidos {
    private HashMap<Integer, Pedido> pedidos = new HashMap<>();
    private int contadorPedidos = 1;

    public Pedido criarPedido(String nomeCliente) {
        Pedido novo = new Pedido(contadorPedidos, nomeCliente);
        pedidos.put(novo.getNumero(), novo);
        contadorPedidos++;
        return novo;
    }

    public Pedido buscarPedido(int numero) {
        return pedidos.get(numero);
    }

    // Cancela o pedido inteiro: ele sai do mapa e, por isso, do total do dia.
    // O contador não volta atrás, então o número de um pedido cancelado nunca é reutilizado.
    public boolean cancelarPedido(int numero) {
        return pedidos.remove(numero) != null;
    }

    public Collection<Pedido> getPedidos() {
        return pedidos.values();
    }

    public double calcularTotalDoDia() {
        double total = 0;
        for (Pedido p : pedidos.values()) {
            total += p.calcularTotal();
        }
        return total;
    }
}