package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test the rake command builder.
 */
@RunWith(MockitoJUnitRunner.class)
public class RakeCommandBuilderTest {

    @Mock
    RubyLocator rubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

    @Before
    public void setUp() throws Exception {

        when(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RakeCommandBuilder.RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);
        when(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RakeCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

    }

    @Test
    public void testAddRubyExecutable() throws Exception {
        RakeCommandBuilder rakeCommandBuilder = new RakeCommandBuilder(rubyLocator, rubyRuntime);

        rakeCommandBuilder.addRubyExecutable();

        assertEquals(1, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
    }


    @Test
    public void testAddIfVerbose() throws Exception {
        RakeCommandBuilder rakeCommandBuilder = new RakeCommandBuilder(rubyLocator, rubyRuntime);

        rakeCommandBuilder.addIfVerbose(null);
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfVerbose("false");
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfVerbose("true");

        assertEquals(1, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RakeCommandBuilder.VERBOSE_ARG, commandsIterator.next());
    }

    @Test
    public void testAddIfTrace() throws Exception {
        RakeCommandBuilder rakeCommandBuilder = new RakeCommandBuilder(rubyLocator, rubyRuntime);

        rakeCommandBuilder.addIfTrace(null);
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfTrace("false");
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfTrace("true");
        assertEquals(1, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RakeCommandBuilder.TRACE_ARG, commandsIterator.next());
    }
}
