import React, { useState } from "react";
import "../styles/Profile.css";

export default function Profile() {

    const [userName, setUserName] = useState(
        localStorage.getItem("user_name") || ""
    );

    const [mailAddress, setMailAddress] = useState(
        localStorage.getItem("email") || ""
    );
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
            } else {

                alert(data.message);

            }

        } catch (error) {
            console.error(error);
            alert("通信エラーが発生しました。時間をおいて再度お試しください。");

        }
    };

    return (
        <div className="profileContainer">

            <div className="header">
                <div className="profileTitle">
                    <h2 className="title">プロフィール編集</h2>
                </div>
            </div>

            <div className="profileBox profilecard">

                <form className="profileForm">
                    <div>
                        <label>ユーザー名</label>

                        <input
                            className="profileInput"
                            placeholder="ユーザー名"
                            value={userName}
                            onChange={(e) =>
                                setUserName(e.target.value)
                            }
                        />
                    </div>


                    <div>
                        <label>メールアドレス</label>
                        <input
                            className="profileInput"
                            placeholder="メールアドレス"
                            value={mailAddress}
                            onChange={(e) =>
                                setMailAddress(e.target.value)
                            }
                        />
                    </div>

                    <div>
                        <label>新しいパスワード</label>
                        <input
                            className="profileInput"
                            placeholder="パスワード"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
                        />
                    </div>

                </form>
                <br />
                <button
                    className="primaryButton"
                    onClick={handleSave}>
                    保存
                </button>
            </div>

        </div>
    );
}