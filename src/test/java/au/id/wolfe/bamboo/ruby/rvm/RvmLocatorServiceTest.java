package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.util.SystemHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    private FileSystemHelper fileSystemHelper;

    @Mock
    private SystemHelper systemHelper;

    private RvmRubyRuntimeLocatorService rvmLocatorService;

    @Before
    public void setup() {
        rvmLocatorService = new RvmRubyRuntimeLocatorService(fileSystemHelper, systemHelper);
    }

    @Test
    public void testLocateRvmInstallation() {

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
    public void testGetRubyLocator() {
        when(systemHelper.getUserHome()).thenReturn(USER_HOME);

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation(userRvm);

        RubyLocator rvmRubyLocator = rvmLocatorService.getRvmRubyLocator();

        assertNotNull(rvmRubyLocator);

    }

    @Test(expected = PathNotFoundException.class)
    public void testGetRubyLocatorWhenNotFound() {
        when(systemHelper.getUserHome()).thenReturn(null);
        when(fileSystemHelper.pathExists("/usr/local/rvm")).thenReturn(false);
        when(fileSystemHelper.pathExists("/opt/local/rvm")).thenReturn(false);

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation(userRvm);

        rvmLocatorService.getRvmRubyLocator();
    }

    private void primeMockWithRvmInstallation(RvmInstallation rvmInstallation) {
        when(fileSystemHelper.pathExists(rvmInstallation.getInstallPath())).thenReturn(true);
        when(fileSystemHelper.pathExists(rvmInstallation.getRubiesPath())).thenReturn(true);
        when(fileSystemHelper.pathExists(rvmInstallation.getGemsPath())).thenReturn(true);

    }

}
