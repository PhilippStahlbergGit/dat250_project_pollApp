# Report for assignment 7
I had already made my Dockerfile in a previous assignment so this was not any problem and i also have made the github workflow docker.yml so i push the image to DockerHub so i will try to pull the image from dockerhub and run it from there.
When doing this i had a problem where i could not connect to Docker Daemon, but opening docker desktop fixed this.
Now i can run 
docker run --platform linux/amd64 -d --name pollapp -p 8080:8080 sondregarnaes/experiment_dat250:latest
Which pulls the image from dockerhub and i can go into localhost:8080 and look at my app.
i can then do 
docker ps -a | grep pollapp
to see the status of the container and i can see that it is running.
This does not use Rabbit or Redis and this would need to be specified in the docker.yml file or a docker-compose.