package au.id.wolfe.bamboo.ruby.fixtures;

import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmInstallation;

import java.io.File;

import static au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService.Constants;

/**
 * Testing fixture data.
 */
public final class RvmFixtures {

    public static final String USER_HOME = "/home/markw";

    private RvmFixtures() {
    }

    public static RubyRuntime getJRubyRuntimeDefaultGemSet() {
        return new RubyRuntime("jruby-1.6.5", "default", USER_HOME + "/.rvm/rubies/jruby-1.6.5/bin/jruby", USER_HOME + "/.rvm/gems/jruby-1.6.5");
    }

    public static RubyRuntime getMRIRubyRuntimeDefaultGemSet() {
        return new RubyRuntime("ruby-1.9.3-p0", "default", USER_HOME + "/.rvm/rubies/ruby-1.9.3-p0/bin/ruby", USER_HOME + "/.rvm/gems/ruby-1.9.3-p0");
    }

    public static RvmInstallation getUserRvmInstallation() {

        String rvmPath = USER_HOME + File.separator + Constants.LOCAL_RVM_HOME_FOLDER_NAME;

        return new RvmInstallation(rvmPath,
                RvmInstallation.Type.USER,
                rvmPath + File.separator + Constants.RVM_RUBIES_FOLDER_NAME,
                rvmPath + File.separator + Constants.RVM_GEMS_FOLDER_NAME);
    }

    public static RvmInstallation getSystemRvmInstallation() {

        String rvmPath = "/usr/local/rvm";

        return new RvmInstallation(rvmPath, RvmInstallation.Type.SYSTEM,
                rvmPath + File.separator + Constants.RVM_RUBIES_FOLDER_NAME,
                rvmPath + File.separator + Constants.RVM_GEMS_FOLDER_NAME);

    }
}
