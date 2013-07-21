package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.tasks.AbstractTaskTest;
import au.id.wolfe.bamboo.ruby.util.TaskUtils;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the Capistrano Ruby task.
 */
@RunWith(MockitoJUnitRunner.class)
public class CapistranoTaskTest extends AbstractTaskTest {

    private static final String DEPLOY_SETUP_TASKS = "deploy:setup";

    private CapistranoTask capistranoTask = new CapistranoTask();

    @Before
    public void setUp() throws Exception {
        capistranoTask.setEnvironmentVariableAccessor(environmentVariableAccessor);
        capistranoTask.setProcessService(processService);
        capistranoTask.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);
        capistranoTask.setCapabilityContext(capabilityContext);

        Capability capability = mock(Capability.class);
        CapabilitySet capabilitySet = mock(CapabilitySet.class);

        when(capability.getValue()).thenReturn(rubyRuntime.getRubyExecutablePath());
        when(capabilitySet.getCapability(TaskUtils.buildCapabilityLabel(rubyLabel))).thenReturn(capability);
        when(capabilityContext.getCapabilitySet()).thenReturn(capabilitySet);

    }

    @Test
    @Override
    public void testBuildCommandList() {

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("tasks", DEPLOY_SETUP_TASKS);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("debug", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.buildExecutablePath(rubyExecutablePath, CapistranoCommandBuilder.CAP_COMMAND)).thenReturn(RvmFixtures.CAP_PATH);
        when(rvmRubyLocator.buildExecutablePath(rubyExecutablePath, CapistranoCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        final List<String> commandList = capistranoTask.buildCommandList(rubyLabel, configurationMap);

        final Iterator<String> commandsIterator = commandList.iterator();

        assertThat(commandsIterator.next(), is(rubyRuntime.getRubyExecutablePath()));
        assertThat(commandsIterator.next(), is(RvmFixtures.BUNDLER_PATH));
        assertThat(commandsIterator.next(), is("exec"));
        assertThat(commandsIterator.next(), is("cap"));
        assertThat(commandsIterator.next(), is(DEPLOY_SETUP_TASKS));

        assertThat(commandList.size(), is(5));
    }

    @Test
    @Override
    public void testBuildEnvironment() {

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        final Map<String, String> envVars = capistranoTask.buildEnvironment(rubyLabel, configurationMap);

        assertThat(envVars.size(), is(0));

    }
}
