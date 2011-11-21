package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.RubyRuntime;
import au.id.wolfe.bamboo.ruby.RubyRuntimeService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sun.jna.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Locates the ruby version manager (RVM) installation then assembles a list of ruby runtimes. THis class currently
 * uses the filesystem to discover ruby installations and their associated gem sets.
 *
 * There are a couple of issues with this approach:
 *  - If there are gems installed in the global and selected gemset there can be issues loading the library.
 *
 * The reason I selected this approach is because RVM uses a lot of shell functions to smooth the experiance for the
 * users, this doesn't jell well with the current method of execution I am using in bamboo.
 *
 * Need to do more research on how other environments, namely IntelliJ/Rubymine deal with these issues.
 */
public class RvmRubyRuntimeService implements RubyRuntimeService {

    private static final Logger log = LoggerFactory.getLogger(RvmRubyRuntimeService.class);

    public static final String SYSTEM_RVM_PATH = "/usr/local/rvm";

    public List<RubyRuntime> getRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newLinkedList();

        if (Platform.isWindows()) {
            log.warn("No RVM on windows bro.");
            return rubyRuntimeList;
        }

        File rvmPath = getRvmPath();

        if (rvmPath == null) {
            log.warn("No RVM installation not found.");
            return rubyRuntimeList;
        }

        File rubiesPath = new File(rvmPath.getAbsolutePath() + File.separator + "rubies");
        File gemsPath = new File(rvmPath.getAbsolutePath() + File.separator + "gems");

        if (rubiesPath.exists() && gemsPath.exists()) {

            for (File rubyPath : rubiesPath.listFiles(new RubyPathFilter())) {

                String rubyName = rubyPath.getName();

                GemPathFilter gemPathFilter = new GemPathFilter(rubyName);

                for (File gemSetPath : gemsPath.listFiles(gemPathFilter)) {

                    String gemSetName = getGemSetName(rubyName, gemSetPath.getName());

                    String globalGemSetPath = buildGlobalGemSetPath(rubyName, gemsPath.getAbsolutePath());

                    RubyRuntime rubyRuntime = buildRubyRuntime(rubyPath, gemSetName, gemSetPath.getAbsolutePath(), globalGemSetPath);

                    log.debug("detected " + rubyRuntime);

                    rubyRuntimeList.add(rubyRuntime);
                }

            }

        }

        return rubyRuntimeList;
    }

    public RubyRuntime getRubyRuntime(String rvmRubyAndGemSetName) {

        File rvmPath = getRvmPath();

        if (rvmPath == null) {
            throw new IllegalArgumentException("No RVM installation not found.");
        }

        File rubyPath = new File(rvmPath.getAbsolutePath() + File.separator + "rubies" + File.separator + getRubyName(rvmRubyAndGemSetName));
        File gemsPath = new File(rvmPath.getAbsolutePath() + File.separator + "gems");

        String gemSetPath = buildGemSetPath(gemsPath, rvmRubyAndGemSetName);

        String globalGemSetPath = buildGlobalGemSetPath(getRubyName(rvmRubyAndGemSetName), gemsPath.getAbsolutePath());

        return buildRubyRuntime(rubyPath, rvmRubyAndGemSetName, gemSetPath, globalGemSetPath);

    }

    @Override
    public String getPathToScript(RubyRuntime rubyRuntime, String command){

        Preconditions.checkNotNull(rubyRuntime, "Ruby runtime required.");
        Preconditions.checkNotNull(rubyRuntime.getBinPath(), "Ruby runtime bin path not set.");

        String[] tokens = rubyRuntime.getBinPath().split(":");

        for (String pathToken:tokens){
            File binPath = new File(pathToken);

            if (binPath.exists()){

                File[] binFileArray = binPath.listFiles();

                for (File binFile: binFileArray){

                    if (binFile.getName().equals(command)  && binFile.canExecute()){
                        return binFile.getAbsolutePath();
                    }
                }
            }
        }

        throw new IllegalArgumentException("No command found with name = " + command);
    }

    public String getRubyName(String rvmRubyAndGemSetName) {
        String[] tokens = rvmRubyAndGemSetName.split("@");
        Preconditions.checkArgument(tokens.length == 2, "Invalid rvm identifier.");
        return tokens[0];
    }

    String buildGemSetPath(File gemsPath, String rvmRubyAndGemSetName) {

        if (rvmRubyAndGemSetName.endsWith("@default")){
            String rubyName = getRubyName(rvmRubyAndGemSetName);
            return new File(gemsPath.getAbsolutePath() + File.separator + rubyName).getAbsolutePath();
        } else {
            return new File(gemsPath.getAbsolutePath() + File.separator + rvmRubyAndGemSetName).getAbsolutePath();
        }

    }

    String buildGlobalGemSetPath(String rubyName, String gemsPath) {
        StringBuilder sb = new StringBuilder();

        sb.append(gemsPath);
        sb.append(File.separator);
        sb.append(rubyName);
        sb.append("@global");

        return sb.toString();
    }

    RubyRuntime buildRubyRuntime(File rubyPath, String gemSetName, String gemSetPath, String globalGemSetPath) {

        String gemPath = String.format("%s:%s", gemSetPath, globalGemSetPath);
        String binPath = String.format("%s:%s", buildBinPath(gemSetPath), buildBinPath(globalGemSetPath));

        return new RubyRuntime(
                gemSetName,
                getRubyExecutablePath(rubyPath),
                rubyPath.getAbsolutePath(),
                gemSetPath,
                gemPath,
                binPath
        );
    }

    String buildBinPath(String gemSetPath) {

        StringBuilder sb = new StringBuilder();

        sb.append(gemSetPath);
        sb.append(File.separator);
        sb.append("bin");

        return sb.toString();
    }

    String getRubyExecutablePath(File rubyPath) {
        return rubyPath.getAbsolutePath() + File.separator + "bin" + File.separator + "ruby";
    }

    String getGemSetName(String rubyName, String gemSetName) {

        if (rubyName.equals(gemSetName)) {
            return String.format("%s@default", rubyName);
        }

        return gemSetName;
    }

    /**
     * Detects the file location of the RVM installation, tries the users home directory then the system installation
     * path.
     *
     * @return File referencing the RVM installation path.
     */
    File getRvmPath() {

        List<String> rvmPaths = Arrays.asList(System.getenv("HOME") + File.separatorChar + ".rvm", SYSTEM_RVM_PATH);

        for (String path : rvmPaths){

            File rvmPath = new File(path);

            if (rvmPath.exists()) {
                return rvmPath;
            }
        }

        return null;
    }

    /**
     * This filter ignores the default ruby directory, which is just a link.
     */
    class RubyPathFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return !file.getName().equals("default");
        }

    }

    /**
     * This filter locates gemsets for a given ruby version, it ignores the global one
     * as this is shared by all gemsets for a given ruby.
     */
    class GemPathFilter implements FileFilter {

        private final String name;

        public GemPathFilter(String name) {
            this.name = name;
        }

        @Override
        public boolean accept(File file) {
            return file.getName().startsWith(name)
                    && !file.getName().endsWith("@global");
        }
    }
}
