package au.id.wolfe.bamboo.ruby.rvm;


import com.atlassian.fage.Pair;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 */
public class RvmUtil {

    public static String BIN_FOLDER_RELATIVE_PATH = "/bin";
    public static String DEFAULT_GEMSET_SEPARATOR = "@";
    public static String RVM_RUBIES_FOLDER_NAME = "rubies";
    public static String RVM_GEMS_FOLDER_NAME = "gems";
    public static String GLOBAL_GEMSET_NAME = "global";
    public static String MY_RUBY_HOME = "MY_RUBY_HOME";
    public static String GEM_HOME = "GEM_HOME";
    public static String GEM_PATH = "GEM_PATH";
    public static String PATH = "PATH";
    public static String BUNDLE_HOME = "BUNDLE_PATH";
    public static String RVM_RUBY_STRING = "rvm_ruby_string";
    public static String RVM_GEM_SET = "gemset";


    public static Pair<String, String> parseRubyRuntimeName(final String rubyRuntimeName) {

        final StringTokenizer stringTokenizer = new StringTokenizer(rubyRuntimeName, DEFAULT_GEMSET_SEPARATOR);

        if (stringTokenizer.countTokens() == 2) {
            return new Pair<String, String>(stringTokenizer.nextToken(), stringTokenizer.nextToken());
        } else {
            throw new IllegalArgumentException("Could not parse rubyRuntime string, expected something like ruby-1.9.2@rails31, not " + rubyRuntimeName);
        }

    }

    public static String buildExecutablePath(final String rubiesPath, final String rubyName) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rubiesPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(rubyName);
        stringBuilder.append(BIN_FOLDER_RELATIVE_PATH);
        stringBuilder.append(File.separator);
        stringBuilder.append(getExecutableName(rubyName));

        return stringBuilder.toString();
    }

    public static String buildGemHomePath(final String gemsPath, final String rubyName, final String gemSetName) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gemsPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(gemSetDirectoryName(rubyName, gemSetName));

        return stringBuilder.toString();

    }

    public static List<String> buildGemBinSearchPath(final String gemsPath, final String rubyName, final String gemSetName) {

        StringBuilder stringBuilder;

        List<String> searchPathList = Lists.newLinkedList();

        stringBuilder = new StringBuilder();
        stringBuilder.append(buildGemHomePath(gemsPath, rubyName, gemSetName));
        stringBuilder.append(BIN_FOLDER_RELATIVE_PATH);
        searchPathList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder.append(buildGlobalGemPath(gemsPath, rubyName));
        stringBuilder.append(BIN_FOLDER_RELATIVE_PATH);
        searchPathList.add(stringBuilder.toString());

        return searchPathList;
    }

    public static String buildRubiesPath(String rvmInstallPath) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rvmInstallPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(RVM_RUBIES_FOLDER_NAME);

        return stringBuilder.toString();
    }

    public static String buildGemsPath(String rvmInstallPath) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rvmInstallPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(RVM_GEMS_FOLDER_NAME);

        return stringBuilder.toString();
    }

    private static String getExecutableName(String rubyName) {

        if (rubyName.startsWith("jruby")) {
            return "jruby";
        } else {
            return "ruby";
        }

    }

    public static String gemSetName(String rubyName, String gemSetName) {
        return gemSetName.equals(rubyName) ? "default" : gemSetName;
    }

    public static String gemSetDirectoryName(String rubyName, String gemSetName) {
        return gemSetName.equals("default") ? rubyName : rubyName + DEFAULT_GEMSET_SEPARATOR + gemSetName;
    }

    public static String buildRubyHomePath(String rubiesPath, String rubyName) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rubiesPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(rubyName);

        return stringBuilder.toString();

    }

    public static String buildGemPath(String gemsPath, String rubyName, String gemSetName) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildGemHomePath(gemsPath, rubyName, gemSetName));
        stringBuilder.append(File.pathSeparator);
        stringBuilder.append(buildGlobalGemPath(gemsPath, rubyName));

        return stringBuilder.toString();
    }

    private static String buildGlobalGemPath(String gemsPath, String rubyName) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gemsPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(rubyName);
        stringBuilder.append(DEFAULT_GEMSET_SEPARATOR);
        stringBuilder.append(GLOBAL_GEMSET_NAME);

        return stringBuilder.toString();
    }

    public static String buildGemBinPath(String gemsPath, String rubyName, String gemSetName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gemsPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(gemSetDirectoryName(rubyName, gemSetName));
        stringBuilder.append(BIN_FOLDER_RELATIVE_PATH);
        stringBuilder.append(File.pathSeparator);
        stringBuilder.append(gemsPath);
        stringBuilder.append(File.separator);
        stringBuilder.append(rubyName);
        stringBuilder.append(DEFAULT_GEMSET_SEPARATOR);
        stringBuilder.append(GLOBAL_GEMSET_NAME);
        stringBuilder.append(BIN_FOLDER_RELATIVE_PATH);

        return stringBuilder.toString();
    }


    /**
     * Simple function to split a string into tokens which are loaded into a list, note order IS important.
     *
     * @param targets String to split on one or more whitespace characters.
     * @return List containing the tokens
     */
    public static List<String> splitRakeTargets(String targets) {

        if (targets.matches(".*\\s.*")) {
            return Arrays.asList(targets.split("\\s+"));
        } else {
            return Arrays.asList(targets);
        }

    }
}
