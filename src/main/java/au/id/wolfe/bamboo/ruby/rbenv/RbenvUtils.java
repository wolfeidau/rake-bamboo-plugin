package au.id.wolfe.bamboo.ruby.rbenv;

import com.atlassian.fage.Pair;
import com.google.common.base.Preconditions;

import java.io.File;
import java.util.StringTokenizer;

/**
 * All the rbenv utility functions in one place.
 */
public class RbenvUtils {

    public static final String BIN_FOLDER_RELATIVE_PATH = "/bin";
    public static final String RBENV_VERSIONS_FOLDER_NAME = "versions";
    public static final String DEFAULT_GEMSET_SEPARATOR = "@";
    public static final String DEFAULT_GEMSET_NAME = "default";

    public static String buildRbenvRubiesPath(String userRbenvInstallPath) {
        return userRbenvInstallPath +
                File.separator +
                RBENV_VERSIONS_FOLDER_NAME;
    }

    /**
     * Splits the ruby runtime name into a ruby name and gem set name.
     *
     * @param rubyRuntimeName The name to be split.
     * @return A pair containing the two tokens.
     */
    public static Pair<String, String> parseRubyRuntimeName(final String rubyRuntimeName) {

        final StringTokenizer stringTokenizer = new StringTokenizer(rubyRuntimeName, DEFAULT_GEMSET_SEPARATOR);

        if (stringTokenizer.countTokens() == 2) {

            Pair<String, String> tokens = new Pair<String, String>(stringTokenizer.nextToken(), stringTokenizer.nextToken());

            // ensure the second token is default as we can't have any gem sets.
            Preconditions.checkArgument(tokens.right().equals(DEFAULT_GEMSET_NAME), "Bad gem set, expected only default gemset for rbenv runtimes.");

            return tokens;

        } else {
            throw new IllegalArgumentException("Could not parse rubyRuntime string, expected something like ruby-1.9.2-p123@default, not " + rubyRuntimeName);
        }

    }

    public static String buildRubyExecutablePath(String userRbenvInstallPath, String rubyName) {
        return userRbenvInstallPath +
                File.separator +
                RBENV_VERSIONS_FOLDER_NAME +
                File.separator +
                rubyName +
                BIN_FOLDER_RELATIVE_PATH +
                File.separator +
                "ruby";
    }
}
