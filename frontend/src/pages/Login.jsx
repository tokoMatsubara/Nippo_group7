import React, { useState } from "react";
import { Link } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:8080/api/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    mailAdress: email,
                    password: password
                })
            });

            if(!res.ok){
                const errorData = await res.json().catch(() => ({}));
                alert(errorData.message || "ログインに失敗しました");
                return;
            }

            const data = await res.json();

            console.log("ログイン成功:", data);

            // JWTなしなので user_id を保存
            localStorage.setItem("user_id", data.user_id);
            window.location.href = "/dashboard";

        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div>
            <h1>ログイン</h1>

            <form onSubmit={handleLogin}>
                <input
                    placeholder="メール"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="パスワード"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">ログイン</button>
            </form>
            <p>
                <Link to="/register">新規登録</Link>
            </p>
        </div>
    );
}