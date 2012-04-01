package au.id.wolfe.bamboo.ruby.common;

import au.id.wolfe.bamboo.ruby.locator.RubyLocator;

/**
 * ruby locator services.
 */
public interface RubyRuntimeLocatorService {

    RubyLocator getRubyLocator();

    /**
     * Check if the ruby runtime manager is installed.
     *
     * @return boolean, true indicates ruby runtime manager is installed.
     */
    boolean isInstalled();

    String getRuntimeManagerName();
}
