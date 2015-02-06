/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.hostinfo;

/**
 *
 * @author maskimko
 */
public class Stats {
    public static void main(String[] args) {
        ExecCommand ec = new ExecCommand("urraco", 22,  "testuser", "123456", null);
        CommandReturn cr = ec.executeCommand("uptime");
        if (cr != null ){
            System.out.println(cr.getStdOut());
        } else {
            System.err.println("Cannot process...");
        }
    }
   
}
