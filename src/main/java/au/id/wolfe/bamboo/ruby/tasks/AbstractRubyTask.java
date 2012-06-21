package au.id.wolfe.bamboo.ruby.tasks;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Basis for ruby tasks.
 */
public abstract class AbstractRubyTask implements TaskType {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    protected final Logger log = LoggerFactory.getLogger(AbstractRubyTask.class);

    protected ProcessService processService;

    private RubyLocatorServiceFactory rubyLocatorServiceFactory;

    protected EnvironmentVariableAccessor environmentVariableAccessor;

    public AbstractRubyTask() {

    }

    @NotNull
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        final ConfigurationMap config = taskContext.getConfigurationMap();
        final String rubyRuntimeLabel = config.get("ruby");

        try {

            final RubyLabel rubyLabel = RubyLabel.getLabelFromString(rubyRuntimeLabel);

            Map<String, String> envVars = buildEnvironment(rubyLabel, config);

            List<String> commandsList = buildCommandList(rubyLabel, config);

            ExternalProcess externalProcess = processService.createProcess(taskContext,
                    new ExternalProcessBuilder()
                            .env(envVars)
                            .command(commandsList)
                            .workingDirectory(taskContext.getWorkingDirectory()));

            externalProcess.execute();

            return taskResultBuilder.checkReturnCode(externalProcess, 0).build();

        } catch (IllegalArgumentException e){
            taskContext.getBuildContext().getErrorCollection().addErrorMessage(e.getMessage());
            return TaskResultBuilder.create(taskContext).failedWithError().build();
        } catch (PathNotFoundException e ){
            taskContext.getBuildContext().getErrorCollection().addErrorMessage(e.getMessage());
            return TaskResultBuilder.create(taskContext).failedWithError().build();
        }

    }

    /**
     * Invoked as a part of the task execute routine to enable modifications to the environment.
     *
     * @param rubyRuntimeLabel The ruby runtime label.
     * @param config           The configuration map.
     * @return Map containing final env to be used when executing the task.
     */
    protected abstract Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config);

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

    public void setRubyLocatorServiceFactory(RubyLocatorServiceFactory rubyLocatorServiceFactory) {
        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
    }

    public void setEnvironmentVariableAccessor(EnvironmentVariableAccessor environmentVariableAccessor) {
        this.environmentVariableAccessor = environmentVariableAccessor;
    }
}

