package au.id.wolfe.bamboo.ruby.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;

import com.google.common.collect.Lists;

public abstract class AbstractCommandBuilder<T extends AbstractCommandBuilder<?>> {

    protected final Logger log = LoggerFactory.getLogger( getClass() );

    private final RubyLocator rvmRubyLocator;
    private final RubyRuntime rubyRuntime;
    private final String rubyExecutablePath;
    private List<String> commandList = Lists.newLinkedList();

    public AbstractCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        this.rvmRubyLocator = rvmRubyLocator;
        this.rubyRuntime = rubyRuntime;
        this.rubyExecutablePath = rubyExecutablePath;
    }

    /**
     * Append the ruby executable to the command list.
     *
     * @return T command builder.
     */
    @SuppressWarnings( "unchecked" )
    public T addRubyExecutable() {

        getCommandList().add( getRubyExecutablePath() );
        return (T) this;
    }

    protected List<String> getCommandList() {

        return commandList;
    }

    /**
     * Builds the list of commands.
     *
     * @return The list of commands.
     */
    public List<String> build() {

        log.info( "commandList {}", getCommandList().toString() );
        return getCommandList();
    }

    protected RubyLocator getRvmRubyLocator() {

        return rvmRubyLocator;
    }

    protected RubyRuntime getRubyRuntime() {

        return rubyRuntime;
    }

    protected String getRubyExecutablePath() {

        return rubyExecutablePath;
    }
}
