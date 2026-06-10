import { useNavigate, NavLink } from "react-router-dom";
import React, { useEffect, useState } from "react";
import "../styles/Header.css";
import ThemeToggle from "./ThemeToggle";
import logoIcon from "../assets/a_clean_flat_modern_vector_icon_on_a_white_backgro.png";
import { useLocation } from "react-router-dom";
export default function Header() {
    const navigate = useNavigate();
    const [remindIsRead, setRemindIsRead] = useState(true);
    const location = useLocation();

    const userName = localStorage.getItem("user_name");

    useEffect(() => {
        fetchData();
    }, [location.pathname]);

    const fetchData = async () => {
        try {
            const userId = localStorage.getItem("user_id");

            const remindIsReadRes = await fetch(
                `http://localhost:8080/api/remind/is_read/${userId}`
            );
            const remindIsReadData = await remindIsReadRes.json();
            console.log(JSON.stringify(remindIsReadData, null, 2));

            setRemindIsRead(remindIsReadData.isRead);

        } catch (err) {
            console.error(err);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("user_id");
        localStorage.removeItem("user_name");
        navigate("/login");
    };

    return (
        <header className="appHeader">

            <div className="logo" onClick={() => navigate("/dashboard")}>

                <img src={logoIcon} alt="logo" className="logoIcon" />

                日報アプリ
            </div>

            <nav className="navLinks">
                <NavLink to="/dashboard">ダッシュボード</NavLink>
                <NavLink to="/create-report">日報作成</NavLink>
                <NavLink to="/remind" className="navItemWithBadge">
                    リマインド

                    {!remindIsRead && (
                        <span className="redDot" />
                    )}
                </NavLink>

                {/* 日付付きルートは固定リンクにしにくいので例だけ */}
                {/* 必要ならボタン遷移にする */}
                {/* <NavLink to="/daily-list/2026-01-01/2026-01-31">一覧</NavLink> */}
            </nav>
            <ThemeToggle />

            <div className="headerRight">
                <span className="userName">
                    {userName || "ゲスト"}
                </span>

                <button className="logoutBtn" onClick={handleLogout}>
                    ログアウト
                </button>
            </div>

        </header>
    );
}