[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='ruby.runtime' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]

[@ww.textfield labelKey='bundle.path' name='path' required='false' cssClass="long-field" /]

[@ww.checkbox labelKey='bundle.binstubs'
                  name='binstubs'
                  toggle='true' /]