// ログイン画面
import logoIcon from "../assets/logo.png";
import "../styles/Login.css";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const params = useParams();

    useEffect(() => {

        const registeredEmail = localStorage.getItem("email");
        if(registeredEmail !== null){
            setEmail(registeredEmail);
        }
    }, [])

    const handleLogin = async (e) => {
        e.preventDefault();
        localStorage.removeItem("email");

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

                <img src={logoIcon} alt="logo" className="loginIcon" />

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

                    <button
                        className="registerButton"
                        type="button"
                        onClick={() => navigate("/register")}
                    >
                        新規登録
                    </button>

                </form>

            </div>

        </div>
    );
}