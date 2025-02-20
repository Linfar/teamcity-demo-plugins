package teamcity.demo.plugins.connections;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.oauth.OAuthProvider;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * OAuthProvider is the base class for project-level Connections
 * <p>
 * The OAuthProvider extension should be added to the Spring context in order TeamCity Core
 * can find it.
 * <p>
 * Project-level Connections are recommended to use to provide end users ability to set
 * up connections to third-party resources in a way when it's possible to control the visibility
 * (only sub-projects have access to a connection configured in a project) and to reuse this connection
 * in many builds.
 */
public class CustomConnection extends OAuthProvider {

    public static final String CUSTOM_CONNECTION_TYPE = "CustomConnection";
    private final PluginDescriptor pluginDescriptor;

    public CustomConnection(PluginDescriptor pluginDescriptor) {
        this.pluginDescriptor = pluginDescriptor;
    }

    /**
     * The properties processor can check the form during save action and raise errors
     *
     * @return
     */
    @Nullable
    @Override
    public PropertiesProcessor getPropertiesProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> map) {
                return Collections.emptyList();
            }
        };
    }

    @NotNull
    @Override
    public String getType() {
        return CUSTOM_CONNECTION_TYPE;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultProperties() {
        return Collections.emptyMap();
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Custom Connection";
    }

    @Nullable
    @Override
    public String getEditParametersUrl() {
        // PluginDescriptor.getPluginResourcesPath resolves the real location of the JSP after it's unpacked from the plugin
        return this.pluginDescriptor.getPluginResourcesPath("customConnectionEditForm.jsp");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
