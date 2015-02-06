/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.hostinfo;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 *
 * @author maskimko
 */
class LoggerAdapter implements com.jcraft.jsch.Logger {

    private Logger logger = null;
    private int installedLevel = com.jcraft.jsch.Logger.INFO;

    public LoggerAdapter() {
        this(Integer.parseInt(ResourceBundle.getBundle("ssh").getString("log_level")), LoggerAdapter.class);
    }

    public LoggerAdapter(int level, String className) {
        this.installedLevel = level;
        this.logger = Logger.getLogger(className);

    }

    public LoggerAdapter(int level, Class c) {
        this(level, c.getName());
    }

    @Override
    public boolean isEnabled(int level) {
        return level >= installedLevel;
    }

    @Override
    public void log(int level, String message) {
        Priority p = null;
        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                p = Priority.DEBUG;
                break;
            case com.jcraft.jsch.Logger.INFO:
                p = Priority.INFO;
                break;
            case com.jcraft.jsch.Logger.WARN:
                p = Priority.WARN;
                break;
            case com.jcraft.jsch.Logger.ERROR:
                p = Priority.ERROR;
                break;
            case com.jcraft.jsch.Logger.FATAL:
                p = Priority.FATAL;
                break;
            default:
                p = Priority.INFO;
                break;
        }
        logger.log(p, message);
    }

}
