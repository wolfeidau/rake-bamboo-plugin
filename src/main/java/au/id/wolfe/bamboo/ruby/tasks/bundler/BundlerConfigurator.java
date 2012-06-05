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

    private static final String PATH_KEY = "path";
    private static final String BIN_STUBS_KEY = "binstubs";

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put(RUBY_KEY, params.getString(RUBY_KEY));
        config.put(PATH_KEY, params.getString(PATH_KEY));
        config.put(BIN_STUBS_KEY, params.getString(BIN_STUBS_KEY));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {

        log.debug("populateContextForCreate");

        context.put(RUBY_KEY, "");
        context.put(PATH_KEY, "");
        context.put(BIN_STUBS_KEY, "");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForEdit");

        context.put(RUBY_KEY, taskDefinition.getConfiguration().get(RUBY_KEY));
        context.put(PATH_KEY, taskDefinition.getConfiguration().get(PATH_KEY));
        context.put(BIN_STUBS_KEY, taskDefinition.getConfiguration().get(BIN_STUBS_KEY));

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        log.debug("populateContextForView");

        context.put(RUBY_KEY, taskDefinition.getConfiguration().get(RUBY_KEY));
        context.put(PATH_KEY, taskDefinition.getConfiguration().get(PATH_KEY));
        context.put(BIN_STUBS_KEY, taskDefinition.getConfiguration().get(BIN_STUBS_KEY));

    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {

        String ruby = params.getString(RUBY_KEY);

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError(RUBY_KEY, "You must specify a ruby runtime");
        }

    }
}