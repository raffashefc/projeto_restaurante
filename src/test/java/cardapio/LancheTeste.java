package cardapio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("Lanche novo vem com todos os ingredientes (descrição = nome)")
    void lancheNovoVemComTodosOsIngredientes() {
        Lanche lanche = new Lanche("X-Burger", 16.00);

        assertTrue(lanche.isQueijo());
        assertTrue(lanche.isAlface());
        assertTrue(lanche.isTomate());
        assertTrue(lanche.isCebola());
        assertEquals("X-Burger", lanche.getDescricao());
    }

    @Test
    @DisplayName("Retirar cebola acrescenta 'sem cebola' à descrição")
    void retirarCebolaMudaDescricao() {
        Lanche lanche = LancheFactory.xBacon();

        lanche.setCebola(false);

        assertEquals("X-Bacon sem cebola", lanche.getDescricao());
    }

    @Test
    @DisplayName("Vários ingredientes retirados aparecem na ordem queijo, alface, tomate, cebola")
    void variosIngredientesRetirados() {
        Lanche lanche = LancheFactory.xBacon();

        lanche.setCebola(false);
        lanche.setQueijo(false);
        lanche.setTomate(false);

        assertEquals("X-Bacon sem queijo sem tomate sem cebola", lanche.getDescricao());
    }

    @Test
    @DisplayName("Retirar ingrediente não muda o preço")
    void retirarIngredienteNaoMudaPreco() {
        Lanche lanche = LancheFactory.xBacon();

        lanche.setCebola(false);
        lanche.setAlface(false);

        assertEquals(22.00, lanche.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("Retirar ingrediente que o lanche não tinha também funciona")
    void retirarIngredienteQueNaoTinha() {
        Lanche lanche = LancheFactory.xBurger();

        lanche.setCebola(false);

        assertEquals("X-Burger sem cebola", lanche.getDescricao());
        assertEquals(16.00, lanche.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("Personalizar um lanche não afeta o outro")
    void personalizarUmNaoAfetaOutro() {
        Lanche primeiro = LancheFactory.xBacon();
        Lanche segundo = LancheFactory.xBacon();

        primeiro.setCebola(false);

        assertEquals("X-Bacon sem cebola", primeiro.getDescricao());
        assertEquals("X-Bacon", segundo.getDescricao());
    }
}