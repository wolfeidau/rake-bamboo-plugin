package au.id.wolfe.bamboo.ruby.rvm;

import java.io.File;

import static au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService.*;

/**
 * Assembles and verifies a ruby runtime.
 */
public class RubyRuntimeBuilder {

    private String rubyName;
    private String gemSetName;

    private String rubyExecutablePath;
    private String rubyHome;

    private String gemHome;
    private String gemPath;

    private String binPath;

    private RvmInstallation rvmInstallation;
    private final FileSystemHelper fileSystemHelper;

    public static RubyRuntimeBuilder create(FileSystemHelper fileSystemHelper){
        return new RubyRuntimeBuilder(fileSystemHelper);
    }

    private RubyRuntimeBuilder(FileSystemHelper fileSystemHelper) {
        this.fileSystemHelper = fileSystemHelper;
    }

    public RubyRuntimeBuilder locateRuby(String rubyName) {
        this.rubyName = rubyName;
        rubyHome = rvmInstallation.getRubiesPath() + File.separator + rubyName;
        rubyExecutablePath = rubyHome + Constants.RVM_BIN_FOLDER_RELATIVE_PATH + getExecutableName(rubyName);
        return this;
    }

    public RubyRuntimeBuilder locateGems(String gemSetName){

        gemHome = rvmInstallation.getGemsPath() + File.separator + rubyName;

        return this;
    }

    private String getExecutableName(String rubyName) {

        if (rubyName.startsWith("jruby")) {
            return "jruby";
        } else {
            return "ruby";
        }

    }

    public RubyRuntimeBuilder usingRvm(RvmInstallation rvmInstallation) {
        this.rvmInstallation = rvmInstallation;
        return this;
    }

    public RubyRuntimeBuilder usingRubyRuntime(String rubyName, String gemSetName) {
        this.rubyName = rubyName;
        this.gemSetName = gemSetName;
        return this;
    }

    public RubyRuntime buildAndValidate() {

        rubyHome = rvmInstallation.getRubiesPath() + File.separator + rubyName;
        rubyExecutablePath = rubyHome + Constants.RVM_BIN_FOLDER_RELATIVE_PATH + getExecutableName(rubyName);


        return new RubyRuntime(rubyName, gemSetName);
    }
}
