package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.BaseRubyTask;
import au.id.wolfe.bamboo.ruby.rvm.RvmUtil;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskType;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class RakeTask extends BaseRubyTask implements TaskType {

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rvmRubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        final String rakefile = config.get("rakefile");
        final String rakelibdir = config.get("rakelibdir");

        final String targets = config.get("targets");
        Preconditions.checkArgument(targets != null);

        final String bundleExecFlag = config.get("bundleexec");
        final String verboseFlag = config.get("verbose");
        final String traceFlag = config.get("trace");

        final List<String> targetList = RvmUtil.splitRakeTargets(targets);

        final RubyRuntime rubyRuntime = rvmRubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime());

        return new RakeCommandBuilder(rvmRubyLocator, rubyRuntime)
                .addRubyExecutable()
                .addIfBundleExec(bundleExecFlag)
                .addRakeExecutable()
                .addIfRakeFile(rakefile)
                .addIfRakeLibDir(rakelibdir)
                .addIfVerbose(verboseFlag)
                .addIfTrace(traceFlag)
                .addTargets(targetList)
                .build();

    }

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(), currentEnvVars);
    }


}
