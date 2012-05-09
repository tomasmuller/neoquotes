Introduction
------------
This is a Heroku-ready Stock Exchange symbol lookup demo application using:
Neo4j, VoiceXML, JRuby, Sinatra, Spring Data, Spring Framework, Maven, Java,
Jetty, Twitter Bootstrap, jQuery, Arbor.js, and data provided by [Yahoo Finance](http://finance.yahoo.com/).

If you want more details about the development of this application, [click here](http://tomasmuller.com.br/2012/02/12/talking-with-neo4j-graphs/).


Getting Started Locally
-----------------------
 * Download an install Java (JDK 1.7.0_02-b13 is fine)
 * Install JRuby 1.6.7 (use [RVM](https://rvm.beginrescueend.com/) or [rbenv](https://github.com/sstephenson/rbenv))
 * Download and install Maven 3.0.3
 * Download and install Neo4J 1.6
   * `export NEO4J_REST_URL=http://localhost:7474/db/data`
   * `export NEO4J_LOGIN=""`
   * `export NEO4J_PASSWORD=""`
   * Start Neo4j local server
 * `git clone https://github.com/tomasmuller/neoquotes.git`
 * `cd neoquotes/app/java`
 * `mvn package`
 * `cd ../../`
 * `bundle exec trinidad -t -r -e development`
 * open http://localhost:3000


Pushing to Heroku
------------------
 * heroku create --stack cedar --buildpack https://github.com/tomasmuller/neoquotes-heroku-buildpack.git
 * heroku config:add RACK_ENV=production --app `<app name>`
 * heroku addons:add neo4j --app `<app name>`
 * git push heroku master
 * heroku open --app `<app name>`


How does it work?
-----------------
Check the [NeoQuotes Heroku Buildpack](https://github.com/tomasmuller/neoquotes-heroku-buildpack) readme file
for more information.


Local development
-----------------
To run your rake tasks, unit tests for example, simply run:

    jruby -S bundle exec rake test

To add Java dependencies, modify the `app/java/pom.xml` file and re-run `mvn install` or `mvn package`.

To run Junit tests is a matter of running `mvn test`.

**Don't forget to start neo4j local server, and export required environment variables before running any mvn command.**
Otherwise you'll get connection refused errors.


Thanks
-------
Neo4j for the amazing piece of software and for sponsoring the [challenge](http://neo4j-challenge.herokuapp.com/).
Of course, to all others great open-source technologies used in this application.

