package au.id.wolfe.bamboo.ruby.locator;

import au.id.wolfe.bamboo.ruby.common.RubyRuntimeLocatorService;
import au.id.wolfe.bamboo.ruby.common.PathNotFoundException;
import au.id.wolfe.bamboo.ruby.rvm.RvmRubyRuntimeLocatorService;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Manages the various Ruby Runtime Managers.
 */
public class RubyLocatorServiceFactory {

    private RubyRuntimeLocatorService rvmRubyRuntimeLocatorService;

    public void setRvmRubyRuntimeLocatorService(RvmRubyRuntimeLocatorService rvmRubyRuntimeLocatorService) {
        this.rvmRubyRuntimeLocatorService = rvmRubyRuntimeLocatorService;
    }

    public List<RubyRuntimeLocatorService> getLocatorServices(){
        return ImmutableList.of(rvmRubyRuntimeLocatorService);
    }
    
    public RubyLocator acquireRubyLocator(String rubyRuntimeManager){

        if (rubyRuntimeManager.equals(RvmRubyRuntimeLocatorService.MANAGER_LABEL)){

            if (rvmRubyRuntimeLocatorService.isInstalled()){
                return rvmRubyRuntimeLocatorService.getRubyLocator();
            } else {
                throw new PathNotFoundException("Unable to locate RVM installation.");
            }

        } else {
            throw new IllegalArgumentException("Unable to locate runtime manager for - " + rubyRuntimeManager);
        }

    }
}
