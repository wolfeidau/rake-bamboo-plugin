[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='ruby.runtime' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]

[@ww.textfield labelKey='ruby.workingSubDirectory' name='workingSubDirectory' required='false' cssClass="long-field" /]

[@ww.textfield labelKey='bundler.path' name='path' required='false' cssClass="long-field" /]

[@ww.textfield labelKey='bundler.environment' name='environmentVariables' required='false' cssClass="long-field" /]

[@ww.checkbox labelKey='bundler.binstubs'
                  name='binstubs'
                  toggle='true' /]