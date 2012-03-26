package au.id.wolfe.bamboo.ruby.bundler;

import au.id.wolfe.bamboo.ruby.common.BaseRubyTask;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs bundler to install the gems required by the project.
 */
public class BundlerTask extends BaseRubyTask implements TaskType {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String BUNDLE_INSTALL_ARG = "install";

    @Override
    protected List<String> buildCommandList(String rubyRuntimeName, ConfigurationMap config) {

        if(rvmLocatorService.locateRvmInstallation() != null && rvmLocatorService.locateRvmInstallation().isSystemInstall()){
            throw new IllegalArgumentException("Can't use bundle install task with a system installation of RVM.");
        }

        final RubyLocator rubyLocator = getRubyLocator();

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeName);

        List<String> commandsList = Lists.newLinkedList();

        commandsList.add(rubyRuntime.getRubyExecutablePath());

        commandsList.add(rubyLocator.searchForRubyExecutable(rubyRuntimeName, BUNDLE_COMMAND));
        commandsList.add(BUNDLE_INSTALL_ARG);

        return commandsList;
    }

    @Override
    protected Map<String, String> buildEnvironment(String rubyRuntimeName, ConfigurationMap config) {

        log.info("Using runtime {}", rubyRuntimeName);

        Preconditions.checkArgument(rubyRuntimeName != null);

        Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        return getRubyLocator().buildEnv(rubyRuntimeName, currentEnvVars);
    }

}
