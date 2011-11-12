package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.RubyRuntime;
import au.id.wolfe.bamboo.ruby.RubyRuntimeService;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Bamboo task which runs Ruby Rake
 */
public class RakeTask implements TaskType {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    private static final Logger log = LoggerFactory.getLogger(RakeConfigurator.class);

    private final ProcessService processService;
    private final RubyRuntimeService rubyRuntimeService;

    public RakeTask(ProcessService processService, RubyRuntimeService rubyRuntimeService) {

        this.processService = processService;
        this.rubyRuntimeService = rubyRuntimeService;

    }

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);

        final ConfigurationMap config = taskContext.getConfigurationMap();

        String ruby = config.get("ruby");
        Preconditions.checkNotNull(ruby);

        String targets = config.get("targets");
        Preconditions.checkNotNull(targets);

        List<String> targetList = splitTargets(targets);

        RubyRuntime rubyRuntime = rubyRuntimeService.getRubyRuntime(ruby);
        String rakeScriptPath = rubyRuntimeService.getPathToScript(rubyRuntime, "rake");

        log.info("ruby install {}", rubyRuntime.getRubyHome());

        Map<String, String> env = Maps.newHashMap();

        env.put("MY_RUBY_HOME", rubyRuntime.getRubyHome());
        env.put("GEM_HOME", rubyRuntime.getGemHome());
        env.put("GEM_PATH", rubyRuntime.getGemPath());

        List<String> commandsList = Lists.newLinkedList();

        commandsList.add(rubyRuntime.getPath());
        commandsList.add(rakeScriptPath);
        commandsList.addAll(targetList);

        ExternalProcess externalProcess = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .env(env)
                        .command(commandsList)
                        .workingDirectory(taskContext.getWorkingDirectory()));

        externalProcess.execute();

        return taskResultBuilder.checkReturnCode(externalProcess, 0).build();
    }

    public static List<String> splitTargets(String targets) {

        if (targets.matches(".*\\s.*")) {
            return Arrays.asList(targets.split("\\s"));
        } else {
            return Arrays.asList(targets);
        }

    }

}
