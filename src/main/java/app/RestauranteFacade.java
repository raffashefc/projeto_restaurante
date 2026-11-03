package app;

import cardapio.Lanche;
import cardapio.LancheFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import pedido.GerenciadorPedidos;
import pedido.Pedido;

public class RestauranteFacade {
    private GerenciadorPedidos gerenciador = new GerenciadorPedidos();

    // Devolve objetos novos a cada chamada: nenhum Lanche é compartilhado entre pedidos.
    public List<Lanche> getCardapio() {
        List<Lanche> cardapio = new ArrayList<>();
        cardapio.add(criarLanche(1));
        cardapio.add(criarLanche(2));
        cardapio.add(criarLanche(3));
        return cardapio;
    }

    // Pede o lanche à factory (sempre uma instância nova). Devolve null se a opção não existir.
    public Lanche criarLanche(int opcao) {
        switch (opcao) {
            case 1:
                return LancheFactory.xBurger();
            case 2:
                return LancheFactory.xSalada();
            case 3:
                return LancheFactory.xBacon();
            default:
                return null;
        }
    }

    public Pedido registrarPedido(String nomeCliente) {
        return gerenciador.criarPedido(nomeCliente);
    }

    public boolean opcaoLancheValida(int opcao) {
        return criarLanche(opcao) != null;
    }

    // Adiciona o lanche sem personalização.
    public boolean adicionarItem(int numeroPedido, int opcaoLanche) {
        return adicionarItem(numeroPedido, opcaoLanche, new ArrayList<>());
    }

    // Cria um lanche NOVO, retira os ingredientes pedidos e adiciona ao pedido.
    // Devolve false se o pedido ou a opção de lanche não existirem.
    public boolean adicionarItem(int numeroPedido, int opcaoLanche, List<String> ingredientesARetirar) {
        Pedido pedido = gerenciador.buscarPedido(numeroPedido);
        Lanche lanche = criarLanche(opcaoLanche);
        if (pedido == null || lanche == null) {
            return false;
        }
        retirarIngredientes(lanche, ingredientesARetirar);
        pedido.adicionarItem(lanche);
        return true;
    }

    private void retirarIngredientes(Lanche lanche, List<String> ingredientesARetirar) {
        if (ingredientesARetirar == null) {
            return;
        }
        for (String ingrediente : ingredientesARetirar) {
            switch (ingrediente) {
                case "queijo":
                    lanche.setQueijo(false);
                    break;
                case "alface":
                    lanche.setAlface(false);
                    break;
                case "tomate":
                    lanche.setTomate(false);
                    break;
                case "cebola":
                    lanche.setCebola(false);
                    break;
                default:
                    break;
            }
        }
    }

    // A posição é a que a pessoa viu na lista numerada (começa em 1).
    // Devolve false se o pedido ou a posição não existirem.
    public boolean removerItem(int numeroPedido, int posicao) {
        Pedido pedido = gerenciador.buscarPedido(numeroPedido);
        if (pedido == null || posicao < 1 || posicao > pedido.getItens().size()) {
            return false;
        }
        pedido.removerItem(posicao - 1);
        return true;
    }

    public boolean cancelarPedido(int numeroPedido) {
        return gerenciador.cancelarPedido(numeroPedido);
    }

    public Pedido buscarPedido(int numero) {
        return gerenciador.buscarPedido(numero);
    }

    public Collection<Pedido> listarPedidos() {
        return gerenciador.getPedidos();
    }

    public Set<String> getCombinacoesDoDia() {
        return gerenciador.getCombinacoesDoDia();
    }

    public double calcularTotalDoDia() {
        return gerenciador.calcularTotalDoDia();
    }
}