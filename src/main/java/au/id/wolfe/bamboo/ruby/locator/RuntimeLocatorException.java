package au.id.wolfe.bamboo.ruby.locator;

/**
 * Thrown when there is an error loading or building a ruby runtime locator.
 */
public class RuntimeLocatorException extends Exception{

    public RuntimeLocatorException(String s) {
        super(s);
    }

    public RuntimeLocatorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RuntimeLocatorException(Throwable throwable) {
        super(throwable);
    }
}
