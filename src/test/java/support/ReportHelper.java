package support;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportHelper {

    private static ExtentReports extent;
    private static ExtentTest test;

    static {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    public static void startTest(String testName) {
        test = extent.createTest(testName);
    }

    public static void logInfo(String message) {
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    public static void logError(String message) {
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }

    public static void logError(Throwable e) {
        if (test != null) {
            test.log(Status.FAIL, "Erro: " + e.getMessage());
        }
    }

    public static void endTest() {
        if (extent != null) {
            extent.flush();
        }
    }
}
