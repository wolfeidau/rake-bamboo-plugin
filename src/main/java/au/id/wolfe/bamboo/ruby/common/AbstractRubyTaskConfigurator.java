package au.id.wolfe.bamboo.ruby.common;

import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds ruby specific helpers to the abstract task configurator to get rid
 * of duplication.
 */
public class AbstractRubyTaskConfigurator extends AbstractTaskConfigurator {

    protected static final Logger log = LoggerFactory.getLogger(AbstractRubyTaskConfigurator.class);

    protected static final String CREATE_MODE = "create";
    protected static final String EDIT_MODE = "edit";
    protected static final String MODE = "mode";

    protected static final String RUBY_KEY = "ruby";

    protected static final String CTX_UI_CONFIG_BEAN = "uiConfigBean";

    protected UIConfigSupport uiConfigBean;

    public void setUiConfigBean(final UIConfigSupport uiConfigBean) {
        this.uiConfigBean = uiConfigBean;
    }


}
