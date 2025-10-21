import { useState, useEffect } from "react"
import './App.css'
function App() {

  const [polls, setPolls] = useState([])
  const [users, setUsers] = useState([])

  const [username, setUsername] = useState("")
  const [email, setEmail] = useState("")
  const [loggedIn, setLoggedIn] = useState(false)
  const [userId, setUserId] = useState(null)

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
    fetch("/users", {
      method: "POST",
      headers: {"Content-Type": "application/json" },
      body: JSON.stringify({ username, email })
    })
      .then(res => res.json())
      .then(data => {
        setLoggedIn(true)
        setUserId(data.userId)
        fetch("/users")
        .then(res => res.json())
        .then(data => setUsers(data))
      })

      .catch(() => alert("Failed to connect to backend"))
  }

  const handleCreatePolls = (e) => {
    e.preventDefault()
    if(!userId) {
      alert("You must be logged in to create a poll")
      return
    }
    fetch(`/polls/${userId}`, {
      method: "POST",
      headers: {"Content-Type": "application/json" },
      body: JSON.stringify({ question, options:[
        { caption: option1},
        { caption: option2}
      ]})
    })
      .then(() => {
        fetch("/polls")
          .then(res => res.json())
          .then(data => setPolls(data))
      })
      
  }

  const handleVote = (pollId, optionIdx) => {
    if(!userId) {
      alert("You must be logged in to vote")
      return
    }
    fetch(`/vote/${userId}/${pollId}`, {
      method: "POST",
      headers: {"Content-Type": "application/json" },
      body: JSON.stringify({ optionIndex: optionIdx + 1 })
    })
      .then(() => {
        fetch("/polls")
          .then(res => res.json())
          .then(data => setPolls(data))
      })
  }

  return (
    <>
      <h1>Poll App</h1>
      <div>
        <div id="Hele siden">
          {!loggedIn && (
            <div id="Login">
              <h2>Log in</h2>
              <form onSubmit={handleLogin} className="loginform">
                <input type="text" placeholder="username" value={username} onChange={e => setUsername(e.target.value)} required />
                <input type="text" placeholder="email"  value={email} onChange={e => setEmail(e.target.value)} required />
                <button type="submit" id="loginbutton">Log in</button>
              </form>
            </div>
          )}
          {loggedIn &&(
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
          

          <div className="pollComponent">
            {polls.map(poll => {
              const creator = users.find(u => u.userId === poll.createdBy)
              return(
              <div key={poll.pollId} className="poll-box">
                <h4>Poll number: {poll.pollId}</h4>
                <h2>{poll.question}</h2>
                <ul>
                  {poll.options.map((opt, idx) => (
                    <li key={idx}>{opt.caption} - {opt.votes} votes {" "}
                    <button onClick={() => handleVote(poll.pollId, idx)}>Vote</button> </li>
                  ))}
                </ul>
                <h3>Created by: {creator ? creator.username : "Unknown"}</h3>
              </div>
              )
            })}
          </div>
        </div>
      </div>
    </>
  )
}

export default App
