package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class RakeTask extends AbstractRubyTask {

    public static final String RAKE_FILE = "rakefile";
    public static final String RAKE_LIB_DIR = "rakelibdir";
    public static final String TARGETS = "targets";
    public static final String BUNDLE_EXEC = "bundleexec";

    public static final String ENVIRONMENT = "environmentVariables";

    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        // Get the variables from our configuration
        final String environmentVariables = config.get(ENVIRONMENT);

        Map<String, String> configEnvVars = environmentVariableAccessor.splitEnvironmentAssignments(environmentVariables);

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(),
                ImmutableMap.<String, String>builder().putAll(currentEnvVars).putAll(configEnvVars).build());
    }

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rvmRubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        final String rakeFile = config.get(RAKE_FILE);
        final String rakeLibDir = config.get(RAKE_LIB_DIR);

        final String targets = config.get(TARGETS);
        Preconditions.checkArgument(targets != null); // TODO Fix Error handling

        final String bundleExecFlag = config.get(BUNDLE_EXEC);
        final String verboseFlag = config.get(VERBOSE);
        final String traceFlag = config.get(TRACE);

        final List<String> targetList = RvmUtils.splitRakeTargets(targets);

        final RubyRuntime rubyRuntime = rvmRubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime()); // TODO Fix Error handling

        return new RakeCommandBuilder(rvmRubyLocator, rubyRuntime)
                .addRubyExecutable()
                .addIfBundleExec(bundleExecFlag)
                .addRakeExecutable(bundleExecFlag)
                .addIfRakeFile(rakeFile)
                .addIfRakeLibDir(rakeLibDir)
                .addIfVerbose(verboseFlag)
                .addIfTrace(traceFlag)
                .addTargets(targetList)
                .build();

    }

}
