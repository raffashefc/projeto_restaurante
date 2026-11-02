### Projeto 1: Sistema de Restaurante (Nível 1)

### Objetivo

Construir o sistema de um restaurante em Java, com cardápio, pedidos e fechamento do dia, organizado em pacotes e com cada classe cuidando de uma responsabilidade.

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
    │   │   └── Lanche.java
    │   └── pedido/
    │       ├── Pedido.java
    │       └── GerenciadorPedidos.java
    └── test/java/
        ├── cardapio/LancheTest.java
        └── pedido/
            ├── PedidoTest.java
            └── GerenciadorPedidosTest.java
```

| Pacote | O que guarda | Por que ali |
|---|---|---|
| `app` | `Main`, `MenuConsole`, `Restaurante` | É a camada que conversa com a pessoa e coordena as operações. Tudo que mexe com tela e fluxo fica junto. |
| `cardapio` | `Lanche` | É o "produto" do restaurante. Não depende de nenhum outro pacote. |
| `pedido` | `Pedido`, `GerenciadorPedidos` | Tudo que trata de comandas: uma comanda (`Pedido`) e o conjunto delas (`GerenciadorPedidos`). |
| `financeiro` e `util` | (ainda vazios) | Só serão criados quando houver classe para colocar neles. O Git não versiona pasta vazia. |

**A regra que guiou a divisão:** cada parte tem uma responsabilidade, e as dependências vão sempre em uma direção só.

```
app  ──►  pedido  ──►  cardapio
```

- `cardapio` não conhece ninguém.
- `pedido` conhece `cardapio` (um pedido tem lanches), mas não conhece `app`.
- `app` conhece os dois e é a única camada que imprime e lê dados.

Com isso, dá para trocar o console por outra interface (por exemplo, uma tela gráfica) mexendo só em `app`, sem tocar em `pedido` nem `cardapio`. Os testes ficam em `src/test/java` com os mesmos nomes de pacote das classes que testam.

### Responsabilidade de cada classe

| Classe | Responsabilidade |
|---|---|
| `Lanche` | Guarda nome e preço, com validação nos setters |
| `Pedido` | Cuida de **uma** comanda: número, cliente e lista de lanches |
| `GerenciadorPedidos` | Guarda **todos** os pedidos (`HashMap`), gera o número e soma o total do dia |
| `Restaurante` | Coordena as operações e define o cardápio |
| `MenuConsole` | Toda a leitura (`Scanner`) e exibição (`System.out`) |
| `Main` | Controla o fluxo principal (loop e `switch`), sem cálculo nem impressão |

`Pedido` e `GerenciadorPedidos` são classes separadas de propósito (o S do SOLID): o pedido não sabe quantos pedidos existem, e o gerenciador não sabe calcular o total de uma comanda.

---

### 2. Regras implementadas

- **Lanche:** o nome não pode ser nulo nem vazio, e o preço não pode ser negativo. O construtor reutiliza os setters, então a validação fica em um lugar só. Valor inválido lança `IllegalArgumentException`.
- **Pedido:** o nome do cliente não pode ser nulo nem vazio. O número chega pronto pelo construtor. `calcularTotal()` percorre a lista com `for`.
- **GerenciadorPedidos:** `criarPedido` gera o número com `contadorPedidos++`. O usuário nunca digita o número de um pedido novo, então a numeração não se repete (o `HashMap` não recusa chave repetida, ele **substitui**).
- **Lanche novo a cada escolha:** `criarLanche(opcao)` faz `new Lanche(...)` toda vez. Nenhum objeto é compartilhado entre pedidos.

### Cardápio

| Opção | Lanche | Preço |
|---|---|---|
| 1 | X-Burger | R$ 16,00 |
| 2 | X-Salada | R$ 18,00 |
| 3 | X-Bacon | R$ 22,00 |

---

### 3. Um pedido do começo ao fim

Execução real do sistema, com a classe responsável por cada passo:

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

**O que cada classe fez nesse caminho:**

| Passo | Quem executa |
|---|---|
| Mostrar o menu e ler a opção `1` | `MenuConsole` (`exibirMenu`, `lerOpcao`) |
| Ler o nome "Brianna" | `MenuConsole` (`lerNomeCliente`) |
| Criar o pedido e dar o número `#1` | `Restaurante` → `GerenciadorPedidos.criarPedido` → `new Pedido(1, "Brianna")` |
| Mostrar o cardápio e ler a escolha `3` | `MenuConsole` (`exibirCardapio`, `lerOpcaoLanche`) |
| Criar o X-Bacon e colocar no pedido | `Restaurante.adicionarItem` → `criarLanche(3)` → `Pedido.adicionarItem` |
| Perguntar "mais um item?" | `MenuConsole` (`perguntarMaisItem`) |
| Repetir com a escolha `2` (X-Salada) | Mesmo caminho, com um `new Lanche` novo |
| Calcular R$ 40,00 | `Pedido.calcularTotal` (22 + 18) |
| Mostrar o pedido | `MenuConsole.exibirPedido` |
| Fechamento do dia | `GerenciadorPedidos.calcularTotalDoDia` → `MenuConsole.exibirFechamentoDoDia` |

Repare que o `Main` só repassa o que o `MenuConsole` leu para a `Restaurante`, e o que ela devolve para o `MenuConsole` exibir.

---

### 4. Padrão de projeto: Facade

**Onde está:** `Restaurante`, no pacote `app`.

**O problema que resolveu:** para registrar um item em um pedido, é preciso fazer várias coisas em ordem: achar o pedido no `GerenciadorPedidos`, criar o `Lanche` certo com o preço certo e colocá-lo no `Pedido`. Sem a fachada, o `Main` teria que conhecer todas essas classes e coordenar esse passo a passo:

```java
// Sem a fachada: o Main precisa conhecer tudo
GerenciadorPedidos gerenciador = new GerenciadorPedidos();
Pedido pedido = gerenciador.buscarPedido(numero);
Lanche lanche;
switch (escolha) {
    case 1: lanche = new Lanche("X-Burger", 16.00); break;
    case 2: lanche = new Lanche("X-Salada", 18.00); break;
    case 3: lanche = new Lanche("X-Bacon", 22.00); break;
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

**Observação:** o `switch` de `criarLanche` já é um começo de Factory (decide qual objeto criar em um único lugar). Ele pode evoluir para uma fábrica de verdade nos próximos níveis, quando entrarem os adicionais.

---

### 5. Testes com JUnit

Foram escritos 10 testes em `src/test/java`, seguindo o padrão AAA (Arrange, Act, Assert):

| Classe de teste | Testes |
|---|---|
| `LancheTest` | lanche válido guarda nome e preço; nome vazio lança exceção; nome nulo lança exceção; preço negativo lança exceção |
| `PedidoTest` | total soma o preço dos itens; pedido sem itens tem total zero; cliente vazio lança exceção |
| `GerenciadorPedidosTest` | números sequenciais e sem repetição; buscar pedido inexistente devolve `null`; total do dia soma todos os pedidos |

Exemplo de teste:

```java
@Test
@DisplayName("Total soma o preço de todos os itens")
void totalSomaPrecosDosItens() {
    // Arrange
    Pedido pedido = new Pedido(1, "Ana");
    pedido.adicionarItem(new Lanche("X-Burger", 16.00));
    pedido.adicionarItem(new Lanche("X-Bacon", 22.00));

    // Act
    double total = pedido.calcularTotal();

    // Assert
    assertEquals(38.00, total, 0.0001);
}
```

**Como rodar** (na pasta `projeto_restaurante`):

```
mvn test
```

**Saída esperada** quando tudo passa (os tempos variam):

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 -- in cardapio.LancheTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 -- in pedido.PedidoTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 -- in pedido.GerenciadorPedidosTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Print da execução:**

![saida cli](/src/asset/mv-teste.png)

---

### 6. Como rodar o sistema

Pré-requisitos: JDK 17 ou superior e Maven (`sudo apt install maven`).

```
mvn compile
java -cp target/classes app.Main
```

---

### 7. Problemas que apareceram no caminho

| Problema | Causa | Solução |
|---|---|---|
| `mvn: command not found` | Maven não estava instalado | `sudo apt install maven` |
| `No sources to compile` | Os `.java` estavam soltos na raiz do projeto | Mover para `src/main/java/<pacote>/` |
| `class, interface, enum, or record expected` na última linha | Um ` {` sobrando depois do `}` que fecha a classe | Deixar só `}` na última linha |
| `ClassNotFoundException: app.Main` | A compilação falhou, então `target/classes` não existia | Corrigir os erros e rodar `mvn compile` de novo |

---

### 8. O que foi aprendido

- Organizar o projeto em pacotes com dependências em uma direção só.
- Centralizar a validação nos setters e reutilizá-los no construtor.
- Separar a classe que cuida de **um** pedido da que guarda **todos** (o S do SOLID).
- Usar a fachada para o `Main` não precisar conhecer as classes de negócio.
- Escrever testes unitários com AAA e `assertThrows` para as regras de validação.
- Usar o `javac` direto para ver a linha exata do erro, que é mais claro que a saída do Maven.

### 9. Próximos passos

- Nível 2: adicionais do lanche e a factory.

