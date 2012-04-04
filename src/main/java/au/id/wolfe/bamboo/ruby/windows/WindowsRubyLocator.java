package au.id.wolfe.bamboo.ruby.windows;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.atlassian.bamboo.v2.build.agent.capability.ExecutablePathUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.system.SystemRubyUtils.parseRubyVersionString;
import static au.id.wolfe.bamboo.ruby.util.ExecUtils.getGemPathString;
import static au.id.wolfe.bamboo.ruby.util.ExecUtils.getRubyVersionString;

/**
 * Locates Ruby run times installed in windows.
 */
public class WindowsRubyLocator implements RubyLocator {

    private static final Logger log = LoggerFactory.getLogger(WindowsRubyLocator.class);
    private static final List<String> filterList =
            ImmutableList.of(EnvUtils.MY_RUBY_HOME, EnvUtils.GEM_HOME, EnvUtils.GEM_PATH, EnvUtils.BUNDLE_HOME);

    private final FileSystemHelper fileSystemHelper;

    public WindowsRubyLocator(FileSystemHelper fileSystemHelper) {
        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        Map<String, String> filteredRubyEnv = Maps.newHashMap();

        // As everything is static with the system ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        for (String key : currentEnv.keySet()) {
            if (!filterList.contains(key)) {
                filteredRubyEnv.put(key, currentEnv.get(key));
            }
        }

        return filteredRubyEnv;
    }

    @Override
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        // I don't really like this however it will work as long as the user
        // doesn't have more than one ruby installed. Need to do more research
        // on how best to deal with that.
        File rubyExecutable = ExecutablePathUtils.detectExecutableOnPath(name);

        log.info("ruby executable {}", rubyExecutable);

        if (rubyExecutable != null) {
            return rubyExecutable.getPath();
        }

        throw new PathNotFoundException("Ruby command not found for rubyRuntime (" + rubyRuntimeName + ") command - " + name);
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyName, String gemSetName) {

        final String rubyRuntimeName = String.format("%s@%s", rubyName, gemSetName);

        return getRubyRuntime(rubyRuntimeName);
    }

    @Override
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {

        final List<RubyRuntime> rubyRuntimeList = listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            if (rubyRuntime.getRubyRuntimeName().equals(rubyRuntimeName)) {
                return rubyRuntime;
            }
        }

        throw new PathNotFoundException("Ruby runtime not found for - " + rubyRuntimeName);
    }

    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newLinkedList();

        // only currently supports windows
        if (SystemUtils.IS_OS_WINDOWS) {

            File rubyExecutable = ExecutablePathUtils.detectExecutableOnPath("ruby");
            log.info("ruby executable {}", rubyExecutable);

            File gemExecutable = ExecutablePathUtils.detectExecutableOnPath("gem");
            log.info("gem executable {}", gemExecutable);

            if (rubyExecutable != null && gemExecutable != null) {

                try {

                    final String rubyVersionString = getRubyVersionString(rubyExecutable.getPath());
                    final String version = parseRubyVersionString(rubyVersionString);
                    final String gemPathString = getGemPathString(gemExecutable.getPath());

                    rubyRuntimeList.add(new RubyRuntime(version, "default", rubyExecutable.getPath(), gemPathString));

                } catch (IOException e) {
                    log.error("IO Exception occurred trying to build Ruby Runtime - " + e.getMessage());
                } catch (InterruptedException e) {
                    log.error("Interrupted Exception occurred trying to build Ruby Runtime - " + e.getMessage());
                }
            }


        } else {
            log.warn("This plugin currently only supports Windows based operating systems.");
        }

        return rubyRuntimeList;
    }

    @Override
    public boolean hasRuby(String rubyNamePattern) {

        final List<RubyRuntime> rubyRuntimeList = listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            if (rubyRuntime.getRubyName().equals(rubyNamePattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
