package au.id.wolfe.bamboo.ruby.rvm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a ruby runtime installation on a host.
 */
public class RubyRuntime {

    private final String rubyName;
    private final String gemSetName;

    private final String rubyExecutablePath;
    private final String gemPath;

    public RubyRuntime(String rubyName, String gemSetName, String rubyExecutablePath, String gemPath) {
        this.rubyName = rubyName;
        this.gemSetName = gemSetName;
        this.rubyExecutablePath = rubyExecutablePath;
        this.gemPath = gemPath;
    }

    public String getRubyName() {
        return rubyName;
    }

    public String getGemSetName() {
        return gemSetName;
    }

    public String getRubyExecutablePath() {
        return rubyExecutablePath;
    }

    public String getGemPath() {
        return gemPath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
