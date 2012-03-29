package au.id.wolfe.bamboo.ruby.common;

import java.util.StringTokenizer;

/**
 * Used to represent a ruby instance managed by a ruby runtime manager.
 */
public class RubyLabel {

    private String rubyRuntimeManager;
    private String rubyRuntime;

    public RubyLabel(String rubyRuntimeManager, String rubyRuntime) {
        this.rubyRuntimeManager = rubyRuntimeManager;
        this.rubyRuntime = rubyRuntime;
    }

    public String getRubyRuntimeManager() {
        return rubyRuntimeManager;
    }

    public void setRubyRuntimeManager(String rubyRuntimeManager) {
        this.rubyRuntimeManager = rubyRuntimeManager;
    }

    public String getRubyRuntime() {
        return rubyRuntime;
    }

    public void setRubyRuntime(String rubyRuntime) {
        this.rubyRuntime = rubyRuntime;
    }

    @Override
    public String toString() {
        return String.format("%s %s", rubyRuntimeManager, rubyRuntime);
    }

    /**
     * Factory method used to build a ruby runtime name.
     *
     * @param rubyRuntimeLabel The label to be parsed.
     * @return RubyLabel object containing the attributes from the rubyRuntimeLabel.
     */
    public static RubyLabel getLabelFromString(String rubyRuntimeLabel) {
        final StringTokenizer stringTokenizer = new StringTokenizer(rubyRuntimeLabel, " ");

        if (stringTokenizer.countTokens() == 2) {
            return new RubyLabel(stringTokenizer.nextToken(), stringTokenizer.nextToken());
        } else {
            throw new IllegalArgumentException("Could not parse rubyRuntime string, expected something like 'RVM ruby-1.9.2@rails31', not " + rubyRuntimeLabel);
        }

    }
}
