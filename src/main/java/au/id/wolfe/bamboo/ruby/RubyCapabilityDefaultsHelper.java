package au.id.wolfe.bamboo.ruby;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.common.RubyRuntime;
import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.locator.RubyLocatorServiceFactory;
import au.id.wolfe.bamboo.ruby.tasks.rake.RakeTask;
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

    private final RubyLocatorServiceFactory rubyLocatorServiceFactory;

    public RubyCapabilityDefaultsHelper() {
        this.rubyLocatorServiceFactory = new RubyLocatorServiceFactory();
    }

    public RubyCapabilityDefaultsHelper(RubyLocatorServiceFactory rubyLocatorServiceFactory) {
        log.info("loading rubyLocatorServiceFactory = " + rubyLocatorServiceFactory);
        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
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

        log.info("Retrieving a list of runtime managers.");

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : rubyLocatorServiceFactory.getLocatorServices()) {

            log.info("Loading ruby locator service - {}", rubyRuntimeLocatorService.getRuntimeManagerName());

            if (rubyRuntimeLocatorService.isInstalled()) {

                List<RubyRuntime> rvmRubyRuntimeList = rubyRuntimeLocatorService.getRubyLocator().listRubyRuntimes();

                for (RubyRuntime rubyRuntime : rvmRubyRuntimeList) {

                    final RubyLabel rubyLabel = new RubyLabel(rubyRuntimeLocatorService.getRuntimeManagerName(),
                            rubyRuntime.getRubyRuntimeName());
                    final String capabilityLabel = buildCapabilityLabel(RakeTask.RUBY_CAPABILITY_PREFIX, rubyLabel);
                    final Capability capability = new CapabilityImpl(capabilityLabel, rubyRuntime.getRubyExecutablePath());

                    log.info("Adding " + capability);

                    capabilitySet.addCapability(capability);
                }

            }
        }

        return capabilitySet;
    }

    private String buildCapabilityLabel(String prefix, RubyLabel rubyLabel) {
        return String.format("%s.%s", prefix, rubyLabel);
    }
}
