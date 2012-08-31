package au.id.wolfe.bamboo.ruby.windows;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.ExecHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.LineReader;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.system.SystemRubyUtils.parseRubyVersionString;
import static au.id.wolfe.bamboo.ruby.util.ExecUtils.getGemPathString;
import static au.id.wolfe.bamboo.ruby.util.ExecUtils.getRubyVersionString;

/**
 * Locates Ruby run times installed in windows.
 *
 * This ruby locator uses a pretty simplistic method of runtime detection being the which command in windows. This
 * command searches the path for all instances of the file passed in. This routine will take the first instance of
 * said command and return it.
 */
public class WindowsRubyLocator implements RubyLocator {

    private static final Logger log = LoggerFactory.getLogger(WindowsRubyLocator.class);
    private static final List<String> filterList =
            ImmutableList.of(EnvUtils.MY_RUBY_HOME, EnvUtils.GEM_HOME, EnvUtils.GEM_PATH, EnvUtils.BUNDLE_HOME);

    private final ExecHelper execHelper;

    public WindowsRubyLocator(ExecHelper execHelper) {
        this.execHelper = execHelper;
    }

    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        final Map<String, String> filteredRubyEnv = Maps.newHashMap();

        // As everything is static with the system ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        for (Map.Entry<String, String> entry : currentEnv.entrySet()) {
            if (!filterList.contains(entry.getKey())) {
                filteredRubyEnv.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredRubyEnv;
    }

    @Override
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        // I don't really like this however it will work as long as the user
        // doesn't have more than one ruby installed. Need to do more research
        // on how best to deal with that.
        String rubyExecutable = detectExecutableOnPath(name);

        log.info("ruby executable {}", rubyExecutable);

        if (rubyExecutable != null) {
            return rubyExecutable;
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

            final String rubyExecutable = detectExecutableOnPath("ruby.exe");
            log.info("ruby executable {}", rubyExecutable);

            final String gemExecutable = detectExecutableOnPath("gem.bat");
            log.info("gem executable {}", gemExecutable);

            if (rubyExecutable != null && gemExecutable != null) {

                try {

                    final String rubyVersionString = getRubyVersionString(rubyExecutable);
                    final String version = parseRubyVersionString(rubyVersionString);
                    final String gemPathString = getGemPathString(gemExecutable);

                    rubyRuntimeList.add(new RubyRuntime(version, "default", rubyExecutable, gemPathString));

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

    public String detectExecutableOnPath(String executableName) {

        try {

            final String executablePathOutput = execHelper.getExecutablePath(executableName, true);

            if (executablePathOutput.contains("Could not find files")) {
                throw new PathNotFoundException("Unable to locate executable - " + executableName);
            }

            final LineReader lineReader = new LineReader(new StringReader(executablePathOutput));

            String line;

            if ((line = lineReader.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            log.error("IOException occurred locating executable - " + e.getMessage());
        } catch (InterruptedException e) {
            log.error("Interrupted occurred locating executable - " + e.getMessage());
        }

        throw new PathNotFoundException("Unable to locate executable - " + executableName);
    }

}
