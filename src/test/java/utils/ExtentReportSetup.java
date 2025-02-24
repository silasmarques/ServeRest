package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportSetup {
    private static ExtentReports extent;

    public static ExtentReports setupExtentReport() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("build/reports/ExtentReport.html");
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
