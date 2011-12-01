package au.id.wolfe.bamboo.ruby.common;

import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
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
public abstract class BaseRubyTask {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    protected final Logger log = LoggerFactory.getLogger(BaseRubyTask.class);

    protected final ProcessService processService;
    protected final RvmLocatorService rvmLocatorService;
    protected final EnvironmentVariableAccessor environmentVariableAccessor;

    public BaseRubyTask(ProcessService processService, RvmLocatorService rvmLocatorService, EnvironmentVariableAccessor environmentVariableAccessor) {

        this.processService = processService;
        this.rvmLocatorService = rvmLocatorService;
        this.environmentVariableAccessor = environmentVariableAccessor;

    }

    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        Map<String, String> envVars = buildEnvironment(taskContext);

        List<String> commandsList = buildCommandList(taskContext);

        ExternalProcess externalProcess = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .env(envVars)
                        .command(commandsList)
                        .workingDirectory(taskContext.getWorkingDirectory()));

        externalProcess.execute();

        return taskResultBuilder.checkReturnCode(externalProcess, 0).build();

    }

    protected abstract Map<String,String> buildEnvironment(@NotNull TaskContext taskContext);

    protected abstract List<String> buildCommandList(@NotNull TaskContext taskContext);

    protected RubyLocator getRubyLocator() {
        return rvmLocatorService.getRvmRubyLocator();
    }

}

