import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Profile.css";
import profileIcon from "../assets/Nippo_profile.png";

export default function Profile() {

    const [userName, setUserName] = useState(
        localStorage.getItem("user_name") || ""
    );

    const [mailAddress, setMailAddress] = useState(
        localStorage.getItem("email") || ""
    );

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [isEditingName, setIsEditingName] = useState(false);
    const [isEditingEmail, setIsEditingEmail] = useState(false);

    const navigate = useNavigate();

    const isNameOrEmailEditing = isEditingName || isEditingEmail;

    /* =========================
        USERNAME UPDATE
    ========================= */
    const handleUsernameUpdate = async () => {
        try {
            const response = await fetch(
                "http://localhost:8080/api/user/username",
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ userName }),
                    credentials: "include"
                }
            );

            if (!response.ok) throw new Error(`HTTP ${response.status}`);

            const data = await response.json();

            if (data.success) {
                localStorage.setItem("user_name", userName);
                alert("ユーザー名を更新しました");
                setIsEditingName(false);
            } else {
                alert(data.message);

                // ★ 失敗時リセット
                setUserName(localStorage.getItem("user_name") || "");
            }

        } catch (error) {
            console.error(error);
            alert("ユーザー名の変更に失敗しました");

            // ★ 失敗時リセット
            setUserName(localStorage.getItem("user_name") || "");
        }
    };

    /* =========================
        EMAIL UPDATE
    ========================= */
    const handleEmailUpdate = async () => {
        try {
            const response = await fetch(
                "http://localhost:8080/api/user/email",
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ mailAddress }),
                    credentials: "include"
                }
            );

            if (!response.ok) throw new Error(`HTTP ${response.status}`);

            const data = await response.json();

            if (data.success) {
                localStorage.setItem("email", mailAddress);
                alert("メールアドレスを更新しました");
                setIsEditingEmail(false);
            } else {
                alert(data.message);

                // ★ 失敗時リセット
                setMailAddress(localStorage.getItem("email") || "");
            }

        } catch (error) {
            console.error(error);
            alert("メールアドレスの変更に失敗しました");

            // ★ 失敗時リセット
            setMailAddress(localStorage.getItem("email") || "");
        }
    };

    /* =========================
        PASSWORD UPDATE
    ========================= */
    const handlePasswordUpdate = async () => {

        if (!currentPassword) {
            alert("現在のパスワードを入力してください");
            return;
        }

        if (newPassword !== confirmPassword) {
            alert("新しいパスワードが一致しません");
            return;
        }

        try {
            const response = await fetch(
                "http://localhost:8080/api/user/password",
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        currentPassword,
                        newPassword
                    }),
                    credentials: "include"
                }
            );

            if (!response.ok) throw new Error(`HTTP ${response.status}`);

            const data = await response.json();

            if (data.success) {
                alert("パスワードを更新しました");
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            } else {
                alert(data.message);
            }

        } catch (error) {
            console.error(error);
            alert("パスワードの変更に失敗しました");
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

            {/* USER / EMAIL */}
            <div className="profileBox profilecard">

                <div>
                    <label>ユーザー名</label>
                    <br />
                    <div className="inputRow">
                        <input
                            className="profileInput"
                            value={userName}
                            onChange={(e) => setUserName(e.target.value)}
                            disabled={!isEditingName}
                        />

                        <button
                            type="button"
                            className="editButton"
                            disabled={isNameOrEmailEditing && !isEditingName}
                            onClick={() => {
                                if (isEditingName) handleUsernameUpdate();
                                setIsEditingName(!isEditingName);
                            }}
                        >
                            {isEditingName ? "保存" : "🖋"}
                        </button>
                    </div>
                </div>

                <div>
                    <label>メールアドレス</label>
                    <br />
                    <div className="inputRow">
                        <input
                            className="profileInput"
                            value={mailAddress}
                            onChange={(e) => setMailAddress(e.target.value)}
                            disabled={!isEditingEmail}
                        />

                        <button
                            type="button"
                            className="editButton"
                            disabled={isNameOrEmailEditing && !isEditingEmail}
                            onClick={() => {
                                if (isEditingEmail) handleEmailUpdate();
                                setIsEditingEmail(!isEditingEmail);
                            }}
                        >
                            {isEditingEmail ? "保存" : "🖋"}
                        </button>
                    </div>
                </div>
                {/* PASSWORD */}

                <br />

                <hr />

                <div>
                    <label>パスワード</label>
                    <br />
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="現在のパスワード"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        disabled={isNameOrEmailEditing}
                    />
                </div>
                <br />
                <div>
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="新しいパスワード"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        disabled={isNameOrEmailEditing}
                    />
                </div>
                <br />
                <div>
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="新しいパスワード（確認）"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        disabled={isNameOrEmailEditing}
                    />
                </div>
                <br />
                <button
                    type="button"
                    className="editButton"
                    onClick={handlePasswordUpdate}
                    disabled={isNameOrEmailEditing}
                >
                    パスワードを変更する
                </button>
            </div>





        </div>
    );
}