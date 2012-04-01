package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Lists;
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
    RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    RubyRuntimeLocatorService rubyRuntimeLocatorService;

    @Mock
    RvmRubyLocator rvmRubyLocator;

    @Mock
    CapabilitySet capabilitySet;

    RubyCapabilityDefaultsHelper rubyCapabilityDefaultsHelper;

    @Before
    public void setUp() throws Exception {

        rubyCapabilityDefaultsHelper = new RubyCapabilityDefaultsHelper(rubyLocatorServiceFactory);

        when(rubyLocatorServiceFactory.getLocatorServices()).thenReturn(Lists.newArrayList(rubyRuntimeLocatorService));

    }

    @Test
    public void testAddDefaultCapabilities() throws Exception {

        final RubyRuntime rubyRuntimeMRI = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        final RubyRuntime rubyRuntimeJRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();

        when(rubyRuntimeLocatorService.getRuntimeManagerName()).thenReturn("RVM");
        when(rubyRuntimeLocatorService.getRubyLocator()).thenReturn(rvmRubyLocator);
        when(rubyRuntimeLocatorService.isInstalled()).thenReturn(true);
        when(rvmRubyLocator.listRubyRuntimes()).thenReturn(asList(rubyRuntimeMRI, rubyRuntimeJRuby));

        rubyCapabilityDefaultsHelper.addDefaultCapabilities(capabilitySet);

        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby.RVM " + rubyRuntimeMRI.getRubyRuntimeName(), rubyRuntimeMRI.getRubyExecutablePath()));
        verify(capabilitySet).addCapability(new CapabilityImpl("system.builder.ruby.RVM " + rubyRuntimeJRuby.getRubyRuntimeName(), rubyRuntimeJRuby.getRubyExecutablePath()));
    }
}
