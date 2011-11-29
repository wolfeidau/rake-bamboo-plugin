package au.id.wolfe.bamboo.ruby.rvm.util;

import au.id.wolfe.bamboo.ruby.rvm.PathNotFoundException;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Delegate which performs all file system related operations.
 */
public class FileSystemHelper {

    /**
     * Given the path to an executable check if it exists, and verify it is executable.
     *
     * @param fileSystemPath The path.
     * @return True if it exists, otherwise false.
     */
    public boolean executableFileExists(String fileSystemPath) {
        File executable = new File(fileSystemPath);
        return executable.exists() && executable.canExecute();
    }

    /**
     * Given a path check if it exists.
     *
     * @param fileSystemPath The path.
     * @return True if it exists, otherwise false.
     */
    public boolean pathExists(String fileSystemPath) {
        return new File(fileSystemPath).exists();
    }

    /**
     * Check if the path exists.
     *
     * @param path    The filesystem path to check.
     * @param message The basis for the message returned if there is an error.
     * @throws au.id.wolfe.bamboo.ruby.rvm.PathNotFoundException
     *          is thrown when the path supplied doesn't exist.
     */
    public void assertPathExists(String path, String message) {
        if (!pathExists(path)) {
            throw new PathNotFoundException(String.format("%s - %s", message, path));
        }
    }


    /**
     * List the contents of a directory.
     *
     * @param path  The directory path to list.
     * @param regex The regex to check the file against.
     * @return Array containing the a list of files.
     * @throws PathNotFoundException is thrown when the path supplied doesn't exist.
     */
    public List<String> listPathDirNames(final String path, final String regex) {
        return listPathDirNames(path, new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                return file.isDirectory() && fileName.matches(regex);
            }
        });
    }

    /**
     * List the contents of a directory.
     *
     * @param path The directory path to list.
     * @return Array containing the a list of files.
     * @throws PathNotFoundException is thrown when the path supplied doesn't exist.
     */
    public List<String> listPathDirNames(final String path) {
        return listPathDirNames(path, new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return file.isDirectory();
            }
        });
    }

    /**
     * List the contents of a directory applying the given file name filter.
     *
     * @param path           The directory path to list.
     * @param fileNameFilter File name filter to apply.
     * @return Array containing the a list of files.
     * @throws PathNotFoundException is thrown when the path supplied doesn't exist.
     */
    public List<String> listPathDirNames(final String path, final FilenameFilter fileNameFilter) {

        File file = new File(path);

        if (!file.exists()) {
            throw new PathNotFoundException(String.format("%s - %s", "The path supplied doesn't exist", path));
        }

        Preconditions.checkArgument(file.isDirectory(), "The path supplied is not a directory.");

        return Arrays.asList(file.list(fileNameFilter));

    }
}
