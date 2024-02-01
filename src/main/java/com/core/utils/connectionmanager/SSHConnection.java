package main.java.com.core.utils.connectionmanager;

import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHConnection {

    public static void main(String[] args) {
        String host = "your_host";
        String username = "your_username";
        String password = "your_password";
        int port = 22;
        
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            Channel channel = session.openChannel("shell");
            channel.connect();
            
            // Perform your SSH operations here
            
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
