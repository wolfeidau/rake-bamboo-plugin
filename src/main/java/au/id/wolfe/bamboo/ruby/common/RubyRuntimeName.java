package au.id.wolfe.bamboo.ruby.common;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.StringTokenizer;

/**
 * Class which holds ruby runtime names
 */
public class RubyRuntimeName {

    String version;
    String gemSet;

    public static final String DEFAULT_GEMSET_SEPARATOR = "@";
    public static final String DEFAULT_GEMSET_NAME = "default";

    public RubyRuntimeName(String version, String gemSet) {
        this.version = version;
        this.gemSet = gemSet;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGemSet() {
        return gemSet;
    }

    public void setGemSet(String gemSet) {
        this.gemSet = gemSet;
    }

    public static RubyRuntimeName parseString(String rubyRuntimeName) {

        final StringTokenizer stringTokenizer = new StringTokenizer(rubyRuntimeName, DEFAULT_GEMSET_SEPARATOR);

        if (stringTokenizer.countTokens() == 2) {

            return new RubyRuntimeName(stringTokenizer.nextToken(), stringTokenizer.nextToken());

        } else {
            throw new IllegalArgumentException("Could not parse rubyRuntime string, expected something like ruby-1.9.2-p123@default, not " + rubyRuntimeName);
        }

    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("version", version).
                append("gemSet", gemSet).
                toString();
    }
}
