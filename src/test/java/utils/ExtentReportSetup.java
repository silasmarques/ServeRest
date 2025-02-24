package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;

public class ExtentReportSetup {
    private static ExtentReports extent;

    public static ExtentReports setupExtentReport() {
        if (extent == null) {

            // Remove relatório antigo se existir
            File reportFile = new File("reports/Report.html");
            if (reportFile.exists()) {
                reportFile.delete();
            }

            ExtentSparkReporter spark = new ExtentSparkReporter("reports/Report.html");
            spark.config().setDocumentTitle("Relatório Automação API");
            spark.config().setReportName("Resultados dos Testes Automatizados");
            spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Projeto", "Serverest API Tests");
            extent.setSystemInfo("Autor", "Silas Marques");
            extent.setSystemInfo("Ambiente", "QA");
        }
        return extent;
    }
}
