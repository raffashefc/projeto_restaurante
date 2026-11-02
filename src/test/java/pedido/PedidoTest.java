package pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cardapio.ItemCardapio;
import cardapio.Lanche;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe Pedido")
class Pedidotest {

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
}