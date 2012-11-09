package au.id.wolfe.bamboo.ruby.rvm;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeName;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test the RVM utility functions class.
 */
public class RvmUtilsTest {

    @Test
    public void testParseRubyRuntimeName() throws Exception {

        RubyRuntimeName rubyRuntimeTokens = RvmUtils.parseRubyRuntimeName("ruby-1.9.3-p0@rails31");

        assertEquals("ruby-1.9.3-p0", rubyRuntimeTokens.getVersion());
        assertEquals("rails31", rubyRuntimeTokens.getGemSet());

    }

    @Test
    public void testBuildGemSetName() throws Exception {

        String gemSetName = RvmUtils.buildGemSetName("ruby-1.9.3-p0", "ruby-1.9.3-p0");

        assertEquals("default", gemSetName);
    }

    @Test
    public void testBuildGemSetDirectoryName() {

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtils.buildGemSetDirectoryName("ruby-1.9.3-p0", "default");
        assertEquals("ruby-1.9.3-p0", gemSetDirectoryName);

        gemSetDirectoryName = RvmUtils.buildGemSetDirectoryName("ruby-1.9.3-p0", "rails31");
        assertEquals("ruby-1.9.3-p0@rails31", gemSetDirectoryName);

    }

    @Test
    public void testBuildGemHomePath() {

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtils.buildGemHomePath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0", gemSetDirectoryName);

        gemSetDirectoryName = RvmUtils.buildGemHomePath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31", gemSetDirectoryName);
    }

    @Test
    public void testBuildBinPath() {

        String binPath;

        binPath = RvmUtils.buildBinPath("/home/markw/.rvm/rubies", "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin:/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin", binPath);

        binPath = RvmUtils.buildBinPath("/home/markw/.rvm/rubies", "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin:/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin", binPath);
    }

    @Test
    public void testBuildRubyBinPath() {
        String rubyBinPath;

        rubyBinPath = RvmUtils.buildRubyBinPath("/home/markw/.rvm/rubies", "ruby-1.9.3-p0");
        assertEquals("/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin", rubyBinPath);
    }

    @Test
    public void testSplitRakeTargets() {

        String targets = "db:migrate spec";

        List<String> targetList = RvmUtils.splitRakeTargets(targets);

        assertEquals(2, targetList.size());

        assertTrue(targetList.contains("db:migrate"));
        assertTrue(targetList.contains("spec"));

    }
}
