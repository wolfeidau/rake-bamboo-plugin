package au.id.wolfe.bamboo.ruby.rvm;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Delegate which performs all file system related operations.
 */
public class FileSystemHelper {

    public boolean pathExists(String fileSystemPath) {
        return new File(fileSystemPath).exists();
    }

    /**
     * Check if the path exists.
     *
     * @param path The filesystem path to check.
     * @param message The basis for the message returned if there is an error.
     * @throws PathNotFoundException is thrown when the path supplied doesn't exist.
     */
    public void assertPathExists(String path, String message) {
        if (!pathExists(path)){
            throw new PathNotFoundException(String.format("%s - %s",  message, path));
        }
    }
}
