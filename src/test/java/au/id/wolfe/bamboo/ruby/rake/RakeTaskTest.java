package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static au.id.wolfe.bamboo.ruby.rake.RakeCommandBuilder.*;

/**
 * Do some basic checking of the rake task.
 */
@RunWith(MockitoJUnitRunner.class)
public class RakeTaskTest {

    public static final String DB_MIGRATE_TARGET = "db:migrate";
    @Mock
    ProcessService processService;

    @Mock
    RvmLocatorService rvmLocatorService;

    @Mock
    EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    TaskContext taskContext;

    @Mock
    RubyLocator rubyLocator;

    RakeTask rakeTaskTester = new RakeTask();

    @Before
    public void setUp() throws Exception {

        rakeTaskTester.setEnvironmentVariableAccessor(environmentVariableAccessor);
        rakeTaskTester.setProcessService(processService);
        rakeTaskTester.setRvmLocatorService(rvmLocatorService);

    }

    @Test
    public void testBuildCommandList() {

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("targets", DB_MIGRATE_TARGET);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("trace", "false");

        when(taskContext.getConfigurationMap()).thenReturn(configurationMap);

        // rvmLocatorService.getRvmRubyLocator();
        when(rvmLocatorService.getRvmRubyLocator()).thenReturn(rubyLocator);

        // rubyLocator.getRubyRuntime(rubyRuntimeName);
        when(rubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        when(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);

        List<String> commandList = rakeTaskTester.buildCommandList(taskContext);

        assertEquals(5, commandList.size());

        Iterator<String> commandsIterator = commandList.iterator();

        assertEquals(rubyRuntime.getRubyExecutablePath(), commandsIterator.next());
        assertEquals(RvmFixtures.BUNDLER_PATH, commandsIterator.next());
        assertEquals(BUNDLE_EXEC_ARG, commandsIterator.next());
        assertEquals(RvmFixtures.RAKE_PATH, commandsIterator.next());
        assertEquals(DB_MIGRATE_TARGET, commandsIterator.next());
    }


    @Test
    public void testBuildEnvironment() {
        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        // taskContext.getConfigurationMap()
        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(taskContext.getConfigurationMap()).thenReturn(configurationMap);

        // rvmLocatorService.getRvmRubyLocator();
        when(rvmLocatorService.getRvmRubyLocator()).thenReturn(rubyLocator);

        // getRubyLocator().buildEnv(rubyRuntimeName, currentEnvVars);
        when(rubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        Map<String, String> envVars = rakeTaskTester.buildEnvironment(taskContext);

        assertTrue(envVars.size() == 0);

    }

}
