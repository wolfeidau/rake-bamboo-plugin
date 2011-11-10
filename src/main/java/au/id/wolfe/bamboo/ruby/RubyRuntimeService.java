package au.id.wolfe.bamboo.ruby;

import java.util.List;

/**
 * This service locates the ruby runtimes.
 */
public interface RubyRuntimeService {

    List<RubyRuntime> getRubyRuntimes();

    RubyRuntime getRubyRuntime(String rvmRubyAndGemSetName);

    String getPathToScript(RubyRuntime rubyRuntime, String command);

}
