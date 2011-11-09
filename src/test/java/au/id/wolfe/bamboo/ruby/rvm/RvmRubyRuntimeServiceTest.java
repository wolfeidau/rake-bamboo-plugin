package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.RubyRuntime;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the functions around detecting RVM installation and listing the managed ruby runtimes
 */
public class RvmRubyRuntimeServiceTest {

    RvmRubyRuntimeService rvmRubyRuntimeService;

    @Before
    public void setup() {
        rvmRubyRuntimeService = new RvmRubyRuntimeService();
    }

    @Test
    public void testGetRubyRuntimes() throws Exception {

        List<RubyRuntime> rubyRuntimes = rvmRubyRuntimeService.getRubyRuntimes();

        RubyRuntime rubyRuntime = new RubyRuntime(
                "ruby-1.9.3-p0@rails31",
                "/Users/markw/.rvm/rubies/ruby-1.9.3-p0/bin/ruby",
                "/Users/markw/.rvm/gems/ruby-1.9.3-p0@rails31",
                "/Users/markw/.rvm/gems/ruby-1.9.3-p0@rails31",
                "/Users/markw/.rvm/gems/ruby-1.9.3-p0@rails31/bin"
        );

        assertTrue(rubyRuntimes.contains(rubyRuntime));

    }

    @Test
    public void testGetGemSetName() {

        String gemSetNameDefault = rvmRubyRuntimeService.getGemSetName(
                new File("/Users/markw/.rvm/rubies/ruby-1.9.3-p0/"),
                new File("/Users/markw/.rvm/gems/ruby-1.9.3-p0")
        );

        assertEquals("ruby-1.9.3-p0@default", gemSetNameDefault);

        String gemSetNameRails31 = rvmRubyRuntimeService.getGemSetName(
                new File("/Users/markw/.rvm/rubies/ruby-1.9.3-p0/"),
                new File("/Users/markw/.rvm/gems/ruby-1.9.3-p0@rails31")
        );

        assertEquals("ruby-1.9.3-p0@rails31", gemSetNameRails31);

    }


}
