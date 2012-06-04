package au.id.wolfe.bamboo.ruby.tasks.bundler;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Builder to assemble the bundler command list.
 */
public class BundlerCommandBuilder {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String PATH_ARG = "--path";
    public static final String BIN_STUBS_ARG = "--binstubs";
    public static final String INSTALL_ARG = "install";

    private final RubyLocator rvmRubyLocator;
    private final RubyRuntime rubyRuntime;

    private final List<String> commandList = Lists.newLinkedList();

    public BundlerCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime) {
        this.rvmRubyLocator = rvmRubyLocator;
        this.rubyRuntime = rubyRuntime;
    }

    public BundlerCommandBuilder addRubyExecutable() {
        commandList.add(rubyRuntime.getRubyExecutablePath());
        return this;
    }

    /**
     * Append the bundle executable to the command list.
     *
     * @return Bundler command builder.
     */
    public BundlerCommandBuilder addBundleExecutable() {
        commandList.add(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND));
        return this;
    }

    /**
     * Will conditionally append bundle path parameter if path is not empty.
     *
     * @param path String which takes either null or a bundle path.
     * @return Bundler command builder.
     */
    public BundlerCommandBuilder addPath(@Nullable String path) {
        if (StringUtils.isNotEmpty(path)) {
            commandList.add(PATH_ARG);
            commandList.add(path);
        }

        return this;
    }

    /**
     * Will conditionally append the bin stubs switch if the bin stubs flag is "true"
     *
     * @param binStubsFlag String which takes null or "true".
     * @return Bundler command builder.
     */
    public BundlerCommandBuilder addIfBinStubs(@Nullable String binStubsFlag) {

        if (BooleanUtils.toBoolean(binStubsFlag)) {
            commandList.add(BIN_STUBS_ARG);
        }
        return this;

    }

    /**
     * Will append the install argument.
     *
     * @return Bundler command builder.
     */
    public BundlerCommandBuilder addInstall() {
        commandList.add(INSTALL_ARG);
        return this;
    }

    /**
     * Builds the list of commands.
     *
     * @return The list of commands.
     */
    public List<String> build() {
        return commandList;
    }

}
