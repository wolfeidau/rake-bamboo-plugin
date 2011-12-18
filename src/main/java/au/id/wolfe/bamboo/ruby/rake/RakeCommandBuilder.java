package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Builder to assemble the rake command list.
 */
public class RakeCommandBuilder {

    public static final String BUNDLE_COMMAND = "bundle";
    public static final String RAKE_COMMAND = "rake";

    public static final String BUNDLE_EXEC_ARG = "exec";

    public static final String VERBOSE_ARG = "--verbose";
    public static final String TRACE_ARG = "--trace";

    private RubyLocator rubyLocator;
    private RubyRuntime rubyRuntime;

    private List<String> commandList = Lists.newLinkedList();

    public RakeCommandBuilder(RubyLocator rubyLocator, RubyRuntime rubyRuntime) {
        this.rubyLocator = rubyLocator;
        this.rubyRuntime = rubyRuntime;
    }

    public RakeCommandBuilder addRubyExecutable() {
        commandList.add(rubyRuntime.getRubyExecutablePath());
        return this;
    }

    public RakeCommandBuilder addIfBundleExec(@Nullable String bundleFlag) {
        if (BooleanUtils.toBoolean(bundleFlag)) {
            commandList.add(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND));
            commandList.add(BUNDLE_EXEC_ARG);
        }
        return this;
    }

    public RakeCommandBuilder addRakeExecutable() {
        commandList.add(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND));
        return this;
    }

    public RakeCommandBuilder addIfVerbose(@Nullable String verboseFlag) {
        if (BooleanUtils.toBoolean(verboseFlag)) {
            commandList.add(VERBOSE_ARG);
        }
        return this;
    }

    public RakeCommandBuilder addIfTrace(@Nullable String traceFlag) {
        if (BooleanUtils.toBoolean(traceFlag)) {
            commandList.add(TRACE_ARG);
        }
        return this;
    }

    public RakeCommandBuilder addTargets(List<String> targets) {
        commandList.addAll(targets);
        return this;
    }

    public List<String> build() {
        return commandList;
    }
}
