package app;

import cardapio.ItemCardapio;
import cardapio.Lanche;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import pedido.Pedido;

public class MenuConsole {
    private static final Locale BR = Locale.forLanguageTag("pt-BR");
    private Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        System.out.println();
        System.out.println("=== RESTAURANTE ===");
        System.out.println("1 - Registrar novo pedido");
        System.out.println("2 - Listar pedidos do dia");
        System.out.println("3 - Fechar restaurante");
        System.out.println("4 - Modificar pedido");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
    }

    public int lerOpcao() {
        return lerInteiro();
    }

    public String lerNomeCliente() {
        System.out.print("Nome do cliente: ");
        return scanner.nextLine();
    }

    public void exibirCardapio(List<Lanche> cardapio) {
        System.out.println();
        System.out.println("--- Cardápio ---");
        for (int i = 0; i < cardapio.size(); i++) {
            Lanche l = cardapio.get(i);
            System.out.println((i + 1) + " - " + l.getNome() + " - " + formatarPreco(l.getPreco()));
            System.out.println("      " + String.join(", ", l.getIngredientes()));
        }
    }

    public int lerOpcaoLanche() {
        System.out.print("Escolha o lanche: ");
        return lerInteiro();
    }

    public boolean perguntarMaisItem() {
        return lerSimNao("Deseja adicionar mais um item? (s/n): ");
    }

    // Pergunta quais ingredientes retirar e devolve os nomes escolhidos.
    public List<String> lerIngredientesParaRetirar() {
        List<String> retirar = new ArrayList<>();
        String[] ingredientes = {"queijo", "alface", "tomate", "cebola"};

        System.out.println("Quer retirar algum ingrediente? (o preço não muda)");
        for (String ingrediente : ingredientes) {
            if (lerSimNao("  Retirar " + ingrediente + "? (s/n): ")) {
                retirar.add(ingrediente);
            }
        }
        return retirar;
    }

    private boolean lerSimNao(String pergunta) {
        while (true) {
            System.out.print(pergunta);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Responda com 's' ou 'n'.");
        }
    }

    public void exibirPedido(Pedido pedido) {
        System.out.println();
        System.out.println("Pedido #" + pedido.getNumero() + " - Cliente: " + pedido.getNomeCliente());
        for (ItemCardapio item : pedido.getItens()) {
            System.out.println("  - " + item.getDescricao() + " - " + formatarPreco(item.getPreco()));
        }
        System.out.println("  Total: " + formatarPreco(pedido.calcularTotal()));
    }

    public void exibirPedidos(Collection<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado hoje.");
            return;
        }
        System.out.println();
        System.out.println("--- Pedidos do dia ---");
        for (Pedido p : pedidos) {
            exibirPedido(p);
        }
    }

    public void exibirFechamentoDoDia(double total, Set<String> combinacoes) {
        System.out.println();
        System.out.println("=== FECHAMENTO DO DIA ===");
        System.out.println("Total arrecadado: " + formatarPreco(total));
        System.out.println("Combinações diferentes pedidas: " + combinacoes.size());
        // O HashSet não tem ordem; ordenamos só para exibir sempre na mesma sequência.
        for (String combinacao : new TreeSet<>(combinacoes)) {
            System.out.println("  - " + combinacao);
        }
    }

    public int lerNumeroPedido() {
        System.out.print("Número do pedido: ");
        return lerInteiro();
    }

    public void exibirMenuModificacao() {
        System.out.println();
        System.out.println("--- Modificar pedido ---");
        System.out.println("1 - Acrescentar item");
        System.out.println("2 - Remover item");
        System.out.println("3 - Cancelar pedido inteiro");
        System.out.println("0 - Voltar");
        System.out.print("Opção: ");
    }

    public void exibirItensNumerados(Pedido pedido) {
        System.out.println();
        System.out.println("Itens do pedido #" + pedido.getNumero() + ":");
        int posicao = 1;
        for (ItemCardapio item : pedido.getItens()) {
            System.out.println("  " + posicao + " - " + item.getDescricao() + " - " + formatarPreco(item.getPreco()));
            posicao++;
        }
    }

    public int lerNumeroItem() {
        System.out.print("Número do item a remover: ");
        return lerInteiro();
    }

    public boolean confirmarCancelamento(int numeroPedido) {
        return lerSimNao("Cancelar o pedido #" + numeroPedido + " inteiro? (s/n): ");
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void fechar() {
        scanner.close();
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String formatarPreco(double valor) {
        return String.format(BR, "R$ %.2f", valor);
    }
}