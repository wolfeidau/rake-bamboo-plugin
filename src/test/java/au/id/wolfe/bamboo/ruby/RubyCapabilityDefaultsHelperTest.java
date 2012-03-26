package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.atlassian.bamboo.v2.build.agent.capability.ExecutablePathUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the ruby capabilities helper.
 */
@RunWith(MockitoJUnitRunner.class)
public class RubyCapabilityDefaultsHelperTest {

    private static final Logger log = LoggerFactory.getLogger(RubyCapabilityDefaultsHelperTest.class);

    @Mock
    RvmLocatorService rvmLocatorService;

    @Mock
    RubyLocator rubyLocator;

    @Mock
    CapabilitySet capabilitySet;

    RubyCapabilityDefaultsHelper rubyCapabilityDefaultsHelper;

    @Before
    public void setUp() throws Exception {

        rubyCapabilityDefaultsHelper = new RubyCapabilityDefaultsHelper(rvmLocatorService);

    }

    @Test
    public void testAddDefaultCapabilities() throws Exception {

        //final String rubyPath = ExecutablePathUtils.findExecutablePath("ruby");
        //final String gemPath = ExecutablePathUtils.findExecutablePath("gem");

        //log.info("ruby = " + rubyPath);
        //log.info("gem = " + gemPath);

        final RubyRuntime rubyRuntimeMRI = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        final RubyRuntime rubyRuntimeJRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();

        when(rvmLocatorService.getRvmRubyLocator()).thenReturn(rubyLocator);
        when(rvmLocatorService.isRvmInstalled()).thenReturn(true);
        when(rubyLocator.listRubyRuntimes()).thenReturn(asList(rubyRuntimeMRI, rubyRuntimeJRuby));

        rubyCapabilityDefaultsHelper.addDefaultCapabilities(capabilitySet);

        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby.RVM " + rubyRuntimeMRI.getRubyRuntimeName(), rubyRuntimeMRI.getRubyExecutablePath()));
        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby.RVM " + rubyRuntimeJRuby.getRubyRuntimeName(), rubyRuntimeJRuby.getRubyExecutablePath()));
    }
}
