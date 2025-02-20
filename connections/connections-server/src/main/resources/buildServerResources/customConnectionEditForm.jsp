<%-- this JSP is inlined to a <table> containing Save and Cancel buttons below the form --%>

<%-- can be used for all JSPs - includes many useful taglibs --%>
<%@ include file="/include-internal.jsp" %>
<%-- taglibs needed for the form --%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="oauth" tagdir="/WEB-INF/tags/oauth" %>

<%-- renders a required field "Display name" --%>
<oauth:displayName note="Name this connection to distinguish it from others."/>

<%-- renders username --%>
<tr>
    <th><label for="user">Username:<l:star/></label></th>
    <td>
        <props:textProperty name="user" className="longField"/>
        <%-- TeamCity will put a validation error from OAuthProvider.getPropertiesProcessor() here --%>
        <span class="error" id="error_user"></span>
        <span class="smallNote">Username</span>
    </td>
</tr>

<tr>
    <th><label for="secure:passwd">Password:<l:star/></label></th>
    <td>
        <%-- password properties are safely passed to backend, no additional efforts needed --%>
        <props:passwordProperty name="secure:passwd" className="longField"/>
        <%-- TeamCity will put a validation error from OAuthProvider.getPropertiesProcessor() here --%>
        <span class="error" id="error_secure:passwd"></span>
    </td>
</tr>