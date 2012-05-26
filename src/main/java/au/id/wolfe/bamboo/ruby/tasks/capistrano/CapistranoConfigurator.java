package au.id.wolfe.bamboo.ruby.tasks.capistrano;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Capistrano configurator which acts as a binding to the task UI in bamboo.
 */
public class CapistranoConfigurator extends AbstractRubyTaskConfigurator {

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put("ruby", params.getString("ruby"));
        config.put("bundleexec", params.getString("bundleexec"));

        config.put("verbose", params.getString("verbose"));
        config.put("debug", params.getString("debug"));

        config.put("tasks", params.getString("tasks"));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        log.debug("populateContextForCreate");

        context.put("ruby", "");
        context.put("bundleexec", "");

        context.put("verbose", "");
        context.put("debug", "");

        context.put("tasks", "");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForEdit");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));
        context.put("bundleexec", taskDefinition.getConfiguration().get("bundleexec"));

        context.put("verbose", taskDefinition.getConfiguration().get("verbose"));
        context.put("debug", taskDefinition.getConfiguration().get("debug"));

        context.put("tasks", taskDefinition.getConfiguration().get("tasks"));

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForView");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));
        context.put("bundleexec", taskDefinition.getConfiguration().get("bundleexec"));

        context.put("verbose", taskDefinition.getConfiguration().get("verbose"));
        context.put("debug", taskDefinition.getConfiguration().get("debug"));

        context.put("tasks", taskDefinition.getConfiguration().get("tasks"));
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
