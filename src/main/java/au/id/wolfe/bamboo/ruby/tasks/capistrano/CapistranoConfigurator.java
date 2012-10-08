package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

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


    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {

        final Map<String, String> map = super.generateTaskConfigMap(params, previousTaskDefinition);

        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(map, params, FIELDS_TO_COPY);

        return map;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        log.debug("populateContextForCreate");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3

    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForEdit");

        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3

    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForView");

        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);

    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        String ruby = params.getString("ruby");

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError("ruby", "You must specify a ruby runtime");
        }

        final String targets = params.getString("tasks");

        if (StringUtils.isEmpty(targets)) {
            errorCollection.addError("tasks", "You must specify at least one task");
        }

    }
}
