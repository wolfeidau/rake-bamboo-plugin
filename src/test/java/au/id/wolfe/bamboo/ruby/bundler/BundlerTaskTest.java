package au.id.wolfe.bamboo.ruby.bundler;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmInstallation;
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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Do some basic checking of the bundler task.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerTaskTest {

    @Mock
    ProcessService processService;

    @Mock
    RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    RvmRubyLocator rvmRubyLocator;

    BundlerTask bundlerTaskTester = new BundlerTask();

    @Before
    public void setUp() throws Exception {

        bundlerTaskTester.setEnvironmentVariableAccessor(environmentVariableAccessor);
        bundlerTaskTester.setProcessService(processService);

        //rubyLocatorServiceFactory.setRvmLocatorService(rvmLocatorService);
        bundlerTaskTester.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);
        
    }

    @Test
    public void testBuildCommandList() {

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BundlerTask.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        List<String> commandList = bundlerTaskTester.buildCommandList(rubyLabel, configurationMap);

        assertTrue(commandList.size() == 3);

        assertTrue(commandList.contains(rubyRuntime.getRubyExecutablePath()));
        assertTrue(commandList.contains(RvmFixtures.BUNDLER_PATH));
        assertTrue(commandList.contains(BundlerTask.BUNDLE_INSTALL_ARG));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildCommandListWhenRvmInstallationIsTypeSystem() {

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);
        when(rvmRubyLocator.readOnly()).thenReturn(true);

        bundlerTaskTester.buildCommandList(rubyLabel, configurationMap);
    }

    @Test
    public void testBuildEnvironment() {
        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        Map<String, String> envVars = bundlerTaskTester.buildEnvironment(rubyLabel, configurationMap);

        assertTrue(envVars.size() == 0);

    }

}
