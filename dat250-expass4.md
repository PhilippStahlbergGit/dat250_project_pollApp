# Report for assignment 4

Start by adding the necessary dependencies for the project

When i copied the code for the PollsTest i had a problem where i could not resolve the import PersistenceConfiguration
It seemed the PersistenceConfiguration was not working so i tried using just the Persistence and then defining a persistence.xml file. I did some more troubleshooting and ended up updating the version implementation ("jakarta.persistence:jakarta.persistence-api:3.2.0") from 3.1.0 and it worked.
When doing the annotations i did alot of testing and looking at what the output was from the tests seeing what was missing of annotations.
