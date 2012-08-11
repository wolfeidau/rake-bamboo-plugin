package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.util.SystemHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

/**
 * Unit test for rbenv locator service.
 */
@RunWith(MockitoJUnitRunner.class)
public class RbenvRubyRuntimeLocatorServiceTest {

    private static final String TEST_HOME_DIR = "/Users/markw";
    private static final String RBENV_DIR = TEST_HOME_DIR + "/.rbenv";

    private RbenvRubyRuntimeLocatorService rbenvRubyRuntimeLocatorService;

    @Mock
    private FileSystemHelper fileSystemHelper;

    @Mock
    private SystemHelper systemHelper;

    @Before
    public void setUp() throws Exception {

        rbenvRubyRuntimeLocatorService = new RbenvRubyRuntimeLocatorService(fileSystemHelper, systemHelper);
        when(systemHelper.getUserHome()).thenReturn(TEST_HOME_DIR);

    }


    @Test
    public void testGetRubyLocator() throws Exception {

        when(fileSystemHelper.pathExists(RBENV_DIR)).thenReturn(true);
        RubyLocator rubyLocator = rbenvRubyRuntimeLocatorService.getRubyLocator();
        assertThat(rubyLocator, notNullValue());

    }

    @Test
    public void testIsInstalled() throws Exception {

        when(fileSystemHelper.pathExists(RBENV_DIR)).thenReturn(true);
        assertThat(rbenvRubyRuntimeLocatorService.isInstalled(), equalTo(true));

    }

    @Test
    public void testGetRuntimeManagerName() throws Exception {

        assertThat(rbenvRubyRuntimeLocatorService.getRuntimeManagerName(), equalTo("rbenv"));

    }
}
