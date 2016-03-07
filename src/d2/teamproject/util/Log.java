package d2.teamproject.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Log implements Thread.UncaughtExceptionHandler {
    private static Level EXCEPTION_LEVEL = Level.SEVERE;

    private static Logger logger;
    private FileHandler fh;

    public Log(String name, Level logLevel) {
        logger = Logger.getLogger(name);
        logger.setLevel(logLevel);
        setLogOutput(new SimpleDateFormat("'log/" + name +
                "-'yyyy-MM-dd hh-mm-ss'.log'").format(new Date()));
    }

    public Log setLogOutput(String path) {
        try {
            // Create the log directory if it doesn't exist
            new File(path).getParentFile().mkdirs();

            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s] %5$s%6$s%n");
            fh = new FileHandler(path);
            fh.setFormatter(new SimpleFormatter());

            logger.setUseParentHandlers(false); // Stops logging to the console
            logger.addHandler(fh);
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(logger.getLevel());
            logger.addHandler(ch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void exit(int exitcode) {
        format(Level.INFO, "MailClient exiting with %d", exitcode);
        close();
    }
    public void close() {
        if (fh != null)
            fh.close();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        String exceptionPadding = "~~~~~~~~~~~~~~~~~~~~~~~~~~";
        logger.log(EXCEPTION_LEVEL, addThreadName(exceptionPadding + t.getStackTrace() + exceptionPadding), t);
    }

    private String addThreadName(String msg) {
        StackTraceElement el = Thread.currentThread().getStackTrace()[3];
        String classn = el.getClassName();
        String caller = classn.substring(classn.lastIndexOf('.')) + " @ " + el.getLineNumber();
        return "[" + Thread.currentThread().getName() + "]\t[" + caller + "]\t> " + msg;
    }

    public void format(Level level, String format, Object... args) {
        logger.log(level, addThreadName(String.format(format, args)));
    }
    public void finest(String msg) { logger.finest(addThreadName(msg)); }
    public void finer(String msg) { logger.finer(addThreadName(msg)); }
    public void fine(String msg) { logger.fine(addThreadName(msg)); }
    public void info(String msg) { logger.info(addThreadName(msg)); }
    public void warning(String msg) { logger.log(Level.WARNING, addThreadName(msg)); }
    public void severe(String msg) { logger.log(Level.SEVERE, addThreadName(msg)); }
    public void exception(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        logger.log(EXCEPTION_LEVEL, addThreadName(sw.toString()));
    }
}
