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
}
