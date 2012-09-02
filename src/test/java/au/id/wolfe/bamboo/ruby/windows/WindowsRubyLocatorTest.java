package au.id.wolfe.bamboo.ruby.windows;

import au.id.wolfe.bamboo.ruby.util.ExecHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 *
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WindowsRubyLocatorTest {

    private final String whereOutput = "C:\\Windows\\System32\\notepad.exe\r\n" +
            "C:\\Windows\\notepad.exe\r\n" +
            "\r\n";

    @Mock
    ExecHelper execHelper;

    WindowsRubyLocator windowsRubyLocator;

    @Before
    public void setup() throws Exception {

        when(execHelper.getExecutablePath(any(String.class), eq(true))).thenReturn(whereOutput);

        windowsRubyLocator = new WindowsRubyLocator(execHelper);

    }

    @Test
    public void testDetectExecutableOnPath() throws Exception {

        String path = windowsRubyLocator.detectExecutableOnPath("notepad.exe");

        assertThat(path, equalTo("C:\\Windows\\System32\\notepad.exe"));
    }

}
