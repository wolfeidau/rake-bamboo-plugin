package au.id.wolfe.bamboo.ruby.rvm;

import com.atlassian.fage.Pair;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test the RVM utility functions class.
 */
public class RvmUtilTest {

    @Test
    public void testParseRubyRuntimeName() throws Exception {

        Pair<String, String> rubyRuntimeTokens = RvmUtil.parseRubyRuntimeName("ruby-1.9.3-p0@rails31");

        assertEquals("ruby-1.9.3-p0", rubyRuntimeTokens.left());
        assertEquals("rails31", rubyRuntimeTokens.right());

    }

    @Test
    public void testGemSetName() throws Exception {

        String gemSetName = RvmUtil.gemSetName("ruby-1.9.3-p0", "ruby-1.9.3-p0");

        assertEquals("default", gemSetName);
    }

    @Test
    public void testGemSetDirectoryName(){

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtil.gemSetDirectoryName("ruby-1.9.3-p0", "default");
        assertEquals("ruby-1.9.3-p0", gemSetDirectoryName);

        gemSetDirectoryName = RvmUtil.gemSetDirectoryName("ruby-1.9.3-p0", "rails31");
        assertEquals("ruby-1.9.3-p0@rails31", gemSetDirectoryName);

    }

    @Test
    public void testBuildGemHomePath(){

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtil.buildGemHomePath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0", gemSetDirectoryName);

        gemSetDirectoryName = RvmUtil.buildGemHomePath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31", gemSetDirectoryName);
    }

    @Test
    public void testBuildGemBinPath(){

        String gemBinPath;

        gemBinPath = RvmUtil.buildGemBinPath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin", gemBinPath);

        gemBinPath = RvmUtil.buildGemBinPath("/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31");
        assertEquals("/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin", gemBinPath);
    }

    @Test
    public void testSplitRakeTargets(){

        String targets = "db:migrate spec";

        List<String> targetList =  RvmUtil.splitRakeTargets(targets);

        assertEquals(2, targetList.size());

        assertTrue(targetList.contains("db:migrate"));
        assertTrue(targetList.contains("spec"));

    }
}
