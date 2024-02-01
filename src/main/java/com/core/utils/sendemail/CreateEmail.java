package main.java.com.core.utils.sendemail;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateEmail {
    ArrayList<String> emailList = new ArrayList<>();
    public static StringBuffer sbrTest = new StringBuffer("");
    String emailRecipient = "";


    public void createEmailReport(HashMap<String, String> suiteDetails) {
        try {
            StringBuffer sbr = new StringBuffer("");
            String suiteName = suiteDetails.get("suiteName");
            String failureReason = suiteDetails.get("failureReason");
            String env = suiteDetails.get("env");
            String reportPath = suiteDetails.get("reportPath");
            String emailCustomSubject = "Env : [" + env + "] -> Suite : [" + suiteName + "] failure reason :" + failureReason;

            String fontColor = "border: 1px solid black;color:red";

            sbr.append("<table style='border: 2px solid black;'>\n");
            sbr.append("<tr style ='border: 2px solid black'>\n");
            sbr.append("<th style='border: 2px solid black;'>Suite</th>\n");
            sbr.append("<th style='border: 2px solid black;'>Status</th>\n");
            sbr.append("<th style='border: 2px solid black;'>Result URL</th>\n");
            sbr.append("</tr>");

            sbr.append("<tr style='border: 1px solid black'>\n");
            sbr.append("<td style='border: 1px solid black'>" + suiteDetails.get("suiteName") + "</td>\n");
            sbr.append("<td style=\"" + fontColor + "\"><center>Stopped</center></td>\n");
            sbr.append("<td style=\"" + fontColor + "\">\n" + "<a target=\"_blank\" href=" + reportPath + "  style=\\\"\"+fontColor+\"\\\">Result</a>\n");
            sbr.append("</td>\n");
            sbr.append("</tr>");

            sbr.append("</table>");
            sbr.append("<p>This email message and all attachement transmitted with it may contain legally privileged and ocnfidential information</p>");

            String emailBody = sbr.toString();

            String[] receiptList = emailRecipient.split(",");

            for (int i = 0; i < receiptList.length; i++) {
                this.emailList.add(receiptList[i]);
            }

            System.out.println("Email Body : " + emailBody);
            System.out.println("Email Subject : " + emailCustomSubject);
            System.out.println("Email Address : " + this.emailList.size());
            SendEmail sendEmail = new SendEmail();
            sendEmail.sendToEmail(this.emailList, emailCustomSubject, emailBody);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
