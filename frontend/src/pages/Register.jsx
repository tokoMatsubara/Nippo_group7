// 新規登録画面

import "../styles/Register.css";
import React, { useState } from "react";
import { Link } from "react-router-dom";

export default function Register() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert("パスワードが一致しません");
            return;
        }

        try {
            const res = await fetch("http://localhost:8080/api/auth/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    user_name: username,
                    mail_address: email,
                    password: password
                })
            });

            if (!res.ok) {
                const errorData = await res.json().catch(() => ({}));
                alert(errorData.message || "登録に失敗しました");
                return;
            }

            const data = await res.json();

            console.log("登録成功:", data);

            alert("登録完了");
            window.location.href = "/login";

        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="registerContainer">

            <div className="registerBox card">

                <h1 className="registerTitle">新規登録</h1>

                <form className="registerForm" onSubmit={handleSubmit}>

                    <input
                        className="registerInput"
                        placeholder="ユーザー名"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <input
                        className="registerInput"
                        placeholder="メール"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        className="registerInput"
                        type="password"
                        placeholder="パスワード"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <input
                        className="registerInput"
                        type="password"
                        placeholder="パスワード（確認）"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />

                    <button className="registerButton" type="submit">
                        登録
                    </button>

                </form>

                <p className="registerLink">
                    <Link to="/login">戻る</Link>
                </p>

            </div>

        </div>
    );
}