/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.hostinfo;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maskimko
 */
public class ExecCommandTest {

    public ExecCommandTest() {
    }

    /**
     * Test of executeCommand method, of class ExecCommand.
     */
    @Test
    public void testExecuteCommand() {
        System.out.println("executeCommand");
        String command = "uptime";
        ResourceBundle rb = ResourceBundle.getBundle("test");
        String testHost = rb.getString("testSSHHost");
        int testPort = Integer.parseInt(rb.getString("testSSHPort"));
        String testUser = rb.getString("testUser");
        String testPassword = rb.getString("testPassword");
        String testId = rb.getString("testIdentity");
        ExecCommandImpl instance = new ExecCommandImpl(testHost, testPort, testUser, testPassword, testId);

        CommandReturn result = instance.executeCommand(command);
        System.out.println("Got output: " + result.getStdOut());
        System.out.println("Expecting to match \"load average\"");
        Pattern p = Pattern.compile(".*load\\s*average.*", Pattern.DOTALL);
        Matcher m = p.matcher(result.getStdOut());
        assertTrue(m.matches());
    }

    @Test
    public void testExecuteCommands() {
        System.out.println("executeCommands");
        String[] command = new String[]{"uptime", "/usr/sbin/ip l l", "last", "cat /proc/cpuinfo"};
        ResourceBundle rb = ResourceBundle.getBundle("test");
        String testHost = rb.getString("testSSHHost");
        int testPort = Integer.parseInt(rb.getString("testSSHPort"));
        String testUser = rb.getString("testUser");
        String testPassword = rb.getString("testPassword");
        String testId = rb.getString("testIdentity");
        ExecCommandImpl instance = new ExecCommandImpl(testHost, testPort, testUser, testPassword, testId);

        CommandReturn[] results = instance.executeCommand(command);
        for (CommandReturn r : results) {
            System.out.println("Got output: " + r.getStdOut());
            assertTrue(r.getStdOut().length() > 0 || r.getErrOut().length() > 0);
        }
    }

}
