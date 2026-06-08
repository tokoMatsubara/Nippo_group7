import { useNavigate, NavLink } from "react-router-dom";
import logoIcon from "../assets/a_clean_flat_modern_vector_icon_on_a_white_backgro.png";
import "../styles/Header.css";

export default function Header() {
    const navigate = useNavigate();

    const userName = localStorage.getItem("user_name");

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
                <NavLink to="/remind">リマインド</NavLink>

                {/* 日付付きルートは固定リンクにしにくいので例だけ */}
                {/* 必要ならボタン遷移にする */}
                {/* <NavLink to="/daily-list/2026-01-01/2026-01-31">一覧</NavLink> */}
            </nav>

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