package au.id.wolfe.bamboo.ruby.rvm;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Test the RVM utility functions class.
 */
public class RvmUtilTest {
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
}
