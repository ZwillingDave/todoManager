import React from 'react';
import logo from './logo.svg';
import './App.css';
import * as http from "node:http";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <section>
          <h1>TodoManager</h1>
        </section>
        <section className={"auth-section"}>
          <form className={"login-field"} onSubmit={event => {event.preventDefault(); login();}}>
            <h1>Login</h1>
            <input id={"login-email"} type={"email"} placeholder={"Email"}></input>
            <input id={"login-password"} type={"password"} placeholder={"Password"}></input>
            <button id={"login-btn"}>Login</button>
          </form>
          <form className={"signup-field"} onSubmit={event => {event.preventDefault(); alert("Submitted")}}>
            <h1>Signup</h1>
            <input id={"signup-username"} type={"text"} placeholder={"Username"}></input>
            <input id={"signup-email"} type={"email"} placeholder={"Email"}></input>
            <input id={"signup-password"} type={"password"} placeholder={"Password"}></input>
            <input id={"signup-confirmPassword"} type={"password"} placeholder={"Confirm Password"}></input>
            <button id={"signup-btn"}>Signup</button>
          </form>
        </section>


      </header>
    </div>
  );
}

async function login() {
    const email = document.getElementById("login-email") as HTMLInputElement;
    const password = document.getElementById("login-password") as HTMLInputElement;
    const response = await window.fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email.value,
            password: password.value
        }),
    })
    const data = await response.json();
    console.log(data.message);

}
export default App;
