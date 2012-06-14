package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtils;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskType;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class RakeTask extends AbstractRubyTask implements TaskType {

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(), currentEnvVars);
    }

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rvmRubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager()); // TODO Fix Error handling

        final String rakeFile = config.get("rakefile");
        final String rakeLibDir = config.get("rakelibdir");

        final String targets = config.get("targets");
        Preconditions.checkArgument(targets != null); // TODO Fix Error handling

        final String bundleExecFlag = config.get("bundleexec");
        final String verboseFlag = config.get("verbose");
        final String traceFlag = config.get("trace");

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
