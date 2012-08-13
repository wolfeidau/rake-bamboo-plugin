package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * RVM locator tests
 */
@RunWith(MockitoJUnitRunner.class)
public class RubyLocatorTest {

    @Mock
    FileSystemHelper fileSystemHelper;

    RvmRubyLocator rvmRubyLocator;

    @Before
    public void setUp() throws Exception {
        rvmRubyLocator = new RvmRubyLocator(fileSystemHelper, RvmFixtures.getUserRvmInstallation());
    }

    @Test
    public void testBuildEnv() throws Exception {

        final RubyRuntime mriRuby = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(mriRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRuby.getGemPath())).thenReturn(true);

        Map<String, String> currentEnvVars = Maps.newHashMap();

        currentEnvVars.put("PATH", RvmFixtures.TEST_CURRENT_PATH);

        Map<String, String> envVars = rvmRubyLocator.buildEnv("ruby-1.9.3-p0@default", currentEnvVars);

        assertTrue(envVars.containsKey(EnvUtils.MY_RUBY_HOME));

        assertTrue(envVars.containsKey(EnvUtils.GEM_HOME));
        assertEquals(RvmFixtures.GEM_HOME, envVars.get(EnvUtils.GEM_HOME));

        assertTrue(envVars.containsKey(EnvUtils.GEM_PATH));

        assertTrue(envVars.containsKey(EnvUtils.BUNDLE_HOME));
        assertEquals(RvmFixtures.BUNDLE_HOME, envVars.get(EnvUtils.BUNDLE_HOME));

        assertTrue(envVars.containsKey(RvmUtils.RVM_RUBY_STRING));
        assertEquals(mriRuby.getRubyName(), envVars.get(RvmUtils.RVM_RUBY_STRING));

        assertTrue(envVars.containsKey(RvmUtils.RVM_GEM_SET));
        assertEquals(mriRuby.getGemSetName(), envVars.get(RvmUtils.RVM_GEM_SET));

        assertTrue(envVars.containsKey(EnvUtils.PATH));

        assertEquals(RvmFixtures.getMRIRubyRuntimeDefaultBinPath(), envVars.get("PATH"));
    }

    @Test
    public void testGetRubyHome() throws Exception {

    }

    @Test
    public void testGetGemBinPath() throws Exception {

    }

    @Test
    public void testGetRubyRuntime() throws Exception {

        final RubyRuntime mriRuby = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(mriRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRuby.getGemPath())).thenReturn(true);

        RubyRuntime rubyRuntime;

        rubyRuntime = rvmRubyLocator.getRubyRuntime("ruby-1.9.3-p0@default");

        assertEquals(mriRuby, rubyRuntime);

        final RubyRuntime jRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(jRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(jRuby.getGemPath())).thenReturn(true);

        rubyRuntime = rvmRubyLocator.getRubyRuntime("jruby-1.6.5@default");

        assertEquals(jRuby, rubyRuntime);
    }

    @Test
    public void testListRubyRuntimes() throws Exception {

        when(fileSystemHelper.listPathDirNames("/home/markw/.rvm/rubies")).thenReturn(
                Lists.newArrayList(
                        "jruby-1.6.5",
                        "ruby-1.9.3-p0")
        );

        when(fileSystemHelper.listPathDirNames("/home/markw/.rvm/gems")).thenReturn(
                Lists.newArrayList(
                        "jruby-1.6.5",
                        "jruby-1.6.5@global",
                        "ruby-1.9.3-p0",
                        "ruby-1.9.3-p0@global",
                        "ruby-1.9.3-p0@rails31")
        );

        final RubyRuntime mriRuby = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(mriRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRuby.getGemPath())).thenReturn(true);

        final RubyRuntime jRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(jRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(jRuby.getGemPath())).thenReturn(true);

        List<RubyRuntime> rubyRuntimeList = rvmRubyLocator.listRubyRuntimes();

        final RubyRuntime mriRubyRails31 = RvmFixtures.getMRIRubyRuntimeRails31GemSet();

        when(fileSystemHelper.pathExists(mriRubyRails31.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRubyRails31.getGemPath())).thenReturn(true);

        assertEquals(3, rubyRuntimeList.size());

        assertTrue(rubyRuntimeList.contains(getMRIRubyRuntimeDefaultGemSet()));
        assertTrue(rubyRuntimeList.contains(getMRIRubyRuntimeRails31GemSet()));
        assertTrue(rubyRuntimeList.contains(getJRubyRuntimeDefaultGemSet()));

    }

    @Test
    public void testSearchForRubyExecutable() {

        final RubyRuntime mriRuby = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(mriRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRuby.getGemPath())).thenReturn(true);
        when(fileSystemHelper.executableFileExists(mriRuby.getGemPath() + "/bin/rake")).thenReturn(true);

        String executablePath = rvmRubyLocator.searchForRubyExecutable("ruby-1.9.3-p0@default", "rake");

        assertEquals(mriRuby.getGemPath() + "/bin/rake", executablePath);

    }


    @Test(expected = IllegalArgumentException.class)
    public void testSearchForRubyExecutableWhenNotFound() {

        final RubyRuntime mriRuby = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();

        when(fileSystemHelper.pathExists(mriRuby.getRubyExecutablePath())).thenReturn(true);
        when(fileSystemHelper.pathExists(mriRuby.getGemPath())).thenReturn(true);
        when(fileSystemHelper.executableFileExists(mriRuby.getGemPath() + "/bin/rake")).thenReturn(false);

        rvmRubyLocator.searchForRubyExecutable("ruby-1.9.3-p0@default", "rake");
    }


    @Test
    public void testHasRuby() throws Exception {

        when(fileSystemHelper.listPathDirNames("/home/markw/.rvm/rubies")).thenReturn(
                Lists.newArrayList(
                        "jruby-1.6.5",
                        "ruby-1.9.3-p0")
        );

        assertTrue(rvmRubyLocator.hasRuby("ruby-1.9.3-p0"));
        assertTrue(rvmRubyLocator.hasRuby("jruby-1.6.5"));
        assertFalse(rvmRubyLocator.hasRuby("ruby-1.9.2-p0"));
    }
}
