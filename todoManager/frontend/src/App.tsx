import React from 'react';
import Cookies from 'js-cookie';
import {useNavigate} from 'react-router-dom';
import './styles/App.css';


function App() {
    const navigate = useNavigate();

    const login = async () => {

        const email = document.getElementById("login-email") as HTMLInputElement;
        const password = document.getElementById("login-password") as HTMLInputElement;
        await window.fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email.value,
                password: password.value
            }),
        }).then(response => response.ok ? response.json() : alert("Invalid credentials"))
            .then(async data => {
                if (data && data.success) {
                    Cookies.set("token", data.token);


                    const res = await fetch("http://localhost:8080/api/auth/decodePayload", {
                        method: "GET",
                        credentials: "include",
                    });

                    const payload = await res.json();
                    const expiryDate = new Date(payload.expiryDate);

                    Cookies.set("token", data.token, {expires: expiryDate});
                    navigate(data.path);
                }
            })
            .catch(error => alert(error))
        ;

    }

    const signup = async () => {
        const username = document.getElementById("signup-username") as HTMLInputElement;
        const email = document.getElementById("signup-email") as HTMLInputElement;
        const password = document.getElementById("signup-password") as HTMLInputElement;
        const confirmPassword = document.getElementById("signup-confirmPassword") as HTMLInputElement;

        if (password.value !== confirmPassword.value) {
            alert("Passwords do not match");
            return;
        }
        await window.fetch("http://localhost:8080/api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: username.value,
                email: email.value,
                password: password.value
            }),
        }).then(response => response.ok ? response.json() : alert("Something went wrong. Try again."))
            .then(() => {
                    const loginMail = document.getElementById("login-email") as HTMLInputElement;
                    const loginpw = document.getElementById("login-password") as HTMLInputElement;
                    loginMail.value = email.value;
                    loginpw.value = password.value;
                    login();

                }
            );
    }

    return (
        <div className="App">
            <header className="App-header">
                <section>
                    <h1>TodoManager</h1>
                </section>
                <section className={"auth-section"}>
                    <form className={"login-field"} onSubmit={event => {
                        event.preventDefault();
                        login()
                    }}>
                        <h1>Login</h1>
                        <input id={"login-email"} type={"email"} placeholder={"Email"}></input>
                        <input id={"login-password"} type={"password"} placeholder={"Password"}></input>
                        <button id={"login-btn"}>Login</button>
                    </form>
                    <form className={"signup-field"} onSubmit={event => {
                        event.preventDefault();
                        signup()
                    }}>
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

export default App;
