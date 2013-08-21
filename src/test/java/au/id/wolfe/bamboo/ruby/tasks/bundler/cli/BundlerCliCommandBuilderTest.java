package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;

/**
 * Test the rake command builder.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerCliCommandBuilderTest {

    @Mock
    RvmRubyLocator rvmRubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    BundlerCliCommandBuilder bundlerCliCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);
        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        bundlerCliCommandBuilder = new BundlerCliCommandBuilder(rvmRubyLocator, rubyRuntime, RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath());
    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        bundlerCliCommandBuilder.addRubyExecutable();
        assertEquals(1, bundlerCliCommandBuilder.build().size());

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
    }


    @Test
    public void testAddIfRakefile() throws Exception {

        bundlerCliCommandBuilder.addIfRakeFile(null);
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfRakeFile("Rakefile");

        assertEquals(2, bundlerCliCommandBuilder.build().size());

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(BundlerCliCommandBuilder.RAKEFILE_ARG, commandsIterator.next());
        assertEquals("Rakefile", commandsIterator.next());
    }

    @Test
    public void testAddRakeExecutableWithBundleExec(){

        final String bundleExecFlag = "true";

        bundlerCliCommandBuilder.addIfBundleExec(bundleExecFlag).addRakeExecutable(bundleExecFlag);

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(RvmFixtures.BUNDLER_PATH, commandsIterator.next());
        assertEquals(BundlerCliCommandBuilder.BUNDLE_EXEC_ARG, commandsIterator.next());
        assertEquals(BundlerCliCommandBuilder.RAKE_COMMAND, commandsIterator.next());

    }

    @Test
    public void testAddRakeExecutable(){

        final String bundleExecFlag = "false";

        bundlerCliCommandBuilder.addRakeExecutable(bundleExecFlag);

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(RvmFixtures.RAKE_PATH, commandsIterator.next());

    }


    @Test
    public void testAddIfRakeLibDir() throws Exception {

        bundlerCliCommandBuilder.addIfRakeLibDir(null);
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfRakeLibDir("./rakelib");

        assertEquals(2, bundlerCliCommandBuilder.build().size());

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(BundlerCliCommandBuilder.RAKELIBDIR_ARG, commandsIterator.next());
        assertEquals("./rakelib", commandsIterator.next());
    }

    @Test
    public void testAddIfVerbose() throws Exception {

        bundlerCliCommandBuilder.addIfVerbose(null);
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfVerbose("false");
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfVerbose("true");

        assertEquals(1, bundlerCliCommandBuilder.build().size());

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(BundlerCliCommandBuilder.VERBOSE_ARG, commandsIterator.next());
    }

    @Test
    public void testAddIfTrace() throws Exception {

        bundlerCliCommandBuilder.addIfTrace(null);
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfTrace("false");
        assertEquals(0, bundlerCliCommandBuilder.build().size());

        bundlerCliCommandBuilder.addIfTrace("true");
        assertEquals(1, bundlerCliCommandBuilder.build().size());

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertEquals(BundlerCliCommandBuilder.TRACE_ARG, commandsIterator.next());
    }
}
