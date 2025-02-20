<%-- this JSP is inlined to a <table> containing Save and Cancel buttons below the form --%>

<%-- can be used for all JSPs - includes many useful taglibs --%>
<%@ include file="/include-internal.jsp" %>

<%-- taglibs needed for the form --%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="oauth" tagdir="/WEB-INF/tags/oauth" %>
<%-- beans provided by core --%>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="buildForm" scope="request"
             type="jetbrains.buildServer.controllers.admin.projects.EditableBuildTypeSettingsForm"/>
<%-- used to easily access available coonections --%>
<jsp:useBean id="buildFeature" scope="request" type="teamcity.demo.plugins.connections.CustomConnectionBuildFeature"/>
<%@ page import="teamcity.demo.plugins.connections.CustomConnectionBuildFeature" %>

<tr class="noBorder">
    <th>
        <label for="${CustomConnectionBuildFeature.CONNECTION_ID}">Select Custom Connection to use:</label>
    </th>
    <td>
        <%-- chooser to select one of the existing connections --%>
        <props:selectProperty id="${CustomConnectionBuildFeature.CONNECTION_ID}" name="${CustomConnectionBuildFeature.CONNECTION_ID}">
            <props:option value="">No custom connection selected</props:option>
            <%-- iterating through all available connections to show an option for each of tham --%>
            <c:forEach var="conn" items="${buildFeature.getConnections(buildForm.project)}">
                <!-- propertiesBean.properties['connectionId'] contains the currently selected connection for an existing build feature -->
                <forms:option selected="${propertiesBean.properties[CustomConnectionBuildFeature.CONNECTION_ID]==conn.id}"  value='${conn.id}'><c:out value="${conn.connectionDisplayName}"/></forms:option>
            </c:forEach>
        </props:selectProperty>
        <%-- TeamCity will put a validation error here --%>
        <span class="error" id="error_${CustomConnectionBuildFeature.CONNECTION_ID}"></span>
        <span class="smallNote">TeamCity will sign in to this Custom Connection before the build and sign out after the build</span>
    </td>
</tr>
