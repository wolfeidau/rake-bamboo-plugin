package au.id.wolfe.bamboo.ruby.locator;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.rbenv.RbenvRubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.system.SystemRubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.windows.WindowsRubyRuntimeLocatorService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Manages the various Ruby Runtime Managers.
 */
public class RubyLocatorServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(RubyLocatorServiceFactory.class);

    private final List<RubyRuntimeLocatorService> rubyRuntimeLocatorServices;

    public RubyLocatorServiceFactory() {
        rubyRuntimeLocatorServices = Lists.newArrayList(new SystemRubyRuntimeLocatorService(),
                new RbenvRubyRuntimeLocatorService(),
                new RvmRubyRuntimeLocatorService(),
                new WindowsRubyRuntimeLocatorService());

        log.info("Loaded ruby runtime managers {} ", rubyRuntimeLocatorServices);
    }

    public RubyLocatorServiceFactory(SystemRubyRuntimeLocatorService systemRubyRuntimeLocatorService,
                                     RvmRubyRuntimeLocatorService rvmRubyRuntimeLocatorService,
                                     RbenvRubyRuntimeLocatorService rbenvRubyRuntimeLocatorService,
                                     WindowsRubyRuntimeLocatorService windowsRubyRuntimeLocatorService) {

        rubyRuntimeLocatorServices = Lists.newArrayList(systemRubyRuntimeLocatorService,
                rbenvRubyRuntimeLocatorService,
                rvmRubyRuntimeLocatorService,
                windowsRubyRuntimeLocatorService);

        log.info("Loaded ruby runtime managers {} ", rubyRuntimeLocatorServices);

    }

    public List<RubyRuntimeLocatorService> getLocatorServices() {
        return rubyRuntimeLocatorServices;
    }

    public RubyLocator acquireRubyLocator(String rubyRuntimeManager) {

        // default to RVM in the case of a missing runtime manager tag.
        if (rubyRuntimeManager == null || rubyRuntimeManager.equals("")) {
            rubyRuntimeManager = RvmRubyRuntimeLocatorService.MANAGER_LABEL;
        }

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : rubyRuntimeLocatorServices) {
            if (rubyRuntimeLocatorService.getRuntimeManagerName().equals(rubyRuntimeManager)) {
                if (rubyRuntimeLocatorService.isInstalled()) {
                    return rubyRuntimeLocatorService.getRubyLocator();
                } else {
                    throw new PathNotFoundException("Unable to locate Runtime Manager installation.");
                }
            }
        }

        throw new IllegalArgumentException("Unable to locate runtime manager for - " + rubyRuntimeManager);
    }
}
