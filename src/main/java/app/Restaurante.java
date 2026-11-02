package app;

import cardapio.Lanche;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pedido.GerenciadorPedidos;
import pedido.Pedido;

public class Restaurante {
    private GerenciadorPedidos gerenciador = new GerenciadorPedidos();

    // Devolve objetos novos a cada chamada: nenhum Lanche é compartilhado entre pedidos.
    public List<Lanche> getCardapio() {
        List<Lanche> cardapio = new ArrayList<>();
        cardapio.add(criarLanche(1));
        cardapio.add(criarLanche(2));
        cardapio.add(criarLanche(3));
        return cardapio;
    }

    // Cria um Lanche NOVO a cada chamada. Devolve null se a opção não existir.
    public Lanche criarLanche(int opcao) {
        switch (opcao) {
            case 1:
                return new Lanche("X-Burger", 16.00);
            case 2:
                return new Lanche("X-Salada", 18.00);
            case 3:
                return new Lanche("X-Bacon", 22.00);
            default:
                return null;
        }
    }

    public Pedido registrarPedido(String nomeCliente) {
        return gerenciador.criarPedido(nomeCliente);
    }

    // Devolve false se o pedido ou a opção de lanche não existirem.
    public boolean adicionarItem(int numeroPedido, int opcaoLanche) {
        Pedido pedido = gerenciador.buscarPedido(numeroPedido);
        Lanche lanche = criarLanche(opcaoLanche);
        if (pedido == null || lanche == null) {
            return false;
        }
        pedido.adicionarItem(lanche);
        return true;
    }

    public Pedido buscarPedido(int numero) {
        return gerenciador.buscarPedido(numero);
    }

    public Collection<Pedido> listarPedidos() {
        return gerenciador.getPedidos();
    }

    public double calcularTotalDoDia() {
        return gerenciador.calcularTotalDoDia();
    }
} 