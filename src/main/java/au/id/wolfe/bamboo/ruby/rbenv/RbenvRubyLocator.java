package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.atlassian.fage.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.util.EnvUtils.filterList;

/**
 * rbenv ruby locator
 * <p/>
 * Some things we know:
 * - You will typically only have one version of a given ruby installed at one time.
 * - There are no gemsets (YAY!)
 */
public class RbenvRubyLocator implements RubyLocator {

    private final FileSystemHelper fileSystemHelper;
    private final String userRbenvInstallPath;

    public RbenvRubyLocator(FileSystemHelper fileSystemHelper, String userRbenvInstallPath) {
        this.fileSystemHelper = fileSystemHelper;
        this.userRbenvInstallPath = userRbenvInstallPath;
    }

    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        Map<String, String> filteredRubyEnv = Maps.newHashMap();

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        // As everything is static with the rbenv ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        for (Map.Entry<String, String> entry : currentEnv.entrySet()) {
            if (!filterList.contains(entry.getKey())) {
                filteredRubyEnv.put(entry.getKey(), entry.getValue());
            }
        }

        // prepend the ruby bin director to the path.
        if (currentEnv.containsKey("PATH")){
            String pathEnvEntry = currentEnv.get("PATH");
            filteredRubyEnv.put("PATH", RbenvUtils.buildRbenvRubyBinDirectoryPath(userRbenvInstallPath, rubyRuntime.getRubyName()) + File.pathSeparator + pathEnvEntry);
        }


        return filteredRubyEnv;
    }

    @Override
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        RubyRuntime rubyRuntime = getRubyRuntime(rubyRuntimeName);

        // search the ruby bin directory for the command
        String commandPath = RbenvUtils.buildRbenvRubyBinPath(userRbenvInstallPath, rubyRuntime.getRubyName(), name);

        fileSystemHelper.executableFileExists(commandPath);

        return commandPath;
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyName, String gemSetName) {

        final String rubyExecutablePath = RbenvUtils.buildRubyExecutablePath(userRbenvInstallPath, rubyName);
        fileSystemHelper.assertPathExists(rubyExecutablePath, "Unable to location ruby executable for " + rubyName);

        return new RubyRuntime(rubyName, gemSetName, rubyExecutablePath, null);
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {

        Pair<String, String> rubyRuntimeTokens = RbenvUtils.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.left();
        final String gemSetName = rubyRuntimeTokens.right();

        return getRubyRuntime(rubyName, gemSetName);
    }

    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newArrayList();

        List<String> rubiesList = fileSystemHelper.listPathDirNames(RbenvUtils.buildRbenvRubiesPath(userRbenvInstallPath));

        for (String rubyPath : rubiesList) {
            rubyRuntimeList.add(getRubyRuntime(rubyPath, RbenvUtils.DEFAULT_GEMSET_NAME));
        }

        return rubyRuntimeList;
    }

    @Override
    public boolean hasRuby(String rubyRuntimeName) {

        Pair<String, String> rubyRuntimeTokens = RbenvUtils.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.left();

        final String rubyExecutablePath = RbenvUtils.buildRubyExecutablePath(userRbenvInstallPath, rubyName);

        return fileSystemHelper.pathExists(rubyExecutablePath);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
