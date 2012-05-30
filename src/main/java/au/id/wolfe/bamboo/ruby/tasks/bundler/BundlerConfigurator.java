package au.id.wolfe.bamboo.ruby.tasks.bundler;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;
import au.id.wolfe.bamboo.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Bundler configurator which acts as a binding to the task UI in bamboo.
 */
public class BundlerConfigurator extends AbstractRubyTaskConfigurator {

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put("ruby", params.getString("ruby"));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        log.debug("populateContextForCreate");

        context.put("ruby", "");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForEdit");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForView");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));

    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        String ruby = params.getString("ruby");

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError("ruby", "You must specify a ruby runtime");
        }

    }
}