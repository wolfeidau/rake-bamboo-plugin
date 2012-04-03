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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Test the system ruby locator
 */
public class SystemRubyLocatorTest {

    FileSystemHelper fileSystemHelper = new FileSystemHelper();

    SystemRubyLocator systemRubyLocator;

    @Before
    public void setup(){
        systemRubyLocator = new SystemRubyLocator(fileSystemHelper);
    }

    @Test
    public void testListRubyRuntimes() throws Exception {

        List<RubyRuntime> rubyRuntimeList =  systemRubyLocator.listRubyRuntimes();

        assertThat(rubyRuntimeList.size(), equalTo(1));

        assertThat(rubyRuntimeList.get(0).getRubyName(), equalTo("1.8.7-p249"));


    }
}
