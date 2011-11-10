package au.id.wolfe.bamboo.ruby;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class RakeTask implements TaskType {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    private static final Logger log = LoggerFactory.getLogger(RakeConfigurator.class);

    private final ProcessService processService;
    private final EnvironmentVariableAccessor environmentVariableAccessor;
    private final CapabilityContext capabilityContext;
    private final RubyRuntimeService rubyRuntimeService;

    public RakeTask(ProcessService processService, EnvironmentVariableAccessor environmentVariableAccessor,
                    CapabilityContext capabilityContext, RubyRuntimeService rubyRuntimeService) {

        this.processService = processService;
        this.environmentVariableAccessor = environmentVariableAccessor;
        this.capabilityContext = capabilityContext;
        this.rubyRuntimeService = rubyRuntimeService;

    }

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        final BuildLogger buildLogger = taskContext.getBuildLogger();

        final ConfigurationMap config = taskContext.getConfigurationMap();

        String ruby = config.get("ruby");
        Preconditions.checkNotNull(ruby);

        String targets = config.get("targets");
        Preconditions.checkNotNull(targets);

        log.info("ruby install {}", getRubyInstallationDirectory(ruby));

        RubyRuntime rubyRuntime = rubyRuntimeService.getRubyRuntime(ruby);
        String rakeScriptPath = rubyRuntimeService.getPathToScript(rubyRuntime, "rake");

        Map<String, String> env = Maps.newHashMap();
        env.put("MY_RUBY_HOME", getRubyInstallationDirectory(ruby));
        env.put("GEM_HOME", rubyRuntime.getGemHome());
        env.put("GEM_PATH", rubyRuntime.getGemPath());

        List<String> commandsList = Arrays.asList(rubyRuntime.getPath(), rakeScriptPath, targets);

        ExternalProcess externalProcess = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .env(env)
                        .command(commandsList)
                        .workingDirectory(taskContext.getWorkingDirectory()));

        externalProcess.execute();

        return taskResultBuilder.checkReturnCode(externalProcess, 0).build();
    }

    private String getRubyInstallationDirectory(final String label) {
        final Capability capability = capabilityContext.getCapabilitySet().getCapability(RUBY_CAPABILITY_PREFIX + "." + label);
        Preconditions.checkNotNull(capability, "Capability");
        return new File(capability.getValue()).getParentFile().getParent();
    }

}
