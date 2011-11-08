package au.id.wolfe.bamboo.ruby;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Ruby runtime installation.
 */
public class RubyRuntime {

    private String name;
    private String path;

    private String gemHome;
    private String gemPath;
    private String binPath;

    public RubyRuntime(String name, String path, String gemHome, String gemPath, String binPath) {
        this.name = name;
        this.path = path;
        this.gemHome = gemHome;
        this.gemPath = gemPath;
        this.binPath = binPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGemHome() {
        return gemHome;
    }

    public void setGemHome(String gemHome) {
        this.gemHome = gemHome;
    }

    public String getGemPath() {
        return gemPath;
    }

    public void setGemPath(String gemPath) {
        this.gemPath = gemPath;
    }

    public String getBinPath() {
        return binPath;
    }

    public void setBinPath(String binPath) {
        this.binPath = binPath;
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
