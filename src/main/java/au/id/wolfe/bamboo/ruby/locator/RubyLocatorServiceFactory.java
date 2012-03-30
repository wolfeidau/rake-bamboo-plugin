package au.id.wolfe.bamboo.ruby.locator;

import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.rvm.RvmLocatorService;

/**
 * Manages the various Ruby Runtime Managers.
 */
public class RubyLocatorServiceFactory {

    private RvmLocatorService rvmLocatorService;

    public void setRvmLocatorService(RvmLocatorService rvmLocatorService) {
        this.rvmLocatorService = rvmLocatorService;
    }
    
    public RubyLocator acquireRubyLocator(String rubyRuntimeManager){

        if (rubyRuntimeManager.equals(RvmLocatorService.MANAGER_LABEL)){

            if (rvmLocatorService.isRvmInstalled()){
                return rvmLocatorService.getRvmRubyLocator();
            } else {
                throw new PathNotFoundException("Unable to locate RVM installation.");
            }

        } else {
            throw new IllegalArgumentException("Unable to locate runtime manager for - " + rubyRuntimeManager);
        }

    }
}
