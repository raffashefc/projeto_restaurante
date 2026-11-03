package pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cardapio.ComBaconExtra;
import cardapio.ComCheddar;
import cardapio.Lanche;
import cardapio.LancheFactory;
import java.util.HashSet;
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

    @Test
    @DisplayName("Combinações repetidas contam uma vez só")
    void combinacoesRepetidasContamUmaVez() {
        // Arrange: X-Bacon (2 vezes), X-Bacon + cheddar e X-Salada sem tomate
        Pedido p1 = gerenciador.criarPedido("Ana");
        p1.adicionarItem(LancheFactory.xBacon());
        Pedido p2 = gerenciador.criarPedido("Bia");
        p2.adicionarItem(LancheFactory.xBacon());
        p2.adicionarItem(new ComCheddar(LancheFactory.xBacon()));
        Pedido p3 = gerenciador.criarPedido("Caio");
        Lanche semTomate = LancheFactory.xSalada();
        semTomate.setTomate(false);
        p3.adicionarItem(semTomate);

        // Act
        HashSet<String> combinacoes = gerenciador.getCombinacoesDoDia();

        // Assert
        assertEquals(3, combinacoes.size());
        assertTrue(combinacoes.contains("X-Bacon"));
        assertTrue(combinacoes.contains("X-Bacon + cheddar"));
        assertTrue(combinacoes.contains("X-Salada sem tomate"));
    }

    @Test
    @DisplayName("Sem pedidos, não há combinações")
    void semPedidosNaoHaCombinacoes() {
        assertEquals(0, gerenciador.getCombinacoesDoDia().size());
    }

    @Test
    @DisplayName("Pedido cancelado não conta nas combinações")
    void pedidoCanceladoNaoContaNasCombinacoes() {
        Pedido p1 = gerenciador.criarPedido("Ana");
        p1.adicionarItem(LancheFactory.xBurger());
        Pedido p2 = gerenciador.criarPedido("Bia");
        p2.adicionarItem(LancheFactory.xBacon());

        gerenciador.cancelarPedido(p1.getNumero());

        HashSet<String> combinacoes = gerenciador.getCombinacoesDoDia();
        assertEquals(1, combinacoes.size());
        assertTrue(combinacoes.contains("X-Bacon"));
    }

    @Test
    @DisplayName("Limitação: a ordem dos adicionais gera descrições diferentes")
    void ordemDosAdicionaisContaComoCombinacoesDiferentes() {
        Pedido p = gerenciador.criarPedido("Ana");
        p.adicionarItem(new ComBaconExtra(new ComCheddar(LancheFactory.xBacon())));
        p.adicionarItem(new ComCheddar(new ComBaconExtra(LancheFactory.xBacon())));

        // Mesmos adicionais e mesmo preço, mas o HashSet conta duas descrições
        assertEquals(2, gerenciador.getCombinacoesDoDia().size());
    }
}