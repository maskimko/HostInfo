/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.hostinfo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ResourceBundle;


/**
 *
 * @author maskimko
 */
public class ExecCommand {

    private InetSocketAddress server;
    private String user;
    private String password;
    private String identity;
    private JSch jsch = null;
    private Logger logger = new LoggerAdapter();

    public ExecCommand(InetSocketAddress server, String user, String password, String identity) throws IllegalArgumentException {
        if (password == null && identity == null) {
            throw new IllegalArgumentException("At least one of identity or password must be not null");
        }
        this.server = server;
        this.user = user;
        this.password = password;
        this.identity = identity;
        this.jsch = new JSch();
        
        JSch.setLogger(logger);
        String knownHosts = null;
        try {
            ResourceBundle sshProps = ResourceBundle.getBundle("ssh");
            knownHosts = sshProps.getString("known_hosts");
            if (knownHosts != null) {
                jsch.setKnownHosts(knownHosts);
            }
            String id = sshProps.getString("identity_file");
            File idFile = new File(id);
            if (idFile.exists() && idFile.canRead()) {
                jsch.addIdentity(id);
            }
        } catch (JSchException ex) {
          logger.log(Logger.WARN, "Cannot load known_hosts file");
        }

    }

    public ExecCommand(String hostname, int port, String user, String password, String identity) throws IllegalArgumentException {
        this(new InetSocketAddress(hostname, port), user, password, identity);
    }
  

    public CommandReturn executeCommand(String command) {
        CommandReturn cr = null;
        int exitStatus = 0;
        Session session = null;
        InputStream errs = null;
        InputStream outs = null;
        Channel channel = null;

        try {
            session = jsch.getSession(user, server.getHostString(), server.getPort());
            UserInfo ui = new CommandUserInfo(password);
            session.setUserInfo(ui);
            session.connect();

            channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(command);

            errs = ((ChannelExec) channel).getErrStream();
            outs = channel.getInputStream();
            //FIXME Hardoded timeout 20 seconds;
            channel.connect(20000);
            StringBuilder outsb = new StringBuilder();
            StringBuilder errsb = new StringBuilder();
            BufferedReader outbr = new BufferedReader(new InputStreamReader(outs));
            BufferedReader errbr = new BufferedReader(new InputStreamReader(errs));
            String l = null;
            while ((l = outbr.readLine()) != null) {
                outsb.append(l).append("\n");
                if (channel.isClosed()) {
                    if (outs.available() > 0) {
                        continue;
                    }
                    exitStatus = channel.getExitStatus();
                    logger.log(Logger.INFO, "Exit Status: " + exitStatus);
                    break;
                }
            }
            while ((l = errbr.readLine()) != null) {
                errsb.append(l).append("\n");
            }
            cr = new CommandReturnImpl(exitStatus, outsb.toString(), errsb.toString());
        } catch (JSchException ex) {
            logger.log(Logger.ERROR, "Cannot establish connection " + ex.getMessage());
        } catch (IOException ex) {
             logger.log(Logger.ERROR, "IO error " + ex.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return cr;
    }

    public static class CommandUserInfo implements UserInfo {

        private String uiPassword;
        private String uiPassphrase;

        public CommandUserInfo(String password, String passphrase) {
            this.uiPassword = password;
            this.uiPassphrase = passphrase;
        }

        public CommandUserInfo(String password) {
            this(password, null);
        }

        @Override
        public String getPassphrase() {
            return uiPassphrase;
        }

        @Override
        public String getPassword() {
            return uiPassword;
        }

        @Override
        public boolean promptPassword(String string) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String string) {
            return true;
        }

        @Override
        public boolean promptYesNo(String string) {
            return true;
        }

        @Override
        public void showMessage(String string) {
           new LoggerAdapter(Logger.INFO, CommandUserInfo.class).log(Logger.INFO, "SSH Message: " + string);
        }


    }

}
