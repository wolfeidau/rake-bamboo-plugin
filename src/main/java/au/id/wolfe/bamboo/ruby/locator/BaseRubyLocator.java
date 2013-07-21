package au.id.wolfe.bamboo.ruby.locator;

import com.google.common.base.Preconditions;
import org.apache.maven.wagon.PathUtils;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;

/**
 * Common logic used in all implementations of the Ruby Locator.
 */
public class BaseRubyLocator {

    public String buildExecutablePath(String rubyExecutablePath, String command) {

        final String executablePath = PathUtils.dirname(rubyExecutablePath) + File.separator + command;

        Preconditions.checkArgument(FileUtils.fileExists(executablePath), "Executable " + command + " not found in ruby bin path.");

        return executablePath;
    }

}
