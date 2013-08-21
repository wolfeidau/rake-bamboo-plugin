package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import java.util.List;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;

import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;

/**
 * Task which interacts with Capistrano.
 */
public class CapistranoTask extends AbstractRubyTask {

    public static final String TASKS = "tasks";
    public static final String BUNDLE_EXEC = "bundleexec";
    public static final String VERBOSE = "verbose";
    public static final String DEBUG = "debug";

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

        final String rubyExecutablePath = getRubyExecutablePath(rubyRuntimeLabel);

        return new CapistranoCommandBuilder(rubyLocator, rubyRuntime, rubyExecutablePath)
                .addRubyExecutable()
                .addIfBundleExec(bundleExecFlag)
                .addCapistranoExecutable(bundleExecFlag)
                .addIfDebug(debugFlag)
                .addIfVerbose(verboseFlag)
                .addTasks(tasksList)
                .build();
    }

}
