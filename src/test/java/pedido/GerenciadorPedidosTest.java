package pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cardapio.Lanche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe GerenciadorPedidos")
class GerenciadorPedidosTest {

    private GerenciadorPedidos gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorPedidos();
    }

    @Test
    @DisplayName("Números dos pedidos são sequenciais e nunca se repetem")
    void numerosSaoSequenciais() {
        Pedido p1 = gerenciador.criarPedido("Ana");
        Pedido p2 = gerenciador.criarPedido("Bia");

        assertEquals(1, p1.getNumero());
        assertEquals(2, p2.getNumero());
    }

    @Test
    @DisplayName("Buscar pedido inexistente devolve null")
    void buscarInexistenteDevolveNull() {
        assertNull(gerenciador.buscarPedido(99));
    }

    @Test
    @DisplayName("Total do dia soma o total de todos os pedidos")
    void totalDoDiaSomaTodosOsPedidos() {
        // Arrange
        Pedido p1 = gerenciador.criarPedido("Ana");
        p1.adicionarItem(new Lanche("X-Burger", 16.00));
        Pedido p2 = gerenciador.criarPedido("Bia");
        p2.adicionarItem(new Lanche("X-Salada", 18.00));
        p2.adicionarItem(new Lanche("X-Bacon", 22.00));

        // Act
        double total = gerenciador.calcularTotalDoDia();

        // Assert
        assertEquals(56.00, total, 0.0001);
    }

    @Test
    @DisplayName("Cancelar pedido remove o pedido e o tira do total do dia")
    void cancelarPedidoTiraDoTotalDoDia() {
        // Arrange
        Pedido p1 = gerenciador.criarPedido("Ana");
        p1.adicionarItem(new Lanche("X-Burger", 16.00));
        Pedido p2 = gerenciador.criarPedido("Bia");
        p2.adicionarItem(new Lanche("X-Bacon", 22.00));

        // Act
        boolean cancelou = gerenciador.cancelarPedido(p1.getNumero());

        // Assert
        assertTrue(cancelou);
        assertNull(gerenciador.buscarPedido(p1.getNumero()));
        assertEquals(22.00, gerenciador.calcularTotalDoDia(), 0.0001);
    }

    @Test
    @DisplayName("Cancelar pedido inexistente devolve false")
    void cancelarPedidoInexistenteDevolveFalse() {
        assertFalse(gerenciador.cancelarPedido(99));
    }

    @Test
    @DisplayName("Número de um pedido cancelado não é reutilizado")
    void numeroDePedidoCanceladoNaoEhReutilizado() {
        Pedido p1 = gerenciador.criarPedido("Ana");
        gerenciador.cancelarPedido(p1.getNumero());

        Pedido p2 = gerenciador.criarPedido("Bia");

        assertEquals(2, p2.getNumero());
    }
}