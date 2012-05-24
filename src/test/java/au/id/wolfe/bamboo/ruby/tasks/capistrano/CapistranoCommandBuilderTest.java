package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 *
 */
public class CapistranoCommandBuilderTest {

    @Mock
    RvmRubyLocator rvmRubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

    private CapistranoCommandBuilder capistranoCommandBuilder;

    @Before
    public void setUp() throws Exception {

        capistranoCommandBuilder = new CapistranoCommandBuilder(rvmRubyLocator, rubyRuntime);

    }

    @Test
    public void testAddRubyExecutable() throws Exception {

    }

    @Test
    public void testAddIfBundleExec() throws Exception {

    }

    @Test
    public void testAddRakeExecutable() throws Exception {

    }

    @Test
    public void testAddIfVerbose() throws Exception {

    }

    @Test
    public void testAddIfDebug() throws Exception {

    }

    @Test
    public void testAddTargets() throws Exception {

    }

    @Test
    public void testBuild() throws Exception {

    }
}
