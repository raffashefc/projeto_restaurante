package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

// Se a entrada inválida não fosse descartada do Scanner, a leitura repetiria para sempre.
// O @Timeout faz o teste falhar em vez de travar.
@Timeout(5)
@DisplayName("Testes de entrada inválida no MenuConsole")
class MenuConsoleTest {

    private PrintStream saidaOriginal;
    private ByteArrayOutputStream saida;

    @BeforeEach
    void capturarSaida() {
        saidaOriginal = System.out;
        saida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    private Scanner scannerComEntrada(String entrada) {
        return new Scanner(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
    }

    private String textoImpresso() {
        return saida.toString(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Número válido é lido sem aviso")
    void numeroValidoSemAviso() {
        MenuConsole menu = new MenuConsole(scannerComEntrada("2\n"));

        assertEquals(2, menu.lerOpcao());
        assertFalse(textoImpresso().contains("Entrada inválida"));
    }

    @Test
    @DisplayName("Letra avisa e pede de novo")
    void letraAvisaEPedeDeNovo() {
        MenuConsole menu = new MenuConsole(scannerComEntrada("abc\n3\n"));

        assertEquals(3, menu.lerOpcao());
        assertTrue(textoImpresso().contains("Entrada inválida"));
    }

    @Test
    @DisplayName("'12abc' não é número: é recusado e a leitura se repete")
    void numeroMisturadoComLetraEhInvalido() {
        MenuConsole menu = new MenuConsole(scannerComEntrada("12abc\n5\n"));

        assertEquals(5, menu.lerOpcao());
        assertTrue(textoImpresso().contains("Entrada inválida"));
    }

    @Test
    @DisplayName("O Scanner fica limpo depois do erro: o nome seguinte é lido corretamente")
    void scannerFicaLimpoDepoisDoErro() {
        MenuConsole menu = new MenuConsole(scannerComEntrada("abc\n4\nBrianna\n"));

        assertEquals(4, menu.lerOpcao());
        assertEquals("Brianna", menu.lerNomeCliente());
    }

    @Test
    @DisplayName("Todas as leituras numéricas tratam entrada inválida")
    void todasAsLeiturasNumericasSaoTratadas() {
        assertEquals(0, new MenuConsole(scannerComEntrada("x\n0\n")).lerOpcao());
        assertEquals(2, new MenuConsole(scannerComEntrada("x\n2\n")).lerOpcaoLanche());
        assertEquals(1, new MenuConsole(scannerComEntrada("x\n1\n")).lerNumeroPedido());
        assertEquals(3, new MenuConsole(scannerComEntrada("x\n3\n")).lerNumeroItem());
    }
}