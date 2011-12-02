package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the ruby capabilities helper.
 */
@RunWith(MockitoJUnitRunner.class)
public class RubyCapabilityDefaultsHelperTest {

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

        final RubyRuntime rubyRuntimeMRI = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        final RubyRuntime rubyRuntimeJRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();

        when(rvmLocatorService.getRvmRubyLocator()).thenReturn(rubyLocator);
        when(rubyLocator.listRubyRuntimes()).thenReturn(Lists.<RubyRuntime>newArrayList(rubyRuntimeMRI, rubyRuntimeJRuby));

        rubyCapabilityDefaultsHelper.addDefaultCapabilities(capabilitySet);

        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby." + rubyRuntimeMRI.getRubyRuntimeName(), rubyRuntimeMRI.getRubyExecutablePath()));
        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby." + rubyRuntimeJRuby.getRubyRuntimeName(), rubyRuntimeJRuby.getRubyExecutablePath()));
    }
}
