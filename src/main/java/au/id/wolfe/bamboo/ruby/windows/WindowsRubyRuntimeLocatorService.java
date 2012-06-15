package au.id.wolfe.bamboo.ruby.windows;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.locator.RubyLocator;
import au.id.wolfe.bamboo.ruby.util.ExecHelper;
import au.id.wolfe.bamboo.ruby.util.FileSystemHelper;
import org.apache.commons.lang.SystemUtils;

/**
 * This service assists with detection of ruby installations in windows.
 */
public class WindowsRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    public static final String MANAGER_LABEL = "WINDOWS";

    private final WindowsRubyLocator windowsRubyLocator;

    public WindowsRubyRuntimeLocatorService() {
        this.windowsRubyLocator = new WindowsRubyLocator(new ExecHelper());
    }

    public WindowsRubyRuntimeLocatorService(ExecHelper execHelper) {
        this.windowsRubyLocator = new WindowsRubyLocator(execHelper);
    }

    @Override
    public RubyLocator getRubyLocator() {
        return windowsRubyLocator;
    }

    @Override
    public boolean isInstalled() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    @Override
    public String getRuntimeManagerName() {
        return MANAGER_LABEL;
    }
}
