package au.id.wolfe.bamboo.ruby.fixtures;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmInstallation;

import java.io.File;

import static au.id.wolfe.bamboo.ruby.rvm.RvmRubyRuntimeLocatorService.Constants;

/**
 * Testing fixture data.
 */
public final class RvmFixtures {

    public static final String USER_HOME = "/home/markw";
    public static final String TEST_CURRENT_PATH = "/usr/bin:/bin";

    public static final String GEM_HOME = USER_HOME + "/.rvm/gems/ruby-1.9.3-p0";
    public static final String BUNDLE_HOME = GEM_HOME;
    public static final String BUNDLER_PATH = USER_HOME + "/.rvm/gems/ruby-1.9.3-p0/bin/bundle";
    public static final String RAKE_PATH = USER_HOME + "/.rvm/gems/ruby-1.9.3-p0/bin/rake";
    public static final String CAP_PATH = USER_HOME + "/.rvm/gems/ruby-1.9.3-p0/bin/cap";

    private RvmFixtures() {
    }

    public static RubyRuntime getJRubyRuntimeDefaultGemSet() {
        return new RubyRuntime("jruby-1.6.5", "default", USER_HOME + "/.rvm/rubies/jruby-1.6.5/bin/jruby", USER_HOME + "/.rvm/gems/jruby-1.6.5");
    }

    public static RubyRuntime getMRIRubyRuntimeDefaultGemSet() {
        return new RubyRuntime("ruby-1.9.3-p0", "default", USER_HOME + "/.rvm/rubies/ruby-1.9.3-p0/bin/ruby", USER_HOME + "/.rvm/gems/ruby-1.9.3-p0");
    }

    public static RubyRuntime getMRIRubyRuntimeRails31GemSet() {
        return new RubyRuntime("ruby-1.9.3-p0", "rails31", USER_HOME + "/.rvm/rubies/ruby-1.9.3-p0/bin/ruby", USER_HOME + "/.rvm/gems/ruby-1.9.3-p0@rails31");
    }

    public static String getMRIRubyRuntimeDefaultBinPath() {
        return USER_HOME + "/.rvm/gems/ruby-1.9.3-p0/bin" + ":" + USER_HOME + "/.rvm/gems/ruby-1.9.3-p0@global/bin" + ":" + USER_HOME+ "/.rvm/rubies/ruby-1.9.3-p0/bin" + ":" + TEST_CURRENT_PATH;
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
