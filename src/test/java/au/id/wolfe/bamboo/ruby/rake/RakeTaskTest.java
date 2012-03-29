package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
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
import static org.mockito.Matchers.eq;
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
    RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    RvmLocatorService rvmLocatorService;

    @Mock
    EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    RvmRubyLocator rvmRubyLocator;

    RakeTask rakeTaskTester = new RakeTask();

    @Before
    public void setUp() throws Exception {

        rakeTaskTester.setEnvironmentVariableAccessor(environmentVariableAccessor);
        rakeTaskTester.setProcessService(processService);

        rubyLocatorServiceFactory.setRvmLocatorService(rvmLocatorService);
        rakeTaskTester.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);

    }

    @Test
    public void testBuildCommandList() {

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("targets", DB_MIGRATE_TARGET);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("trace", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);

        List<String> commandList = rakeTaskTester.buildCommandList(rubyLabel, configurationMap);

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
        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        Map<String, String> envVars = rakeTaskTester.buildEnvironment(rubyLabel, configurationMap);

        assertTrue(envVars.size() == 0);

    }

}
