package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.tasks.AbstractTaskTest;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static au.id.wolfe.bamboo.ruby.tasks.rake.RakeCommandBuilder.BUNDLE_COMMAND;
import static au.id.wolfe.bamboo.ruby.tasks.rake.RakeCommandBuilder.RAKE_COMMAND;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Test the Capistrano Ruby task.
 */
public class CapistranoTaskTest extends AbstractTaskTest{

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

        RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());

        ConfigurationMap configurationMap = new ConfigurationMapImpl();

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());
        configurationMap.put("tasks", DEPLOY_SETUP_TASKS);
        configurationMap.put("bundleexec", "true");
        configurationMap.put("verbose", "false");
        configurationMap.put("debug", "false");

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), RAKE_COMMAND)).thenReturn(RvmFixtures.RAKE_PATH);

        List<String> commandList = capistranoTask.buildCommandList(rubyLabel, configurationMap);

    }

    @Test
    @Override
    public void testBuildEnvironment() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
