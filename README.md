# teamcity-demo-plugins
This repository contains some demo plugins for TeamCity

All the plugins are located in separate subdirectories and use TeamCity maven SDK

Whenever you want to create a new plugin, we recommend using either maven archetype if you prefer maven:
```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DarchetypeRepository=https://download.jetbrains.com/teamcity-repository -DarchetypeArtifactId=teamcity-server-plugin -DarchetypeGroupId=org.jetbrains.teamcity.archetypes -DarchetypeVersion=RELEASE -DteamcityVersion=2024.12
```
or gradle: https://github.com/rodm/gradle-teamcity-plugin
