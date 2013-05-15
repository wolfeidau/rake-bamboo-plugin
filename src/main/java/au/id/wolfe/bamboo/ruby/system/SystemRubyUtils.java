package au.id.wolfe.bamboo.ruby.system;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various util functions used by the system ruby locator.
 */
public final class SystemRubyUtils {

    static final Pattern ruby18pattern = Pattern.compile("ruby (\\d\\.\\d\\.\\d) \\((.*) patchlevel (\\d+)\\) \\[(.*)\\]");
    static final Pattern ruby19pattern = Pattern.compile("ruby (\\d+\\.\\d+\\.\\d)+p(\\d+) \\((.*)\\) \\[(.*)\\]");
    static final Pattern ruby20pattern = Pattern.compile("ruby (\\d+\\.\\d+\\.\\d)+p(\\d+) \\((.*)\\) \\[(.*)\\]");

    public static String parseRubyVersionString(String rubyVersionString) {

        if (rubyVersionString.contains("1.8")) {

            Matcher matcher = ruby18pattern.matcher(rubyVersionString);

            if (matcher.matches() && matcher.groupCount() == 4) {
                return String.format("%s-p%s", matcher.group(1), matcher.group(3));
            } else {
                throw new IllegalArgumentException("Unable to parse ruby version string.");
            }

        } else {

            Matcher matcher = ruby19pattern.matcher(rubyVersionString);

            if (matcher.matches() && matcher.groupCount() == 4) {
                return String.format("%s-p%s", matcher.group(1), matcher.group(2));
            } else {
                throw new IllegalArgumentException("Unable to parse ruby version string.");
            }

        }
    }

    public static String buildPath(String parentPath, String fileName) {
        return parentPath + File.separator + fileName;
    }

}
