# ASPServerExecutor

## Deploying ASPServerExecutor.war in Tomcat
1. Go to Tomcat webapps folder and paste it.
2. Go to Tomcat/bin folder and start Tomcat by clicking *startup.bat* for Windows, for Linux run *startup.sh*
3. Go to webapps/ASPServerExecutor/WEB-INF/classes/resources folder and change the value *absolute_path* in _config.properties_ file with your absolute path of the solver ASP
4. Restart Tomcat