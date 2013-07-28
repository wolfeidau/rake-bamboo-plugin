package au.id.wolfe.bamboo.ruby.util;

import au.id.wolfe.bamboo.ruby.common.RubyLabel;
import au.id.wolfe.bamboo.ruby.tasks.rake.RakeTask;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.google.common.base.Preconditions;
import org.apache.maven.wagon.PathUtils;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;

/**
 * Contains utility methods used in tasks and capability helper.
 */
public final class TaskUtils {

    public static final String RUBY_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".ruby";

    public static String buildCapabilityLabel(RubyLabel rubyLabel) {
        return String.format("%s.%s", RUBY_CAPABILITY_PREFIX, rubyLabel);
    }

}
