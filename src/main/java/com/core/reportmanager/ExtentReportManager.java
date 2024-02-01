package main.java.com.core.reportmanager;



import java.util.Objects;

public class ExtentReportManager {

    private static ExtentSparkReporter extentSparkReporterForFail = null;
    private static ExtentSparkReporter extentSparkReporterForPass = null;
    private static ExtentTest extentTest;
    private static ExtentReports extentReports;

//    protected Logger logger = Logger.getLogger(ExtentReportManager.class);

    /**
     * The function initializes Extent Reports with two Spark Reporters for generating separate HTML
     * reports for failed and passed tests.
     *
     * @param resultPath The resultPath parameter is a String that represents the path where the extent
     *                   report files will be saved.
     */
    public void initExtentReport(String resultPath) {
        try {
            if (Objects.nonNull(resultPath)) {
                extentSparkReporterForFail =
                        new ExtentSparkReporter(resultPath + "\\TestResultFail.html")
                                .filter()
                                .statusFilter()
                                .as(new Status[]{Status.FAIL, Status.WARNING, Status.SKIP})
                                .apply()
                                .viewConfigurer()
                                .viewOrder()
                                .as(new ViewName[]{ViewName.TEST, ViewName.DASHBOARD, ViewName.CATEGORY})
                                .apply();
                extentSparkReporterForPass =
                        new ExtentSparkReporter(resultPath + "\\TestResultPass.html")
                                .filter()
                                .statusFilter()
                                .as(new Status[]{Status.PASS})
                                .apply()
                                .viewConfigurer()
                                .viewOrder()
                                .as(new ViewName[]{ViewName.TEST, ViewName.DASHBOARD, ViewName.CATEGORY})
                                .apply();
                extentReports = new ExtentReports();
                extentReports.attachReporter(extentSparkReporterForFail, extentSparkReporterForPass);
            }
        } catch (Exception e) {
//            logger.error(e);
        }
    }

    /**
     * The function creates a test case in the extentReports object with the given name.
     *
     * @param testCaseName The parameter "testCaseName" is a string that represents the name of the test
     *                     case.
     */
    public void createTest(String testCaseName) {
        extentTest = extentReports.createTest(testCaseName);
    }

    /**
     * The function writes the contents of the extentReports object to an HTML file.
     */
    public void writeToHtmlFile() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
    }

    public void close() {
        extentReports.flush();
    }

    public void log(String status, String message, boolean flushToHtml) {
        switch (status.toUpperCase()) {
            case "PASS":
                extentTest.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
                break;
            case "FAIL":
                extentTest.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));

                break;
            case "INFO":
                extentTest.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.RED));

                break;
            case "WARN":
                extentTest.log(Status.WARNING, MarkupHelper.createLabel(message, ExtentColor.RED));

                break;
            case "ERROR":
                extentTest.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));

                break;
            case "SKIP":
                extentTest.log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.RED));

                break;
        }
        if (flushToHtml) {
            extentReports.flush();
        }
    }

    public void log(String status, String message, String screenshot) {
        switch (status.toUpperCase()) {
            case "PASS":
                extentTest.log(Status.PASS, String.valueOf(MarkupHelper.createLabel(message, ExtentColor.GREEN)), MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64" + screenshot).build());
                break;
            case "ERROR":
            case "FAIL":
                extentTest.log(Status.FAIL, String.valueOf(MarkupHelper.createLabel(message, ExtentColor.RED)), MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64" + screenshot).build());
                break;
            case "INFO":
                extentTest.log(Status.INFO, message, MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64" + screenshot).build());

                break;
            case "WARN":
            case "WARNING":
                extentTest.log(Status.WARNING, String.valueOf(MarkupHelper.createLabel(message, ExtentColor.AMBER)), MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64" + screenshot).build());
                break;
            case "SKIP":
                extentTest.log(Status.SKIP, String.valueOf(MarkupHelper.createLabel(message, ExtentColor.INDIGO)), MediaEntityBuilder.createScreenCaptureFromBase64String("data:image/png;base64" + screenshot).build());
                break;
        }

        extentReports.flush();

    }

    public void log(String status, String message) {
        log(status, message, true);
    }

    public void insertTable(String tableTile, String[][] table) {
        String htmlString = convertArrayToHtmlString(table);
        extentTest.log(Status.INFO, "------ Table for " + tableTile + " ------");
        extentTest.log(Status.INFO, htmlString);
    }

    public String convertArrayToHtmlString(String[][] table) {
        StringBuilder tempStr = new StringBuilder();
        tempStr.append("<!DOCTYPE html><html><body>");
        tempStr.append("<table layout='fixed';width='100%'><tr>");
        tempStr.append("<th width='15%'>").append("S.N.").append("</th>");
        for (int iCol = 0; iCol < table[0].length; iCol++) {
            int widthPer = 90 / (table[0].length);
            tempStr.append("<th width='").append(widthPer).append("%'>").append(table[0][iCol]).append("</th>");
        }
        tempStr.append("</tr>");

        for (int iRow = 1; iRow < table.length; iRow++) {
            tempStr.append("<tr>");
            tempStr.append("<td>").append(iRow).append("</td>");
            for (int iCol = 0; iCol < table[iRow].length; iCol++) {
                tempStr.append("<td>").append(table[iRow][iCol]).append("</td>");
            }
            tempStr.append("</tr>");
        }
        tempStr.append("</table></body></html>");
        return tempStr.toString();
    }

    public String convertArrayToHtmlColor(String[][] table, int firstColumnIndex) {
        StringBuilder tempStr = new StringBuilder();
        tempStr.append("<!DOCTYPE html><html><body>");
        tempStr.append("<table layout='fixed';width='100%'><tr>");
        tempStr.append("<th width='15%'>").append("S.N.").append("</th>");
        for (int iCol = 0; iCol < table[0].length; iCol++) {
            int widthPer = 90 / (table[0].length);
            tempStr.append("<th width='").append(widthPer).append("%'>").append(table[0][iCol]).append("</th>");
        }
        tempStr.append("</tr>");
        for (int iRow = 0; iRow <table.length ; iRow++) {
            tempStr.append("<tr>");
            tempStr.append("<td>").append(iRow).append("</td>");
            for (int iCol = 0; iCol < table[iRow].length; iCol++) {
                tempStr.append("<td>").append(table[iRow][iCol]).append("</td>");
            }

            if (table[iRow][firstColumnIndex-1]!= null) {
                if (table[iRow][firstColumnIndex-1].equalsIgnoreCase(table[iRow][firstColumnIndex])){
                    tempStr.append("<td>").append(table[iRow][firstColumnIndex-1]).append("</td>").append("<td>").append(table[iRow][firstColumnIndex]).append("</td>");
                }else{
                    tempStr.append("<td bgcolor=#ec7063>").append(table[iRow][firstColumnIndex-1]).append("</td>").append("<td bgcolor=#ec7063>>").append(table[iRow][firstColumnIndex]).append("</td>");
                }
            }

            for (int iCol = firstColumnIndex+1; iCol <table[iRow].length ; iCol++) {
                tempStr.append("<td>").append(table[iRow][iCol]).append("</td>");

            }
            tempStr.append("</tr>");

        }

        tempStr.append("</table></body></html>");
        return tempStr.toString();
    }
}
