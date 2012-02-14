Introduction
------------
This is a Heroku-ready Stock Exchange symbol lookup demo application using:
 * Neo4j
 * VoiceXML
 * JRuby
 * Sinatra
 * Spring Data
 * Spring Framework
 * Maven
 * Java
 * Jetty
 * Twitter Bootstrap
 * jQuery
 * Arbor.js
 * and data provided by [Yahoo Finance](http://finance.yahoo.com/).

If you want more information about this application, [click here](http://tomasmuller.com.br/2012/02/12/talking-with-neo4j-graphs/).


Getting Started Locally
-----------------------
 * Download an install Java (JDK 1.7.0_02-b13 is fine)
 * rvm use jruby-1.6.5
 * Download and install Maven 3.0.3
 * Download and install Neo4J 1.6
   * export NEO4J_REST_URL=http://localhost:7474/db/data
   * export NEO4J_LOGIN=""
   * export NEO4J_PASSWORD=""
   * Start Neo4j local server
 * git clone https://github.com/tomasmuller/neoquotes
 * cd neoquotes
 * mvn install (or mvn package)
 * mvn jetty:run
 * open http://localhost:8080

Obs: if you experience OutOfMemory errors while developing using `mvn jetty:run`, try increasing Maven PermSize and MaxPermSize:
    
    export MAVEN_OPTS="-XX:PermSize=256M -XX:MaxPermSize=512M"

Pushing to Heroku
------------------
 * heroku create --stack cedar
 * heroku config:add RACK_ENV=production --app `<app name>`
 * heroku addons:add neo4j --app `<app name>`
 * git push heroku master
 * heroku open --app `<app name>`


How does it work?
-----------------

Heroku detects the pom.xml file and selects Java as the application's
langauge. A `mvn install` is run as part of the Heroku slug
compliation. Two tasks in `pom.xml` (install-bundler and bundle-install)
handle setting up the Ruby side of the application.

The `Procfile` uses script/jruby to setup the jruby environment and start
Jetty up.

Local development
-----------------

Basically use `script/bundle` in place of the normal `bundle` command.

So to add Ruby dependencies, edit your `Jemfile`, then run
`jruby -S script/bundle install`. Or any of your other "favorite"
bundler commands.

To run your rake tasks, unit tests for example, simply run:
    
    jruby -S script/bundle exec rake test
    
To add Java dependencies, modify the `pom.xml` file and re-run `mvn install`.

Run junit tests is a matter of running `mvn test`.

**Don't forget to start neo4j local server, and export required environment variables before running any mvn command.**
Otherwise you'll get connection refused errors.

Thanks
-------

Neo4j for the amazing piece of software and for sponsoring the [challenge](http://neo4j-challenge.herokuapp.com/).
Of course, to all others great open-source technologies used in this application.
