<%-- this JSP is inlined to a <table> containing Save and Cancel buttons below the form --%>

<%-- can be used for all JSPs - includes many useful taglibs --%>
<%@ include file="/include-internal.jsp" %>
<%-- taglibs needed for the form --%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="oauth" tagdir="/WEB-INF/tags/oauth" %>

<%-- renders a required field "Display name" --%>
<oauth:displayName note="Name this connection to distinguish it from others."/>
<%@ page import="teamcity.demo.plugins.connections.CustomConnection" %>

<bs:linkScript>
    /js/bs/testConnection.js
</bs:linkScript>
<script>
    BS.OAuthConnectionDialog.submitTestConnection = function () {
        var that = this;
        BS.PasswordFormSaver.save(this, '<c:url value="/repo/registry-test-connection.html"/>', OO.extend(BS.ErrorsAwareListener, {
            onFailedTestConnectionError: function (elem) {
                var text = "";
                if (elem.firstChild) {
                    text = elem.firstChild.nodeValue;
                }
                BS.TestConnectionDialog.show(false, text, $('testConnectionButton'));
            },
            onCompleteSave: function (form, responseXML) {
                var err = BS.XMLResponse.processErrors(responseXML, this, form.propertiesErrorsHandler);
                BS.ErrorsAwareListener.onCompleteSave(form, responseXML, err);
                if (!err) {
                    this.onSuccessfulSave(responseXML);
                }
            },
            onSuccessfulSave: function (responseXML) {
                that.enable();
                var additionalInfo = "";
                var testConnectionResultNodes = responseXML.documentElement.getElementsByTagName("testConnectionResult");
                if (testConnectionResultNodes && testConnectionResultNodes.length > 0) {
                    var testConnectionResult = testConnectionResultNodes.item(0);
                    if (testConnectionResult.firstChild) {
                        additionalInfo = testConnectionResult.firstChild.nodeValue;
                    }
                }
                BS.TestConnectionDialog.show(true, additionalInfo, $('testConnectionButton'));
            }
        }));
        return false;
    };

    var afterClose = BS.OAuthConnectionDialog.afterClose;
    BS.OAuthConnectionDialog.afterClose = function () {
        $j('.testConnectionButton').remove();
        afterClose()
    };

    var connectionTypeChanged = function(){
        $j('#OAuthConnectionDialog .testConnectionButton').detach();
        $j('#typeSelector').unbind('change', connectionTypeChanged);
    };

    $j('#typeSelector').on('change', connectionTypeChanged);
</script>

<%-- renders username --%>
<tr>
    <th><label for="${CustomConnection.USER}">Username:<l:star/></label></th>
    <td>
        <props:textProperty name="${CustomConnection.USER}" className="longField"/>
        <%-- TeamCity will put a validation error from OAuthProvider.getPropertiesProcessor() here --%>
        <span class="error" id="error_${CustomConnection.USER}"></span>
        <span class="smallNote">Username</span>
    </td>
</tr>

<tr>
    <th><label for="${CustomConnection.PASSWORD}">Password:<l:star/></label></th>
    <td>
        <%-- password properties are safely passed to backend, no additional efforts needed --%>
        <props:passwordProperty name="${CustomConnection.PASSWORD}" className="longField"/>
        <%-- TeamCity will put a validation error from OAuthProvider.getPropertiesProcessor() here --%>
        <span class="error" id="error_${CustomConnection.PASSWORD}"></span>
    </td>
</tr>
<forms:submit id="testConnectionButton" type="button" label="Test Connection" className="testConnectionButton"
              onclick="return BS.OAuthConnectionDialog.submitTestConnection();"/>
<bs:dialog dialogId="testConnectionDialog" title="Test Connection" closeCommand="BS.TestConnectionDialog.close();"
           closeAttrs="showdiscardchangesmessage='false'">
    <div id="testConnectionStatus"></div>
    <div id="testConnectionDetails" class="mono"></div>
</bs:dialog>
<script>
    $j('#OAuthConnectionDialog .popupSaveButtonsBlock .testConnectionButton').remove();
    $j("#testConnectionButton").appendTo($j('#OAuthConnectionDialog .popupSaveButtonsBlock')[0])
</script>