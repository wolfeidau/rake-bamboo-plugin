package au.id.wolfe.bamboo.ruby.rvm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Unit test for RVM related operations.
 */
@RunWith(MockitoJUnitRunner.class)
public class RvmLocatorServiceTest {

    @Mock
    FileSystemHelper fileSystemHelper;

    @Mock
    SystemHelper systemHelper;

    RvmLocatorService rvmLocatorService;

    @Before
    public void setup() {
        rvmLocatorService = new RvmLocatorService(fileSystemHelper, systemHelper);
    }

    @Test
    public void testLocateRvmInstallation() {

        //RvmInstallation rvmInstallation;

        when(systemHelper.getUserHome()).thenReturn(USER_HOME);

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation(userRvm);

        RvmInstallation rvmUserInstallation = rvmLocatorService.locateRvmInstallation();

        assertNotNull(rvmUserInstallation);

        assertTrue(rvmUserInstallation.isUserInstall());

        assertEquals(getUserRvmInstallation(), rvmUserInstallation);

        assertFalse(rvmUserInstallation.isReadOnly());

        reset(fileSystemHelper);

        RvmInstallation systemRvm = getSystemRvmInstallation();

        primeMockWithRvmInstallation(systemRvm);

        RvmInstallation rvmSystemInstallation = rvmLocatorService.locateRvmInstallation();

        assertNotNull(rvmSystemInstallation);

        assertEquals(getSystemRvmInstallation(), rvmSystemInstallation);

        assertTrue(rvmSystemInstallation.isReadOnly());

        reset(fileSystemHelper);

        rvmSystemInstallation = rvmLocatorService.locateRvmInstallation();

        assertNull(rvmSystemInstallation);

    }

    @Test
    public void testListRubiesManagedByRvmInstallation() {

        when(systemHelper.getUserHome()).thenReturn(USER_HOME);

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation(userRvm);

        List<RubyRuntime> rubyRuntimeList = rvmLocatorService.listRubyRuntimes();

        assertEquals(2, rubyRuntimeList.size());

        assertTrue(rubyRuntimeList.contains(getJRubyRuntimeDefaultGemSet()));

        assertTrue(rvmLocatorService.hasRuby("ruby-1.9.3-p0"));
        assertTrue(rvmLocatorService.hasRuby("jruby-1.6.5"));

    }

    @Test
    public void testLocateRubyManagedByRvmInstallation() {

        RubyRuntime rubyRuntime;

        rubyRuntime = rvmLocatorService.getRubyRuntime("ruby-1.9.3-p0");

        assertEquals("ruby-1.9.3-p0", rubyRuntime.getRubyName());
        assertEquals("default", rubyRuntime.getGemSetName());

        rubyRuntime = rvmLocatorService.getRubyRuntime("ruby-1.9.3-p0@rails31");

        assertEquals("ruby-1.9.3-p0", rubyRuntime.getRubyName());
        assertEquals("rails31", rubyRuntime.getGemSetName());


    }

    @Test
    public void testBuildEnvForRubyManagedByRvmInstallation() {

        RubyRuntime rubyRuntime = rvmLocatorService.getRubyRuntime("ruby-1.9.3-p0");

        Map<String, String> envVars = rvmLocatorService.buildEnv(rubyRuntime);

        assertTrue(envVars.containsKey(RvmLocatorService.Constants.MY_RUBY_HOME));
        assertTrue(envVars.containsKey(RvmLocatorService.Constants.GEM_HOME));
        assertTrue(envVars.containsKey(RvmLocatorService.Constants.GEM_PATH));
        assertTrue(envVars.containsKey(RvmLocatorService.Constants.BUNDLE_HOME));
        assertTrue(envVars.containsKey(RvmLocatorService.Constants.RVM_RUBY_STRING));
        assertTrue(envVars.containsKey(RvmLocatorService.Constants.RVM_GEM_SET));

    }

    private void primeMockWithRvmInstallation(RvmInstallation rvmInstallation){
        when(fileSystemHelper.pathExists(rvmInstallation.getInstallPath())).thenReturn(true);
        when(fileSystemHelper.pathExists(rvmInstallation.getRubiesPath())).thenReturn(true);
        when(fileSystemHelper.pathExists(rvmInstallation.getGemsPath())).thenReturn(true);

    }

}
