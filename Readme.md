### Projeto 1: Sistema de Restaurante (Níveis 1 e 2)

### Objetivo

Construir o sistema de um restaurante em Java, com cardápio, pedidos e fechamento do dia, organizado em pacotes e com cada classe cuidando de uma responsabilidade.

- **Nível 1:** cardápio, pedidos e fechamento do dia.
- **Nível 2 (até aqui):** interface `ItemCardapio` e `LancheFactory`.

---

### 1. Como organizei os pacotes e por quê

```
projeto_restaurante/
├── pom.xml
└── src/
    ├── main/java/
    │   ├── app/
    │   │   ├── Main.java
    │   │   ├── MenuConsole.java
    │   │   └── Restaurante.java
    │   ├── cardapio/
    │   │   ├── ItemCardapio.java
    │   │   ├── Lanche.java
    │   │   └── LancheFactory.java
    │   └── pedido/
    │       ├── Pedido.java
    │       └── GerenciadorPedidos.java
    └── test/java/
        ├── cardapio/
        │   ├── LancheTest.java
        │   └── LancheFactoryTest.java
        └── pedido/
            ├── PedidoTest.java
            └── GerenciadorPedidosTest.java
```

| Pacote | O que guarda | Por que ali |
|---|---|---|
| `app` | `Main`, `MenuConsole`, `Restaurante` | É a camada que conversa com a pessoa e coordena as operações. Tudo que mexe com tela e fluxo fica junto. |
| `cardapio` | `ItemCardapio`, `Lanche`, `LancheFactory` | É o "produto" do restaurante: o que se vende e como é criado. Não depende de nenhum outro pacote. |
| `pedido` | `Pedido`, `GerenciadorPedidos` | Tudo que trata de comandas: uma comanda (`Pedido`) e o conjunto delas (`GerenciadorPedidos`). |
| `financeiro` e `util` | (ainda vazios) | Só serão criados quando houver classe para colocar neles. O Git não versiona pasta vazia. |

**A regra que guiou a divisão:** cada parte tem uma responsabilidade, e as dependências vão sempre em uma direção só.

```
app  ──►  pedido  ──►  cardapio
```

- `cardapio` não conhece ninguém.
- `pedido` conhece `cardapio` (um pedido tem itens do cardápio), mas não conhece `app`.
- `app` conhece os dois e é a única camada que imprime e lê dados.

Com isso, dá para trocar o console por outra interface (por exemplo, uma tela gráfica) mexendo só em `app`, sem tocar em `pedido` nem `cardapio`. Os testes ficam em `src/test/java` com os mesmos nomes de pacote das classes que testam.

### Responsabilidade de cada classe

| Classe | Responsabilidade |
|---|---|
| `ItemCardapio` | Contrato do que pode entrar em um pedido: tem descrição e preço |
| `Lanche` | Guarda nome, preço e ingredientes, com validação nos setters. Implementa `ItemCardapio` |
| `LancheFactory` | Cria os lanches padrão do cardápio (X-Burger, X-Salada, X-Bacon) |
| `Pedido` | Cuida de **uma** comanda: número, cliente e lista de `ItemCardapio` |
| `GerenciadorPedidos` | Guarda **todos** os pedidos (`HashMap`), gera o número e soma o total do dia |
| `Restaurante` | Coordena as operações e pede os lanches à factory |
| `MenuConsole` | Toda a leitura (`Scanner`) e exibição (`System.out`) |
| `Main` | Controla o fluxo principal (loop e `switch`), sem cálculo nem impressão |

`Pedido` e `GerenciadorPedidos` são classes separadas de propósito (o S do SOLID): o pedido não sabe quantos pedidos existem, e o gerenciador não sabe calcular o total de uma comanda.

---

### 2. Regras implementadas

- **Lanche:** o nome não pode ser nulo nem vazio, e o preço não pode ser negativo. O construtor reutiliza os setters, então a validação fica em um lugar só. Valor inválido lança `IllegalArgumentException`. A lista de ingredientes é copiada na criação, então dois lanches nunca compartilham a mesma lista.
- **Pedido:** o nome do cliente não pode ser nulo nem vazio. O número chega pronto pelo construtor. `adicionarItem` recebe um `ItemCardapio` (não só `Lanche`). `calcularTotal()` percorre a lista com `for`.
- **GerenciadorPedidos:** `criarPedido` gera o número com `contadorPedidos++`. O usuário nunca digita o número de um pedido novo, então a numeração não se repete (o `HashMap` não recusa chave repetida, ele **substitui**).
- **Lanche novo a cada escolha:** cada método da `LancheFactory` faz `new Lanche(...)` a cada chamada. Dois clientes que pedem X-Bacon recebem objetos diferentes, e alterar um não muda o outro.

### Cardápio

| Opção | Lanche | Preço | Ingredientes |
|---|---|---|---|
| 1 | X-Burger | R$ 16,00 | pão, hambúrguer, queijo, alface, tomate |
| 2 | X-Salada | R$ 18,00 | pão, hambúrguer, queijo, alface, tomate, cebola |
| 3 | X-Bacon | R$ 22,00 | pão, hambúrguer, queijo, bacon, alface, tomate, cebola |

---

### 3. Um pedido do começo ao fim

Execução real do sistema (Nível 1), com a classe responsável por cada passo:

```
=== RESTAURANTE ===
1 - Registrar novo pedido
2 - Listar pedidos do dia
3 - Fechar restaurante
0 - Sair
Opção: 1
Nome do cliente: Brianna

--- Cardápio ---
1 - X-Burger - R$ 16,00
2 - X-Salada - R$ 18,00
3 - X-Bacon - R$ 22,00
Escolha o lanche: 3
Deseja adicionar mais um item? (s/n): s

--- Cardápio ---
1 - X-Burger - R$ 16,00
2 - X-Salada - R$ 18,00
3 - X-Bacon - R$ 22,00
Escolha o lanche: 2
Deseja adicionar mais um item? (s/n): n

Pedido #1 - Cliente: Brianna
  - X-Bacon - R$ 22,00
  - X-Salada - R$ 18,00
  Total: R$ 40,00
```

Depois, listar e fechar o dia:

```
Opção: 2

--- Pedidos do dia ---

Pedido #1 - Cliente: Brianna
  - X-Bacon - R$ 22,00
  - X-Salada - R$ 18,00
  Total: R$ 40,00

Opção: 3

=== FECHAMENTO DO DIA ===
Total arrecadado: R$ 40,00

Opção: 0
Até logo!
```

![saida cli](/src/asset/image1.png)
![saida cli](/src/asset/image2.png)

**A partir do Nível 2**, o cardápio também mostra os ingredientes de cada lanche:

```
--- Cardápio ---
1 - X-Burger - R$ 16,00
      pão, hambúrguer, queijo, alface, tomate
2 - X-Salada - R$ 18,00
      pão, hambúrguer, queijo, alface, tomate, cebola
3 - X-Bacon - R$ 22,00
      pão, hambúrguer, queijo, bacon, alface, tomate, cebola
```

**O que cada classe fez nesse caminho:**

| Passo | Quem executa |
|---|---|
| Mostrar o menu e ler a opção `1` | `MenuConsole` (`exibirMenu`, `lerOpcao`) |
| Ler o nome "Brianna" | `MenuConsole` (`lerNomeCliente`) |
| Criar o pedido e dar o número `#1` | `Restaurante` → `GerenciadorPedidos.criarPedido` → `new Pedido(1, "Brianna")` |
| Mostrar o cardápio e ler a escolha `3` | `MenuConsole` (`exibirCardapio`, `lerOpcaoLanche`) |
| Criar o X-Bacon e colocar no pedido | `Restaurante.adicionarItem` → `criarLanche(3)` → `LancheFactory.xBacon()` → `Pedido.adicionarItem` |
| Perguntar "mais um item?" | `MenuConsole` (`perguntarMaisItem`) |
| Repetir com a escolha `2` (X-Salada) | Mesmo caminho, com um `Lanche` novo vindo da factory |
| Calcular R$ 40,00 | `Pedido.calcularTotal` (22 + 18) |
| Mostrar o pedido | `MenuConsole.exibirPedido` |
| Fechamento do dia | `GerenciadorPedidos.calcularTotalDoDia` → `MenuConsole.exibirFechamentoDoDia` |

Repare que o `Main` só repassa o que o `MenuConsole` leu para a `Restaurante`, e o que ela devolve para o `MenuConsole` exibir.

---

### 4. Padrão de projeto: Facade

**Onde está:** `Restaurante`, no pacote `app`.

**O problema que resolveu:** para registrar um item em um pedido, é preciso fazer várias coisas em ordem: achar o pedido no `GerenciadorPedidos`, pedir o `Lanche` certo e colocá-lo no `Pedido`. Sem a fachada, o `Main` teria que conhecer todas essas classes e coordenar esse passo a passo:

```java
// Sem a fachada: o Main precisa conhecer tudo
GerenciadorPedidos gerenciador = new GerenciadorPedidos();
Pedido pedido = gerenciador.buscarPedido(numero);
Lanche lanche;
switch (escolha) {
    case 1: lanche = LancheFactory.xBurger(); break;
    case 2: lanche = LancheFactory.xSalada(); break;
    case 3: lanche = LancheFactory.xBacon(); break;
    default: lanche = null;
}
pedido.adicionarItem(lanche);
```

Isso mistura fluxo do programa com regras de negócio, e qualquer mudança (um lanche novo, outra forma de guardar os pedidos) obrigaria a mexer no `Main`.

**Com a fachada**, o `Main` conhece um objeto só e pede o que quer:

```java
// Com a fachada: uma chamada
sistema.adicionarItem(pedido.getNumero(), escolha);
```

Dentro da `Restaurante`, o passo a passo fica escondido:

```java
public boolean adicionarItem(int numeroPedido, int opcaoLanche) {
    Pedido pedido = gerenciador.buscarPedido(numeroPedido);
    Lanche lanche = criarLanche(opcaoLanche);
    if (pedido == null || lanche == null) {
        return false;
    }
    pedido.adicionarItem(lanche);
    return true;
}
```

**Como ela segue o padrão:** guarda a classe envolvida em um atributo (`GerenciadorPedidos gerenciador`), não precisa implementar interface e não proíbe usar as outras classes diretamente. Só oferece um caminho mais simples.

---

### 5. Padrão de projeto: Factory (Nível 2)

**Onde está:** `LancheFactory`, no pacote `cardapio`.

**O problema que resolveu:** no Nível 1, a `Restaurante` criava os lanches diretamente, com nome e preço escritos no código:

```java
case 1: return new Lanche("X-Burger", 16.00);
case 2: return new Lanche("X-Salada", 18.00);
case 3: return new Lanche("X-Bacon", 22.00);
```

Com isso, a fachada precisava saber **como** cada lanche é construído (preço, ingredientes). Qualquer mudança no cardápio obrigava a mexer em uma classe que deveria só coordenar, e esse conhecimento podia se espalhar por outras partes do programa.

**Com a factory**, essa decisão fica em um único lugar:

```java
public class LancheFactory {
    private LancheFactory() { }

    public static Lanche xBurger() {
        return new Lanche("X-Burger", 16.00,
                Arrays.asList("pão", "hambúrguer", "queijo", "alface", "tomate"));
    }

    public static Lanche xSalada() { /* ... */ }

    public static Lanche xBacon() { /* ... */ }
}
```

E a `Restaurante` só pede o lanche, sem saber como ele é construído:

```java
case 1: return LancheFactory.xBurger();
case 2: return LancheFactory.xSalada();
case 3: return LancheFactory.xBacon();
```

**Por que uma instância nova a cada chamada:** se duas pessoas pedirem X-Bacon, elas recebem objetos diferentes. Isso será importante na personalização: se um cliente tirar a cebola do lanche dele, o lanche de outro cliente não pode mudar.

**Construtor privado:** a classe só tem métodos estáticos, então ninguém precisa criar uma `LancheFactory`.

### 6. A interface ItemCardapio (Nível 2)

```java
public interface ItemCardapio {
    String getDescricao();
    double getPreco();
}
```

`Lanche` implementa a interface, e o `Pedido` passou a guardar `ArrayList<ItemCardapio>` e a receber `ItemCardapio` em `adicionarItem`. Assim o pedido depende do contrato, e não da classe concreta: qualquer coisa com descrição e preço pode entrar na comanda, o que será necessário para os adicionais.

---

### 7. Testes com JUnit

Foram escritos 17 testes em `src/test/java`, seguindo o padrão AAA (Arrange, Act, Assert):

| Classe de teste | Testes |
|---|---|
| `LancheTest` (5) | lanche válido guarda nome e preço; nome vazio lança exceção; nome nulo lança exceção; preço negativo lança exceção; lanche é um `ItemCardapio` e a descrição é o nome |
| `LancheFactoryTest` (5) | nome, preço e ingredientes de cada lanche (3 testes); duas chamadas devolvem objetos diferentes; alterar um lanche não afeta o outro |
| `PedidoTest` (4) | total soma o preço dos itens; pedido sem itens tem total zero; cliente vazio lança exceção; pedido aceita qualquer `ItemCardapio` |
| `GerenciadorPedidosTest` (3) | números sequenciais e sem repetição; buscar pedido inexistente devolve `null`; total do dia soma todos os pedidos |

Exemplo de teste:

```java
@Test
@DisplayName("Alterar um lanche não afeta o outro")
void alterarUmLancheNaoAfetaOutro() {
    Lanche primeiro = LancheFactory.xBacon();
    Lanche segundo = LancheFactory.xBacon();

    primeiro.setPreco(99.00);

    assertEquals(99.00, primeiro.getPreco(), 0.0001);
    assertEquals(22.00, segundo.getPreco(), 0.0001);
}
```

**Como rodar** (na pasta `projeto_restaurante`):

```
mvn test
```

**Saída esperada** quando tudo passa (a ordem e os tempos variam):

```
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in cardapio.LancheFactoryTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in cardapio.LancheTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 -- in pedido.GerenciadorPedidosTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 -- in pedido.PedidoTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Print da execução:**

![saida cli](/src/asset/mv-teste.png)

---

### 8. Como rodar o sistema

Pré-requisitos: JDK 17 ou superior e Maven (`sudo apt install maven`).

```
mvn compile
java -cp target/classes app.Main
```

---

### 9. Problemas que apareceram no caminho

| Problema | Causa | Solução |
|---|---|---|
| `mvn: command not found` | Maven não estava instalado | `sudo apt install maven` |
| `No sources to compile` | Os `.java` estavam soltos na raiz do projeto | Mover para `src/main/java/<pacote>/` |
| `class, interface, enum, or record expected` na última linha | Um ` {` sobrando depois do `}` que fecha a classe | Deixar só `}` na última linha |
| `ClassNotFoundException: app.Main` | A compilação falhou, então `target/classes` não existia | Corrigir os erros e rodar `mvn compile` de novo |
| `The build file has been changed and may need reload` | O VS Code detectou mudança no `pom.xml` | Clicar em **Yes** ou rodar **Java: Reload Projects** |

---

### 10. O que foi aprendido

- Organizar o projeto em pacotes com dependências em uma direção só.
- Centralizar a validação nos setters e reutilizá-los no construtor.
- Separar a classe que cuida de **um** pedido da que guarda **todos** (o S do SOLID).
- Usar a fachada para o `Main` não precisar conhecer as classes de negócio.
- Usar a factory para concentrar a criação dos lanches e garantir uma instância nova a cada pedido.
- Depender de uma interface (`ItemCardapio`) em vez de uma classe concreta.
- Escrever testes unitários com AAA e `assertThrows` para as regras de validação.
- Usar o `javac` direto para ver a linha exata do erro, que é mais claro que a saída do Maven.

### 11. Próximos passos

- Nível 2: adicionais do lanche.