package au.id.wolfe.bamboo.ruby.tasks.bundler;

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
public class BundlerConfigurator extends AbstractTaskConfigurator {

    private static final Logger log = LoggerFactory.getLogger(BundlerConfigurator.class);

    public static final String CREATE_MODE = "create";
    public static final String EDIT_MODE = "edit";
    public static final String MODE = "mode";

    private static final String CTX_UI_CONFIG_BEAN = "uiConfigBean";

    private UIConfigSupport uiConfigBean;

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put("ruby", params.getString("ruby"));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {
        super.populateContextForCreate(context);
        log.info("populateContextForCreate");

        context.put("ruby", "");

        context.put(MODE, CREATE_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        super.populateContextForEdit(context, taskDefinition);
        log.info("populateContextForEdit");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));

        context.put(MODE, EDIT_MODE);
        context.put(CTX_UI_CONFIG_BEAN, uiConfigBean);  // NOTE: This is not normally necessary and will be fixed in 3.3.3
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        super.populateContextForView(context, taskDefinition);
        log.info("populateContextForView");

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));

    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {
        super.validate(params, errorCollection);

        String ruby = params.getString("ruby");

        if (StringUtils.isEmpty(ruby)) {
            errorCollection.addError("ruby", "You must specify a ruby runtime");
        }

    }

    public void setUiConfigBean(final UIConfigSupport uiConfigBean) {
        this.uiConfigBean = uiConfigBean;
    }
}