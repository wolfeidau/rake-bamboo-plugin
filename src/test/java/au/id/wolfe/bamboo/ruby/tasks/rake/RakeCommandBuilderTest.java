package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
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
    RvmRubyLocator rvmRubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

    RakeCommandBuilder rakeCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RakeCommandBuilder.RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RakeCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        rakeCommandBuilder = new RakeCommandBuilder(rvmRubyLocator, rubyRuntime);
    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        rakeCommandBuilder.addRubyExecutable();
        assertEquals(1, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
    }


    @Test
    public void testAddIfRakefile() throws Exception {

        rakeCommandBuilder.addIfRakeFile(null);
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfRakeFile("Rakefile");

        assertEquals(2, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RakeCommandBuilder.RAKEFILE_ARG, commandsIterator.next());
        assertEquals("Rakefile", commandsIterator.next());
    }

    @Test
    public void testAddRakeExecutableWithBundleExec(){

        final String bundleExecFlag = "true";

        rakeCommandBuilder.addIfBundleExec(bundleExecFlag).addRakeExecutable(bundleExecFlag);

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RvmFixtures.BUNDLER_PATH, commandsIterator.next());
        assertEquals(RakeCommandBuilder.BUNDLE_EXEC_ARG, commandsIterator.next());
        assertEquals(RakeCommandBuilder.RAKE_COMMAND, commandsIterator.next());

    }

    @Test
    public void testAddRakeExecutable(){

        final String bundleExecFlag = "false";

        rakeCommandBuilder.addRakeExecutable(bundleExecFlag);

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RvmFixtures.RAKE_PATH, commandsIterator.next());

    }


    @Test
    public void testAddIfRakeLibDir() throws Exception {

        rakeCommandBuilder.addIfRakeLibDir(null);
        assertEquals(0, rakeCommandBuilder.build().size());

        rakeCommandBuilder.addIfRakeLibDir("./rakelib");

        assertEquals(2, rakeCommandBuilder.build().size());

        Iterator<String> commandsIterator = rakeCommandBuilder.build().iterator();

        assertEquals(RakeCommandBuilder.RAKELIBDIR_ARG, commandsIterator.next());
        assertEquals("./rakelib", commandsIterator.next());
    }

    @Test
    public void testAddIfVerbose() throws Exception {

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
