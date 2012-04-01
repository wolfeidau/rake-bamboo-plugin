package au.id.wolfe.bamboo.ruby.system;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;

/**
 * Minimal ruby runtime manager designed to locate system installations.
 */
public class SystemRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    public static final String MANAGER_LABEL = "SYSTEM";

    public SystemRubyLocator systemRubyLocator;

    public SystemRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper) {
        systemRubyLocator = new SystemRubyLocator(fileSystemHelper);
    }

    @Override
    public RubyLocator getRubyLocator() {
        return systemRubyLocator;
    }

    @Override
    public boolean isInstalled() {
        return true;
    }

    @Override
    public String getRuntimeManagerName() {
        return MANAGER_LABEL;
    }
}
