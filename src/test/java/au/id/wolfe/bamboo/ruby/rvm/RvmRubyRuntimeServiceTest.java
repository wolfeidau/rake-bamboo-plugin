package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.RubyRuntime;
import org.junit.Before;
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

        assertTrue(rubyRuntimes.size() > 1);

    }


}
