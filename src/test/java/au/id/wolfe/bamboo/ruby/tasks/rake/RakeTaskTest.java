package au.id.wolfe.bamboo.ruby.tasks.rake;

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

import static au.id.wolfe.bamboo.ruby.tasks.rake.RakeCommandBuilder.BUNDLE_EXEC_ARG;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Do some basic checking of the rake task.
 */
@RunWith(MockitoJUnitRunner.class)
public class RakeTaskTest extends AbstractTaskTest {

    private static final String DB_MIGRATE_TARGET = "db:migrate";

    private RakeTask rakeTask = new RakeTask();

    @Before
    public void setUp() throws Exception {

        rakeTask.setEnvironmentVariableAccessor(environmentVariableAccessor);
        rakeTask.setProcessService(processService);
        rakeTask.setCapabilityContext(capabilityContext);
        rakeTask.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);
        rakeTask.setCapabilityContext(capabilityContext);

        Capability capability = mock(Capability.class);
        CapabilitySet capabilitySet = mock(CapabilitySet.class);

        when(capability.getValue()).thenReturn(rubyRuntime.getRubyExecutablePath());
        when(capabilitySet.getCapability(TaskUtils.buildCapabilityLabel(rubyLabel))).thenReturn(capability);
        when(capabilityContext.getCapabilitySet()).thenReturn(capabilitySet);
    }

    @Test
    public void testBuildCommandList() {

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("targets", DB_MIGRATE_TARGET);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("trace", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);

        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, RakeCommandBuilder.RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);
        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, RakeCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        final List<String> commandList = rakeTask.buildCommandList(rubyLabel, configurationMap);

        assertEquals(5, commandList.size());

        final Iterator<String> commandsIterator = commandList.iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
        assertEquals(RvmFixtures.BUNDLER_PATH, commandsIterator.next());
        assertEquals(BUNDLE_EXEC_ARG, commandsIterator.next());
        assertEquals(RakeCommandBuilder.RAKE_COMMAND, commandsIterator.next());
        assertEquals(DB_MIGRATE_TARGET, commandsIterator.next());
    }


    @Test
    public void testBuildEnvironment() {

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        final Map<String, String> envVars = rakeTask.buildEnvironment(rubyLabel, configurationMap);

        assertTrue(envVars.size() == 0);

    }

}
