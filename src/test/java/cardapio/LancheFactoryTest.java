package cardapio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes da LancheFactory")
class LancheFactoryTest {

    @Test
    @DisplayName("X-Burger: nome, preço e ingredientes")
    void xBurgerTemDadosCorretos() {
        Lanche lanche = LancheFactory.xBurger();

        assertEquals("X-Burger", lanche.getNome());
        assertEquals(16.00, lanche.getPreco(), 0.0001);
        assertEquals(List.of("pão", "hambúrguer", "queijo", "alface", "tomate"),
                lanche.getIngredientes());
    }

    @Test
    @DisplayName("X-Salada: nome, preço e ingredientes")
    void xSaladaTemDadosCorretos() {
        Lanche lanche = LancheFactory.xSalada();

        assertEquals("X-Salada", lanche.getNome());
        assertEquals(18.00, lanche.getPreco(), 0.0001);
        assertEquals(List.of("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"),
                lanche.getIngredientes());
    }

    @Test
    @DisplayName("X-Bacon: nome, preço e ingredientes")
    void xBaconTemDadosCorretos() {
        Lanche lanche = LancheFactory.xBacon();

        assertEquals("X-Bacon", lanche.getNome());
        assertEquals(22.00, lanche.getPreco(), 0.0001);
        assertEquals(List.of("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"),
                lanche.getIngredientes());
    }

    @Test
    @DisplayName("Duas chamadas devolvem objetos diferentes")
    void cadaChamadaCriaInstanciaNova() {
        Lanche primeiro = LancheFactory.xBacon();
        Lanche segundo = LancheFactory.xBacon();

        assertNotSame(primeiro, segundo);
    }

    @Test
    @DisplayName("Alterar um lanche não afeta o outro")
    void alterarUmLancheNaoAfetaOutro() {
        Lanche primeiro = LancheFactory.xBacon();
        Lanche segundo = LancheFactory.xBacon();

        primeiro.setPreco(99.00);

        assertEquals(99.00, primeiro.getPreco(), 0.0001);
        assertEquals(22.00, segundo.getPreco(), 0.0001);
    }
}