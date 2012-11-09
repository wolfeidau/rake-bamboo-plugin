package au.id.wolfe.bamboo.ruby.rvm;


import au.id.wolfe.bamboo.ruby.common.RubyRuntimeName;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * All the RVM utility functions in one place.
 */
public final class RvmUtils {

    public static final String BIN_FOLDER_RELATIVE_PATH = "/bin";
    public static final String DEFAULT_GEMSET_SEPARATOR = "@";
    public static final String RVM_RUBIES_FOLDER_NAME = "rubies";
    public static final String RVM_GEMS_FOLDER_NAME = "gems";
    public static final String GLOBAL_GEMSET_NAME = "global";
    public static final String RVM_RUBY_STRING = "rvm_ruby_string";
    public static final String RVM_GEM_SET = "gemset";

    /**
     * Splits the ruby runtime name into a ruby name and gem set name.
     *
     * @param rubyRuntimeName The name to be split.
     * @return A pair containing the two tokens.
     */
    public static RubyRuntimeName parseRubyRuntimeName(final String rubyRuntimeName) {
        return RubyRuntimeName.parseString(rubyRuntimeName);
    }

    /**
     * Builds full path to the ruby executable given a rubies path and ruby name.
     *
     * @param rubiesPath Absolute path to the rubies within RVM.
     * @param rubyName   The name of the ruby.
     * @return Absolute path to the ruby executable.
     */
    public static String buildRubyExecutablePath(final String rubiesPath, final String rubyName) {

        final StringBuilder rubyExecutablePath = new StringBuilder();
        rubyExecutablePath.append(rubiesPath);
        rubyExecutablePath.append(File.separator);
        rubyExecutablePath.append(rubyName);

        rubyExecutablePath.append(BIN_FOLDER_RELATIVE_PATH);
        rubyExecutablePath.append(File.separator);
        rubyExecutablePath.append(getExecutableName(rubyName));

        return rubyExecutablePath.toString();
    }

    /**
     * Build the absolute path to the ruby gem home.
     *
     * @param gemsPath   Absolute path to the gems within RVM.
     * @param rubyName   The name of the ruby.
     * @param gemSetName The name of the gem set.
     * @return Absolute path to the gem home.
     */
    public static String buildGemHomePath(final String gemsPath, final String rubyName, final String gemSetName) {

        final StringBuilder gemHomePath = new StringBuilder();
        gemHomePath.append(gemsPath);
        gemHomePath.append(File.separator);
        gemHomePath.append(buildGemSetDirectoryName(rubyName, gemSetName));

        return gemHomePath.toString();

    }

    /**
     * Build a list containing paths to search for executables.
     *
     * @param gemsPath   Absolute path to the gems within RVM.
     * @param rubyName   The name of the ruby.
     * @param gemSetName The name of the gem set.
     * @return List of search path elements.
     */
    public static List<String> buildGemBinSearchPath(final String gemsPath, final String rubyName, final String gemSetName) {

        List<String> searchPathList = Lists.newLinkedList();

        final StringBuilder gemPath = new StringBuilder();
        gemPath.append(buildGemHomePath(gemsPath, rubyName, gemSetName));
        gemPath.append(BIN_FOLDER_RELATIVE_PATH);
        searchPathList.add(gemPath.toString());

        final StringBuilder globalGemPath = new StringBuilder();
        globalGemPath.append(buildGlobalGemPath(gemsPath, rubyName));
        globalGemPath.append(BIN_FOLDER_RELATIVE_PATH);
        searchPathList.add(globalGemPath.toString());

        return searchPathList;
    }

    public static String buildRvmRubiesPath(final String rvmInstallPath) {

        final StringBuilder rvmRubiesPath = new StringBuilder();
        rvmRubiesPath.append(rvmInstallPath);
        rvmRubiesPath.append(File.separator);
        rvmRubiesPath.append(RVM_RUBIES_FOLDER_NAME);

        return rvmRubiesPath.toString();
    }

    public static String buildRvmGemsPath(final String rvmInstallPath) {

        final StringBuilder rvmGemsPath = new StringBuilder();
        rvmGemsPath.append(rvmInstallPath);
        rvmGemsPath.append(File.separator);
        rvmGemsPath.append(RVM_GEMS_FOLDER_NAME);

        return rvmGemsPath.toString();
    }

    private static String getExecutableName(final String rubyName) {

        if (rubyName.startsWith("jruby")) {
            return "jruby";
        } else {
            return "ruby";
        }

    }

    public static String buildGemSetName(final String rubyName, final String gemSetName) {
        return gemSetName.equals(rubyName) ? "default" : gemSetName;
    }

    public static String buildGemSetDirectoryName(final String rubyName, final String gemSetName) {
        return gemSetName.equals("default") ? rubyName : rubyName + DEFAULT_GEMSET_SEPARATOR + gemSetName;
    }

    public static String buildRubyHomePath(final String rubiesPath, final String rubyName) {

        final StringBuilder rubyHomePath = new StringBuilder();
        rubyHomePath.append(rubiesPath);
        rubyHomePath.append(File.separator);
        rubyHomePath.append(rubyName);

        return rubyHomePath.toString();

    }

    public static String buildGemPath(final String gemsPath, final String rubyName, final String gemSetName) {

        final StringBuilder gemPath = new StringBuilder();
        gemPath.append(buildGemHomePath(gemsPath, rubyName, gemSetName));
        gemPath.append(File.pathSeparator);
        gemPath.append(buildGlobalGemPath(gemsPath, rubyName));

        return gemPath.toString();
    }

    private static String buildGlobalGemPath(final String gemsPath, final String rubyName) {

        final StringBuilder globalGemPath = new StringBuilder();
        globalGemPath.append(gemsPath);
        globalGemPath.append(File.separator);
        globalGemPath.append(rubyName);
        globalGemPath.append(DEFAULT_GEMSET_SEPARATOR);
        globalGemPath.append(GLOBAL_GEMSET_NAME);

        return globalGemPath.toString();
    }

    public static String buildBinPath(final String rubiesPath, final String gemsPath, final String rubyName, final String gemSetName) {

        List<String> binPathList = buildGemBinSearchPath(gemsPath, rubyName, gemSetName);

        binPathList.add(buildRubyBinPath(rubiesPath, rubyName));

        return StringUtils.join(binPathList, File.pathSeparator);
    }

    public static String buildRubyBinPath(String rubiesPath, String rubyName) {

        final StringBuilder rubyBinPath = new StringBuilder();
        rubyBinPath.append(rubiesPath);
        rubyBinPath.append(File.separator);
        rubyBinPath.append(rubyName);
        rubyBinPath.append(BIN_FOLDER_RELATIVE_PATH);

        return rubyBinPath.toString();
    }

    /**
     * Simple function to split a string into tokens which are loaded into a list, note order IS important.
     *
     * @param targets String to split on one or more whitespace characters.
     * @return List containing the tokens
     */
    public static List<String> splitRakeTargets(final String targets) {

        if (targets.matches(".*\\s.*")) {
            return Arrays.asList(targets.split("\\s+"));
        } else {
            return Arrays.asList(targets);
        }

    }
}
