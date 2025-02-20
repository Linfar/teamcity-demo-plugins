package teamcity.demo.plugins.connections.runner;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * RunType is the base class for custom Runners.
 * <p>
 * The RunType extension should be added to the RunTypeRegistry.
 * <p>
 * Important! This runner doesn't have agent-side implementation, therefore, does nothing in the build
 */
public class RunnerUsingCustomConnection extends RunType {
    public static final String RUNNER_TYPE = "CustomRunner";
    private final PluginDescriptor pluginDescriptor;

    public RunnerUsingCustomConnection(RunTypeRegistry runTypeRegistry,
                                       PluginDescriptor pluginDescriptor) {
        this.pluginDescriptor = pluginDescriptor;
        // The RunType extension should be added to the RunTypeRegistry.
        runTypeRegistry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "A sample runner using custom connection";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "A sample runner demonstrating how to use custom connection in a runner";
    }

    /**
     * The properties processor can check the form during save action and raise errors
     *
     * @return
     */
    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> map) {
                List<InvalidProperty> invalidProperties = new ArrayList<>();
                if (!map.containsKey("connectionId")) {
                    invalidProperties.add(new InvalidProperty("connectionId", "Connection not selected"));
                }
                return invalidProperties;
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath("customConnectionRunnerEditForm.jsp");
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return null;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return Collections.emptyMap();
    }
}
