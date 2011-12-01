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

    private static final String BUNDLE_COMMAND = "bundle";

    public BundlerTask(ProcessService processService, RvmLocatorService rvmLocatorService, EnvironmentVariableAccessor environmentVariableAccessor) {
        super(processService, rvmLocatorService, environmentVariableAccessor);
    }

    @Override
    protected List<String> buildCommandList(@NotNull TaskContext taskContext) {

        final ConfigurationMap config = taskContext.getConfigurationMap();
        final RubyLocator rubyLocator = getRubyLocator();

        final String rubyRuntimeName = config.get("ruby");
        Preconditions.checkArgument(rubyRuntimeName != null);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeName);

        List<String> commandsList = Lists.newLinkedList();

        commandsList.add(rubyRuntime.getRubyExecutablePath());

        commandsList.add(rubyLocator.searchForRubyExecutable(rubyRuntimeName, BUNDLE_COMMAND));
        commandsList.add("install");

        return commandsList;
    }

    @Override
    protected Map<String, String> buildEnvironment(@NotNull TaskContext taskContext) {

        final ConfigurationMap config = taskContext.getConfigurationMap();

        String rubyRuntimeName = config.get("ruby");
        Preconditions.checkArgument(rubyRuntimeName != null);

        Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        return getRubyLocator().buildEnv(rubyRuntimeName, currentEnvVars);
    }

}
