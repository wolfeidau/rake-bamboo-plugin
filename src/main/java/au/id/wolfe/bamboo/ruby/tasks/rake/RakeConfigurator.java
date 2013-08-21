package au.id.wolfe.bamboo.ruby.tasks.rake;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.google.common.collect.Sets;

/**
 * Rake configurator which acts as a binding to the task UI in bamboo.
 */
public class RakeConfigurator extends AbstractRubyTaskConfigurator {

    private static final Set<String> FIELDS_TO_COPY = Sets.newHashSet(
            RUBY_KEY,
            TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY,
            RakeTask.RAKE_FILE,
            RakeTask.RAKE_LIB_DIR,
            RakeTask.TARGETS,
            RakeTask.BUNDLE_EXEC,
            RakeTask.ENVIRONMENT,
            RakeTask.VERBOSE,
            RakeTask.TRACE);

    protected Set<String> getFieldsToCopy(){
        return FIELDS_TO_COPY;
    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        super.validate( params, errorCollection );

        final String targets = params.getString(RakeTask.TARGETS);

        if (StringUtils.isEmpty(targets)) {
            errorCollection.addError("targets", "You must specify at least one target");
        }
    }
}