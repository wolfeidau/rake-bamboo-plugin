package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.google.common.collect.Sets;

/**
 * Capistrano configurator which acts as a binding to the task UI in bamboo.
 */
public class CapistranoConfigurator extends AbstractRubyTaskConfigurator {

    private static final Set<String> FIELDS_TO_COPY = Sets.newHashSet(
            RUBY_KEY,
            TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY,
            CapistranoTask.TASKS,
            CapistranoTask.ENVIRONMENT,
            CapistranoTask.BUNDLE_EXEC,
            CapistranoTask.VERBOSE,
            CapistranoTask.DEBUG);

    protected Set<String> getFieldsToCopy(){
        return FIELDS_TO_COPY;
    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        super.validate( params, errorCollection );
        
        final String targets = params.getString("tasks");

        if (StringUtils.isEmpty(targets)) {
            errorCollection.addError("tasks", "You must specify at least one task");
        }
    }
}
