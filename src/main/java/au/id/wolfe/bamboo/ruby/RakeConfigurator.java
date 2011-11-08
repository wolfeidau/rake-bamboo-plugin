package au.id.wolfe.bamboo.ruby;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Rake task which uses the RVM ruby selected to run rake and build the ruby project. Given a value
 * like 1.9.3-p0@rails31 this task will run rake using that particular ruby/gems combination in RVM.
 */
public class RakeConfigurator extends AbstractTaskConfigurator {

    public static final String CREATE_MODE = "create";
    public static final String EDIT_MODE = "edit";
    public static final String MODE = "mode";

    private final RubyRuntimeService rubyRuntimeService;

    public RakeConfigurator(RubyRuntimeService rubyRuntimeService) {
        this.rubyRuntimeService = rubyRuntimeService;
    }

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

        context.put("ruby", "");

        context.put(MODE, CREATE_MODE);
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        super.populateContextForEdit(context, taskDefinition);

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));
        context.put(MODE, EDIT_MODE);
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {

        super.populateContextForView(context, taskDefinition);

        context.put("ruby", taskDefinition.getConfiguration().get("ruby"));
    }

    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection) {
        super.validate(params, errorCollection);

        String host = params.getString("ruby");

        if (StringUtils.isEmpty(host)) {
            errorCollection.addError("ruby", "You must specify a ruby runtime");
        }
    }

}
