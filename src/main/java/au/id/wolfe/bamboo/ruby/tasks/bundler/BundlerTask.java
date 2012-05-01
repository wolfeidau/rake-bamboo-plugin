package au.id.wolfe.bamboo.ruby.tasks.bundler;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.BaseRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Bamboo task which interfaces with RVM and runs bundler to install the gems required by the project.
 */
public class BundlerTask extends BaseRubyTask implements TaskType {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String BUNDLE_INSTALL_ARG = "install";

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        if(rubyLocator.isReadOnly()){
            // TODO need to rethink the readonly flag a bit.
            log.info("This is a system install of ruby ensure the user bamboo is running under is given permission to sudo see the README.md");
        }

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime());

        final List<String> commandsList = Lists.newLinkedList();

        commandsList.add(rubyRuntime.getRubyExecutablePath());

        commandsList.add(rubyLocator.searchForRubyExecutable(rubyRuntimeLabel.getRubyRuntime(), BUNDLE_COMMAND));
        commandsList.add(BUNDLE_INSTALL_ARG);

        return commandsList;
    }

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        log.info("Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime());

        final Map<String, String> currentEnvVars = environmentVariableAccessor.getEnvironment();

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return rubyLocator.buildEnv(rubyRuntimeLabel.getRubyRuntime(), currentEnvVars);
    }

}
