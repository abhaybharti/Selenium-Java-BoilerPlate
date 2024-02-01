package main.java.com.core.utils.connectionmanager;

import com.google.common.io.CharStreams;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.testkit.reportmanager.ExtentReportManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

public class LinuxConnect {

    public Logger logger = Logger.getLogger(LinuxConnect.class.getName());
    ExtentReportManager rm = null;
    StringBuilder SSH_HOST = null;
    StringBuilder SSH_LOGIN = null;
    StringBuilder SSH_PASSWORD = null;

    public LinuxConnect(Logger logger, ExtentReportManager rm, HashMap<String, String> testData) {
        this.rm = rm;
        this.logger = logger;
        SSH_HOST = new StringBuilder(testData.get("SSH_HOST"));
        SSH_LOGIN = new StringBuilder(testData.get("SSH_LOGIN"));
        SSH_PASSWORD = new StringBuilder(testData.get("SSH_PASSWORD"));
    }

    public void setHostDetails(String host, String userName, String passWord) {
        SSH_HOST = new StringBuilder(host);
        SSH_LOGIN = new StringBuilder(userName);
        SSH_PASSWORD = new StringBuilder(passWord);
    }

    public String runCommand(String command) {
        Session session = setUpSshSession();
        ChannelExec channel = null;
        StringBuilder result = new StringBuilder();
        try {
            session.connect();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            InputStream output = channel.getInputStream();
            channel.connect();
            result = new StringBuilder(CharStreams.toString(new InputStreamReader(output)));
            return result.toString().trim();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(channel, session);
        }
        return result.toString();
    }

    private Session setUpSshSession() {
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SSH_LOGIN.toString(), SSH_HOST.toString(), 22);
            session.setPassword(SSH_PASSWORD.toString());
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    private void closeConnection(ChannelExec channel, Session session) {
        try {
            if (channel != null) {
                channel.disconnect();
            }
        } catch (Exception ignored) {
        }
        session.disconnect();
    }
}
