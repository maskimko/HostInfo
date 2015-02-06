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
public class CommandReturnImpl implements CommandReturn{

    private int ec;
    private String out;
    private String err;

    public CommandReturnImpl(int ec, String out, String err) {
        this.ec = ec;
        this.out = out;
        this.err = err;
    }
    
    
    
    @Override
    public int getExitCode() {
        return ec;
    }

    @Override
    public String getStdOut() {
        return out;
    }

    @Override
    public String getErrOut() {
        return err;
    }
    
}
