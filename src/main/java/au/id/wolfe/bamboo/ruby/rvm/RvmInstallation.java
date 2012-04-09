package au.id.wolfe.bamboo.ruby.rvm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Ruby Version Manager (RVM) Installation details.
 */
public class RvmInstallation {

    private Type installType;
    private String installPath;
    private String rubiesPath;
    private String gemsPath;

    public RvmInstallation(String installPath, Type installType, String rubiesPath, String gemsPath) {
        this.installPath = installPath;
        this.installType = installType;
        this.rubiesPath = rubiesPath;
        this.gemsPath = gemsPath;
    }

    public boolean isReadOnly() {
        return installType.equals(Type.SYSTEM);
    }

    public String getInstallPath() {
        return installPath;
    }

    public boolean isSystemInstall() {
        return installType.equals(Type.SYSTEM);
    }

    public boolean isUserInstall() {
        return installType.equals(Type.USER);
    }

    public String getGemsPath() {
        return gemsPath;
    }

    public String getRubiesPath() {
        return rubiesPath;
    }

    public enum Type {
        USER, SYSTEM
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        int result = installType != null ? installType.hashCode() : 0;
        result = 31 * result + (installPath != null ? installPath.hashCode() : 0);
        result = 31 * result + (rubiesPath != null ? rubiesPath.hashCode() : 0);
        result = 31 * result + (gemsPath != null ? gemsPath.hashCode() : 0);
        return result;
    }
}
