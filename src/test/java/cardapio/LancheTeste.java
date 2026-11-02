package cardapio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da classe Lanche")
class LancheTest {

    @Test
    @DisplayName("Lanche válido guarda nome e preço")
    void criaLancheValido() {
        // Arrange + Act
        Lanche lanche = new Lanche("X-Burger", 16.00);

        // Assert
        assertEquals("X-Burger", lanche.getNome());
        assertEquals(16.00, lanche.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("Nome vazio lança IllegalArgumentException")
    void nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Lanche("   ", 10.00));
    }

    @Test
    @DisplayName("Nome nulo lança IllegalArgumentException")
    void nomeNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Lanche(null, 10.00));
    }

    @Test
    @DisplayName("Preço negativo lança IllegalArgumentException")
    void precoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Lanche("X-Burger", -1.00));
    }

    @Test
    @DisplayName("Lanche é um ItemCardapio e a descrição é o nome")
    void lancheImplementaItemCardapio() {
        ItemCardapio item = new Lanche("X-Salada", 18.00);

        assertEquals("X-Salada", item.getDescricao());
        assertEquals(18.00, item.getPreco(), 0.0001);
    }
}