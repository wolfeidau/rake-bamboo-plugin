[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='rake.ruby' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]

[@ww.textfield labelKey='rake.targets' name='targets' required='true' cssClass="long-field" /]

[@ww.checkbox labelKey='rake.bundleexec'
                  name='bundleexec'
                  toggle='true' /]