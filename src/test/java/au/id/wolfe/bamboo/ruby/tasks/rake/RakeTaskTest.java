package au.id.wolfe.bamboo.ruby.tasks.rake;

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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static au.id.wolfe.bamboo.ruby.tasks.rake.RakeCommandBuilder.*;

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
        rakeTask.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);

    }

    @Test
    public void testBuildCommandList() {

        final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        final RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        final ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("targets", DB_MIGRATE_TARGET);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("trace", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);

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

        final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        final RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        final ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        final Map<String, String> envVars = rakeTask.buildEnvironment(rubyLabel, configurationMap);

        assertTrue(envVars.size() == 0);

    }

}
