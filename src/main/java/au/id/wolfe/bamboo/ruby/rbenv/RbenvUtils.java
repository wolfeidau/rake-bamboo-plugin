package au.id.wolfe.bamboo.ruby.rbenv;

import com.atlassian.fage.Pair;
import com.google.common.base.Preconditions;

import java.io.File;
import java.util.StringTokenizer;

/**
 * All the rbenv utility functions in one place.
 */
public final class RbenvUtils {

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

    /**
     * This function assembles a path using the supplied attributes. It assumes all executables for a given ruby runtime name
     * are located in that rubies bin directory.
     *
     * @param userRbenvInstallPath The location of rbenv.
     * @param rubyName The name of the ruby runtime.
     * @param commandName The command to append to the path.
     * @return The assembled path.
     */
    public static String buildRbenvRubyBinPath(String userRbenvInstallPath, String rubyName, String commandName) {
        return userRbenvInstallPath +
                File.separator +
                RBENV_VERSIONS_FOLDER_NAME +
                File.separator +
                rubyName +
                BIN_FOLDER_RELATIVE_PATH +
                File.separator +
                commandName;
    }

    /**
     * This function assembles the path to the ruby executable using the supplied attributes.
     *
     * @param userRbenvInstallPath The location of rbenv.
     * @param rubyName The name of the ruby runtime.
     * @return The assembled path.
     */
    public static String buildRubyExecutablePath(String userRbenvInstallPath, String rubyName) {
        return buildRbenvRubyBinPath(userRbenvInstallPath, rubyName, "ruby");
    }

    /**
     * This function assembles the bin path using the supplied attributes.
     *
     * @param userRbenvInstallPath The location of rbenv.
     * @param rubyName The name of the ruby runtime.
     * @return The assembled path.
     */
    public static String buildRbenvRubyBinDirectoryPath(String userRbenvInstallPath, String rubyName) {
        return userRbenvInstallPath +
                File.separator +
                RBENV_VERSIONS_FOLDER_NAME +
                File.separator +
                rubyName +
                BIN_FOLDER_RELATIVE_PATH;
    }
}
