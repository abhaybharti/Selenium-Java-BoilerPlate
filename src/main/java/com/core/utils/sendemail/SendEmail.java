package main.java.com.core.utils.sendemail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import java.util.ArrayList;
import java.util.Iterator;

public class SendEmail {

    public SendEmail(){

    }

    public void sendToEmail(ArrayList<String> toEmailList,String emailSubject, String msgText){
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.office365.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("username","password"));
        email.setStartTLSEnabled(true);

        try{
            email.setFrom("email id",emailSubject);
            email.setSubject(emailSubject);
            email.setDebug(true);
            email.setHtmlMsg(msgText);
            Iterator itr = toEmailList.iterator();
            while (itr.hasNext()){
                String emailList = (String)itr.next();
                email.addTo((String)itr.next());
            }
            email.send();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
