package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Test the Capistrano command builder.
 */
@RunWith(MockitoJUnitRunner.class)
public class CapistranoCommandBuilderTest {

    private static final String TEST_TASK = "cold:deploy";

    @Mock
    private RvmRubyLocator rvmRubyLocator;

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

    private CapistranoCommandBuilder capistranoCommandBuilder;

    @Before
    public void setUp() throws Exception {

        capistranoCommandBuilder = new CapistranoCommandBuilder(rvmRubyLocator, rubyRuntime);

        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), CapistranoCommandBuilder.CAP_COMMAND)).thenReturn(RvmFixtures.CAP_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), CapistranoCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

    }

    @Test
    public void testAddRubyExecutable() throws Exception {
        capistranoCommandBuilder.addRubyExecutable();
        assertThat(capistranoCommandBuilder.build(), hasItem(rubyRuntime.getRubyExecutablePath()));
    }

    @Test
    public void testAddIfBundleExec() throws Exception {
        capistranoCommandBuilder.addIfBundleExec("true");
        assertThat(capistranoCommandBuilder.build(), hasItem(RvmFixtures.BUNDLER_PATH));
        assertThat(capistranoCommandBuilder.build(), hasItem("exec"));
        assertThat(capistranoCommandBuilder.build().size(), is(2));

    }

    @Test
    public void testAddCapExecutable() throws Exception {
        capistranoCommandBuilder.addCapistranoExecutable("false");
        assertThat(capistranoCommandBuilder.build(), hasItem(RvmFixtures.CAP_PATH));
    }

    @Test
    public void testAddCapExecutableWithBundleExec() throws Exception {
        capistranoCommandBuilder.addCapistranoExecutable("true");
        assertThat(capistranoCommandBuilder.build(), hasItem("cap"));
    }

    @Test
    public void testAddIfVerbose() throws Exception {
        capistranoCommandBuilder.addIfVerbose("true");
        assertThat(capistranoCommandBuilder.build(), hasItem("--verbose"));
    }

    @Test
    public void testAddIfDebug() throws Exception {
        capistranoCommandBuilder.addIfDebug("true");
        assertThat(capistranoCommandBuilder.build(), hasItem("--debug"));

    }

    @Test
    public void testAddTasks() throws Exception {
        capistranoCommandBuilder.addTasks(Lists.newArrayList(TEST_TASK));
        assertThat(capistranoCommandBuilder.build(), hasItem(TEST_TASK));
    }

    @Test
    public void testBuild() throws Exception {

        String bundleExecFlag = "true";

        capistranoCommandBuilder.addRubyExecutable().addIfBundleExec(bundleExecFlag).addCapistranoExecutable(bundleExecFlag).addIfDebug("true").addIfVerbose("true").addTasks(Lists.newArrayList(TEST_TASK));
        assertThat(capistranoCommandBuilder.build().size(), is(7));
    }
}
