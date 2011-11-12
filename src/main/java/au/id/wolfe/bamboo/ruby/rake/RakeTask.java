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
 * Bamboo task which executes Ruby Rake.
 *
 * This class sets up populates environment required to run a ruby process, while enabling it to locate the gems
 * associated with that installation.
 *
 */
public class RakeTask implements TaskType {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    private static final Logger log = LoggerFactory.getLogger(RakeConfigurator.class);
    
    public static final String RAKE_COMMAND = "rake";
    
    public static final String MY_RUBY_HOME_ENV_VAR = "MY_RUBY_HOME";
    public static final String GEM_HOME_ENV_VAR = "GEM_HOME";
    public static final String GEM_PATH_ENV_VAR = "GEM_PATH";

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

        final String ruby = config.get("ruby");
        Preconditions.checkNotNull(ruby);

        final String targets = config.get("targets");        
        Preconditions.checkNotNull(targets);

        List<String> targetList = splitTargets(targets);

        RubyRuntime rubyRuntime = rubyRuntimeService.getRubyRuntime(ruby);
        String rakeScriptPath = rubyRuntimeService.getPathToScript(rubyRuntime, RAKE_COMMAND);

        log.info("ruby install {}", rubyRuntime.getRubyHome());

        Map<String, String> env = Maps.newHashMap();

        env.put(MY_RUBY_HOME_ENV_VAR, rubyRuntime.getRubyHome());
        env.put(GEM_HOME_ENV_VAR, rubyRuntime.getGemHome());
        env.put(GEM_PATH_ENV_VAR, rubyRuntime.getGemPath());

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

    /**
     * Simple function to split a string into tokens which are loaded into a list, note order IS important.
     *
     * @param targets String to split on one or more whitespace characters.
     * @return List containing the tokens
     */
    public static List<String> splitTargets(String targets) {

        if (targets.matches(".*\\s.*")) {
            return Arrays.asList(targets.split("\\s+"));
        } else {
            return Arrays.asList(targets);
        }

    }

}
