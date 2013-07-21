package au.id.wolfe.bamboo.ruby.locator;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface RubyLocator {

    /**
     * Given a ruby runtime it will assert it exists, then build the environment variables required
     * to execute commands under this runtime.
     *
     * @param rubyRuntimeName The name of the ruby runtime.
     * @param rubyExecutablePath The full path to the ruby executable.
     * @return Map of environment variables.
     */
    Map<String, String> buildEnv(String rubyRuntimeName, String rubyExecutablePath, Map<String, String> currentEnv);

    /**
     * Given a ruby name 1.9.3-p0 and gem set name rails31 return a ruby runtime object for it.
     * <p/>
     * This also checks the ruby and gem set exist.
     *
     * @param rubyName   The name of the ruby
     * @param gemSetName The name of the gem set
     * @return A ruby runtime.
     * @throws IllegalArgumentException thrown if the ruby runtime name is an invalid format.
     * @throws au.id.wolfe.bamboo.ruby.common.PathNotFoundException
     *                                  thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    RubyRuntime getRubyRuntime(String rubyName, String gemSetName);

    /**
     * Given a ruby runtime name for example 1.9.3-p0@rails31 return a ruby runtime object for it.
     * <p/>
     * This also checks the ruby and gem set exist.
     *
     * @param rubyRuntimeName The name of the ruby runtime
     * @return A ruby runtime.
     * @throws IllegalArgumentException thrown if the ruby runtime name is an invalid format.
     * @throws au.id.wolfe.bamboo.ruby.common.PathNotFoundException
     *                                  thrown if the ruby runtime supplied doesn't exist in rvm.
     */
    RubyRuntime getRubyRuntime(String rubyRuntimeName);

    /**
     * Build a list of ruby run time and gem set combinations from rvm.
     *
     * @return List of ruby run times and gem sets.
     */
    List<RubyRuntime> listRubyRuntimes();

    /**
     * Given a the name and version of a ruby interpreter check if it is installed in RVM.
     *
     * @param rubyNamePattern will be checked against the rubies installed using String#startsWith
     * @return true if it is installed, otherwise false.
     */
    boolean hasRuby(String rubyNamePattern);

    /**
     * Indicates whether the ruby installations are read only, for instance RVM system installations.
     *
     * TODO Need to actually test if stuff is read only rather than guessing based on it's path.
     *
     * @return true if ready only installation(s) of ruby.
     */
    boolean isReadOnly();

    /**
     * Uses the ruby executable to guess the path of associated ruby commands.
     *
     * @param rubyExecutablePath The full path to the ruby executable.
     * @param command The command to generate a path for.
     * @return The full path of the executable.
     */
    String buildExecutablePath(String rubyExecutablePath, String command);
}
