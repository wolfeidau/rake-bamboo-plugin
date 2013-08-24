package au.id.wolfe.bamboo.ruby.tasks.bundler.cli;

import static au.id.wolfe.bamboo.ruby.tasks.bundler.AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Do some basic checking of the rake task.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerCliTaskTest extends AbstractTaskTest {

    private static final String ARGS_BINSTUBS = "binstubs";
    private static final String ARGS_ELASTIC_BEANSTALK = "elastic-beanstalk";
    private static final String ARGS_BINSTUBS_ELASTIC_BEANSTALK = ARGS_BINSTUBS + " " + ARGS_ELASTIC_BEANSTALK;

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

        configurationMap.put(BundlerCliTask.RUBY, rubyRuntime.getRubyRuntimeName());
        configurationMap.put(BundlerCliTask.ARGUMENTS, ARGS_BINSTUBS_ELASTIC_BEANSTALK);
        configurationMap.put(BundlerCliTask.BUNDLE_EXEC, "true");
        configurationMap.put(BundlerCliTask.VERBOSE, "false");
        configurationMap.put(BundlerCliTask.TRACE, "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);
        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.buildExecutablePath(rubyRuntime.getRubyRuntimeName(), rubyExecutablePath, BundlerCliCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        final List<String> commandList = bundlerCliTask.buildCommandList(rubyLabel, configurationMap);

        assertThat(5, equalTo(commandList.size()));

        final Iterator<String> commandsIterator = commandList.iterator();

        assertThat(rubyRuntime.getRubyExecutablePath(), equalTo(commandsIterator.next()));
        assertThat(RvmFixtures.BUNDLER_PATH, equalTo(commandsIterator.next()));
        assertThat(BUNDLE_EXEC_ARG, equalTo(commandsIterator.next()));
        assertThat(ARGS_BINSTUBS, equalTo(commandsIterator.next()));
        assertThat(ARGS_ELASTIC_BEANSTALK, equalTo(commandsIterator.next()));
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
