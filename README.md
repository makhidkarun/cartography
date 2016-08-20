cartography
===========

Traveller Stellar Cartography


This page serves as the home page for Stellar Cartrograpy project, a free
project to build a star mapping utility using Java as the programming engine
and XML as the database and data storage system.

The itch to scratch for doing this project is two fold. The first is as a
learning experience for Java, XML, and their interaction. I've discovered
my programming skillset becoming out of date recently and needing a way to
update it. The second is I wanted to write an article for Traveller describing
using the T20 System generation (Natural resources and Trade Balance) for a
economic system similar to GT:Far Trader. I'm not sure it would be any simpler,
but it would be more available. In order to make sure the rules work, I needed
to generate the trade maps. But none of the existing Traveller mapping
programs (Galactic, Heaven & Earth, Astrogator, Universe) will let me enter,
queuy, and map the data as I need it. So on to Traveller Stellar Cartography.

This project is maven run, even though there is some ant project files still
around.

To build the project run:

mvn package

To run the project:

java -jar target/cartography-1.0-SNAPSHOT-jar-with-dependencies.jar

There are many known failures, obvious bugs, crashes, NPEs, and other bad thing. 
Pull requests welcome. 



