package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.rake.RakeTask;
import au.id.wolfe.bamboo.ruby.rvm.RubyRuntime;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Detects ruby runtimes which were installed using rvm.
 */
public class RubyCapabilityDefaultsHelper implements CapabilityDefaultsHelper {

    private static final Logger log = LoggerFactory.getLogger(RubyCapabilityDefaultsHelper.class);

    private final RvmLocatorService rvmLocatorService;

    public RubyCapabilityDefaultsHelper(RvmLocatorService rvmLocatorService) {
        this.rvmLocatorService = rvmLocatorService;
    }

    /**
     * Adds default Capabilities for this module. Used during auto-detecting server capabilities.
     *
     * @param capabilitySet to add the capability to
     * @return the supplied capabilitySet with the new capabilities included if found, else the given capabilitySet
     */
    @NotNull
    @Override
    public CapabilitySet addDefaultCapabilities(@NotNull CapabilitySet capabilitySet) {

        log.debug("Loading ruby runtimes managed by RVM");

        List<RubyRuntime> rubyRuntimeList = rvmLocatorService.getRvmRubyLocator().listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            Capability capability = new CapabilityImpl(RakeTask.RUBY_CAPABILITY_PREFIX + "." + rubyRuntime.getRubyRuntimeName(), rubyRuntime.getRubyExecutablePath());
            capabilitySet.addCapability(capability);
        }

        return capabilitySet;
    }
}
