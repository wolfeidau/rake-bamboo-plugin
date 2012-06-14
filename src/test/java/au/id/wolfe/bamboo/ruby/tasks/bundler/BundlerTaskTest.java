package au.id.wolfe.bamboo.ruby.tasks.bundler;

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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

/**
 * Do some basic checking of the bundler task.
 */
@RunWith(MockitoJUnitRunner.class)
public class BundlerTaskTest extends AbstractTaskTest {

    BundlerTask bundlerTask = new BundlerTask();

    RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    RubyLabel rubyLabel = new RubyLabel("RVM", rubyRuntime.getRubyRuntimeName());
    ConfigurationMap configurationMap = new ConfigurationMapImpl();

    @Before
    public void setUp() throws Exception {

        bundlerTask.setEnvironmentVariableAccessor(environmentVariableAccessor);
        bundlerTask.setProcessService(processService);
        bundlerTask.setRubyLocatorServiceFactory(rubyLocatorServiceFactory);

        configurationMap.put("ruby", rubyRuntime.getRubyRuntimeName());

        when(rubyLocatorServiceFactory.acquireRubyLocator(eq("RVM"))).thenReturn(rvmRubyLocator);

    }

    @Test
    public void testBuildCommandList() {

        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BundlerCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        List<String> commandList = bundlerTask.buildCommandList(rubyLabel, configurationMap);

        Iterator<String> commandTokens = commandList.iterator();

        assertThat(commandTokens.next(), is(rubyRuntime.getRubyExecutablePath()));
        assertThat(commandTokens.next(), is(RvmFixtures.BUNDLER_PATH));
        assertThat(commandTokens.next(), is("install"));

    }

    @Test
    public void testBuildCommandListWithPathAndBinStubs(){
        when(rvmRubyLocator.getRubyRuntime(rubyRuntime.getRubyRuntimeName())).thenReturn(rubyRuntime);
        when(rvmRubyLocator.searchForRubyExecutable(rubyRuntime.getRubyRuntimeName(), BundlerCommandBuilder.BUNDLE_COMMAND)).thenReturn(RvmFixtures.BUNDLER_PATH);

        configurationMap.put("path", "gems");
        configurationMap.put("binstubs", "true");

        List<String> commandList = bundlerTask.buildCommandList(rubyLabel, configurationMap);

        Iterator<String> commandTokens = commandList.iterator();

        assertThat(commandTokens.next(), is(rubyRuntime.getRubyExecutablePath()));
        assertThat(commandTokens.next(), is(RvmFixtures.BUNDLER_PATH));
        assertThat(commandTokens.next(), is("install"));
        assertThat(commandTokens.next(), is("--path"));
        assertThat(commandTokens.next(), is("gems"));
        assertThat(commandTokens.next(), is("--binstubs"));
    }

    @Test
    public void testBuildEnvironment() {

        when(rvmRubyLocator.buildEnv(rubyRuntime.getRubyRuntimeName(), Maps.<String, String>newHashMap())).thenReturn(Maps.<String, String>newHashMap());

        Map<String, String> envVars = bundlerTask.buildEnvironment(rubyLabel, configurationMap);

        assertThat(envVars.size(), is(0));

    }

}
