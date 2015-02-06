/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.hostinfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
        ExecCommand instance = new ExecCommand("urraco", 22, "testuser", "123456", null);
       
        CommandReturn result = instance.executeCommand(command);
        System.out.println("Got output: " + result.getStdOut());
        System.out.println("Expecting to match \"load average\"");
        Pattern p = Pattern.compile(".*load\\s*average.*", Pattern.DOTALL);
        Matcher m = p.matcher(result.getStdOut());
        assertTrue(m.matches());
    }
    
}
