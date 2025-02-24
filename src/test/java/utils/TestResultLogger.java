package utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultLogger implements TestWatcher {

    private static final String REPORT_PATH = "build/reports/tests-report.html";

    static {
        try {
            if (!Files.exists(Paths.get("build/reports"))) {
                Files.createDirectories(Paths.get("build/reports"));
            }
            PrintWriter writer = new PrintWriter(new FileWriter(REPORT_PATH, false));
            writer.println("<html><head><title>Relatório de Testes</title><style>");
            writer.println("body { font-family: Arial; padding: 20px; background-color: #f7f9fc; }");
            writer.println(".passed { color: green; } .failed { color: red; } .skipped { color: orange; }");
            writer.println("</style></head><body><h2>Relatório de Testes Automatizados</h2><ul>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logResult(String testName, String status) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_PATH, true))) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            writer.printf("<li class='%s'>[%s] - %s (%s)</li>%n", status.toLowerCase(), time, testName, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logResult(context.getDisplayName(), "PASSED");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logResult(context.getDisplayName(), "FAILED");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logResult(context.getDisplayName(), "SKIPPED");
    }

    // Ao finalizar a execução dos testes
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_PATH, true))) {
                writer.println("</ul></body></html>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
