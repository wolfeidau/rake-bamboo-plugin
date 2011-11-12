package au.id.wolfe.bamboo.ruby.rake;

import au.id.wolfe.bamboo.ruby.rake.RakeTask;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Needs more work that is for sure.
 */
public class RakeTaskTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSplitTargets(){
        List<String> targetList = RakeTask.splitTargets("db:migrate spec");

        assertTrue(targetList.contains("db:migrate"));
        assertTrue(targetList.contains("spec"));
    }
}
