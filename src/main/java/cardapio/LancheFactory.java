package cardapio;

import java.util.Arrays;

public class LancheFactory {

    // Classe utilitária: só tem métodos estáticos, ninguém precisa criar uma LancheFactory.
    private LancheFactory() {
    }

    // Cada método cria uma instância NOVA de Lanche a cada chamada.

    public static Lanche xBurger() {
        return new Lanche("X-Burger", 16.00,
                Arrays.asList("pão", "hambúrguer", "queijo", "alface", "tomate"));
    }

    public static Lanche xSalada() {
        return new Lanche("X-Salada", 18.00,
                Arrays.asList("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"));
    }

    public static Lanche xBacon() {
        return new Lanche("X-Bacon", 22.00,
                Arrays.asList("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"));
    }
}