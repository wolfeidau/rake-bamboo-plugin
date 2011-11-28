package au.id.wolfe.bamboo.ruby.rvm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * RVM Utility methods, these are entirely  as there is only one filesystem involved.
 */
public final class RvmLocatorService {

    public static final String[] KNOWN_RVM_HOME_PATHS = new String[]{"/usr/local/rvm", "/opt/local/rvm"};

    final FileSystemHelper fileSystemHelper;
    final SystemHelper systemHelper;

    public RvmLocatorService() {
        fileSystemHelper = new FileSystemHelper();
        systemHelper = new SystemHelper();
    }

    public RvmLocatorService(FileSystemHelper fileSystemHelper, SystemHelper systemHelper) {
        this.fileSystemHelper = fileSystemHelper;
        this.systemHelper = systemHelper;
    }

    /**
     * This method will locate the RVM installation, preferring user installations before system ones.
     *
     * @return RvmInstallation located.
     */
    @Nullable
    public RvmInstallation locateRvmInstallation() {

        // if (Windows) log warn and return

        final String userRvmInstallPath = systemHelper.getUserHome() + File.separator + Constants.LOCAL_RVM_HOME_FOLDER_NAME;

        if (fileSystemHelper.pathExists(userRvmInstallPath)) {
            return checkRvmInstallation(userRvmInstallPath, RvmInstallation.Type.USER);
        }

        for (String rvmSystemPath : KNOWN_RVM_HOME_PATHS) {
            if (fileSystemHelper.pathExists(rvmSystemPath)) {
                return checkRvmInstallation(rvmSystemPath, RvmInstallation.Type.SYSTEM);
            }
        }

        // none found
        return null;
    }

    /**
     * Given a ruby runtime it will assert it exists, then build the environment variables required
     * to execute commands under this runtime.
     *
     * @param rubyRuntime The ruby runtime.
     * @return Map of environment variables.
     */
    public Map<String, String> buildEnv(RubyRuntime rubyRuntime) {


        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Given a the path to a ruby executable return the ruby home path.
     *
     * @param rubyExecutablePath Path to ruby executable.
     * @return The ruby home path.
     */
    public String getRubyHome(String rubyExecutablePath) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Given a ruby runtime build the gem bin search path.
     *
     * @param rubyRuntime The ruby runtime.
     * @return String containing the ruby bin search path.
     */
    public String getGemBinPath(RubyRuntime rubyRuntime) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Given a ruby runtime name for example 1.9.3-p0@rails31 return a ruby runtime object for it.
     * <p/>
     * This also checks the ruby and gem set exist.
     *
     * @param rubyRuntimeName The name of the ruby runtime
     * @return A ruby runtime.
     */
    public RubyRuntime getRubyRuntime(String rubyRuntimeName) {


        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Build a list of ruby run time and gem set combinations from rvm.
     *
     * @return List of ruby run times and gem sets.
     */
    public List<RubyRuntime> listRubyRuntimes() {

/*
        List<RubyRuntime> rubyRuntimeList = Lists.newLinkedList();

        RvmInstallation rvmInstallation = locateRvmInstallation();



        String[] rubiesArray = fileSystemHelper.listPathDirNames(rvmInstallation.getRubiesPath());

        for (String rubyName : rubiesArray){
            // populate the default gem set
            if (fileSystemHelper.pathExists(rvmInstallation.getGemsPath(), rubyName)){


                String rubyHome = rvmInstallation.getRubiesPath() + File.separator + rubyName;
                String gemHome = rvmInstallation.getGemsPath() + File.separator + rubyName;

                RubyRuntime rubyRuntime = new RubyRuntime(rubyName, rubyName, gemHome);
            }
        }
*/

        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Given a the name and version of a ruby interpreter check if it is installed in RVM.
     *
     * @param rubyName Name and version of a ruby interpreter
     * @return true if it is installed, otherwise false.
     */
    public boolean hasRuby(String rubyName) {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private RubyRuntime buildRubyRuntime(RvmInstallation rvmInstallation, String rubyName, String gemSetName){

        String rubyRuntimeName = rubyName + Constants.DEFAULT_GEMSET_SEPARATOR + gemSetName;
        String gemHome = rvmInstallation.getGemsPath()+ File.separator + gemSetName;
        String globalGemHome = rvmInstallation.getGemsPath()+ File.separator + Constants.RVM_GLOBAL_GEMS_FOLDER_NAME;


        return null;
    }

    private RvmInstallation checkRvmInstallation(String rvmInstallPath, RvmInstallation.Type installType){

        //Preconditions.checkNotNull(rvmInstallPath, "No RVM installation found.");

        String rubiesPath = rvmInstallPath + File.separator + Constants.RVM_RUBIES_FOLDER_NAME;

        Preconditions.checkState(fileSystemHelper.pathExists(rubiesPath), "RVM Installation missing rubies directory.");

        //rvmInstallation.setRubiesPath(rubiesPath);

        String gemsPath = rvmInstallPath + File.separator + Constants.RVM_GEMS_FOLDER_NAME;

        Preconditions.checkState(fileSystemHelper.pathExists(gemsPath), "RVM Installation missing rubies directory.");

        //rvmInstallation.setGemsPath(gemsPath);

        return new RvmInstallation(rvmInstallPath, installType, rubiesPath, gemsPath);
    }

    public interface Constants {

        String LOCAL_RVM_HOME_FOLDER_NAME = ".rvm";
        String RVM_GEMS_FOLDER_NAME = "gems";
        String RVM_GLOBAL_GEMS_FOLDER_NAME = "gems";
        String RVM_RUBIES_FOLDER_NAME = "rubies";
        String RVM_BIN_FOLDER_RELATIVE_PATH = "/bin";
        String DEFAULT_GEMSET_SEPARATOR = "@";
        String RVM_GEMSET_SEPARATOR_ENVVAR = "rvm_gemset_separator";
        String GLOBAL_GEMSET_NAME = "global";

        String MY_RUBY_HOME = "MY_RUBY_HOME";
        String GEM_HOME = "GEM_HOME";
        String GEM_PATH = "GEM_PATH";
        String BUNDLE_HOME = "BUNDLE_PATH";
        String RVM_RUBY_STRING = "rvm_ruby_string";
        String RVM_GEM_SET = "gemset";
    }
}
