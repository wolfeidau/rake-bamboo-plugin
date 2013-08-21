package au.id.wolfe.bamboo.ruby.tasks.bundler.install;

import java.util.List;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;

import com.atlassian.bamboo.configuration.ConfigurationMap;

/**
 * Bamboo task which interfaces with RVM and runs bundler to install the gems required by the project.
 */
public class BundlerInstallTask extends AbstractRubyTask {

    public static final String PATH = "path";
    public static final String BIN_STUBS = "binstubs";

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        final String path = config.get(PATH);
        final String binStubsFlag = config.get(BIN_STUBS);

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime(rubyRuntimeLabel.getRubyRuntime());

        final String rubyExecutablePath = getRubyExecutablePath(rubyRuntimeLabel);

        return new BundlerInstallCommandBuilder(rubyLocator, rubyRuntime, rubyExecutablePath)
                .addRubyExecutable()
                .addBundleExecutable()
                .addInstall()
                .addPath(path)
                .addIfBinStubs(binStubsFlag)
                .build();
    }

}
