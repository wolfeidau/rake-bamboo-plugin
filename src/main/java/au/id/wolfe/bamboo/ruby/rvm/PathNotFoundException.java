package au.id.wolfe.bamboo.ruby.rvm;

/**
 * This exception is thrown if there is an issue locating a path.
 */
public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(String message) {
        super(message);
    }

}
