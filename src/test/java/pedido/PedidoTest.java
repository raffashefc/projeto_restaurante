package pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cardapio.ComBaconExtra;
import cardapio.ComCheddar;
import cardapio.ItemCardapio;
import cardapio.Lanche;
import cardapio.LancheFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe Pedido")
class PedidoTest {

    @Test
    @DisplayName("Total soma o preço de todos os itens")
    void totalSomaPrecosDosItens() {
        // Arrange
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(new Lanche("X-Burger", 16.00));
        pedido.adicionarItem(new Lanche("X-Bacon", 22.00));

        // Act
        double total = pedido.calcularTotal();

        // Assert
        assertEquals(38.00, total, 0.0001);
    }

    @Test
    @DisplayName("Pedido sem itens tem total zero")
    void pedidoSemItensTemTotalZero() {
        Pedido pedido = new Pedido(1, "Ana");

        assertEquals(0.0, pedido.calcularTotal(), 0.0001);
    }

    @Test
    @DisplayName("Cliente com nome vazio lança IllegalArgumentException")
    void clienteVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(1, "  "));
    }

    @Test
    @DisplayName("Pedido aceita qualquer ItemCardapio, não só Lanche")
    void pedidoAceitaQualquerItemCardapio() {
        // Arrange: um item que não é Lanche
        ItemCardapio extra = new ItemCardapio() {
            @Override
            public String getDescricao() {
                return "Item extra";
            }

            @Override
            public double getPreco() {
                return 3.00;
            }
        };
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(new Lanche("X-Burger", 16.00));
        pedido.adicionarItem(extra);

        // Act
        double total = pedido.calcularTotal();

        // Assert
        assertEquals(19.00, total, 0.0001);
    }

    @Test
    @DisplayName("Pedido soma o preço de um lanche com adicionais")
    void pedidoSomaLancheComAdicionais() {
        // Arrange
        ItemCardapio lanche = new ComBaconExtra(new ComCheddar(LancheFactory.xBacon()));
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(lanche);
        pedido.adicionarItem(LancheFactory.xBurger());

        // Act
        double total = pedido.calcularTotal();

        // Assert: 32,00 + 16,00
        assertEquals(48.00, total, 0.0001);
    }

    @Test
    @DisplayName("Remover um item atualiza a lista e o total")
    void removerItemAtualizaTotal() {
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(LancheFactory.xBurger());   // 16,00
        pedido.adicionarItem(LancheFactory.xBacon());    // 22,00

        pedido.removerItem(0);

        assertEquals(1, pedido.getItens().size());
        assertEquals("X-Bacon", pedido.getItens().get(0).getDescricao());
        assertEquals(22.00, pedido.calcularTotal(), 0.0001);
    }

    @Test
    @DisplayName("Remover posição inexistente lança IllegalArgumentException")
    void removerPosicaoInexistenteLancaExcecao() {
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(LancheFactory.xBurger());

        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(1));
        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(-1));
    }

    @Test
    @DisplayName("Trocar item: remove o X-Bacon e acrescenta um X-Bacon sem cebola")
    void trocarItemRemoveEAcrescenta() {
        // Arrange
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(LancheFactory.xBacon());

        // Act: remove o item anterior e acrescenta o novo, personalizado
        pedido.removerItem(0);
        Lanche semCebola = LancheFactory.xBacon();
        semCebola.setCebola(false);
        pedido.adicionarItem(semCebola);

        // Assert
        assertEquals(1, pedido.getItens().size());
        assertEquals("X-Bacon sem cebola", pedido.getItens().get(0).getDescricao());
        assertEquals(22.00, pedido.calcularTotal(), 0.0001);
    }

    @Test
    @DisplayName("Remover todos os itens deixa o pedido vazio e com total zero")
    void removerTodosOsItensZeraTotal() {
        Pedido pedido = new Pedido(1, "Ana");
        pedido.adicionarItem(LancheFactory.xBurger());
        assertFalse(pedido.estaVazio());

        pedido.removerItem(0);

        assertTrue(pedido.estaVazio());
        assertEquals(0.0, pedido.calcularTotal(), 0.0001);
    }
}