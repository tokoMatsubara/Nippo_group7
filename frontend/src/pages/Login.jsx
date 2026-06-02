// ログイン画面　
// 雛形(松原)→css適用(今藤)

import "../styles/Login.css";
import React, { useState } from "react";
import { Link } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch("/api/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email,
                    password
                })
            });

            const data = await res.json();

            console.log("ログイン成功:", data);

            localStorage.setItem("user_id", data.user_id);

            window.location.href = "/dashboard";

        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="loginContainer">

            <div className="loginBox">

                <h1 className="loginTitle">ログイン</h1>

                <form className="loginForm" onSubmit={handleLogin}>

                    <input
                        className="loginInput"
                        placeholder="メール"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        className="loginInput"
                        type="password"
                        placeholder="パスワード"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <button className="loginButton" type="submit">
                        ログイン
                    </button>

                </form>

                <p className="loginLink">
                    <Link to="/register">新規登録</Link>
                </p>

            </div>

        </div>
    );
}