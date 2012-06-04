package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Assemble the command list for cap.
 */
public class CapistranoCommandBuilder {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String CAP_COMMAND = "cap";

    public static final String BUNDLE_EXEC_ARG = "exec";

    public static final String VERBOSE_ARG = "--verbose";
    public static final String DEBUG_ARG = "--debug";

    private RubyLocator rvmRubyLocator;
    private RubyRuntime rubyRuntime;

    private List<String> commandList = Lists.newLinkedList();

    public CapistranoCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime) {
        this.rvmRubyLocator = rvmRubyLocator;
        this.rubyRuntime = rubyRuntime;
    }

    /**
     * Append the ruby executable to the command list.
     *
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addRubyExecutable() {
        commandList.add(rubyRuntime.getRubyExecutablePath());
        return this;
    }

    /**
     * Will conditionally add bundle exec if bundle flag is "true".
     *
     * @param bundleFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfBundleExec(@Nullable String bundleFlag) {
        if (BooleanUtils.toBoolean(bundleFlag)) {
            commandList.add(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND));
            commandList.add(BUNDLE_EXEC_ARG);
        }
        return this;
    }

    /**
     * Append the rake executable to the command list.
     *
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addCapistranoExecutable(@Nullable String bundleFlag) {
        if (BooleanUtils.toBoolean(bundleFlag)){
            commandList.add(CAP_COMMAND);
        } else {
            commandList.add(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), CAP_COMMAND));
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param verboseFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfVerbose(@Nullable String verboseFlag) {
        if (BooleanUtils.toBoolean(verboseFlag)) {
            commandList.add(VERBOSE_ARG);
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param debugFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfDebug(@Nullable String debugFlag) {
        if (BooleanUtils.toBoolean(debugFlag)) {
            commandList.add(DEBUG_ARG);
        }
        return this;
    }

    /**
     * Will append the supplied list of tasks to the command list.
     *
     * @param tasks List of targets.
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addTasks(List<String> tasks) {
        commandList.addAll(tasks);
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
