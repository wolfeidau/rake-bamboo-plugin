package au.id.wolfe.bamboo.ruby.tasks.bundler.install;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;

import com.atlassian.bamboo.task.TaskConfigConstants;
import com.google.common.collect.Sets;

/**
 * Bundler configurator which acts as a binding to the task UI in bamboo.
 */
public class BundlerInstallConfigurator extends AbstractRubyTaskConfigurator {

    private static final Set<String> FIELDS_TO_COPY = Sets.newHashSet(
            RUBY_KEY,
            TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY,
            BundlerInstallTask.PATH,
            BundlerInstallTask.ENVIRONMENT,
            BundlerInstallTask.BIN_STUBS);

    protected Set<String> getFieldsToCopy(){
        return FIELDS_TO_COPY;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        super.populateContextForCreate( context );
        context.put(BundlerInstallTask.PATH, "vendor/bundle");
    }
}