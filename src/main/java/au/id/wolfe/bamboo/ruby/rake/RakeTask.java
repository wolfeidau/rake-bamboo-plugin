package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtil;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class RakeTask implements TaskType {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";
    public static final String RAKE_COMMAND = "rake";

    private final Logger log = LoggerFactory.getLogger(RakeTask.class);


    private final ProcessService processService;
    private final RvmLocatorService rvmLocatorService;
    private final EnvironmentVariableAccessor environmentVariableAccessor;

    public RakeTask(ProcessService processService, RvmLocatorService rvmLocatorService, EnvironmentVariableAccessor environmentVariableAccessor) {

        this.processService = processService;
        this.rvmLocatorService = rvmLocatorService;
        this.environmentVariableAccessor = environmentVariableAccessor;

    }

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        final ConfigurationMap config = taskContext.getConfigurationMap();

        final String ruby = config.get("ruby");
        Preconditions.checkNotNull(ruby);

        final String targets = config.get("targets");
        Preconditions.checkNotNull(targets);

        List<String> targetList = RvmUtil.splitRakeTargets(targets);

        RubyLocator rubyLocator = rvmLocatorService.getRvmRubyLocator();

        Preconditions.checkNotNull(rubyLocator, "Unable to locate RVM installation.");

        RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(ruby);
        String rakeScriptPath = rubyLocator.searchForRubyExecutable(ruby, RAKE_COMMAND);

        Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        Map<String, String> envVars = rubyLocator.buildEnv(ruby, currentEnvVars);

        List<String> commandsList = Lists.newLinkedList();

        commandsList.add(rubyRuntime.getRubyExecutablePath());
        commandsList.add(rakeScriptPath);
        commandsList.addAll(targetList);

        ExternalProcess externalProcess = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .env(envVars)
                        .command(commandsList)
                        .workingDirectory(taskContext.getWorkingDirectory()));

        externalProcess.execute();

        return taskResultBuilder.checkReturnCode(externalProcess, 0).build();
    }

}
