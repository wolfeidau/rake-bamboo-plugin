package au.id.wolfe.bamboo.ruby.rvm;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService.Constants.DEFAULT_GEMSET_SEPARATOR;

/**
 *
 */
public class RubyRuntimeBuilderTest {

    Logger log = LoggerFactory.getLogger(RubyRuntimeBuilderTest.class);

    static final String RUBY_NAME = "ruby-1.9.3-p0";
    static final String GEM_SET_NAME = "rails31";
    static final String RUBY_RUNTIME_NAME = "1.9.3-p0@rails31";


    @Test
    public void testBuildingRubyRuntime() {

        FileSystemHelper fileSystemHelper = new FileSystemHelper();
        SystemHelper systemHelper = new SystemHelper();
        RvmLocatorService rvmLocatorService = new RvmLocatorService(fileSystemHelper, systemHelper);

        RvmInstallation rvmInstallation = rvmLocatorService.locateRvmInstallation();

        String rubyName = RUBY_NAME;
        String gemSetName = GEM_SET_NAME;

        String rubiesPath = rvmInstallation.getRubiesPath();
        String gemsPath = rvmInstallation.getGemsPath();

        String rubyHomePath = rubiesPath + File.separator + rubyName;

        log.info(rubyHomePath);

        fileSystemHelper.assertPathExists(rubyHomePath, "Ruby path doesn't exist");

        String gemHomePath = gemsPath + File.separator + gemSetName(rubyName, gemSetName);

        log.info(gemHomePath);

        fileSystemHelper.assertPathExists(gemHomePath, "Gem path doesn't exist");

        RubyRuntimeBuilder.create(fileSystemHelper).usingRvm(rvmInstallation).usingRubyRuntime(rubyName, gemSetName).buildAndValidate();

    }

    private String gemSetName(String rubyName, String gemSetName) {
        return GEM_SET_NAME.equals("default") ? rubyName : rubyName + DEFAULT_GEMSET_SEPARATOR + gemSetName;
    }
}
