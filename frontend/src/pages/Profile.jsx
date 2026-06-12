import React, { useState } from "react";

export default function Profile() {

    const [userName, setUserName] = useState(
        localStorage.getItem("user_name") || ""
    );

    const [mailAddress, setMailAddress] = useState("");
    const [password, setPassword] = useState("");

    const handleSave = async () => {

        console.log("保存ボタン押下");

        console.log({
            user_name: userName,
            mail_address: mailAddress,
            password: password
        });

        try {

            const response = await fetch(
                "http://localhost:8080/api/user/profile",
                {
                    method: "PUT",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        user_name: userName,
                        mail_address: mailAddress,
                        password: password
                    })
                }
            );

            const data = await response.json();

            console.log(data);

            if (data.status === "success") {

                localStorage.setItem(
                    "user_name",
                    userName
                );

                alert("プロフィールを更新しました");
            }

        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{ padding: "20px" }}>

            <h2>プロフィール編集</h2>

            <div>
                <label>ユーザー名</label>
                <br />
                <input
                    type="text"
                    value={userName}
                    onChange={(e) =>
                        setUserName(e.target.value)
                    }
                />
            </div>

            <br />

            <div>
                <label>メールアドレス</label>
                <br />
                <input
                    type="email"
                    value={mailAddress}
                    onChange={(e) =>
                        setMailAddress(e.target.value)
                    }
                />
            </div>

            <br />

            <div>
                <label>新しいパスワード</label>
                <br />
                <input
                    type="password"
                    value={password}
                    onChange={(e) =>
                        setPassword(e.target.value)
                    }
                />
            </div>

            <br />

            <button onClick={handleSave}>
                保存
            </button>

        </div>
    );
}