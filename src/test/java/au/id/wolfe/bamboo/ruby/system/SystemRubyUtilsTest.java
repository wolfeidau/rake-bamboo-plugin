package au.id.wolfe.bamboo.ruby.system;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the System Ruby Util methods.
 */
public class SystemRubyUtilsTest {

    static final String rubyOsxVersionString  =
            "ruby 1.8.7 (2010-01-10 patchlevel 249) [universal-darwin11.0]";

    static final String rubyWindowsVersionString = "ruby 1.9.3p125 (2012-02-16) [i386-mingw32]";

    @Test
    public void testParseOsxRubyVersionString() throws Exception {

        String rubyVersion =  SystemRubyUtils.parseRubyVersionString(rubyOsxVersionString);

        assertThat(rubyVersion, equalTo("1.8.7-p249"));
    }

    @Test
    public void testParseWindowsRubyVersionString() throws Exception {

        String rubyVersion =  SystemRubyUtils.parseRubyVersionString(rubyWindowsVersionString);

        assertThat(rubyVersion, equalTo("1.9.3-p125"));
    }
}
