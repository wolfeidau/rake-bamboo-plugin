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
    
    String rvmInstallPath;

    @Before
    public void setup() {
        rvmRubyRuntimeService = new RvmRubyRuntimeService();
        rvmInstallPath = rvmRubyRuntimeService.getRvmPath().getAbsolutePath();
    }

    @Test
    public void testGetRubyRuntimes() throws Exception {

        List<RubyRuntime> rubyRuntimes = rvmRubyRuntimeService.getRubyRuntimes();

        RubyRuntime rubyRuntime = getRails31RubyRuntime();

        assertTrue(rubyRuntimes.contains(rubyRuntime));

    }

    @Test
    public void testGetGemSetName() {

        String gemSetNameDefault = rvmRubyRuntimeService.getGemSetName(
                "ruby-1.9.3-p0",
                "ruby-1.9.3-p0"
        );

        assertEquals("ruby-1.9.3-p0@default", gemSetNameDefault);

        String gemSetNameRails31 = rvmRubyRuntimeService.getGemSetName(
                "ruby-1.9.3-p0",
                "ruby-1.9.3-p0@rails31"
        );

        assertEquals("ruby-1.9.3-p0@rails31", gemSetNameRails31);

    }

    @Test
    public void testGetRubyRuntime() {

        RubyRuntime rubyRuntimeDefault = rvmRubyRuntimeService.getRubyRuntime("ruby-1.9.3-p0@default");

        assertEquals(getDefaultRubyRuntime(), rubyRuntimeDefault);

        RubyRuntime rubyRuntimeRails31 = rvmRubyRuntimeService.getRubyRuntime("ruby-1.9.3-p0@rails31");

        assertEquals(getRails31RubyRuntime(), rubyRuntimeRails31);
    }

    @Test
    public void test(){
        String rakeScriptPath = rvmRubyRuntimeService.getPathToScript(getRails31RubyRuntime(), "rake");
        assertEquals(rvmInstallPath + "/gems/ruby-1.9.3-p0@rails31/bin/rake", rakeScriptPath);

    }

    private RubyRuntime getRails31RubyRuntime() {
        return new RubyRuntime(
                "ruby-1.9.3-p0@rails31",
                rvmInstallPath + "/rubies/ruby-1.9.3-p0/bin/ruby",
                rvmInstallPath + "/rubies/ruby-1.9.3-p0",
                rvmInstallPath + "/gems/ruby-1.9.3-p0@rails31",
                rvmInstallPath + "/gems/ruby-1.9.3-p0@rails31:" + rvmInstallPath + "/gems/ruby-1.9.3-p0@global",
                rvmInstallPath + "/gems/ruby-1.9.3-p0@rails31/bin:" + rvmInstallPath + "/gems/ruby-1.9.3-p0@global/bin"
        );
    }

    private RubyRuntime getDefaultRubyRuntime() {
        return new RubyRuntime(
                "ruby-1.9.3-p0@default",
                rvmInstallPath + "/rubies/ruby-1.9.3-p0/bin/ruby",
                rvmInstallPath + "/rubies/ruby-1.9.3-p0",
                rvmInstallPath + "/gems/ruby-1.9.3-p0",
                rvmInstallPath + "/gems/ruby-1.9.3-p0:" + rvmInstallPath + "/gems/ruby-1.9.3-p0@global",
                rvmInstallPath + "/gems/ruby-1.9.3-p0/bin:" + rvmInstallPath + "/gems/ruby-1.9.3-p0@global/bin"
        );
    }

// <RubyRuntime[name=ruby-1.9.3-p0@default,path=/Users/markw/.rvm/rubies/ruby-1.9.3-p0/bin/ruby,gemHome=/Users/markw/.rvm/gems/ruby-1.9.3-p0,gemPath=/Users/markw/.rvm/gems/ruby-1.9.3-p0:/gems/ruby-1.9.3-p0@global,binPath=/Users/markw/.rvm/gems/ruby-1.9.3-p0/bin:/gems/ruby-1.9.3-p0@global/bin]> but was:
// <RubyRuntime[name=ruby-1.9.3-p0@default,path=/Users/markw/.rvm/rubies/ruby-1.9.3-p0/bin/ruby,gemHome=/Users/markw/.rvm/gems/ruby-1.9.3-p0,gemPath=/Users/markw/.rvm/gems/ruby-1.9.3-p0:/Users/markw/.rvm/gems/ruby-1.9.3-p0@global,binPath=/Users/markw/.rvm/gems/ruby-1.9.3-p0/bin:/Users/markw/.rvm/gems/ruby-1.9.3-p0@global/bin]>

}
