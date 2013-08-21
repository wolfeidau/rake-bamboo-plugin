package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the rake command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class BundlerCliCommandBuilderTest {

    @Mock
    RvmRubyLocator rvmRubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    BundlerCliCommandBuilder bundlerCliCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when( rvmRubyLocator.buildExecutablePath( rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        bundlerCliCommandBuilder = new BundlerCliCommandBuilder( rvmRubyLocator, rubyRuntime, RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath() );
    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        bundlerCliCommandBuilder.addRubyExecutable();

        assertThat( 1, equalTo( bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertThat( rubyRuntime.getRubyExecutablePath(), equalTo( commandsIterator.next() ) );
    }

    //
    //    @Test
    //    public void testAddIfRakefile() throws Exception {
    //
    //        bundlerCliCommandBuilder.addIfRakeFile(null);
    //        assertThat(0, equalTo(bundlerCliCommandBuilder.build().size()));
    //
    //        bundlerCliCommandBuilder.addIfRakeFile("Rakefile");
    //
    //        assertThat(2, equalTo(bundlerCliCommandBuilder.build().size()));
    //
    //        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();
    //
    //        assertThat(BundlerCliCommandBuilder.RAKEFILE_ARG, commandsIterator.next());
    //        assertThat("Rakefile", equalTo(commandsIterator.next()));
    //    }

    @Test
    public void testWithBundleExec() {

        bundlerCliCommandBuilder.addBundleExecutable();
        bundlerCliCommandBuilder.addIfBundleExec( "true" );

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertThat( RvmFixtures.BUNDLER_PATH, equalTo( commandsIterator.next() ) );
        assertThat( BundlerCliCommandBuilder.BUNDLE_EXEC_ARG, equalTo( commandsIterator.next() ) );
    }

    @Test
    public void testAddIfVerbose() throws Exception {

        bundlerCliCommandBuilder.addIfVerbose( null );
        assertThat( 0, equalTo( bundlerCliCommandBuilder.build().size() ) );

        bundlerCliCommandBuilder.addIfVerbose( "false" );
        assertThat( 0, equalTo( bundlerCliCommandBuilder.build().size() ) );

        bundlerCliCommandBuilder.addIfVerbose( "true" );

        assertThat( 1, equalTo( bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertThat( BundlerCliCommandBuilder.VERBOSE_ARG, equalTo( commandsIterator.next() ) );
    }

    @Test
    public void testAddIfTrace() throws Exception {

        bundlerCliCommandBuilder.addIfTrace( null );
        assertThat( 0, equalTo( bundlerCliCommandBuilder.build().size() ) );

        bundlerCliCommandBuilder.addIfTrace( "false" );
        assertThat( 0, equalTo( bundlerCliCommandBuilder.build().size() ) );

        bundlerCliCommandBuilder.addIfTrace( "true" );
        assertThat( 1, equalTo( bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();

        assertThat( BundlerCliCommandBuilder.TRACE_ARG, equalTo( commandsIterator.next() ) );
    }
}
