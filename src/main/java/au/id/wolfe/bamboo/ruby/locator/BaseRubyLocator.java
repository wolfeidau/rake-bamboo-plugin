package au.id.wolfe.bamboo.ruby.locator;

import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.google.common.base.Preconditions;
import org.apache.maven.wagon.PathUtils;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Common logic used in all implementations of the Ruby Locator.
 */
public abstract class BaseRubyLocator {

    protected final Logger log = LoggerFactory.getLogger(BaseRubyLocator.class);

    protected FileSystemHelper fileSystemHelper;

    public String buildExecutablePath(String rubyRuntimeName, String rubyExecutablePath, String command) {

        final String executablePath = PathUtils.dirname(rubyExecutablePath) + File.separator + command;

        log.info("located executable", executablePath);

        Preconditions.checkArgument(fileSystemHelper.executableFileExists(executablePath), "Executable " + command + " not found in ruby bin path.");

        return executablePath;
    }

}
