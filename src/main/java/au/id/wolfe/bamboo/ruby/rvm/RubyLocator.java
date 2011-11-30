package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.rvm.util.FileSystemHelper;
import com.atlassian.fage.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class locates ruby installations within an Rvm Installation.
 */
public class RubyLocator {

    public static final String RUBY_GEMSET_REGEX = "[\\w\\d\\.\\-_]+@[\\w\\d\\.\\-_]+";
    final FileSystemHelper fileSystemHelper;
    final RvmInstallation rvmInstallation;

    public RubyLocator(FileSystemHelper fileSystemHelper, RvmInstallation rvmInstallation) {
        this.fileSystemHelper = fileSystemHelper;
        this.rvmInstallation = rvmInstallation;
    }

    /**
     * Given a ruby runtime it will assert it exists, then build the environment variables required
     * to execute commands under this runtime.
     *
     * @param rubyRuntimeName The name of the ruby runtime.
     * @return Map of environment variables.
     */
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        final String rubyHomePath = RvmUtil.buildRubyHomePath(rvmInstallation.getRubiesPath(), rubyRuntime.getRubyName());
        final String rubyName = rubyRuntime.getRubyName();
        final String gemHomePath = rubyRuntime.getGemPath();
        final String gemPath = RvmUtil.buildGemPath(rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());
        final String rvmGemSetName = rubyRuntime.getGemSetName();
        final String rvmPathPrefix = RvmUtil.buildGemBinPath(rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());
        final String currentPath = StringUtils.defaultString(currentEnv.get("PATH"), "");

        Map<String, String> envVars = Maps.newHashMap();

        envVars.put(RvmUtil.MY_RUBY_HOME, rubyHomePath);
        envVars.put(RvmUtil.GEM_HOME, gemHomePath);
        envVars.put(RvmUtil.GEM_PATH, gemPath);
        envVars.put(RvmUtil.BUNDLE_HOME, gemPath);
        envVars.put(RvmUtil.RVM_GEM_SET, rvmGemSetName);
        envVars.put(RvmUtil.RVM_RUBY_STRING, rubyName);
        envVars.put(RvmUtil.PATH, rvmPathPrefix + File.pathSeparator + currentPath);

        return envVars;
    }

    /**
     * Given the name of a ruby script locate the executable in the gem path.
     *
     * @param rubyRuntimeName The name of the ruby runtime.
     * @param name            Name the script/executable.
     * @return The full path of the executable.
     * @throws IllegalArgumentException If the command cannot be located in the gem path.
     */
    @Nullable
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        List<String> gemBinSearchList = RvmUtil.buildGemBinSearchPath(rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());

        for (String pathBinSearch : gemBinSearchList) {

            String binCandidate = FilenameUtils.concat(pathBinSearch, name);

            if (fileSystemHelper.executableFileExists(binCandidate)) {
                return binCandidate;
            }
        }

        throw new IllegalArgumentException("Unable to locate executable " + name + " in gem path for ruby runtime " + rubyRuntimeName);

    }

    /**
     * Given a ruby name 1.9.3-p0 and gem set name rails31 return a ruby runtime object for it.
     * <p/>
     * This also checks the ruby and gem set exist.
     *
     * @param rubyName   The name of the ruby
     * @param gemSetName The name of the gem set
     * @return A ruby runtime.
     * @throws IllegalArgumentException thrown if the ruby runtime name is an invalid format.
     * @throws PathNotFoundException    thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    public RubyRuntime getRubyRuntime(final String rubyName, final String gemSetName) {

        final String rubyExecutableName = RvmUtil.buildRubyExecutablePath(rvmInstallation.getRubiesPath(), rubyName);

        fileSystemHelper.assertPathExists(rubyExecutableName, "Cannot locate ruby executable");

        final String gemHomePath = RvmUtil.buildGemHomePath(rvmInstallation.getGemsPath(), rubyName, gemSetName);

        fileSystemHelper.assertPathExists(gemHomePath, "Cannot locate ruby executable");

        return new RubyRuntime(rubyName, gemSetName, rubyExecutableName, gemHomePath);
    }

    /**
     * Given a ruby runtime name for example 1.9.3-p0@rails31 return a ruby runtime object for it.
     * <p/>
     * This also checks the ruby and gem set exist.
     *
     * @param rubyRuntimeName The name of the ruby runtime
     * @return A ruby runtime.
     * @throws IllegalArgumentException thrown if the ruby runtime name is an invalid format.
     * @throws PathNotFoundException    thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {

        Pair<String, String> rubyRuntimeTokens = RvmUtil.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.left();
        final String gemSetName = rubyRuntimeTokens.right();

        return getRubyRuntime(rubyName, gemSetName);
    }

    /**
     * Build a list of ruby run time and gem set combinations from rvm.
     *
     * @return List of ruby run times and gem sets.
     */
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newArrayList();

        List<String> rubiesList = fileSystemHelper.listPathDirNames(rvmInstallation.getRubiesPath());
        List<String> gemSetList = fileSystemHelper.listPathDirNames(rvmInstallation.getGemsPath());

        for (String rubyName : rubiesList) {
            // locate each ruby to gem set combination
            for (String gemSetDirectoryName : gemSetList) {
                if (gemSetDirectoryName.startsWith(rubyName) && !gemSetDirectoryName.endsWith(RvmUtil.GLOBAL_GEMSET_NAME)) {
                    if (rubyName.equals(gemSetDirectoryName)){
                        rubyRuntimeList.add(getRubyRuntime(rubyName, "default"));
                    } else {
                        Pair<String, String> rubyRuntimeTokens = RvmUtil.parseRubyRuntimeName(gemSetDirectoryName);
                        rubyRuntimeList.add(getRubyRuntime(rubyRuntimeTokens.left(), rubyRuntimeTokens.right()));
                    }
                }
            }
        }

        return rubyRuntimeList;
    }

    /**
     * Given a the name and version of a ruby interpreter check if it is installed in RVM.
     *
     * @param rubyNamePattern will be checked against the rubies installed using String#startsWith
     * @return true if it is installed, otherwise false.
     */
    public boolean hasRuby(String rubyNamePattern) {

        List<String> rubiesList = fileSystemHelper.listPathDirNames(rvmInstallation.getRubiesPath());

        for (String rubyName : rubiesList) {
            if (rubyName.startsWith(rubyNamePattern)) {
                return true;
            }
        }

        return false;
    }

}
