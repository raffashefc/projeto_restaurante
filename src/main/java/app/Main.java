package app;

import pedido.Pedido;

public class Main {
    public static void main(String[] args) {
        Restaurante sistema = new Restaurante();
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

    private static void registrarPedido(Restaurante sistema, MenuConsole menu) {
        Pedido pedido;
        try {
            pedido = sistema.registrarPedido(menu.lerNomeCliente());
        } catch (IllegalArgumentException e) {
            menu.exibirMensagem("Erro: " + e.getMessage());
            return;
        }

        boolean adicionarMais = true;
        while (adicionarMais) {
            menu.exibirCardapio(sistema.getCardapio());
            int escolha = menu.lerOpcaoLanche();

            if (sistema.adicionarItem(pedido.getNumero(), escolha)) {
                adicionarMais = menu.perguntarMaisItem();
            } else {
                menu.exibirMensagem("Lanche inválido, tente de novo.");
            }
        }

        menu.exibirPedido(pedido);
    }
}