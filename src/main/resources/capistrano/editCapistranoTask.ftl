[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='ruby.runtime' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]

[@ww.textfield labelKey='ruby.workingSubDirectory' name='workingSubDirectory' required='false' cssClass="long-field" /]

[@ww.textfield labelKey='capistrano.tasks' name='tasks' required='true' cssClass="long-field" /]

[@ww.textfield labelKey='capistrano.environment' name='environmentVariables' required='false' cssClass="long-field" /]

[@ww.checkbox labelKey='ruby.bundleexec'
                  name='bundleexec'
                  toggle='true' /]

[@ww.checkbox labelKey='capistrano.debug'
                  name='debug'
                  toggle='true' /]

[@ww.checkbox labelKey='capistrano.verbose'
                  name='verbose'
                  toggle='true' /]

