package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.common.RubyRuntimeName;
import au.id.wolfe.bamboo.ruby.locator.BaseRubyLocator;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.maven.wagon.PathUtils;

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
public class RbenvRubyLocator extends BaseRubyLocator implements RubyLocator {

    private final FileSystemHelper fileSystemHelper;
    private final String userRbenvInstallPath;

    public RbenvRubyLocator(FileSystemHelper fileSystemHelper, String userRbenvInstallPath) {
        this.fileSystemHelper = fileSystemHelper;
        this.userRbenvInstallPath = userRbenvInstallPath;
    }

    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, String rubyExecutablePath, Map<String, String> currentEnv) {

        Map<String, String> filteredRubyEnv = Maps.newHashMap();

        // As everything is static with the rbenv ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        for (Map.Entry<String, String> entry : currentEnv.entrySet()) {
            if (!filterList.contains(entry.getKey())) {
                filteredRubyEnv.put(entry.getKey(), entry.getValue());
            }
        }

        // prepend the ruby bin director to the path.
        if (currentEnv.containsKey("PATH")) {
            final String pathEnvEntry = currentEnv.get("PATH");
            filteredRubyEnv.put("PATH", PathUtils.dirname(rubyExecutablePath) + File.pathSeparator + pathEnvEntry);
        }

        return filteredRubyEnv;
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyName, String gemSetName) {

        final String rubyExecutablePath = RbenvUtils.buildRubyExecutablePath(userRbenvInstallPath, rubyName);
        fileSystemHelper.assertPathExists(rubyExecutablePath, "Unable to location ruby executable for " + rubyName);

        return new RubyRuntime(rubyName, gemSetName, rubyExecutablePath, null);
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {

        RubyRuntimeName rubyRuntimeTokens = RbenvUtils.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.getVersion();
        final String gemSetName = rubyRuntimeTokens.getGemSet();

        return getRubyRuntime(rubyName, gemSetName);
    }

    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newArrayList();

        List<String> rubiesList = fileSystemHelper.listPathDirNames(RbenvUtils.buildRbenvRubiesPath(userRbenvInstallPath));

        for (String rubyPath : rubiesList) {
            rubyRuntimeList.add(getRubyRuntime(rubyPath, RubyRuntimeName.DEFAULT_GEMSET_NAME));
        }

        return rubyRuntimeList;
    }

    @Override
    public boolean hasRuby(String rubyRuntimeName) {

        RubyRuntimeName rubyRuntimeTokens = RbenvUtils.parseRubyRuntimeName(rubyRuntimeName);

        final String rubyName = rubyRuntimeTokens.getVersion();

        final String rubyExecutablePath = RbenvUtils.buildRubyExecutablePath(userRbenvInstallPath, rubyName);

        return fileSystemHelper.pathExists(rubyExecutablePath);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
