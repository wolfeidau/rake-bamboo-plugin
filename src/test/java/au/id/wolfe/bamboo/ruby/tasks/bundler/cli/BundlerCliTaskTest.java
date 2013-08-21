package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

import static au.id.wolfe.bamboo.ruby.tasks.AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.tasks.AbstractTaskTest;
import au.id.wolfe.bamboo.ruby.util.TaskUtils;

import com.google.common.collect.Maps;

/**
 * Do some basic checking of the rake task.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerCliTaskTest extends AbstractTaskTest {

    private static final String DB_MIGRATE_TARGET = "db:migrate";

    private BundlerCliTask bundlerCliTask = new BundlerCliTask();

    @Before
    public void setUp() throws Exception {

        bundlerCliTask.setEnvironmentVariableAccessor(environmentVariableAccessor);
        bundlerCliTask.setProcessService(processService);
        bundlerCliTask.setCapabilityContext(capabilityContext);
        bundlerCliTask.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);
        bundlerCliTask.setCapabilityContext(capabilityContext);

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

        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);
        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        final List<String> commandList = bundlerCliTask.buildCommandList(rubyLabel, configurationMap);

        assertEquals(5, commandList.size());

        final Iterator<String> commandsIterator = commandList.iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
        assertEquals(RvmFixtures.BUNDLER_PATH, commandsIterator.next());
        assertEquals(BUNDLE_EXEC_ARG, commandsIterator.next());
        assertEquals(BundlerCliCommandBuilder.RAKE_COMMAND, commandsIterator.next());
        assertEquals(DB_MIGRATE_TARGET, commandsIterator.next());
    }


    @Test
    public void testBuildEnvironment() {

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        final Map<String, String> envVars = bundlerCliTask.buildEnvironment(rubyLabel, configurationMap);

        assertTrue(envVars.size() == 0);

    }

}
