package au.id.wolfe.bamboo.ruby.tasks.bundler;

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
 * Bundler configurator which acts as a binding to the task UI in bamboo.
 */
public class BundlerConfigurator extends AbstractRubyTaskConfigurator {

    private static final Set<String> FIELDS_TO_COPY = Sets.newHashSet(
            RUBY_KEY,
            TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY,
            BundlerTask.PATH,
            BundlerTask.ENVIRONMENT,
            BundlerTask.BIN_STUBS);


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

        context.put(BundlerTask.PATH, "vendor/bundle");

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

        String ruby = params.getString(RUBY_KEY);

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError(RUBY_KEY, "You must specify a ruby runtime");
        }

    }
}