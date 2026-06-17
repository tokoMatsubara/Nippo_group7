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
    const [isEditingPassword, setIsEditingPassword] = useState(false);

    const handleUpdate = async () => {

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
                        current_password: currentPassword || null,
                        password: newPassword || null
                    })
                }
            );

            const data = await response.json();

            if (data.status === "success") {

                localStorage.setItem("user_name", userName);

                alert("プロフィールを更新しました");

                // ✔ 各編集モードをリセット
                setIsEditingName(false);
                setIsEditingEmail(false);
                setIsEditingPassword(false);

                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");

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
                                    handleUpdate();
                                }
                                setIsEditingName(!isEditingName);
                            }}
                        >
                            {isEditingName ? "保存" : "編集"}
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
                                    handleUpdate();
                                }
                                setIsEditingEmail(!isEditingEmail);
                            }}
                        >
                            {isEditingEmail ? "保存" : "編集"}
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
                    <input
                        className="profileInput"
                        type="password"
                        placeholder="現在のパスワード"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        disabled={!isEditingPassword}
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
                        disabled={!isEditingPassword}
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
                        disabled={!isEditingPassword}
                    />
                </div>

                {/* 編集ボタン */}
                <div>
                    <button
                        type="button"
                        className="editButton"
                        onClick={() => {
                            if (isEditingPassword) {
                                handleUpdate();
                            }
                            setIsEditingPassword(!isEditingPassword);
                        }}
                    >
                        {isEditingPassword ? "保存" : "パスワードを変更する"}
                    </button>
                </div>

            </div>

        </div>
    );
}