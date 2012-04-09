package au.id.wolfe.bamboo.ruby.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Very simplistic command execution utility functions.
 */
public final class ExecUtils {

    private static final Logger log = LoggerFactory.getLogger(ExecUtils.class);

    public static int cmdExec(String cmdLine, StringBuffer output) throws IOException, InterruptedException {
        return cmdExec(cmdLine, output, false);
    }

    public static int cmdExec(String cmdLine, StringBuffer output, boolean multiline) throws IOException, InterruptedException {

        String line;

        Process p = Runtime.getRuntime().exec(cmdLine);

        BufferedReader input = new BufferedReader
                (new InputStreamReader(p.getInputStream(), Charset.defaultCharset()));

        while ((line = input.readLine()) != null) {
            if (multiline){
                output.append(line);
                output.append(System.getProperty("line.separator"));
            } else {
                output.append(line);
            }
        }
        input.close();


        return p.waitFor();
    }

    public static String getGemPathString(String gemExecutablePath) throws IOException, InterruptedException {

        final String line = gemExecutablePath + " environment gempath";

        final StringBuffer output = new StringBuffer();

        final int exitValue = cmdExec(line, output);

        log.info("ruby exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("ruby exec result = {}", output.toString());
            return output.toString();
        } else {
            throw new IllegalArgumentException("Ruby executable failed to run.");
        }
    }


    public static String getRubyVersionString(String rubyExecutablePath) throws IOException, InterruptedException {

        final String line = rubyExecutablePath + " -v";

        final StringBuffer output = new StringBuffer();

        final int exitValue = cmdExec(line, output);

        log.info("ruby exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("ruby exec result = {}", output.toString());
            return output.toString();
        } else {
            throw new IllegalArgumentException("Ruby executable failed to run.");
        }
    }

}
