package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

import java.util.List;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;

import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;

/**
 * Bamboo task which interfaces with RVM and runs bundler.
 */
public class BundlerCliTask extends AbstractRubyTask {

    public static final String ARGUMENTS = "arguments";
    public static final String BUNDLE_EXEC = "bundleexec";

    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        final String arguments = config.get(ARGUMENTS);
        Preconditions.checkArgument(arguments != null); // TODO Fix Error handling

        final String bundleExecFlag = config.get(BUNDLE_EXEC);
        final String verboseFlag = config.get(VERBOSE);
        final String traceFlag = config.get(TRACE);

        final List<String> argumentList = RvmUtils.splitTokens(arguments);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime()); // TODO Fix Error handling

        final String rubyExecutablePath = getRubyExecutablePath(rubyRuntimeLabel);

        return new BundlerCliCommandBuilder(rubyLocator, rubyRuntime, rubyExecutablePath)
                .addRubyExecutable()
                .addBundleExecutable()
                .addIfBundleExec(bundleExecFlag)
                .addIfVerbose(verboseFlag)
                .addIfTrace(traceFlag)
                .addArguments(argumentList)
                .build();
    }
}
