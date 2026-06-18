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

    // パスワード関連
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    // ✔ 独立編集モード
    const [isEditingName, setIsEditingName] = useState(false);
    const [isEditingEmail, setIsEditingEmail] = useState(false);


    const handleUsernameUpdate = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/api/user/username/${userName}`,
                {
                    method: "PUT",
                    credentials: "include"
                }
            );

            if (!response.ok) {
                const error = new Error(`HTTP ${response.status}`);
                error.status = response.status;
                throw error;
            }

            const data = await response.json();

            if (data.success) {

                localStorage.setItem("user_name", userName);

                alert("プロフィールを更新しました");

                // ✔ 各編集モードをリセット
                setIsEditingName(false);

            } else {
                alert(data.message);
            }

        } catch (error) {
            console.error(error);
            if (error.status === 401) {
                console.log("401認証エラー");
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            } else {
                alert("ユーザーネームの変更に失敗しました");
            }
        }
    }

    const handleEmailUpdate = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/api/user/email/${mailAddress}`,
                {
                    method: "PUT",
                    credentials: "include"
                }
            );

            if (!response.ok) {
                const error = new Error(`HTTP ${response.status}`);
                error.status = response.status;
                throw error;
            }

            const data = await response.json();

            if (data.success) {

                localStorage.setItem("email", mailAddress);

                alert("プロフィールを更新しました");

                // ✔ 各編集モードをリセット
                setIsEditingEmail(false);

                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");

            } else {
                alert(data.message);
            }

        } catch (error) {
            console.error(error);
            if (error.status === 401) {
                console.log("401認証エラー");
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            } else {
                alert("メールアドレスの変更に失敗しました");
            }
        }
    }

    const handlePasswordUpdate = async () => {
        // パスワードチェック（入力されている場合のみ）
        if (currentPassword || newPassword || confirmPassword) {

            if (!currentPassword) {
                alert("現在のパスワードを入力してください");
                return;
            }

            if (newPassword !== confirmPassword) {
                alert("新しいパスワードが一致しません");
                return;
            }
        }

        try {
            const response = await fetch(
                `http://localhost:8080/api/user/password/${currentPassword}/${newPassword}`,
                {
                    method: "PUT",
                    credentials: "include"
                }
            );

            if (!response.ok) {
                const error = new Error(`HTTP ${response.status}`);
                error.status = response.status;
                throw error;
            }

            const data = await response.json();

            if (data.success) {

                localStorage.setItem("email", mailAddress);

                alert("プロフィールを更新しました");

                // ✔ 各編集モードをリセット
                setIsEditingPassword(false);

            } else {
                alert(data.message);
            }

        } catch (error) {
            console.error(error);
            if (error.status === 401) {
                console.log("401認証エラー");
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            } else {
                alert("パスワードの変更に失敗しました");
            }
        }
    }


    return (
        <div className="container">

            <div className="header">
                <div className="headerTitle">
                    <h1 className="title">プロフィール編集</h1>
                    <img src={profileIcon} alt="logo" className="logoIcon" />
                </div>
            </div>

            {/* =========================
                ユーザー名・メール
            ========================= */}
            <div className="profileBox profilecard">

                {/* ユーザー名 */}
                <div>
                    <label>ユーザー名</label>
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
                            onClick={() => {
                                if (isEditingName) {
                                    handleUsernameUpdate();
                                }
                                setIsEditingName(!isEditingName);
                            }}
                        >
                            {isEditingName ? "保存" : "🖋"}
                        </button>
                    </div>
                </div>

                {/* メールアドレス */}
                <div>
                    <label>メールアドレス</label>
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
                            onClick={() => {
                                if (isEditingEmail) {
                                    handleEmailUpdate();
                                }
                                setIsEditingEmail(!isEditingEmail);
                            }}
                        >
                            {isEditingEmail ? "保存" : "🖋"}
                        </button>
                    </div>
                </div>

            </div>

            {/* =========================
                パスワード
            ========================= */}
            <div className="profileBox profilecard">

                {/* 現在のパスワード */}
                <div>
                    <label>パスワード</label>
                    <br />
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="現在のパスワード"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                    />
                </div>

                {/* 新しいパスワード */}
                <div>
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="新しいパスワード"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                </div>

                {/* 確認パスワード */}
                <div>
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="新しいパスワード（確認）"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                </div>

                {/* 編集ボタン */}
                <div>
                    <button
                        type="button"
                        className="editButton"
                        onClick={handlePasswordUpdate}
                    >
                        パスワードを変更する
                    </button>
                </div>

            </div>

        </div>
    );
}