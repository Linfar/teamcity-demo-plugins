package teamcity.demo.plugins.connections.runner;

import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.StatefulObject;
import jetbrains.buildServer.controllers.admin.projects.BuildTypeForm;
import jetbrains.buildServer.controllers.admin.projects.EditRunTypeControllerExtension;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.connections.ConnectionDescriptor;
import jetbrains.buildServer.serverSide.connections.ProjectConnectionsManager;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamcity.demo.plugins.connections.CustomConnection;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An EditRunTypeControllerExtension extension can provide any additional data to the Runner
 * editing UI. In this example RunnerUsingCustomConnectionController provides the list of available
 * connections
 */
public class RunnerUsingCustomConnectionController implements EditRunTypeControllerExtension {
    private final ProjectConnectionsManager projectConnectionsManager;

    public RunnerUsingCustomConnectionController(ProjectConnectionsManager projectConnectionsManager,
                                                 ExtensionHolder extensionHolder) {
        this.projectConnectionsManager = projectConnectionsManager;
        extensionHolder.registerExtension(RunnerUsingCustomConnectionController.class, RunnerUsingCustomConnection.RUNNER_TYPE, this);
    }

    @Override
    public void fillModel(@NotNull HttpServletRequest httpServletRequest, @NotNull BuildTypeForm buildTypeForm, @NotNull Map<String, Object> map) {
        List<ConnectionDescriptor> availableConnectionsOfType = getAvailableConnections(buildTypeForm);
        map.put("availableConnections", availableConnectionsOfType);
    }

    @Override
    public void updateState(@NotNull HttpServletRequest httpServletRequest, @NotNull BuildTypeForm buildTypeForm) {
    }

    @Nullable
    @Override
    public StatefulObject getState(@NotNull HttpServletRequest httpServletRequest, @NotNull BuildTypeForm buildTypeForm) {
        return null;
    }

    @NotNull
    @Override
    public ActionErrors validate(@NotNull HttpServletRequest httpServletRequest, @NotNull BuildTypeForm buildTypeForm) {
        ActionErrors actionErrors = new ActionErrors();
        String connectionId = buildTypeForm.getBuildRunnerBean().getPropertiesBean().getProperties().get(RunnerUsingCustomConnection.CONNECTION_ID);
        if (StringUtil.isEmpty(connectionId)) {
            actionErrors.addError(new InvalidProperty(RunnerUsingCustomConnection.CONNECTION_ID, "Connection not selected"));
        }
        Optional<ConnectionDescriptor> any = getAvailableConnections(buildTypeForm)
                .stream()
                .filter(c -> c.getId().equals(connectionId))
                .findAny();
        if (!any.isPresent()) {
            actionErrors.addError(new InvalidProperty(RunnerUsingCustomConnection.CONNECTION_ID, "Connection not found"));
        }
        return new ActionErrors();
    }

    @Override
    public void updateBuildType(@NotNull HttpServletRequest httpServletRequest, @NotNull BuildTypeForm buildTypeForm, @NotNull BuildTypeSettings buildTypeSettings, @NotNull ActionErrors actionErrors) {
    }

    @NotNull
    private List<ConnectionDescriptor> getAvailableConnections(@NotNull BuildTypeForm buildTypeForm) {
        return projectConnectionsManager.getAvailableConnectionsOfType(buildTypeForm.getProject(), CustomConnection.CUSTOM_CONNECTION_TYPE);
    }
}
