package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.tasks.rake.RakeTask;
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

        if (rvmLocatorService.isRvmInstalled()) {

            List<RubyRuntime> rvmRubyRuntimeList = rvmLocatorService.getRvmRubyLocator().listRubyRuntimes();

            for (RubyRuntime rubyRuntime : rvmRubyRuntimeList) {
                
                final RubyLabel rubyLabel = new RubyLabel(RvmLocatorService.MANAGER_LABEL, rubyRuntime.getRubyRuntimeName());
                final String capabilityLabel = buildCapabilityLabel(RakeTask.RUBY_CAPABILITY_PREFIX, rubyLabel);
                Capability capability = new CapabilityImpl(capabilityLabel, rubyRuntime.getRubyExecutablePath());
                log.info("Adding " + capability);
                capabilitySet.addCapability(capability);
            }

        }
        return capabilitySet;
    }
    
    private String buildCapabilityLabel(String prefix, RubyLabel rubyLabel){
        return String.format("%s.%s", prefix, rubyLabel);
    }
}
