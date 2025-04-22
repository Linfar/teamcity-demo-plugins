<%-- this JSP is inlined to a <table> containing Save and Cancel buttons below the form --%>

<%-- can be used for all JSPs - includes many useful taglibs --%>
<%@ include file="/include-internal.jsp" %>

<%-- taglibs needed for the form --%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="oauth" tagdir="/WEB-INF/tags/oauth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- beans provided by core --%>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="buildForm" scope="request"
             type="jetbrains.buildServer.controllers.admin.projects.EditableBuildTypeSettingsForm"/>
<%@ page import="teamcity.demo.plugins.connections.runner.RunnerUsingCustomConnection" %>

<tr class="noBorder">
    <th>
        <label for="${RunnerUsingCustomConnection.CONNECTION_ID}">Select Custom Connection to use:</label>
    </th>
    <td>
        <%-- chooser to select one of the existing connections --%>
        <props:selectProperty id="${RunnerUsingCustomConnection.CONNECTION_ID}" name="${RunnerUsingCustomConnection.CONNECTION_ID}">
            <props:option value="">No custom connection selected</props:option>
            <%-- iterating through all available connections to show an option for each of tham --%>
            <%--@elvariable id="availableConnections" type="java.util.List<teamcity.demo.plugins.connections.CustomConnection>"--%>
            <c:forEach var="conn" items="${availableConnections}">
                <!-- propertiesBean.properties['connectionId'] contains the currently selected connection for an existing build runner -->
                <forms:option selected="${propertiesBean.properties[RunnerUsingCustomConnection.CONNECTION_ID]==conn.id}"  value='${conn.id}'><c:out value="${conn.connectionDisplayName}"/></forms:option>
            </c:forEach>
        </props:selectProperty>
        <%-- TeamCity will put a validation error here --%>
        <span class="error" id="error_${RunnerUsingCustomConnection.CONNECTION_ID}"></span>
        <span class="smallNote">TeamCity will sign in to this Custom Connection before the build and sign out after the build</span>
    </td>
</tr>
