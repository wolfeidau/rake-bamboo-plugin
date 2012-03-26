package au.id.wolfe.bamboo.ruby.util;

/**
 * Delegate which performs all system related look ups and checks.
 */
public class SystemHelper {

    /**
     * Adapter for the system properties primarily to enable testing of all cases.
     * @return The path to the current users home directory.
     */
    public String getUserHome(){
        return System.getProperty("user.home");
    }

}
