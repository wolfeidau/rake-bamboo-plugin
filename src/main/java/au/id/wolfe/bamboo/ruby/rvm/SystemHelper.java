package au.id.wolfe.bamboo.ruby.rvm;

/**
 * Delegate which performs all system related look ups and checks.
 */
public class SystemHelper {

    public String getUserHome(){
        return System.getProperty("user.home");
    }

}
