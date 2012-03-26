package au.id.wolfe.bamboo.ruby.common;

import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.fage.Pair;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Basis for ruby tasks.
 */
public abstract class BaseRubyTask {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    protected final Logger log = LoggerFactory.getLogger(BaseRubyTask.class);

    protected ProcessService processService;
    protected RvmLocatorService rvmLocatorService; // TODO need a mediator for selection of locator service
    protected EnvironmentVariableAccessor environmentVariableAccessor;

    public BaseRubyTask() {

    }

    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        final ConfigurationMap config = taskContext.getConfigurationMap();
        final String rubyRuntimeLabel = config.get("ruby");

        Pair<String, String> rubyRuntimeParams = getRubyLabelPair(rubyRuntimeLabel);
        
        Map<String, String> envVars = buildEnvironment(rubyRuntimeParams.right(), config);

        List<String> commandsList = buildCommandList(rubyRuntimeParams.right(), config);

        ExternalProcess externalProcess = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .env(envVars)
                        .command(commandsList)
                        .workingDirectory(taskContext.getWorkingDirectory()));

        externalProcess.execute();

        return taskResultBuilder.checkReturnCode(externalProcess, 0).build();

    }

    private Pair<String, String> getRubyLabelPair(String rubyRuntimeLabel) {

        final StringTokenizer stringTokenizer = new StringTokenizer(rubyRuntimeLabel, " ");

        if (stringTokenizer.countTokens() == 2) {
            return new Pair<String, String>(stringTokenizer.nextToken(), stringTokenizer.nextToken());
        } else {
            throw new IllegalArgumentException("Could not parse rubyRuntime string, expected something like ruby-1.9.2@rails31, not " + rubyRuntimeLabel);
        }

    }

    protected abstract Map<String,String> buildEnvironment(String rubyRuntimeName, ConfigurationMap config);

    protected abstract List<String> buildCommandList(String rubyRuntimeName, ConfigurationMap config);

    protected RubyLocator getRubyLocator() {
        return rvmLocatorService.getRvmRubyLocator();
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    public void setRvmLocatorService(RvmLocatorService rvmLocatorService) {
        this.rvmLocatorService = rvmLocatorService;
    }

    public void setEnvironmentVariableAccessor(EnvironmentVariableAccessor environmentVariableAccessor) {
        this.environmentVariableAccessor = environmentVariableAccessor;
    }
}

