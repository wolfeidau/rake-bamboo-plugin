package au.id.wolfe.bamboo.ruby.system;

import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test the system ruby locator
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemRubyLocatorTest {

    @Mock
    FileSystemHelper fileSystemHelper;

    @Mock
    SystemRubyLocator systemRubyLocator;

    @Before
    public void setup() {
        systemRubyLocator = new SystemRubyLocator(fileSystemHelper);
    }

    @Test
    public void testListRubyRuntimes() throws Exception {

        when(fileSystemHelper.pathExists("/usr/bin", "ruby")).thenReturn(true);
        when(fileSystemHelper.pathExists("/usr/bin", "gem")).thenReturn(true);

        List<RubyRuntime> rubyRuntimeList = systemRubyLocator.listRubyRuntimes();

        assertThat(rubyRuntimeList.size(), equalTo(1));

        assertThat(rubyRuntimeList.get(0).getRubyName(), equalTo("1.8.7-p358"));


    }
}
