package app;

import java.util.List;
import pedido.Pedido;

public class Main {
    public static void main(String[] args) {
        RestauranteFacade sistema = new RestauranteFacade();
        MenuConsole menu = new MenuConsole();

        boolean rodando = true;
        while (rodando) {
            menu.exibirMenu();
            int opcao = menu.lerOpcao();

            switch (opcao) {
                case 1:
                    registrarPedido(sistema, menu);
                    break;
                case 2:
                    menu.exibirPedidos(sistema.listarPedidos());
                    break;
                case 3:
                    double total = sistema.calcularTotalDoDia();
                    menu.exibirFechamentoDoDia(total);
                    break;
                case 4:
                    modificarPedido(sistema, menu);
                    break;
                case 0:
                    rodando = false;
                    menu.exibirMensagem("Até logo!");
                    break;
                default:
                    menu.exibirMensagem("Opção inválida.");
            }
        }
        menu.fechar();
    }

    private static void registrarPedido(RestauranteFacade sistema, MenuConsole menu) {
        Pedido pedido;
        try {
            pedido = sistema.registrarPedido(menu.lerNomeCliente());
        } catch (IllegalArgumentException e) {
            menu.exibirMensagem("Erro: " + e.getMessage());
            return;
        }

        boolean adicionarMais = true;
        while (adicionarMais) {
            adicionarUmItem(sistema, menu, pedido);
            adicionarMais = menu.perguntarMaisItem();
        }

        menu.exibirPedido(pedido);
    }

    // Escolhe um lanche (repete até a opção ser válida), pergunta os ingredientes
    // a retirar e adiciona ao pedido. Usado ao criar e ao modificar um pedido.
    private static void adicionarUmItem(RestauranteFacade sistema, MenuConsole menu, Pedido pedido) {
        int escolha;
        while (true) {
            menu.exibirCardapio(sistema.getCardapio());
            escolha = menu.lerOpcaoLanche();
            if (sistema.opcaoLancheValida(escolha)) {
                break;
            }
            menu.exibirMensagem("Lanche inválido, tente de novo.");
        }

        List<String> retirar = menu.lerIngredientesParaRetirar();
        sistema.adicionarItem(pedido.getNumero(), escolha, retirar);
    }

    private static void modificarPedido(RestauranteFacade sistema, MenuConsole menu) {
        int numero = menu.lerNumeroPedido();
        Pedido pedido = sistema.buscarPedido(numero);
        if (pedido == null) {
            menu.exibirMensagem("Pedido não encontrado.");
            return;
        }
        menu.exibirPedido(pedido);

        boolean modificando = true;
        while (modificando) {
            menu.exibirMenuModificacao();
            int opcao = menu.lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarUmItem(sistema, menu, pedido);
                    menu.exibirPedido(pedido);
                    break;
                case 2:
                    removerItem(sistema, menu, pedido);
                    break;
                case 3:
                    if (menu.confirmarCancelamento(pedido.getNumero())) {
                        sistema.cancelarPedido(pedido.getNumero());
                        menu.exibirMensagem("Pedido #" + pedido.getNumero() + " cancelado.");
                        modificando = false;
                    }
                    break;
                case 0:
                    modificando = false;
                    break;
                default:
                    menu.exibirMensagem("Opção inválida.");
            }
        }
    }

    private static void removerItem(RestauranteFacade sistema, MenuConsole menu, Pedido pedido) {
        if (pedido.estaVazio()) {
            menu.exibirMensagem("O pedido não tem itens para remover.");
            return;
        }

        menu.exibirItensNumerados(pedido);
        int posicao = menu.lerNumeroItem();

        if (sistema.removerItem(pedido.getNumero(), posicao)) {
            menu.exibirMensagem("Item removido.");
            menu.exibirPedido(pedido);
        } else {
            menu.exibirMensagem("Item inválido.");
        }
    }
}