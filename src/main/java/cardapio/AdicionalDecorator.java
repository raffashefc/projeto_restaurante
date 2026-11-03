package cardapio;

// Base dos adicionais: é um ItemCardapio e guarda dentro de si outro ItemCardapio.
// Por implementar a mesma interface, um adicional pode embrulhar um Lanche
// ou outro adicional.
public abstract class AdicionalDecorator implements ItemCardapio {
    protected final ItemCardapio item;

    protected AdicionalDecorator(ItemCardapio item) {
        if (item == null) {
            throw new IllegalArgumentException("O item embrulhado não pode ser nulo.");
        }
        this.item = item;
    }
}