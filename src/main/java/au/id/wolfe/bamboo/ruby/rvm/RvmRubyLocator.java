package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.common.RubyRuntimeName;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class locates ruby installations within an Rvm Installation.
 */
public class RvmRubyLocator implements RubyLocator {

    final FileSystemHelper fileSystemHelper;
    final RvmInstallation rvmInstallation;

    public RvmRubyLocator(FileSystemHelper fileSystemHelper, RvmInstallation rvmInstallation) {
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
    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        final String rubyHomePath = RvmUtils.buildRubyHomePath(rvmInstallation.getRubiesPath(), rubyRuntime.getRubyName());
        final String rubyName = rubyRuntime.getRubyName();
        final String gemHomePath = rubyRuntime.getGemPath();
        final String gemPath = RvmUtils.buildGemPath(rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());
        final String rvmGemSetName = rubyRuntime.getGemSetName();
        final String rvmPathPrefix = RvmUtils.buildBinPath(rvmInstallation.getRubiesPath(), rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());

        // get the existing path.
        final String currentPath = StringUtils.defaultString(currentEnv.get("PATH"), "");

        Map<String, String> envVars = Maps.newHashMap();

        // propagate existing environment
        envVars.putAll(currentEnv);

        // overwrite all the ruby related variables
        envVars.put(EnvUtils.MY_RUBY_HOME, rubyHomePath);
        envVars.put(EnvUtils.GEM_HOME, gemHomePath);
        envVars.put(EnvUtils.GEM_PATH, gemPath);
        envVars.put(EnvUtils.BUNDLE_HOME, gemHomePath);
        envVars.put(RvmUtils.RVM_GEM_SET, rvmGemSetName);
        envVars.put(RvmUtils.RVM_RUBY_STRING, rubyName);
        envVars.put(EnvUtils.PATH, rvmPathPrefix + File.pathSeparator + currentPath);

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
    @Override
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        List<String> gemBinSearchList = RvmUtils.buildGemBinSearchPath(rvmInstallation.getGemsPath(), rubyRuntime.getRubyName(), rubyRuntime.getGemSetName());

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
     * @throws au.id.wolfe.bamboo.ruby.common.PathNotFoundException
     *                                  thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    @Override
    public RubyRuntime getRubyRuntime(final String rubyName, final String gemSetName) {

        final String rubyExecutableName = RvmUtils.buildRubyExecutablePath(rvmInstallation.getRubiesPath(), rubyName);

        fileSystemHelper.assertPathExists(rubyExecutableName, "Cannot locate ruby executable");

        final String gemHomePath = RvmUtils.buildGemHomePath(rvmInstallation.getGemsPath(), rubyName, gemSetName);

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
     * @throws au.id.wolfe.bamboo.ruby.common.PathNotFoundException
     *                                  thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    @Override
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {

        RubyRuntimeName rubyRuntimeTokens = RvmUtils.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.getVersion();
        final String gemSetName = rubyRuntimeTokens.getGemSet();

        return getRubyRuntime(rubyName, gemSetName);
    }

    /**
     * Build a list of ruby run time and gem set combinations from rvm.
     *
     * @return List of ruby run times and gem sets.
     */
    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newArrayList();

        List<String> rubiesList = fileSystemHelper.listPathDirNames(rvmInstallation.getRubiesPath());
        List<String> gemSetList = fileSystemHelper.listPathDirNames(rvmInstallation.getGemsPath());

        for (String rubyName : rubiesList) {
            // locate each ruby to gem set combination
            for (String gemSetDirectoryName : gemSetList) {
                if (gemSetDirectoryName.startsWith(rubyName) && !gemSetDirectoryName.endsWith(RvmUtils.GLOBAL_GEMSET_NAME)) {
                    if (rubyName.equals(gemSetDirectoryName)) {
                        rubyRuntimeList.add(getRubyRuntime(rubyName, "default"));
                    } else {
                        RubyRuntimeName rubyRuntimeTokens = RvmUtils.parseRubyRuntimeName(gemSetDirectoryName);
                        rubyRuntimeList.add(getRubyRuntime(rubyRuntimeTokens.getVersion(), rubyRuntimeTokens.getGemSet()));
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
    @Override
    public boolean hasRuby(String rubyNamePattern) {

        List<String> rubiesList = fileSystemHelper.listPathDirNames(rvmInstallation.getRubiesPath());

        for (String rubyName : rubiesList) {
            if (rubyName.startsWith(rubyNamePattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isReadOnly() {
        return rvmInstallation.isSystemInstall();
    }
}
