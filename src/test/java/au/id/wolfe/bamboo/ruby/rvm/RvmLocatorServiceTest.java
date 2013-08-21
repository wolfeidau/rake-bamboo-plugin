package au.id.wolfe.bamboo.ruby.rvm;

import static au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures.USER_HOME;
import static au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures.getSystemRvmInstallation;
import static au.id.wolfe.bamboo.ruby.fixtures.RvmFixtures.getUserRvmInstallation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;

/**
 * Unit test for RVM related operations.
 */
@RunWith(MockitoJUnitRunner.class)
public class RvmLocatorServiceTest {

    @Mock
    private FileSystemHelper fileSystemHelper;

    private RvmRubyRuntimeLocatorService rvmLocatorService;

    @Before
    public void setup() {
        rvmLocatorService = new RvmRubyRuntimeLocatorService(fileSystemHelper);
    }

    @Test
    public void testLocateRvmInstallation() {

        when(fileSystemHelper.getUserHome()).thenReturn(USER_HOME);

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
        when(fileSystemHelper.getUserHome()).thenReturn(USER_HOME);

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation(userRvm);

        RubyLocator rvmRubyLocator = rvmLocatorService.getRvmRubyLocator();

        assertNotNull(rvmRubyLocator);

    }

    @Test(expected = PathNotFoundException.class)
    public void testGetRubyLocatorWhenNotFound() {
        when(fileSystemHelper.getUserHome()).thenReturn(null);
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
