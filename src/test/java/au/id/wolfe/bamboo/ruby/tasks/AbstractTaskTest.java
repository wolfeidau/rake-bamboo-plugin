package au.id.wolfe.bamboo.ruby.tasks;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import org.mockito.Mock;

/**
 * Helper class to house common data.
 */
public abstract class AbstractTaskTest {

    @Mock
    protected ProcessService processService;

    @Mock
    protected CapabilityContext capabilityContext;

    @Mock
    protected Capability capability;

    @Mock
    protected CapabilitySet capabilitySet;

    @Mock
    protected RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    protected EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    protected RvmRubyLocator rvmRubyLocator;

    public abstract void testBuildCommandList() throws Exception;

    public abstract void testBuildEnvironment() throws Exception;

    protected final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    protected final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    protected final RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

    protected final ConfigurationMap configurationMap = new ConfigurationMapImpl();


}
