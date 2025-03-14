package teamcity.demo.plugins.connections;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jetbrains.buildServer.controllers.*;
import jetbrains.buildServer.controllers.admin.projects.PluginPropertiesUtil;
import jetbrains.buildServer.http.SimpleCredentials;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.HTTPRequestBuilder;
import jetbrains.buildServer.util.ssl.SSLTrustStoreProvider;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomConnectionCheckConnectionController extends BaseFormXmlController {
    private final SSLTrustStoreProvider sslTrustStoreProvider;
    private final HTTPRequestBuilder.RequestHandler requestHandler;
    private Gson gson = new Gson();

    public CustomConnectionCheckConnectionController(WebControllerManager webControllerManager,
                                                     SSLTrustStoreProvider sslTrustStoreProvider,
                                                     SBuildServer buildServer,
                                                     HTTPRequestBuilder.RequestHandler requestHandler) {
        super(buildServer);
        this.sslTrustStoreProvider = sslTrustStoreProvider;
        this.requestHandler = requestHandler;

        webControllerManager.registerController("/customConnection/registry-test-connection.html", this);
    }

    @Override
    protected ModelAndView doGet(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse) {
        return null;
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull Element xmlResponse) {
        BasePropertiesBean propertiesBean = new BasePropertiesBean(Collections.emptyMap());
        Map<String, String> properties = new HashMap<>();
        if (!httpServletRequest.getParameterMap().isEmpty()) {
            if (PublicKeyUtil.isPublicKeyExpired(httpServletRequest)) {
                PublicKeyUtil.writePublicKeyExpiredError(xmlResponse);
                return;
            }
            PluginPropertiesUtil.bindPropertiesFromRequest(httpServletRequest, propertiesBean);
            properties.putAll(propertiesBean.getProperties());
        }

        bindPropertiesFromRequestBody(httpServletRequest, properties);

        doTestConnection(properties, xmlResponse);
    }

    private void doTestConnection(Map<String, String> properties, Element xmlResponse) {
        ActionErrors errors = new ActionErrors();

        try {
            HTTPRequestBuilder.Request request = HTTPRequestBuilder
                    .request("https://theservice.com")
                    .withAuthenticateHeader(new SimpleCredentials(properties.get(CustomConnection.USER), properties.get(CustomConnection.PASSWORD)))
                    .withTrustStore(sslTrustStoreProvider.getTrustStore())
                    .build();

//            HTTPRequestBuilder.Response response = IOGuard.allowNetworkCall(() -> requestHandler.doSyncRequest(request));
//            int statusCode = response.getStatusCode();

//            if (statusCode == 200) {
//                XmlResponseUtil.writeTestResult(xmlResponse, "Connection to theservice.com was successful!");
//            } else {
//                errors.addError("Error code", statusCode + " " + "response.getStatusText()");
//            }

            errors.addError("Error code", "The real test connection code is commented out");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void bindPropertiesFromRequestBody(@NotNull HttpServletRequest httpServletRequest, Map<String, String> properties) {
        TypeToken<Map<String, String>> mapType = new TypeToken<Map<String, String>>() {
        };
        try {
            Map<String, String> jsonBody = gson.fromJson(httpServletRequest.getReader(), mapType.getType());
            properties.putAll(jsonBody);
        } catch (Exception ignored) {
        }
    }
}
