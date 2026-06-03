import React, { useState } from "react";
import { Link } from "react-router-dom";

export default function Register() {
    const [emailaddress, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:8080/api/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    userName: "新規ユーザー",
                    mailAddress: emailaddress,
                    password: password
                })
            });

            if(!res.ok){
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
        <div>
            <h1>新規登録</h1>

            <form onSubmit={handleSubmit}>

                <input
                    placeholder="メール"
                    value={emailaddress}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="パスワード"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">登録</button>
            </form>
            <p>
                <Link to="/login">ログイン</Link>
            </p>

        </div>
    );
}