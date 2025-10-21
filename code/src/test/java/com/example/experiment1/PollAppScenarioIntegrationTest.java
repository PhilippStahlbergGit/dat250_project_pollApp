package com.example.experiment1;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.User;
import com.example.experiment1.domain.Vote;
import com.example.experiment1.domain.VoteOption;

import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PollAppScenarioIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient client;
    private String baseUrl;
    private User user1;
    private User user2;
    private Poll poll;
    private Vote vote;

    @BeforeEach
    public void Initiate() {
        client = RestClient.create();
        baseUrl = "http://localhost:" + port;
        
        //Creating user1
        user1 = new User();
        user1.setUsername("sondre");
        user1.setEmail("sondremail");
        client.post()
            .uri(baseUrl + "/users")
            .contentType(MediaType.APPLICATION_JSON)
            .body(user1)
            .retrieve()
            .toBodilessEntity();
            ;
        //Creating user2
        user2 = new User();
        user2.setUsername("tia");
        user2.setEmail("tiamail");
        client.post()
            .uri(baseUrl + "/users")
            .contentType(MediaType.APPLICATION_JSON)
            .body(user2)
            .retrieve()
            .toBodilessEntity();
            ;
        //Creating a poll
        VoteOption option1 = new VoteOption();
        option1.setCaption("Grønn");
        option1.setPresentationOrder(1);

        VoteOption option2 = new VoteOption();
        option2.setCaption("Blå");
        option2.setPresentationOrder(2);

        poll = new Poll();
        poll.setQuestion("Favorittfarge?");
        poll.setOptions(Arrays.asList(option1,option2));

        //user1 creates the poll
        client.post()
            .uri(baseUrl + "/polls/" + user1.getUserId())
            .body(poll)
            .retrieve()
            .toBodilessEntity();

        //Voting on option 1
        vote = new Vote();
        vote.setOptionIndex(1);
        client.post()
            .uri(baseUrl + "/vote/"+ user1.getUserId() + "/" + poll.getPollId())
            .body(vote)
            .retrieve()
            .toBodilessEntity();

    }
        @Test
        public void checkUsers(){
        //Checking that the users were created
        User[] users = client.get()
                        .uri(baseUrl + "/users")
                        .retrieve()
                        .body(User[].class);
        assertThat(users)
            .extracting(User::getUsername)
            .contains(user1.getUsername(), user2.getUsername())
            ;
        }
        
        @Test
        public void checkPolls(){
        //Get polls
        Poll[] polls = client.get()
                        .uri(baseUrl + "/polls")
                        .retrieve()
                        .body(Poll[].class);
        assertThat(polls)
            .extracting(Poll::getQuestion)
            .contains(poll.getQuestion());
        }

        @Test
        public void changeVoteAndCheck() {
        //Changing vote to option 2
        vote.setOptionIndex(2);
        client.post()
            .uri(baseUrl + "/vote/"+ user1.getUserId() + "/" + poll.getPollId())
            .body(vote)
            .retrieve()
            .toBodilessEntity();

        //Check to see the vote is for option 2
        Vote[] votes = client.get()
            .uri(baseUrl + "/vote")
            .retrieve()
            .body(Vote[].class);
        assertThat(votes)
            .extracting(Vote::getOptionIndex)
            .contains(vote.getOptionIndex());
        }

        @Test
        public void deletePollAndCheckVoteIsDeleted() {
        //Delete poll
        client.delete()
            .uri(baseUrl + "/polls/delete/" + poll.getPollId())
            .retrieve()
            .toBodilessEntity();
        Vote[] deleted_votes = client.get()
            .uri(baseUrl + "/vote")
            .retrieve()
            .body(Vote[].class);
        assertThat(deleted_votes).isEmpty();
        }   
    }
