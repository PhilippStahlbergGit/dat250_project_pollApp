import { useState, useEffect } from "react"

function App() {

  const [polls, setPolls] = useState([
    { pollId: "1", question: "Favorite color?", options: [{ caption: "Red" , votes: 3}, { caption: "Blue", votes: 0 }] },
    { pollId: "2", question: "Best season?", options: [{ caption: "Summer", votes:4 }, { caption: "Winter", votes: 3 }] }
  ])

  const [username, setUsername] = useState("")
  const [email, setEmail] = useState("")
  const [loggedIn, setLoggedIn] = useState(false)

  const handleLogin = (e) => {
    e.preventDefault()
    fetch("http://localhost:8080/users", {
      method: "POST",
      headers: {"Content-Type": "application/json" },
      body: JSON.stringify({ username, email })
    })
      .then(res => {
        if (res.ok) {
          setLoggedIn(true)
        } else {
          alert("Failed to create user")
        }
      })
      .catch(() => alert("Failed to connect to backend"))
  }

  //Get polls
  useEffect(() => {
    fetch("http://localhost:8080/polls")
      .then(res => res.json())
      .then(data => setPolls(data))

    })
  
  

  return (
    <>
      <h1>Poll App</h1>
      <div>
        <div id="Hele siden">
          <div id="Login">
            <h2>Log in</h2>
            <form onSubmit={handleLogin}>
              <input type="text" placeholder="username" value={username} onChange={e => setUsername(e.target.value)} required />
              <input type="text" placeholder="email"  value={email} onChange={e => setEmail(e.target.value)} required />
              <button type="submit">Log in</button>
            </form>
          </div>
          <div id="Create poll">
            <h2>Create poll</h2>
            <form>
              <input type="text" placeholder="Question" />
              <input type="text" placeholder="Option"/>
              <input type="text" placeholder="Option"/>
              <button>Submit</button>
            </form>
          </div>

          <div id="polls">
            {polls.map(poll => (
              <div key={poll.pollId}>
                <h2>{poll.question}</h2>
                <ul>
                  {poll.options.map((opt, idx) => (
                    <li key={idx}>{opt.caption} - {opt.votes} votes <button>Vote</button> </li>
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
