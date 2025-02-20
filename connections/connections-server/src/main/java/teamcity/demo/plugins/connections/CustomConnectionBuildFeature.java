package teamcity.demo.plugins.connections;

import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.connections.ProjectConnectionsManager;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * BuildFeature is the base class for buildConfiguration-level Build Features.
 * <p>
 * The BuildFeature extension should be added to the Spring context in order TeamCity Core
 * can find it.
 * <p>
 * It's recommended to use Build Features whenever a build-wide effect is needed - when all
 * steps in the build should be covered. Eg to authorize docker registries, watch files, add monitoring,
 * etc.
 * <p>
 * Important! This build feature doesn't have agent-side implementation, therefore, does nothing in the build
 */
public class CustomConnectionBuildFeature extends BuildFeature {
    private final PluginDescriptor pluginDescriptor;
    private final ProjectConnectionsManager projectConnectionsManager;

    public CustomConnectionBuildFeature(PluginDescriptor pluginDescriptor,
                                        ProjectConnectionsManager projectConnectionsManager) {
        this.projectConnectionsManager = projectConnectionsManager;
        this.pluginDescriptor = pluginDescriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return "CustomConnectionBuildFeature";
    }

    /**
     * The properties processor can check the form during save action and raise errors
     *
     * @return
     */
    @Nullable
    @Override
    public PropertiesProcessor getParametersProcessor(@NotNull BuildTypeIdentity buildTypeOrTemplate) {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> map) {
                ArrayList<InvalidProperty> invalidProperties = new ArrayList<>();
                if (!map.containsKey("connectionId")) {
                    invalidProperties.add(new InvalidProperty("connectionId", "Connection not selected"));
                }
                return invalidProperties;
            }
        };
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Use Custom Connection in build";
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> params) {
        return "Using connection with name " + params.get("repo");
    }

    @Nullable
    @Override
    public String getEditParametersUrl() {
        // PluginDescriptor.getPluginResourcesPath resolves the real location of the JSP after it's unpacked from the plugin
        return pluginDescriptor.getPluginResourcesPath("customConnectionBuildFeatureEditForm.jsp");
    }

    public List<SProjectFeatureDescriptor> getConnections(SProject project) {
        return new ArrayList<>(projectConnectionsManager.getAvailableConnectionsOfType(project, CustomConnection.CUSTOM_CONNECTION_TYPE));
    }
}
