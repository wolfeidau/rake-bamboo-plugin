[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='ruby.runtime' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]

[@ww.textfield labelKey='ruby.workingSubDirectory' name='workingSubDirectory' required='false' cssClass="long-field" /]

[@ww.textfield labelKey='bundler.cli.arguments' name='arguments' required='true' cssClass="long-field" /]

[@ww.textfield labelKey='rake.environment' name='environmentVariables' required='false' cssClass="long-field" /]

[@ww.checkbox labelKey='ruby.bundleexec'
                  name='bundleexec'
                  toggle='true' /]

[@ww.checkbox labelKey='bundler.cli.trace'
                  name='trace'
                  toggle='true' /]

[@ww.checkbox labelKey='rake.verbose'
                  name='verbose'
                  toggle='true' /]

