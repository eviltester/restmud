rem make sure we generate the in built game
call mvn clean -Dtest=CreateJsonFilesForDefaultGamesTest test
rem package it
call mvn compile test package
rem now deploy executable locally with sources
call mvn install:install-file -Dfile=target/restmud-engine-1.4-SNAPSHOT-jar-with-dependencies.jar -DpomFile=pom.xml -Dsources=target/restmud-engine-1.4-SNAPSHOT-sources.jar
