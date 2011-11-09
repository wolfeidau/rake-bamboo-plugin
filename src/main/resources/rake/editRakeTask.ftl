[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.select cssClass="builderSelectWidget" labelKey='Ruby' name='ruby'
            list=uiConfigBean.getExecutableLabels('ruby')
            extraUtility=addExecutableLink required='true' /]