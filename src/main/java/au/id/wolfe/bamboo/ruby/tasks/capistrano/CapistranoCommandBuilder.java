package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractBundleExecCommandBuilder;

/**
 * Assemble the command list for cap.
 */
public class CapistranoCommandBuilder extends AbstractBundleExecCommandBuilder<CapistranoCommandBuilder> {

    public static final String CAP_COMMAND = "cap";

    public static final String VERBOSE_ARG = "--verbose";
    public static final String DEBUG_ARG = "--debug";

    public CapistranoCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        super( rvmRubyLocator, rubyRuntime, rubyExecutablePath );
    }

    /**
     * Append the rake executable to the command list.
     *
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addCapistranoExecutable( @Nullable String bundleFlag ) {

        if ( BooleanUtils.toBoolean( bundleFlag ) ) {
            getCommandList().add( CAP_COMMAND );
        }
        else {
            getCommandList().add( getRvmRubyLocator().buildExecutablePath( getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), CAP_COMMAND ) );
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param verboseFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfVerbose( @Nullable String verboseFlag ) {

        if ( BooleanUtils.toBoolean( verboseFlag ) ) {
            getCommandList().add( VERBOSE_ARG );
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param debugFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfDebug( @Nullable String debugFlag ) {

        if ( BooleanUtils.toBoolean( debugFlag ) ) {
            getCommandList().add( DEBUG_ARG );
        }
        return this;
    }

    /**
     * Will append the supplied list of tasks to the command list.
     *
     * @param tasks List of targets.
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addTasks( List<String> tasks ) {

        getCommandList().addAll( tasks );
        return this;
    }

}
