# Report for assignment 3

First of i started trying to implement the components to do the tasks. I first tried to do the tasks without adding stub-data but realised it was annoying having to add the data for each task. I also realised i was doing a lot more logic in the App.jsx file then i needed like for example creating polls and stuff like that, since the apis would be doing this for me.

I also had a problem where i had no way to acces the user data in the frontend. For example when i wanted to set the userId for routing to "/polls/${userId}" in the handleCreatePolls, this i fixed by returning the user in the createUser function in the UserController. so now i could set the userId in the frontend when i create a user so i could fetch with the userId.

 It was also a little hard trying to test the application having to log in each time i refreshed the page. There lacks a way of authenticating and logging in and out instead of creating a new user everytime. However i think this is not necessary before integrating with a database since it will anyways be deleted for each time i reset the application.

 Deploying worked well and i tried to make it build automatically when i pushed to github, this worked well and i managed to make it work to push it to docker also.

 [Link to code](https://github.com/SondreGarnes/experiment1_dat250)