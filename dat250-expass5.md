# Report for assignment 5

Connecting to the redis-cli was no problem and doing the first case with testing tracking logged in users was okay using SADD SMEMBERS and SREM

I used hash for the complex information and this worked well, after running RedisDataLoader i can see in my redis-cli that the things i added is there.
Also the using the HGETALL we can see all the votes for a poll

Im not sure if i am supposed to connect the redis to the entire project to make the cache work, i dont see another way, but that would require me to save the poll in a cache when i log in

I managed to make it work pretty nicely with the redis, but i had a problem where if i Added votes to the other option, changed vote, it did not remove the vote from the redis, i would need to add a way to delete votes when it is changed.
And also now the polls showed on the website does not show the votes in the redis db
