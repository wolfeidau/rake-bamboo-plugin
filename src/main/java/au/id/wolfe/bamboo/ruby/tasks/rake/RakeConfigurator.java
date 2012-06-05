package au.id.wolfe.bamboo.ruby.tasks.rake;

import au.id.wolfe.bamboo.ruby.common.AbstractRubyTaskConfigurator;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Rake configurator which acts as a binding to the task UI in bamboo.
 */
public class RakeConfigurator extends AbstractRubyTaskConfigurator {

    protected static final String BIN_STUBS_KEY = "binstubs";

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {

        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put(RUBY_KEY, params.getString(RUBY_KEY));
        config.put("rakefile", params.getString("rakefile"));
        config.put("rakelibdir", params.getString("rakelibdir"));
        config.put("targets", params.getString("targets"));
        config.put("bundleexec", params.getString("bundleexec"));

        config.put("verbose", params.getString("verbose"));
        config.put("trace", params.getString("trace"));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        log.debug("populateContextForCreate");

        context.put(RUBY_KEY, "");
        context.put("rakefile", "");
        context.put("rakelibdir", "");
        context.put("targets", "");
        context.put("bundleexec", "");

        context.put("verbose", "");
        context.put("trace", "");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForEdit");

        context.put(RUBY_KEY, taskDefinition.getConfiguration().get(RUBY_KEY));
        context.put("rakefile", taskDefinition.getConfiguration().get("rakefile"));
        context.put("rakelibdir", taskDefinition.getConfiguration().get("rakelibdir"));
        context.put("bundleexec", taskDefinition.getConfiguration().get("bundleexec"));

        context.put("verbose", taskDefinition.getConfiguration().get("verbose"));
        context.put("trace", taskDefinition.getConfiguration().get("trace"));

        context.put("targets", taskDefinition.getConfiguration().get("targets"));

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForView");

        context.put(RUBY_KEY, taskDefinition.getConfiguration().get(RUBY_KEY));
        context.put("rakefile", taskDefinition.getConfiguration().get("rakefile"));
        context.put("rakelibdir", taskDefinition.getConfiguration().get("rakelibdir"));
        context.put("targets", taskDefinition.getConfiguration().get("targets"));
        context.put("bundleexec", taskDefinition.getConfiguration().get("bundleexec"));

        context.put("verbose", taskDefinition.getConfiguration().get("verbose"));
        context.put("trace", taskDefinition.getConfiguration().get("trace"));

    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        final String ruby = params.getString(RUBY_KEY);

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError(RUBY_KEY, "You must specify a ruby runtime");
        }

        final String targets = params.getString("targets");

        if (StringUtils.isEmpty(targets)) {
            errorCollection.addError("targets", "You must specify at least one target");
        }

    }
}