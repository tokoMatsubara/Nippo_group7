// ログイン画面

import "../styles/Login.css";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    mail_address: email,
                    password: password
                }),
                credentials: "include"
            });

            if (!res.ok) {
                const errorData = await res.json().catch(() => ({}));
                alert(errorData.message || "ログインに失敗しました");
                return;
            }

            const data = await res.json();

            console.log("ログイン成功:", data);

            // localStorage.setItem("user_id", data.userId);
            localStorage.setItem("user_name", data.userName);

            navigate("/dashboard");

        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="loginContainer">

            <div className="loginBox card">

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