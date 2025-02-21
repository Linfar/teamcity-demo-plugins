This is a demo plugin showing how to create and use a custom project-level connection in TeamCity.

The connection is defined in the CustomConnection class and rendered in customConnectionEditForm.jsp.

There are 2 usage examples of this connection - in a build feature (CustomConnectionBuildFeature) and in a runner (RunnerUsingCustomConnection).

There's no agent part in this plugin, so feature and runner don't actually do anything in the build.
