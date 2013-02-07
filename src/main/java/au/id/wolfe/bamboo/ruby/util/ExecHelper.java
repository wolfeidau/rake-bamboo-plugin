package au.id.wolfe.bamboo.ruby.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static au.id.wolfe.bamboo.ruby.util.ExecUtils.cmdExec;

/**
 * Exec Helper which is used mainly on windows to locate the ruby executable.
 */
public class ExecHelper {

    private static final Logger log = LoggerFactory.getLogger(ExecHelper.class);

    /**
     * This routine uses an older method for locating exe files in the path and is compatible with xp.
     *
     * @param locatorCommand The command used to locate an executable.
     * @param executable     The command to locate on the path.
     * @param multiLine      Whether or not multi line output should be returned.
     * @return The output returned when the command was run.
     * @throws IOException
     * @throws InterruptedException
     */
    public String getExecutablePath(String locatorCommand, String executable, boolean multiLine) throws IOException, InterruptedException {

        final String line = locatorCommand + " " + executable;

        final StringBuffer output = new StringBuffer();

        final int exitValue = cmdExec(line, output, multiLine);

        log.info("where exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("where exec result = {}", output.toString());
            return output.toString();
        } else {
            throw new IllegalArgumentException("where executable failed to run.");
        }
    }

    /**
     * This routine uses an older method for locating exe files in the path and is compatible with xp.
     *
     * @param executable The command to locate on the path.
     * @param multiLine  Whether or not multi line output should be returned.
     * @return The output returned when the command was run.
     * @throws IOException
     * @throws InterruptedException
     */
    public String getExecutablePath(String executable, boolean multiLine) throws IOException, InterruptedException {

        final String line = "cmd.exe /c for %i in (" + executable + ") do @echo. %~$PATH:i";

        final StringBuffer output = new StringBuffer();

        final int exitValue = cmdExec(line, output, multiLine);

        log.info("where exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("where exec result = {}", output.toString());
            return output.toString().trim();
        } else {
            throw new IllegalArgumentException("where executable failed to run.");
        }
    }

}
