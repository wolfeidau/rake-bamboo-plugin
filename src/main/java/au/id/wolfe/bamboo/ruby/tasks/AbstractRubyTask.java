package au.id.wolfe.bamboo.ruby.tasks;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.util.TaskUtils;

import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.CommonTaskType;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.util.Narrow;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Basis for ruby tasks.
 */
public abstract class AbstractRubyTask implements CommonTaskType {

    public static final String ENVIRONMENT = "environmentVariables";

    protected final Logger log = LoggerFactory.getLogger(AbstractRubyTask.class);

    protected ProcessService processService;

    private RubyLocatorServiceFactory rubyLocatorServiceFactory;

    protected EnvironmentVariableAccessor environmentVariableAccessor;

    protected CapabilityContext capabilityContext;

    public AbstractRubyTask() {

    }

    @NotNull
    public TaskResult execute(@NotNull CommonTaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.newBuilder(taskContext);
        final TaskContext buildTaskContext = Narrow.to(taskContext, TaskContext.class);

        final ConfigurationMap config = taskContext.getConfigurationMap();
        final String rubyRuntimeLabel = config.get("ruby");

        try {

            final RubyLabel rubyLabel = RubyLabel.getLabelFromString(rubyRuntimeLabel);

            Map<String, String> envVars = buildEnvironment(rubyLabel, config);

            List<String> commandsList = buildCommandList(rubyLabel, config);

            ExternalProcess externalProcess = processService.createExternalProcess(taskContext,
                    new ExternalProcessBuilder()
                            .env(envVars)
                            .command(commandsList)
                            .workingDirectory(taskContext.getWorkingDirectory()));

            externalProcess.execute();

            taskResultBuilder.checkReturnCode(externalProcess, 0);

        } catch (IllegalArgumentException e){
            logError(e, buildTaskContext);
            return taskResultBuilder.failedWithError().build();
        } catch (PathNotFoundException e ){
            logError(e, buildTaskContext);
            return taskResultBuilder.failedWithError().build();
        }

        return taskResultBuilder.build();
    }

    private void logError(Throwable e, TaskContext taskContext)
    {
        log.error(e.getMessage(), e);
        if (taskContext != null)
        {
            taskContext.getBuildContext().getErrorCollection().addErrorMessage(e.getMessage());
        }
    }

    /**
     * Invoked as a part of the task execute routine to enable building of a list of elements which will be executed.
     *
     * @param rubyRuntimeLabel The ruby runtime label.
     * @param config           The configuration map.
     * @return List of command elements to be executed.
     */
    protected abstract List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config);

    protected RubyLocator getRubyLocator(String rubyRuntimeManager) {
        if (rubyLocatorServiceFactory == null) {
            rubyLocatorServiceFactory = new RubyLocatorServiceFactory();
        }
        return rubyLocatorServiceFactory.acquireRubyLocator(rubyRuntimeManager);
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    public void setCapabilityContext(CapabilityContext capabilityContext) {
        this.capabilityContext = capabilityContext;
    }

    public void setRubyLocatorServiceFactory(RubyLocatorServiceFactory rubyLocatorServiceFactory) {
        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
    }

    public void setEnvironmentVariableAccessor(EnvironmentVariableAccessor environmentVariableAccessor) {
        this.environmentVariableAccessor = environmentVariableAccessor;
    }

    protected String getRubyExecutablePath(final RubyLabel rubyRuntimeLabel) {
        final Capability capability = capabilityContext.getCapabilitySet().getCapability(TaskUtils.buildCapabilityLabel(rubyRuntimeLabel));
        Preconditions.checkNotNull(capability, "Capability");
        final String rubyRuntimeExecutable = capability.getValue();
        Preconditions.checkNotNull(rubyRuntimeExecutable, "rubyRuntimeExecutable");
        return rubyRuntimeExecutable;
    }

    public Map<String, String> buildEnvironment( RubyLabel rubyRuntimeLabel, ConfigurationMap config ) {
    
        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());
    
        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();
    
        // Get the variables from our configuration
        final String environmentVariables = config.get(ENVIRONMENT);
    
        Map<String, String> configEnvVars = environmentVariableAccessor.splitEnvironmentAssignments(environmentVariables);
    
        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());
    
        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(),
                getRubyExecutablePath(rubyRuntimeLabel),
                ImmutableMap.<String, String>builder().putAll(currentEnvVars).putAll(configEnvVars).build());
    }

}

