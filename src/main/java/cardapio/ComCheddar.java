package cardapio;

public class ComCheddar extends AdicionalDecorator {
    private static final double PRECO = 4.00;

    public ComCheddar(ItemCardapio item) {
        super(item);
    }

    @Override
    public String getDescricao() {
        return item.getDescricao() + " + cheddar";
    }

    @Override
    public double getPreco() {
        return item.getPreco() + PRECO;
    }
}