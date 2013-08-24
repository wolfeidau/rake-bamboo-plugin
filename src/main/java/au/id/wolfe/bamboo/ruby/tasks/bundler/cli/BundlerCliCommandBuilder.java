package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.bundler.AbstractBundleExecCommandBuilder;

/**
 * Builder to assemble the bundle command list.
 * <p/>
 * TODO Need to reconsider the design of this class, probably moving to properties over a list, with the command list being built in the build method.
 */
public class BundlerCliCommandBuilder extends AbstractBundleExecCommandBuilder<BundlerCliCommandBuilder> {
    
    public static final String TRACE_ARG = "--trace";

    public BundlerCliCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        super( rvmRubyLocator, rubyRuntime, rubyExecutablePath );
    }

    /**
     *  super.{@link #addIfBundleExec(String)} adds bundle as well, we are 
     *      a special case where we always have bundle and just need exec
     *      if checked in the UI. 
     */
    @Override
    public BundlerCliCommandBuilder addIfBundleExec( @Nullable String bundleFlag ) {

        if ( BooleanUtils.toBoolean( bundleFlag ) ) {
            getCommandList().add( BUNDLE_EXEC_ARG );
        }
        return this;
    }
    
    /**
     * Append the bundle executable to the command list.
     *
     * @return Bundler command builder.
     */
    public BundlerCliCommandBuilder addBundleExecutable() {
        getCommandList().add(getRvmRubyLocator().buildExecutablePath(getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), BUNDLE_COMMAND));
        return this;
    }

    /**
     * Will conditionally append the trace switch if trace flag is "true"..
     *
     * @param traceFlag String which takes null or "true".
     * @return Rake command builder.
     */
    public BundlerCliCommandBuilder addIfTrace( @Nullable String traceFlag ) {

        if ( BooleanUtils.toBoolean( traceFlag ) ) {
            getCommandList().add( TRACE_ARG );
        }
        return this;
    }

    /**
     * Will append the supplied list of targets to the command list.
     *
     * @param arguments List of targets.
     * @return Rake command builder.
     */
    public BundlerCliCommandBuilder addArguments( List<String> arguments ) {

        getCommandList().addAll( arguments );
        return this;
    }
}
