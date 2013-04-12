Simple java template.

Java web application template, for deploying on the Cedar Stack in Heroku.
Uses Jetty as the app container.

Uncomment in pom.xml and com.force.aus.Main 
1 - JNDI Support
- template contains dependencies and code that will configure application to use JNDI datasource

2 - Memcache HTTPSession manager
- Handle Heroku dyno scaling by using Memcache for session storage

3 - Heroku environment config
- Application properties file for loading local deployment properties and handling same properties on Heroku environment.

