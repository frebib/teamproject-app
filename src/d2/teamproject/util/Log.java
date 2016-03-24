package d2.teamproject.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

/**
 * Provides an accessible frontend to the {@link Logger}
 * Included are convenience methods that format objects into log entries
 * Also handles {@link Thread} {@link Exception}s through a {@link java.lang.Thread.UncaughtExceptionHandler}
 * Should be used as a singleton class
 *
 * @author Joseph Groocock
 */
public class Log implements Thread.UncaughtExceptionHandler {
    private static Level EXCEPTION_LEVEL = Level.SEVERE;

    private static Logger logger;
    private final String name;
    private FileHandler fh;

    public Log(String name, Level logLevel) {
        this.name = name;
        logger = Logger.getLogger(name);
        logger.setLevel(logLevel);
        setLogOutput(new SimpleDateFormat("'log/" + name +
                "-'yyyy-MM-dd hh-mm-ss'.log'").format(new Date()));
    }

    /**
     * Specifies the file path to save the logs into
     * Will overwrite existing file in the location if it exists
     * @param path directory and file name to save log to
     * @return this {@link Log} instance
     */
    public Log setLogOutput(String path) {
        try {
            // Create the log directory if it doesn't exist
            new File(path).getParentFile().mkdirs();

            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$s] %5$s%6$s%n");
            fh = new FileHandler(path);
            fh.setFormatter(new SimpleFormatter());

            logger.setUseParentHandlers(false); // Stops logging to the console
            logger.addHandler(fh);
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(logger.getLevel());
            logger.addHandler(ch);
        } catch (IOException e) {
            try {
                exception(e);
                exit(1, true);
            } catch (Exception e1) {
                e.printStackTrace();
                e1.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Logs program exit then terminates the running application
     * @param exitcode code to exit with. Non-zero values indicate a non-clean exit
     * @param exit closes the application if flag is set
     */
    public void exit(int exitcode, boolean exit) {
        format(Level.INFO, "%s exiting with %d", name, exitcode);
        close();
        if (exit) System.exit(exitcode);
    }

    /**
     * Closes the logger instance
     */
    public void close() {
        if (fh != null)
            fh.close();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        exception(t);
    }

    private String addThreadName(String msg) {
        StackTraceElement el = Arrays
                .stream(Thread.currentThread().getStackTrace())
                .filter(e -> !Thread.class.getName().equals(e.getClassName()))
                .filter(e -> !getClass().getName().equals(e.getClassName()))
                .findFirst()
                .orElse(null);

        String cn = el.getClassName();
        String caller = cn.substring(cn.lastIndexOf('.') + 1) + " @ " + el.getLineNumber();
        return "[" + Thread.currentThread().getName() + "]\t[" + caller + "]\t> " + msg;
    }

    private void format(Level level, String format, Object... args) {
        logger.log(level, addThreadName(String.format(format, args)));
    }
    public void finest(String msg, Object... args) { format(Level.FINEST, msg, args); }
    public void finer(String msg, Object... args)  { format(Level.FINER, msg, args); }
    public void fine(String msg, Object... args)   { format(Level.FINE, msg, args); }
    public void info(String msg, Object... args)   { format(Level.INFO, msg, args); }
    public void warning(String msg, Object... args){ format(Level.WARNING, msg, args); }
    public void severe(String msg, Object... args) { format(Level.SEVERE, msg, args); }
    /**
     * Logs a {@link Throwable} error with a stacktrace
     * @param t
     */
    public void exception(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        logger.log(EXCEPTION_LEVEL, addThreadName(sw.toString()));
    }
}
