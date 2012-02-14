package au.id.wolfe.bamboo.ruby.bundler;

import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.rvm.RubyLocator;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmInstallation;
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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Do some basic checking of the bundler task.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerTaskTest {

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

    @Mock
    RvmInstallation rvmInstallation;

    BundlerTask bundlerTaskTester = new BundlerTask();

    @Before
    public void setUp() throws Exception {

        bundlerTaskTester.setEnvironmentVariableAccessor(environmentVariableAccessor);
        bundlerTaskTester.setProcessService(processService);
        bundlerTaskTester.setRvmLocatorService(rvmLocatorService);

    }

    @Test
    public void testBuildCommandList() {

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        // taskContext.getConfigurationMap()
        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(taskContext.getConfigurationMap()).thenReturn(configurationMap);

        // rvmLocatorService.getRvmRubyLocator();
        when(rvmLocatorService.getRvmRubyLocator()).thenReturn(rubyLocator);

        // rubyLocator.getRubyRuntime(rubyRuntimeName);
        when(rubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BundlerTask.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        List<String> commandList = bundlerTaskTester.buildCommandList(taskContext);

        assertTrue(commandList.size() == 3);

        assertTrue(commandList.contains(rubyRuntime.getRubyExecutablePath()));
        assertTrue(commandList.contains(RvmFixtures.BUNDLER_PATH));
        assertTrue(commandList.contains(BundlerTask.BUNDLE_INSTALL_ARG));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildCommandListWhenRvmInstallationIsTypeSystem() {

        when(rvmLocatorService.locateRvmInstallation()).thenReturn(rvmInstallation);
        when(rvmInstallation.isSystemInstall()).thenReturn(true);

        bundlerTaskTester.buildCommandList(taskContext);
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

        Map<String, String> envVars = bundlerTaskTester.buildEnvironment(taskContext);

        assertTrue(envVars.size() == 0);

    }

}
