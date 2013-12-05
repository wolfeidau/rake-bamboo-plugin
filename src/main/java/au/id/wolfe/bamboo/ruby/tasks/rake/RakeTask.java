package au.id.wolfe.bamboo.ruby.tasks.rake;

import java.util.List;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.locator.RuntimeLocatorException;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;

import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class RakeTask extends AbstractRubyTask {

    public static final String RAKE_FILE = "rakefile";
    public static final String RAKE_LIB_DIR = "rakelibdir";
    public static final String TARGETS = "targets";
    public static final String BUNDLE_EXEC = "bundleexec";

    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) throws RuntimeLocatorException{

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        final String rakeFile = config.get(RAKE_FILE);
        final String rakeLibDir = config.get(RAKE_LIB_DIR);

        final String targets = config.get(TARGETS);
        Preconditions.checkArgument(targets != null); // TODO Fix Error handling

        final String bundleExecFlag = config.get(BUNDLE_EXEC);
        final String verboseFlag = config.get(VERBOSE);
        final String traceFlag = config.get(TRACE);

        final List<String> targetList = RvmUtils.splitTokens(targets);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime()); // TODO Fix Error handling

        final String rubyExecutablePath = getRubyExecutablePath(rubyRuntimeLabel);

        return new RakeCommandBuilder(rubyLocator, rubyRuntime, rubyExecutablePath)
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
