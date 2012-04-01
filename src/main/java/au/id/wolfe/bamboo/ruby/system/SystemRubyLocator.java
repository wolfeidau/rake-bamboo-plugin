package au.id.wolfe.bamboo.ruby.system;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtil;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.system.SystemRubyUtils.buildPath;
import static au.id.wolfe.bamboo.ruby.system.SystemRubyUtils.parseRubyVersionString;
import static au.id.wolfe.bamboo.ruby.util.ExecUtil.cmdExec;

/**
 * Locates system ruby installations.
 * <p/>
 * This assumes that
 * - the shell environment MAY be tainted by RVM so it is avoided.
 * - the 'gem' executable is in the same path as the 'ruby' executable.
 * - at the moment I will locate only one installation to keep it simple.
 */
public class SystemRubyLocator implements RubyLocator {

    private static final Logger log = LoggerFactory.getLogger(SystemRubyLocator.class);

    List<String> searchPaths = ImmutableList.of("/usr/bin", "/usr/local/bin");

    final FileSystemHelper fileSystemHelper;

    public SystemRubyLocator(FileSystemHelper fileSystemHelper) {
        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public Map<String, String> buildEnv(String rubyRuntimeName, Map<String, String> currentEnv) {

        // As everything is static with the system ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        //currentEnv.remove(EnvUtils.MY_RUBY_HOME);
        //currentEnv.remove(EnvUtils.GEM_HOME);
        //currentEnv.remove(EnvUtils.GEM_PATH);
        //currentEnv.remove(EnvUtils.BUNDLE_HOME);
        //currentEnv.remove(RvmUtil.RVM_GEM_SET);
        //currentEnv.remove(RvmUtil.RVM_RUBY_STRING);

        return currentEnv;
    }

    @Override
    public String searchForRubyExecutable(String rubyRuntimeName, String name) {

        // currently commands are found in the /usr/bin directory or not at all.
        for (String path : searchPaths){
            if (fileSystemHelper.executableFileExists(path, name)){
                return path + File.separator + name;
            }
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

    //
    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newLinkedList();

        // only currently supports *nix
        if (SystemUtils.IS_OS_UNIX) {
            for (String path : searchPaths) {


                if (fileSystemHelper.pathExists(path, "ruby") && fileSystemHelper.pathExists(path, "gem")) {
                    try {

                        final String rubyExecutablePath = buildPath(path, "ruby");
                        final String rubyVersionString = getRubyVersionString(rubyExecutablePath);
                        final String version = parseRubyVersionString(rubyVersionString);

                        final String gemPathString = getGemPathString(buildPath(path, "gem"));

                        rubyRuntimeList.add(new RubyRuntime(version, "default", rubyExecutablePath, gemPathString));
                    } catch (IOException e) {
                        // todo log error.
                    } catch (InterruptedException e) {
                        // todo log error.
                    }
                }

            }
        }

        return rubyRuntimeList;
    }

    @Override
    public boolean hasRuby(String rubyNamePattern) {

        List<RubyRuntime> rubyRuntimeList = listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            if (rubyRuntime.getRubyName().equals(rubyNamePattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    private String getGemPathString(String gemExecutablePath) throws IOException, InterruptedException {

        final String line = gemExecutablePath + " environment gempath";

        final StringBuffer output = new StringBuffer();

        int exitValue = cmdExec(line, output);

        log.info("ruby exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("ruby exec result = {}", output.toString());
            return output.toString();
        } else {
            throw new IllegalArgumentException("Ruby executable failed to run.");
        }
    }


    private String getRubyVersionString(String rubyExecutablePath) throws IOException, InterruptedException {

        final String line = rubyExecutablePath + " -v";

        final StringBuffer output = new StringBuffer();

        int exitValue = cmdExec(line, output);

        log.info("ruby exit code  = {}", exitValue);

        if (exitValue == 0) {
            log.info("ruby exec result = {}", output.toString());
            return output.toString();
        } else {
            throw new IllegalArgumentException("Ruby executable failed to run.");
        }
    }

}
