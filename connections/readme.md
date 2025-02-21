This is a demo plugin showing how to create and use a custom project-level connection in TeamCity.

The connection is defined in the CustomConnection class and rendered in customConnectionEditForm.jsp.

There are 2 usage examples of this connection - in a build feature (CustomConnectionBuildFeature) and in a runner (RunnerUsingCustomConnection).

There's no agent part in this plugin, so feature and runner don't actually do anything in the build.

### How to build and install locally

To build: 
1. Execute `mvn package`
2. The built zip archive is located in `target/connections.zip`

To run in a development TeamCity: 
1. Execute `mvn packge tc-sdk:start`
2. Navigate to http://localhost:8111
3. After finished, execute `mvn package tc-sdk:stop`
