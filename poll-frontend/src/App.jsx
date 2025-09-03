import { use, useState } from "react"

function App() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [loggedIn, setLoggedIn] = useState(false)

  const [question, setQuestion] = useState('')
  const [options, setOptions] = useState(['',''])
  const [createdPoll, setCreatedPoll] = useState(null)
  
  const handleLogin = async (e) => {
    e.preventDefault()
    if (username.trim()) {
      setLoggedIn(true);
    }
  };


  return (
    <>
      <h1>Poll App</h1>
      <div>
        {!loggedIn ? (
          <form onSubmit={handleLogin}>
            <input
              type="text"
              placeholder="enter username"
              value={username}
              onChange={e => setUsername(e.target.value)}
            />
            <input
              type="text"
              placeholder="enter email"
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
            <button type="submit">Log In</button>
          </form>
        ) : (
          <div>
            <p>Welcome, {username}</p>
            <div>
              <h2>Create Poll</h2>
              <form onSubmit={handlePoll}>
                <input
                  type="text"
                  placeholder={"Poll question"}
                  value={question}
                  onChange={e => setQuestion(e.target.value)}
                  required
                />
                {options.length > 2 && (
                  <button type="button" onClick={() => removeOption(idx)}>
                    Remove Option
                  </button>
                  
                )}
              </form>

            </div>
          </div>
          
        )}
      </div>
    </>
  )
}

export default App
