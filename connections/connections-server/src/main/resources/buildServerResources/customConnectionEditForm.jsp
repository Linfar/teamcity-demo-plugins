<%-- this JSP is inlined to a <table> containing Save and Cancel buttons below the form --%>

<%-- can be used for all JSPs - includes many useful taglibs --%>
<%@ include file="/include-internal.jsp" %>
<%-- taglibs needed for the form --%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="oauth" tagdir="/WEB-INF/tags/oauth" %>

<%-- renders a required field "Display name" --%>
<oauth:displayName note="Name this connection to distinguish it from others."/>
<%@ page import="teamcity.demo.plugins.connections.CustomConnection" %>

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
    <th><label for="secure:${CustomConnection.PASSWORD}">Password:<l:star/></label></th>
    <td>
        <%-- password properties are safely passed to backend, no additional efforts needed --%>
        <props:passwordProperty name="secure:${CustomConnection.PASSWORD}" className="longField"/>
        <%-- TeamCity will put a validation error from OAuthProvider.getPropertiesProcessor() here --%>
        <span class="error" id="error_secure:${CustomConnection.PASSWORD}"></span>
    </td>
</tr>