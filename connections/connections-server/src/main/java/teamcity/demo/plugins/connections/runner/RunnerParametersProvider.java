package teamcity.demo.plugins.connections.runner;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.connections.ConnectionDescriptor;
import jetbrains.buildServer.serverSide.connections.ProjectConnectionsManager;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import teamcity.demo.plugins.connections.CustomConnection;

import java.util.List;
import java.util.Optional;

/**
 * A BuildStartContextProcessor implementation can provide additional parameters for a starting
 * build. In this example RunnerParametersProvider uses the connectionId defined in build steps to
 * add username and password to the runner.
 */
public class RunnerParametersProvider implements BuildStartContextProcessor {
    private final ProjectConnectionsManager projectConnectionsManager;
    private final ProjectManager projectManager;

    public RunnerParametersProvider(ProjectConnectionsManager projectConnectionsManager,
                                    ProjectManager projectManager) {
        this.projectConnectionsManager = projectConnectionsManager;
        this.projectManager = projectManager;
    }

    @Override
    public void updateParameters(@NotNull BuildStartContext buildStartContext) {
        // iterating through all runners defined in the build
        for (SRunnerContext runnerContext : buildStartContext.getRunnerContexts()) {
            // only for runners of the needed type
            if (runnerContext.getRunType().getType().equals(RunnerUsingCustomConnection.RUNNER_TYPE)) {
                SProject project = projectManager.findProjectById(buildStartContext.getBuild().getProjectId());
                if (project == null) {
                    continue;
                }

                String connectionId = runnerContext.getBuildParameters().get(RunnerUsingCustomConnection.CONNECTION_ID);

                if (StringUtil.isEmpty(connectionId)) {
                    Loggers.SERVER.warn("Connection ID not set for the runner " + RunnerUsingCustomConnection.RUNNER_TYPE + " in build " + buildStartContext.getBuild().getBuildId());
                    continue;
                }
                List<ConnectionDescriptor> availableConnections = projectConnectionsManager.getAvailableConnectionsOfType(project, CustomConnection.CUSTOM_CONNECTION_TYPE);
                Optional<ConnectionDescriptor> any = availableConnections.stream().filter(c -> c.getId().equals(connectionId)).findAny();
                if (!any.isPresent()) {
                    Loggers.SERVER.warn("Unknown connection ID set for the runner " + RunnerUsingCustomConnection.RUNNER_TYPE + " in build " + buildStartContext.getBuild().getBuildId());
                    continue;
                }

                ConnectionDescriptor connectionDescriptor = any.get();

                // customConnectionUserName and customConnectionPassword will be available in the runner on the agent side
                runnerContext.addBuildParameter(RunnerUsingCustomConnection.CONNECTION_USER_NAME, connectionDescriptor.getParameters().get(CustomConnection.USER));
                String password = connectionDescriptor.getParameters().get(CustomConnection.PASSWORD);
                if (StringUtil.isNotEmpty(password)) {
                    runnerContext.addBuildParameter(RunnerUsingCustomConnection.CONNECTION_PASSWORD, password);
                }
            }
        }
    }
}
