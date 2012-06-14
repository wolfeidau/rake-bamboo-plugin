package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.tasks.AbstractTaskTest;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.tasks.capistrano.CapistranoCommandBuilder.BUNDLE_COMMAND;
import static au.id.wolfe.bamboo.ruby.tasks.capistrano.CapistranoCommandBuilder.CAP_COMMAND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
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
    }

    @Test
    @Override
    public void testBuildCommandList() {

        final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        final RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        final ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("tasks", DEPLOY_SETUP_TASKS);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("debug", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), CAP_COMMAND)).thenReturn(RvmFixtures.CAP_PATH);

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
        final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        final RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        final ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        final Map<String, String> envVars = capistranoTask.buildEnvironment(rubyLabel, configurationMap);

        assertThat(envVars.size(), is(0));

    }
}
