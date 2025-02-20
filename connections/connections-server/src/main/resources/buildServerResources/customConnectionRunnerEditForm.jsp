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
<%-- used to easily access available coonections --%>

<tr class="noBorder">
    <th>
        <label for="connectionId">Select Custom Connection to use:</label>
    </th>
    <td>
        <%-- chooser to select one of the existing connections --%>
        <props:selectProperty id="connectionId" name="connectionId">
            <props:option value="">No custom connection selected</props:option>
            <%-- iterating through all available connections to show an option for each of tham --%>
            <%--@elvariable id="availableConnections" type="java.util.List<teamcity.demo.plugins.connections.CustomConnection>"--%>
            <c:forEach var="conn" items="${availableConnections}">
                <!-- propertiesBean.properties['connectionId'] contains the currently selected connection for an existing build feature -->
                <forms:option selected="${propertiesBean.properties['connectionId']==conn.id}"  value='${conn.id}'><c:out value="${conn.connectionDisplayName}"/></forms:option>
            </c:forEach>
        </props:selectProperty>
        <%-- TeamCity will put a validation error here --%>
        <span class="error" id="error_connectionId"></span>
        <span class="smallNote">TeamCity will sign in to this Custom Connection before the build and sign out after the build</span>
    </td>
</tr>
