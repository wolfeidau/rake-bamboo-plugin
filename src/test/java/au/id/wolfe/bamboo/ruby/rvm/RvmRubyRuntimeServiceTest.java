package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.RubyRuntime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test the functions around detecting RVM installation and listing the managed ruby runtimes
 */
public class RvmRubyRuntimeServiceTest {

    @Test
    public void testGetRubyRuntimes() throws Exception {

        RvmRubyRuntimeService rvmRubyRuntimeService = new RvmRubyRuntimeService();

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


}
