package main.java.com.core.listeners;


import main.java.com.core.basetest.TestSetup;
import org.testng.*;

import java.util.ArrayList;
import java.util.List;

public class TestResultListener extends TestSetup implements ITestListener, IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        String testNgFileName = iTestContext.getCurrentXmlTest().getSuite().getFileName();
        String runProfile = testNgFileName;
        System.out.println("TestNG file Name :" + runProfile);
        List<IMethodInstance> testNgXmlList = new ArrayList<>();
        for (IMethodInstance methodInstance : methods) {
            List<String> testCaseList = new ArrayList<>();
            String testCaseName = methodInstance.getMethod().getMethodName();

            if (runProfile.contains("resume")) {
                testCaseList = readTestCaseFromFile(executedTestCaseListFile);
                int lastTestCaseIndex = testCaseList.size() - 1;

                //Delete last test case from test case list by passing index
                testCaseList.remove(lastTestCaseIndex);

                if (!testCaseList.contains(testCaseName)) {
                    System.out.println("[" + testCaseName + "] Added in resume run, does not exist in executedTestCaseList.txt file");
                    testNgXmlList.add(methodInstance);
                }
            } else if (runProfile.contains("failed")) {
                testCaseList = readTestCaseFromFile(executedTestCaseListFile);
                if (testCaseList.contains(testCaseName)) {
                    System.out.println("[" + testCaseName + "] Added in fail run, does exist in failedTestCaseList.txt file");
                }
            } else {
                System.out.println("Running full regression suite, script added : " + testCaseName);
                testNgXmlList.add(methodInstance);
            }
        }
        System.out.println(testNgXmlList.isEmpty() ? "No test cases to run in resume/failed suite found" : testNgXmlList.size() + " test case will be executed");
        return testNgXmlList;
    }

    /**
     * The function is an override of the onTestStart method from the ITestListener interface in Java.
     *
     * @param result The parameter "result" is an object of type ITestResult, which represents the result
     *               of a test method execution. It contains information about the test method, such as its name,
     *               status (pass/fail/skip), start time, end time, and any associated exception.
     */
    @Override
    public void onTestStart(ITestResult result) {
        writeTestCaseIntoFile(result.getName(), executedTestCaseListFile);
    }

    /**
     * The function is an override of the onTestSuccess method from the ITestListener interface in Java.
     *
     * @param result The result parameter is an object of type ITestResult, which represents the result
     *               of a test method execution. It contains information about the test method, such as the test class,
     *               test method name, and the status of the test (success, failure, skipped, etc.).
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        writeTestCaseIntoFile(result.getName(), failedTestCaseListFile, true);
    }

    /**
     * The function is an override of the onTestFailure method in the ITestListener interface.
     *
     * @param result The "result" parameter is an object of type ITestResult, which represents the result
     *               of a test method execution. It contains information about the test method, such as its name,
     *               status (pass/fail), and any associated exception or error.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        writeTestCaseIntoFile(result.getName(), failedTestCaseListFile);
    }

    /**
     * The above function is an override of the onTestSkipped method from the ITestListener interface in
     * Java.
     *
     * @param result The result parameter is an object of type ITestResult, which represents the result
     *               of a test method execution. It contains information about the test method, such as its name,
     *               status (passed, failed, skipped), and any associated exception or error.
     */
    @Override
    public void onTestSkipped(ITestResult result) {

    }

    /**
     * The function is an override method that is called when a test fails but is within the defined
     * success percentage.
     *
     * @param result The result parameter is an object of type ITestResult, which represents the result
     *               of a test method execution. It contains information about the test method, such as its name,
     *               status (pass/fail), and any associated exception or failure message.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    /**
     * The function is an override of the onTestFailedWithTimeout method from the ITestListener interface
     * in Java.
     *
     * @param result The parameter "result" is of type ITestResult and represents the result of a test
     *               execution. It contains information about the test method, the test class, the test status
     *               (pass/fail), and any exception that occurred during the test execution.
     */
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    /**
     * The onStart function is an overridden method that is called when a test context starts.
     *
     * @param context The context parameter is an object of type ITestContext, which provides information
     *                about the current test run. It contains methods to retrieve information about the test suite, test
     *                methods, test parameters, and other details related to the test execution.
     */
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    /**
     * This function is an override of the onFinish method from the ITestListener interface in Java.
     *
     * @param context The context parameter in the onFinish method is an object of type ITestContext. It
     *                represents the test context, which provides information about the current test run, such as the test
     *                suite, test methods, and test results.
     */
    @Override
    public void onFinish(ITestContext context) {

    }
}
