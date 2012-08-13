package au.id.wolfe.bamboo.ruby.rbenv;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.util.EnvUtils;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import au.id.wolfe.bamboo.ruby.util.SystemHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test the rbenv Ruby Locator
 */
@RunWith(MockitoJUnitRunner.class)
public class RbenvRubyLocatorTest {

    final String badGemHomePath = "/usr/lib/ruby/home";

    RbenvRubyLocator rbenvRubyLocator;

    @Mock
    private FileSystemHelper fileSystemHelper;

    @Mock
    private SystemHelper systemHelper;

    final static RubyRuntime ruby192p290default = new RubyRuntime("1.9.2-p290", "default", "/Users/markw/.rbenv/versions/1.9.2-p290/bin/ruby", null);

    @Before
    public void setUp() throws Exception {

        rbenvRubyLocator = new RbenvRubyLocator(fileSystemHelper, "/Users/markw/.rbenv");
        when(systemHelper.getUserHome()).thenReturn("/Users/markw");

    }

    @Test
    public void testBuildEnv() throws Exception {

        // seeding the env with stuff that screw up
        // us running ruby from rbenv installation.
        Map<String, String> existingEnv = Maps.newHashMap();
        existingEnv.put(EnvUtils.GEM_HOME, badGemHomePath);
        existingEnv.put(EnvUtils.PATH, "/usr/bin:/bin:/usr/sbin:/sbin");

        Map<String, String> updatedEnv = rbenvRubyLocator.buildEnv("1.9.2-p290@default", existingEnv);

        assertThat(updatedEnv.containsKey(EnvUtils.GEM_HOME), equalTo(false));

        assertThat(updatedEnv.get(EnvUtils.GEM_HOME), nullValue());
        assertThat(updatedEnv.get(EnvUtils.GEM_PATH), nullValue());

       assertThat(updatedEnv.get(EnvUtils.PATH), equalTo("/Users/markw/.rbenv/versions/1.9.2-p290/bin:/usr/bin:/bin:/usr/sbin:/sbin"));

    }

    @Test
    public void testSearchForRubyExecutable() throws Exception {

        assertThat(rbenvRubyLocator.searchForRubyExecutable("1.9.2-p290@default", "rake"),
                equalTo("/Users/markw/.rbenv/versions/1.9.2-p290/bin/rake"));
    }

    @Test
    public void testGetRubyRuntimeByVersionAndGemset() throws Exception {

        assertThat(
                rbenvRubyLocator.getRubyRuntime("1.9.2-p290", "default"),
                equalTo(ruby192p290default));
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetRubyRuntimeByVersionAndGemsetWithMissingRuby() throws Exception {

        doThrow(new PathNotFoundException("Some exception")).when(fileSystemHelper).assertPathExists(ruby192p290default.getRubyExecutablePath(), "Unable to location ruby executable for " + ruby192p290default.getRubyName());

        rbenvRubyLocator.getRubyRuntime("1.9.2-p290", "default");

    }

    @Test
    public void testGetRubyRuntimeByRuntimeName() throws Exception {

        assertThat(
                rbenvRubyLocator.getRubyRuntime("1.9.2-p290@default"),
                equalTo(ruby192p290default));

    }

    @Test
    public void testListRubyRuntimes() throws Exception {

        when(fileSystemHelper.listPathDirNames(eq("/Users/markw/.rbenv/versions"))).thenReturn(Lists.newArrayList("1.9.2-p180", "1.9.2-p290", "ree-1.8.7-2011.12"));

        assertThat(
                rbenvRubyLocator.listRubyRuntimes(),
                hasItems(ruby192p290default));

    }

    @Test
    public void testHasRuby() throws Exception {

        when(fileSystemHelper.pathExists(ruby192p290default.getRubyExecutablePath())).thenReturn(true);

        assertThat(rbenvRubyLocator.hasRuby("1.9.2-p290@default"), equalTo(true));

        when(fileSystemHelper.pathExists(ruby192p290default.getRubyExecutablePath())).thenReturn(false);

        assertThat(rbenvRubyLocator.hasRuby("1.9.2-p290@default"), equalTo(false));

    }

    @Test
    public void testIsReadOnly() throws Exception {

    }
}
