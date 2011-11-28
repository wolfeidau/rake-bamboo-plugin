package au.id.wolfe.bamboo.ruby.rvm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a ruby runtime installation on a host.
 */
public class RubyRuntime {

    private String rubyName;
    private String gemSetName;

    public RubyRuntime(String rubyName, String gemSetName) {
        this.rubyName = rubyName;
        this.gemSetName = gemSetName;
    }

    public String getRubyName() {
        return rubyName;
    }

    public void setRubyName(String rubyName) {
        this.rubyName = rubyName;
    }

    public String getGemSetName() {
        return gemSetName;
    }

    public void setGemSetName(String gemSetName) {
        this.gemSetName = gemSetName;
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
