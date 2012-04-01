package au.id.wolfe.bamboo.ruby.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Very simplistic command execution utility functions.
 */
public final class ExecUtil {


    public static int cmdExec(String cmdLine, StringBuffer output) throws IOException, InterruptedException {

        String line;

        Process p = Runtime.getRuntime().exec(cmdLine);

        BufferedReader input = new BufferedReader
                (new InputStreamReader(p.getInputStream()));

        while ((line = input.readLine()) != null) {
            output.append(line);
        }
        input.close();


        return p.waitFor();
    }

}
