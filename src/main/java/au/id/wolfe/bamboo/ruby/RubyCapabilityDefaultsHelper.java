package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.rake.RakeTask;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Detects ruby runtimes using rvm.
 */
public class RubyCapabilityDefaultsHelper implements CapabilityDefaultsHelper {

    private static final Logger log = LoggerFactory.getLogger(RubyCapabilityDefaultsHelper.class);

    private final RubyRuntimeService rubyRuntimeService;

    public RubyCapabilityDefaultsHelper(RubyRuntimeService rubyRuntimeService) {
        this.rubyRuntimeService = rubyRuntimeService;
    }

    @NotNull
    @Override
    public CapabilitySet addDefaultCapabilities(@NotNull CapabilitySet capabilitySet) {

        log.debug("Loading ruby capabilities using RVM");

        List<RubyRuntime> rubyRuntimeList = rubyRuntimeService.getRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            Capability capability = new CapabilityImpl(RakeTask.RUBY_CAPABILITY_PREFIX + "." + rubyRuntime.getName(), rubyRuntime.getPath());
            capabilitySet.addCapability(capability);
        }



        return capabilitySet;
    }


}
