package au.id.wolfe.bamboo.ruby.system;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various util functions used by the system ruby locator.
 */
public final class SystemRubyUtils {

    static final Pattern pattern = Pattern.compile("ruby (\\d\\.\\d\\.\\d) \\((.*) patchlevel (\\d+)\\) \\[(.*)\\]");

    public static String parseRubyVersionString(String rubyVersionString) {

        Matcher matcher = pattern.matcher(rubyVersionString);

        if (matcher.matches() && matcher.groupCount() == 4) {
            return String.format("%s-p%s", matcher.group(1), matcher.group(3));
        } else {
            throw new IllegalArgumentException("Unable to parse ruby version string.");
        }

    }

    public static String buildPath(String parentPath, String fileName) {
        return parentPath + File.separator + fileName;
    }

}
