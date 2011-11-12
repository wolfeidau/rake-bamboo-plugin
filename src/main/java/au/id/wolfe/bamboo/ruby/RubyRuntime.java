package au.id.wolfe.bamboo.ruby;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Ruby runtime installation.
 */
public class RubyRuntime {

    private final String name;
    private final String path;
    private final String rubyHome;

    private final String gemHome;
    private final String gemPath;
    private final String binPath;

    public RubyRuntime(String name, String path, String rubyHome, String gemHome, String gemPath, String binPath) {
        this.name = name;
        this.path = path;
        this.rubyHome = rubyHome;
        this.gemHome = gemHome;
        this.gemPath = gemPath;
        this.binPath = binPath;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getRubyHome() {
        return rubyHome;
    }

    public String getGemHome() {
        return gemHome;
    }

    public String getGemPath() {
        return gemPath;
    }

    public String getBinPath() {
        return binPath;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("name", name).
                append("path", path).
                append("gemHome", gemHome).
                append("gemPath", gemPath).
                append("binPath", binPath).
                toString();
    }
}
