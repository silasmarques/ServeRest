package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExtentReportListener implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final ExtentReports extent = ExtentReportSetup.setupExtentReport();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String testName = context.getDisplayName();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            test.get().log(Status.FAIL, "Teste falhou: " + context.getExecutionException().get().getMessage());
        } else {
            test.get().log(Status.PASS, "Teste passou com sucesso!");
        }
        extent.flush();
    }
}
