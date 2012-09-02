package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Task which interacts with Capistrano.
 */
public class CapistranoTask extends AbstractRubyTask {

    public static final String TASKS = "tasks";
    public static final String ENVIRONMENT = "environmentVariables";

    public static final String BUNDLE_EXEC = "bundleexec";
    public static final String VERBOSE = "verbose";
    public static final String DEBUG = "debug";

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        // get variables from our configuration
        final String environmentVariables = config.get(ENVIRONMENT);

        Map<String, String> configEnvVars = environmentVariableAccessor.splitEnvironmentAssignments(environmentVariables);

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(),
                ImmutableMap.<String, String>builder().putAll(currentEnvVars).putAll(configEnvVars).build());
    }

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        final String tasks = config.get(TASKS);
        Preconditions.checkArgument(tasks != null); // TODO Fix Error handling

        final String bundleExecFlag = config.get(BUNDLE_EXEC);
        final String verboseFlag = config.get(VERBOSE);
        final String debugFlag = config.get(DEBUG);

        final List<String> tasksList = RvmUtils.splitRakeTargets(tasks);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime()); // TODO Fix Error handling

        return new CapistranoCommandBuilder(rubyLocator, rubyRuntime)
                .addRubyExecutable()
                .addIfBundleExec(bundleExecFlag)
                .addCapistranoExecutable(bundleExecFlag)
                .addIfDebug(debugFlag)
                .addIfVerbose(verboseFlag)
                .addTasks(tasksList)
                .build();
    }

}
