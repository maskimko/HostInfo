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
public interface CommandReturn {
    public int getExitCode();
    public String getStdOut();
    public String getErrOut();
}
