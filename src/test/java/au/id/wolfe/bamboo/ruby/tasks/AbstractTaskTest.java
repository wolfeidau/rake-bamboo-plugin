package au.id.wolfe.bamboo.ruby.tasks;

import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyLocator;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import org.mockito.Mock;

/**
 * Helper class to house common data.
 */
public abstract class AbstractTaskTest {

    @Mock
    protected ProcessService processService;

    @Mock
    protected RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    protected EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    protected RvmRubyLocator rvmRubyLocator;

    public abstract void testBuildCommandList();

    public abstract void testBuildEnvironment();

}
