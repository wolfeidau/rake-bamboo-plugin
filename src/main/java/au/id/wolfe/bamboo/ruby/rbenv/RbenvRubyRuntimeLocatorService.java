package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.util.SystemHelper;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 */
public class RbenvRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    private static final Logger log = LoggerFactory.getLogger(RbenvRubyRuntimeLocatorService.class);

    private static final String RBENV_DEFAULT_PATH = ".rbenv";

    public static final String MANAGER_LABEL = "rbenv";

    private final FileSystemHelper fileSystemHelper;
    private final SystemHelper systemHelper;

    public RbenvRubyRuntimeLocatorService() {
        fileSystemHelper = new FileSystemHelper();
        systemHelper = new SystemHelper();
    }

    public RbenvRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper, SystemHelper systemHelper) {
        this.fileSystemHelper = fileSystemHelper;
        this.systemHelper = systemHelper;
    }

    @Override
    public RubyLocator getRubyLocator() {
        // No RVM on windows at the moment.
        if (SystemUtils.IS_OS_WINDOWS) {
            log.warn("Windows isn't support for RVM installations");
            return null;
        }

        final String userRbenvInstallPath = systemHelper.getUserHome() + File.separator + RBENV_DEFAULT_PATH;

        log.info("Searching for rbenv installation in users home directory located at - {}", userRbenvInstallPath);

        // Is rbenv is installed in the users home directory
        if (fileSystemHelper.pathExists(userRbenvInstallPath)) {
            return checkRbenvInstallation(userRbenvInstallPath);
        }

        // none found
        return null;
    }

    private RubyLocator checkRbenvInstallation(String userRbenvInstallPath) {

        // check whether the install directory
        final String rubiesPath = RbenvUtils.buildRbenvRubiesPath(userRbenvInstallPath);

        fileSystemHelper.assertPathExists(rubiesPath, "Rbenv Installation missing rubies directory");

        // return the rbenv ruby locator
        return new RbenvRubyLocator(fileSystemHelper, userRbenvInstallPath);
    }


    @Override
    public boolean isInstalled() {
        return getRubyLocator() != null;
    }

    @Override
    public String getRuntimeManagerName() {
        return MANAGER_LABEL;
    }
}
