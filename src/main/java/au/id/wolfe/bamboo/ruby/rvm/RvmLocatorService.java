package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.rvm.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.rvm.util.SystemHelper;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * RVM Utility methods, these are entirely  as there is only one filesystem involved.
 */
public class RvmLocatorService {

    public static final String[] KNOWN_RVM_HOME_PATHS = new String[]{"/usr/local/rvm", "/opt/local/rvm"};

    private final FileSystemHelper fileSystemHelper;
    private final SystemHelper systemHelper;

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

        // No RVM on windows at the moment.
        if (SystemUtils.IS_OS_WINDOWS) {
            return null;
        }

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

    private RvmInstallation checkRvmInstallation(String rvmInstallPath, RvmInstallation.Type installType) {

        final String rubiesPath = RvmUtil.buildRvmRubiesPath(rvmInstallPath);

        fileSystemHelper.assertPathExists(rubiesPath, "RVM Installation missing rubies directory");

        final String gemsPath = RvmUtil.buildRvmGemsPath(rvmInstallPath);

        fileSystemHelper.assertPathExists(gemsPath, "RVM Installation missing gems directory");

        return new RvmInstallation(rvmInstallPath, installType, rubiesPath, gemsPath);
    }

    /**
     * Locates the rvm installation on the host and builds an instance of the ruby locator.
     * @return Instance of a Ruby locator.
     */
    @Nullable
    public RubyLocator getRvmRubyLocator() {
        final RvmInstallation rvmInstallation = locateRvmInstallation();

        if (rvmInstallation != null) {
            return new RubyLocator(fileSystemHelper, rvmInstallation);
        }

        // no rvm installed so not able to supply a ruby locator
        return null;
    }

    public interface Constants {
        String LOCAL_RVM_HOME_FOLDER_NAME = ".rvm";
        String RVM_GEMS_FOLDER_NAME = "gems";
        String RVM_RUBIES_FOLDER_NAME = "rubies";
    }
}
