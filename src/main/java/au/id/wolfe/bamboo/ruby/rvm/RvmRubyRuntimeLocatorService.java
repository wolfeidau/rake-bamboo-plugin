package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.util.SystemHelper;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * RVM Utility methods, these are entirely  as there is only one filesystem involved.
 */
public class RvmRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    private static final Logger log = LoggerFactory.getLogger(RvmRubyRuntimeLocatorService.class);

    public static final String MANAGER_LABEL = "RVM";

    static final String[] KNOWN_RVM_HOME_PATHS = new String[]{"/usr/local/rvm", "/opt/local/rvm"};

    private final FileSystemHelper fileSystemHelper;
    private final SystemHelper systemHelper;

    public RvmRubyRuntimeLocatorService() {
        fileSystemHelper = new FileSystemHelper();
        systemHelper = new SystemHelper();
    }

    public RvmRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper, SystemHelper systemHelper) {
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

        // No RVM on windows at the moment.
        if (SystemUtils.IS_OS_WINDOWS) {
            log.warn("Windows isn't support for RVM installations");
            return null;
        }

        final String userRvmInstallPath = systemHelper.getUserHome() + File.separator + Constants.LOCAL_RVM_HOME_FOLDER_NAME;

        log.info("Searching for rvm installation in users home directory located at - {}", userRvmInstallPath);

        // Is rvm is installed in the users home directory
        if (fileSystemHelper.pathExists(userRvmInstallPath)) {
            return checkRvmInstallation(userRvmInstallPath, RvmInstallation.Type.USER);
        }

        log.info("Search for rvm installation in system paths {}", KNOWN_RVM_HOME_PATHS);

        // Is rvm is installed in one of the system paths
        for (String rvmSystemPath : KNOWN_RVM_HOME_PATHS) {
            if (fileSystemHelper.pathExists(rvmSystemPath)) {
                return checkRvmInstallation(rvmSystemPath, RvmInstallation.Type.SYSTEM);
            }
        }

        // none found
        return null;
    }

    private RvmInstallation checkRvmInstallation(String rvmInstallPath, RvmInstallation.Type installType) {

        final String rubiesPath = RvmUtils.buildRvmRubiesPath(rvmInstallPath);

        fileSystemHelper.assertPathExists(rubiesPath, "RVM Installation missing rubies directory");

        final String gemsPath = RvmUtils.buildRvmGemsPath(rvmInstallPath);

        fileSystemHelper.assertPathExists(gemsPath, "RVM Installation missing gems directory");

        return new RvmInstallation(rvmInstallPath, installType, rubiesPath, gemsPath);
    }

    /**
     * Locates the rvm installation on the host and builds an instance of the ruby locator.
     *
     * @return Instance of a Ruby locator.
     * @throws PathNotFoundException if unable to locate an RVM installation.
     */
    public RubyLocator getRvmRubyLocator() {
        final RvmInstallation rvmInstallation = locateRvmInstallation();

        if (rvmInstallation != null) {
            return new RvmRubyLocator(fileSystemHelper, rvmInstallation);
        }

        // no rvm installed so not able to supply a ruby locator
        throw new PathNotFoundException("Unable to locate RVM installation.");
    }


    /**
     * Check if rvm is installed.
     *
     * @return boolean, true indicates rvm is installed.
     */
    public boolean isRvmInstalled() {
        return locateRvmInstallation() != null;
    }

    public interface Constants {
        String LOCAL_RVM_HOME_FOLDER_NAME = ".rvm";
        String RVM_GEMS_FOLDER_NAME = "gems";
        String RVM_RUBIES_FOLDER_NAME = "rubies";
    }

    @Override
    public RubyLocator getRubyLocator() {
        final RvmInstallation rvmInstallation = locateRvmInstallation();

        if (rvmInstallation != null) {
            return new RvmRubyLocator(fileSystemHelper, rvmInstallation);
        }

        // no rvm installed so not able to supply a ruby locator
        throw new PathNotFoundException("Unable to locate RVM installation.");
    }

    @Override
    public boolean isInstalled() {
        return locateRvmInstallation() != null;
    }

    @Override
    public String getRuntimeManagerName() {
        return MANAGER_LABEL;
    }
}
