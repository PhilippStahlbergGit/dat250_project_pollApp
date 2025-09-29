# Report for assignment 4

Start by adding the necessary dependencies for the project

When i copied the code for the PollsTest i had a problem where i could not resolve the import PersistenceConfiguration
It seemed the PersistenceConfiguration was not working so i tried using just the Persistence and then defining a persistence.xml file. I did some more troubleshooting and ended up updating the version implementation ("jakarta.persistence:jakarta.persistence-api:3.2.0") from 3.1.0 and it worked.
When doing the annotations i did alot of testing and looking at what the output was from the tests seeing what was missing of annotations.
I had some problems making the tests pass, so i made a new variable private String 
    createdBy;
    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;
Because using createdBy did not work to query, therefore i made a new variable. and i had to change the query in the tests to query p.createdByUser instead of p.createdBy. I know this is not ideal and will probably need to be changed in the future. 

I now figured out how to inspect the database

[Users-table] (images/users_table.png)

[Users-table] (images/polls_table.png)

I can see that there is alot of null values telling me that it does not work correctly this is probably why createdBy is not working.


[ Link to code ](https://github.com/SondreGarnes/experiment1_dat250)