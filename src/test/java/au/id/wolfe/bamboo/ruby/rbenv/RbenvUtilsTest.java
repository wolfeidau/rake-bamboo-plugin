package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeName;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the rbenv util methods.
 */
public class RbenvUtilsTest {

    @Test
    public void testBuildRbenvRubiesPath() throws Exception {
        String rubiesPath = RbenvUtils.buildRbenvRubiesPath("/Users/markw/.rbenv");

        assertThat(rubiesPath, equalTo("/Users/markw/.rbenv/versions"));
    }

    @Test
    public void testParseRubyRuntimeName() throws Exception {

        RubyRuntimeName rubyRuntimeName = RbenvUtils.parseRubyRuntimeName("1.9.3-p194@default");

        assertThat(rubyRuntimeName.getVersion(), equalTo("1.9.3-p194"));
        assertThat(rubyRuntimeName.getGemSet(), equalTo("default"));
    }

    @Test
    public void testBuildRubyExecutablePath() throws Exception {

        // /Users/markw/.rbenv/versions/1.9.3-p194/bin/ruby

        String rubyBinPath = RbenvUtils.buildRubyExecutablePath("/Users/markw/.rbenv", "1.9.3-p194");

        assertThat(rubyBinPath, equalTo("/Users/markw/.rbenv/versions/1.9.3-p194/bin/ruby"));

    }

}
