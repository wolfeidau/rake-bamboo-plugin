package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class CapistranoTask extends AbstractRubyTask{

    @Override
    protected Map<String, String> buildEnvironment(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected List<String> buildCommandList(RubyLabel rubyRuntimeLabel, ConfigurationMap config) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
