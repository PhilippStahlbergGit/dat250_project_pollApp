# Report for assignment 2

I started with creating the java beans (User, Poll, etc) with the getters and the setters and the parameterless public constructor,

Then i went on to create the PollManager class which holds the different objects in hash maps.

I then looked at the test scenario and started implementing controllers to handle the scenario.
I realized here it is pretty hard to identify the differen objects based on for example username, so i decided to add an id to all the classes.
I also added the id of the user and the id of the poll a vote was related to so that i could identify who had voted and for what.

Also when i was adding PublishedAt for the Poll i realized it is annoying and can be inconsistent having to write the time in the request, so i instead made it so ut uses Java.time.Instant to set the time the Poll is being made. 
Also for validUntil i decided to make it standard 1 day in advance.

After i had made all the logic in the controllers and made the test scenario work in Postman just running the tests in a sequence i started automating the test using RestClient.
Here i first had some trouble because i wanted to have more tests instead of just one test testing the entire sequence, because i wanted to see where it went wrong if it did, I then realized i could initiate with @BeforeEach to run the start and then just test different aspects.
When running ./gradlew test everything looked fine and all the tests ran nicely 

[Link to code](https://github.com/SondreGarnes/experiment1_dat250)
