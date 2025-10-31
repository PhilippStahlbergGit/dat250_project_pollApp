import { useState, useEffect } from "react"
import './App.css'
function App() {

  const [polls, setPolls] = useState([])
  const [users, setUsers] = useState([])

  const [username, setUsername] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [loggedIn, setLoggedIn] = useState(false)
  const [user, setUser] = useState(null)

  const [question, setQuestion] = useState("")
  const [option1, setOption1] = useState("")
  const [option2, setOption2] = useState("")

  //Fetch users
  useEffect(() => {
    fetch("/users")
    .then(res => res.json())
    .then(data => setUsers(data))
  }, [])
  //Fetch polls
  useEffect(() => {
    fetch("/polls")
      .then(res => res.json())
      .then(data => setPolls(data))

    }, [])

  const handleLogin = (e) => {
    e.preventDefault()
    const credentials = btoa(`${username}:${password}`);
    fetch("/users/me", {
      headers: {
        "Authorization": `Basic ${credentials}`
      }
    })
    .then(res => {
      if (res.ok) {
        return res.json();
      }
      throw new Error('Authentication failed');
    })
    .then(data => {
      setLoggedIn(true);
      setUser(data);
    })
    .catch(() => alert("Login failed. Check username and password."))
  }

  const handleCreateUser = (e) => {
    e.preventDefault()
    fetch("/users", {
      method: "POST",
      headers: {"Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password })
    })
      .then(res => res.json())
      .then(data => {
        alert(`User ${data.username} created! You can now log in.`);
        setUsername("");
        setEmail("");
        setPassword("");
      })
      .catch(() => alert("Failed to create user"))
  }

  const handleCreatePolls = (e) => {
    e.preventDefault()
    if(!user) {
      alert("You must be logged in to create a poll.");
      return;
    }
    const credentials = btoa(`${username}:${password}`);
    fetch(`/polls/${user.userId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Basic ${credentials}`
      },
      body: JSON.stringify({
        question,
        options: [
          { caption: option1 },
          { caption: option2 }
        ]
      })
    })
    .then(res => {
        if (res.ok) {
            alert("Poll created!");
            setQuestion("");
            setOption1("");
            setOption2("");
            // Refresh polls
            fetch("/polls")
              .then(res => res.json())
              .then(data => setPolls(data));
        } else {
            alert("Failed to create poll. Only admins can create polls.");
        }
    })
    .catch(() => alert("Failed to connect to backend"))
      
  }

  const handleVote = (pollId, optionIdx) => {
    if(!user || !username || !password || !user.id) {
      alert("You must be logged in to vote.");
      return;
    }
    const userId = user.id
    const credentials = btoa(`${username}:${password}`);
	
    // vote object to be sent via POST req.
    const voteBody = {
	userId: userId,
	pollId: pollId,
	optionIndex: optionIdx
    }


    fetch(`/vote/${userId}/${pollId}`, {
        method: "POST",
        headers: {
	    "Content-Type": "application/json",
            "Authorization": `Basic ${credentials}`
        },
	body: JSON.stringify(voteBody)
    })
    .then(res => {
        if (res.ok) {
            alert("Vote cast!");
            // Refresh polls to show new vote count
            fetch("/polls")
              .then(res => res.json())
              .then(data => setPolls(data));
        } else {
            alert("Failed to cast vote.");
        }
    })
    .catch(() => alert("Failed to connect to backend"))
  }

  return (
    <>
      <h1>Poll App</h1>
      <div>
        <div id="Hele siden">
          {!loggedIn ? (
            <>
            <div id="Login">
              <h2>Log in</h2>
              <form onSubmit={handleLogin} className="loginform">
                <input type="text" placeholder="username" value={username} onChange={e => setUsername(e.target.value)} required />
                <input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)} required />
                <button type="submit" id="loginbutton">Log In</button>
              </form>
            </div>
            <div id="CreateUser">
              <h2>Create User</h2>
              <form onSubmit={handleCreateUser} className="loginform">
                <input type="text" placeholder="username" value={username} onChange={e => setUsername(e.target.value)} required />
                <input type="text" placeholder="email"  value={email} onChange={e => setEmail(e.target.value)} required />
                <input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)} required />
                <button type="submit" id="createUserButton">Create User</button>
              </form>
            </div>
            </>
          ) : (
            <div>
              <h2>Welcome, {user?.username}</h2>
              {user && user.roles.includes('ADMIN') && (
                <div className="createPollComponent">
                  <h2>Create poll</h2>
                  <form onSubmit={handleCreatePolls}>
                    <input type="text" placeholder="Question" value={question} onChange={e => setQuestion(e.target.value)} required/>
                    <input type="text" placeholder="Option" value={option1} onChange={e => setOption1(e.target.value)} required/>
                    <input type="text" placeholder="Option" value={option2} onChange={e => setOption2(e.target.value)} required/>
                    <button type="submit">Submit</button>
                  </form>
                </div>
              )}
            </div>
          )}
          

          <div className="pollComponent">
            {polls.map(poll => (
              <div key={poll.id} className="poll">
                <h3>{poll.question}</h3>
                <ul>
                  {poll.options.map((option, index) => (
                    <li key={option.id}>
                      {option.caption} ({option.vote} vote)
                      {loggedIn && <button onClick={() => handleVote(poll.pollId, index + 1)}>Vote</button>}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  )
}

export default App
