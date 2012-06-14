package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Builder to assemble the rake command list.
 * <p/>
 * TODO Need to reconsider the design of this class, probably moving to properties over a list, with the command list being built in the build method.
 */
public class RakeCommandBuilder {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String RAKE_COMMAND = "rake";

    public static final String BUNDLE_EXEC_ARG = "exec";

    public static final String RAKEFILE_ARG = "-f";
    public static final String RAKELIBDIR_ARG = "--rakelibdir";

    public static final String VERBOSE_ARG = "--verbose";
    public static final String TRACE_ARG = "--trace";

    private final RubyLocator rvmRubyLocator;
    private final RubyRuntime rubyRuntime;

    private List<String> commandList = Lists.newLinkedList();

    public RakeCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime) {
        this.rvmRubyLocator = rvmRubyLocator;
        this.rubyRuntime = rubyRuntime;
    }

    /**
     * Append the ruby executable to the command list.
     *
     * @return Rake command builder.
     */
    public RakeCommandBuilder addRubyExecutable() {
        commandList.add(rubyRuntime.getRubyExecutablePath());
        return this;
    }

    /**
     * Will conditionally add bundle exec if bundle flag is "true".
     *
     * @param bundleFlag String which takes null or "true".
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfBundleExec(@Nullable String bundleFlag) {
        if (BooleanUtils.toBoolean(bundleFlag)) {
            commandList.add(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND));
            commandList.add(BUNDLE_EXEC_ARG);
        }
        return this;
    }

    /**
     * Append the rake executable to the command list.
     *
     * @param bundleFlag String which takes null or "true", this indicates whether to use short command or full path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addRakeExecutable(@Nullable String bundleFlag) {
        if (BooleanUtils.toBoolean(bundleFlag)) {
            commandList.add(RAKE_COMMAND);
        } else {
            commandList.add(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND));
        }
        return this;
    }

    /**
     * Will conditionally append rake file parameter if rake file is not empty.
     *
     * @param rakeFile String which takes either null or a file path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfRakeFile(@Nullable String rakeFile) {
        if (StringUtils.isNotEmpty(rakeFile)) {
            commandList.add(RAKEFILE_ARG);
            commandList.add(rakeFile);
        }
        return this;
    }

    /**
     * Will conditionally append rake lib directory parameter if rake file is not empty.
     *
     * @param rakeLibDir String which takes either null or a directory path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfRakeLibDir(@Nullable String rakeLibDir) {
        if (StringUtils.isNotEmpty(rakeLibDir)) {
            commandList.add(RAKELIBDIR_ARG);
            commandList.add(rakeLibDir);
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param verboseFlag String which takes null or "true".
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfVerbose(@Nullable String verboseFlag) {
        if (BooleanUtils.toBoolean(verboseFlag)) {
            commandList.add(VERBOSE_ARG);
        }
        return this;
    }

    /**
     * Will conditionally append the trace switch if trace flag is "true"..
     *
     * @param traceFlag String which takes null or "true".
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfTrace(@Nullable String traceFlag) {
        if (BooleanUtils.toBoolean(traceFlag)) {
            commandList.add(TRACE_ARG);
        }
        return this;
    }

    /**
     * Will append the supplied list of targets to the command list.
     *
     * @param targets List of targets.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addTargets(List<String> targets) {
        commandList.addAll(targets);
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
