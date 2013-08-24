package au.id.wolfe.bamboo.ruby.tasks.bundler.install;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractCommandBuilder;

/**
 * Builder to assemble the bundle install command list.
 */
public class BundlerInstallCommandBuilder extends AbstractCommandBuilder<BundlerInstallCommandBuilder>{

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String PATH_ARG = "--path";
    public static final String BIN_STUBS_ARG = "--binstubs";
    public static final String INSTALL_ARG = "install";

    public BundlerInstallCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        super( rvmRubyLocator, rubyRuntime, rubyExecutablePath );
    }

    /**
     * Append the bundle executable to the command list.
     *
     * @return Bundler command builder.
     */
    public BundlerInstallCommandBuilder addBundleExecutable() {
        getCommandList().add(getRvmRubyLocator().buildExecutablePath(getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), BUNDLE_COMMAND));
        return this;
    }

    /**
     * Will conditionally append bundle path parameter if path is not empty.
     *
     * @param path String which takes either null or a bundle path.
     * @return Bundler command builder.
     */
    public BundlerInstallCommandBuilder addPath(@Nullable String path) {
        if (StringUtils.isNotEmpty(path)) {
            getCommandList().add(PATH_ARG);
            getCommandList().add(path);
        }

        return this;
    }

    /**
     * Will conditionally append the bin stubs switch if the bin stubs flag is "true"
     *
     * @param binStubsFlag String which takes null or "true".
     * @return Bundler command builder.
     */
    public BundlerInstallCommandBuilder addIfBinStubs(@Nullable String binStubsFlag) {

        if (BooleanUtils.toBoolean(binStubsFlag)) {
            getCommandList().add(BIN_STUBS_ARG);
        }
        return this;

    }

    /**
     * Will append the install argument.
     *
     * @return Bundler command builder.
     */
    public BundlerInstallCommandBuilder addInstall() {
        getCommandList().add(INSTALL_ARG);
        return this;
    }

}
