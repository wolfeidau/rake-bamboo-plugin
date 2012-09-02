package au.id.wolfe.bamboo.ruby.tasks.bundler;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs bundler to install the gems required by the project.
 */
public class BundlerTask extends AbstractRubyTask {

    public static final String PATH = "path";
    public static final String ENVIRONMENT = "environmentVariables";
    public static final String BIN_STUBS = "binstubs";

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        final String path = config.get(PATH);
        final String binStubsFlag = config.get(BIN_STUBS);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime());

        return new BundlerCommandBuilder(rubyLocator, rubyRuntime)
                .addRubyExecutable()
                .addBundleExecutable()
                .addInstall()
                .addPath(path)
                .addIfBinStubs(binStubsFlag)
                .build();
    }

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        // get variables from our configuration
        final String environmentVariables = config.get(ENVIRONMENT);

        Map<String, String> configEnvVars = environmentVariableAccessor.splitEnvironmentAssignments(environmentVariables);

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(),
                ImmutableMap.<String, String>builder().putAll(currentEnvVars).putAll(configEnvVars).build());
    }

}
