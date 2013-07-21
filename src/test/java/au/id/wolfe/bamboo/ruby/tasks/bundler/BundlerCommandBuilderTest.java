package au.id.wolfe.bamboo.ruby.tasks.bundler;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test the bundler command builder.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerCommandBuilderTest {

    @Mock
    private RvmRubyLocator rvmRubyLocator;

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    private final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    private BundlerCommandBuilder bundlerCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when(rvmRubyLocator.buildExecutablePath(rubyExecutablePath, BundlerCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        bundlerCommandBuilder = new BundlerCommandBuilder(rvmRubyLocator, rubyRuntime, rubyExecutablePath);

    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        bundlerCommandBuilder.addRubyExecutable();
        assertThat(bundlerCommandBuilder.build().size(), is(1));
        assertThat(bundlerCommandBuilder.build(), hasItems(rubyRuntime.getRubyExecutablePath()));

    }

    @Test
    public void testAddBundleExecutable() throws Exception {

        bundlerCommandBuilder.addBundleExecutable();
        assertThat(bundlerCommandBuilder.build(), hasItems(RvmFixtures.BUNDLER_PATH));

    }

    @Test
    public void testAddInstall() throws Exception {

        bundlerCommandBuilder.addInstall();
        assertThat(bundlerCommandBuilder.build(), hasItems("install"));

    }

    @Test
    public void testAddPath() throws Exception {

        bundlerCommandBuilder.addPath("gems");
        assertThat(bundlerCommandBuilder.build(), hasItems("--path"));
        assertThat(bundlerCommandBuilder.build(), hasItems("gems"));

    }

    @Test
    public void testAddIfBinStubs() throws Exception {

        bundlerCommandBuilder.addIfBinStubs("true");
        assertThat(bundlerCommandBuilder.build(), hasItems("--binstubs"));

    }

    @Test
    public void testBuild() throws Exception {

        bundlerCommandBuilder.addRubyExecutable()
                .addBundleExecutable()
                .addInstall()
                .addPath("gems")
                .addIfBinStubs("true")
                .build();

        assertThat(bundlerCommandBuilder.build().size(), is(6));

        Iterator<String> commandTokens = bundlerCommandBuilder.build().iterator();

        assertThat(commandTokens.next(), is(rubyRuntime.getRubyExecutablePath()));
        assertThat(commandTokens.next(), is(RvmFixtures.BUNDLER_PATH));
        assertThat(commandTokens.next(), is("install"));
        assertThat(commandTokens.next(), is("--path"));
        assertThat(commandTokens.next(), is("gems"));
        assertThat(commandTokens.next(), is("--binstubs"));

    }
}
