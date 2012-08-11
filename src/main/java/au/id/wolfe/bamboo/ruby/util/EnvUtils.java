package au.id.wolfe.bamboo.ruby.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Constants and util functions which manipulate the environment.
 */
public final class EnvUtils {

    public static final String MY_RUBY_HOME = "MY_RUBY_HOME";
    public static final String GEM_HOME = "GEM_HOME";
    public static final String GEM_PATH = "GEM_PATH";
    public static final String PATH = "PATH";
    public static final String BUNDLE_HOME = "BUNDLE_PATH";

    public static final List<String> filterList =
            ImmutableList.of(EnvUtils.MY_RUBY_HOME, EnvUtils.GEM_HOME, EnvUtils.GEM_PATH, EnvUtils.BUNDLE_HOME);

}
