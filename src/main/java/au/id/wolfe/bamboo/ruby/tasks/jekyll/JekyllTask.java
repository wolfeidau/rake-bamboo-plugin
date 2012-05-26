package au.id.wolfe.bamboo.ruby.tasks.jekyll;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;

import java.util.List;
import java.util.Map;

/**
 * Task which interacts with Jekyll.
 *
 * Arguments.
 *  --url
 */
public class JekyllTask extends AbstractRubyTask {

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {

        final RubyLocator rubyLocator = getRubyLocator(rubyRuntimeLabel.getRubyRuntimeManager());

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
