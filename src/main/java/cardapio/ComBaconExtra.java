package cardapio;

public class ComBaconExtra extends AdicionalDecorator {
    private static final double PRECO = 6.00;

    public ComBaconExtra(ItemCardapio item) {
        super(item);
    }

    @Override
    public String getDescricao() {
        return item.getDescricao() + " + bacon extra";
    }

    @Override
    public double getPreco() {
        return item.getPreco() + PRECO;
    }
}