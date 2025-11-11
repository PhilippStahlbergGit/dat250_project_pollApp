import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experiment1.service.PollManager;
import com.example.experiment1.service.RabbitMQPollService;
import com.example.experiment1.domain.Poll;

@CrossOrigin
@RestController
@RequestMapping("/polls")
public class PollController {
    PollManager pollManager;
    RabbitMQPollService rabbitMQPollService;

    public PollController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    // only admins can delete a Poll
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{userId}")
    public Poll createPoll(@RequestBody Poll poll, @PathVariable String userId) {
        return pollManager.createPoll(poll, userId);
    }

    @GetMapping
    public Collection<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    // only admins can delete a Poll
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        pollManager.deletePoll(pollId);
    }
}
