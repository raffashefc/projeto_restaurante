package cardapio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes dos adicionais (Decorator)")
class AdicionaisTest {

    @Test
    @DisplayName("ComCheddar acrescenta '+ cheddar' e R$ 4,00")
    void comCheddarAcrescentaDescricaoEPreco() {
        ItemCardapio item = new ComCheddar(LancheFactory.xBurger());

        assertEquals("X-Burger + cheddar", item.getDescricao());
        assertEquals(20.00, item.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("ComBaconExtra acrescenta '+ bacon extra' e R$ 6,00")
    void comBaconExtraAcrescentaDescricaoEPreco() {
        ItemCardapio item = new ComBaconExtra(LancheFactory.xBurger());

        assertEquals("X-Burger + bacon extra", item.getDescricao());
        assertEquals(22.00, item.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("Adicionais empilhados: X-Bacon + cheddar + bacon extra = R$ 32,00")
    void adicionaisEmpilhadosSomamDescricaoEPreco() {
        ItemCardapio lanche = LancheFactory.xBacon();
        lanche = new ComCheddar(lanche);
        lanche = new ComBaconExtra(lanche);

        assertEquals("X-Bacon + cheddar + bacon extra", lanche.getDescricao());
        assertEquals(32.00, lanche.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("A ordem dos adicionais muda a descrição, mas não o preço")
    void ordemMudaDescricaoMasNaoPreco() {
        ItemCardapio lanche = LancheFactory.xBacon();
        lanche = new ComBaconExtra(lanche);
        lanche = new ComCheddar(lanche);

        assertEquals("X-Bacon + bacon extra + cheddar", lanche.getDescricao());
        assertEquals(32.00, lanche.getPreco(), 0.0001);
    }

    @Test
    @DisplayName("Embrulhar não altera o lanche original")
    void embrulharNaoAlteraLancheOriginal() {
        Lanche original = LancheFactory.xBacon();

        ItemCardapio comAdicional = new ComCheddar(original);

        assertEquals(26.00, comAdicional.getPreco(), 0.0001);
        assertEquals(22.00, original.getPreco(), 0.0001);
        assertEquals("X-Bacon", original.getDescricao());
    }

    @Test
    @DisplayName("Embrulhar item nulo lança IllegalArgumentException")
    void embrulharNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new ComCheddar(null));
        assertThrows(IllegalArgumentException.class, () -> new ComBaconExtra(null));
    }
}