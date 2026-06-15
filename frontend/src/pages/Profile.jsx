import React, { useState } from "react";
import "../styles/Profile.css";
import profileIcon from "../assets/Nippo_profile.png";

export default function Profile() {

    const [userName, setUserName] = useState(
        localStorage.getItem("user_name") || ""
    );

    const [mailAddress, setMailAddress] = useState(
        localStorage.getItem("email") || ""
    );
    const [password, setPassword] = useState("");

    const [confirmPassword, setConfirmPassword] = useState("");

    const [isSaved, setIsSaved] = useState(false);

    const handleSave = async () => {

        if (password !== confirmPassword) {
            alert("パスワードが一致しません");
            return;
        }

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

                setIsSaved(true);

                // パスワード欄だけリセット
                setPassword("");
                setConfirmPassword("");

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
        <div className="container">

            <div className="header">
                <div className="headerTitle">
                    <h1 className="title">プロフィール編集</h1>
                    <img src={profileIcon} alt="logo" className="logoIcon" />
                </div>
            </div>

            <div className="profileBox profilecard">

                <form
                    className="profileForm"
                    onSubmit={(e) => {
                        e.preventDefault();
                        handleSave();
                    }}
                >

                    <div>
                        <label>ユーザー名</label>

                        <input
                            className="profileInput"
                            placeholder="ユーザー名"
                            value={userName}
                            onChange={(e) => {
                                setUserName(e.target.value);
                                setIsSaved(false);
                            }}
                        />
                    </div>

                    <div>
                        <label>メールアドレス</label>
                        <input
                            className="profileInput"
                            placeholder="メールアドレス"
                            value={mailAddress}
                            onChange={(e) => {
                                setMailAddress(e.target.value);
                                setIsSaved(false);
                            }}
                        />
                    </div>

                    <div>
                        <label>新しいパスワード</label>
                        <input
                            className="profileInput"
                            type="password"
                            placeholder="パスワード"
                            value={password}
                            onChange={(e) => {
                                setPassword(e.target.value);
                                setIsSaved(false);
                            }}
                        />
                    </div>
                    <div>
                        <input
                            className="profileInput"
                            type="password"
                            placeholder="パスワード（確認）"
                            value={confirmPassword}
                            onChange={(e) => {
                                setConfirmPassword(e.target.value)
                                setIsSaved(false);
                            }}
                        />
                    </div>



                    <br />
                    <button
                        type="submit"
                        className={`primaryButton ${isSaved ? "saved" : ""}`}
                        disabled={isSaved}
                    >
                        {isSaved ? "保存済み" : "保存"}
                    </button>
                </form>
            </div>


        </div>
    );
}