import { useNavigate } from "react-router-dom";
import "../styles/Header.css";

export default function Header() {
    const navigate = useNavigate();

    const userId = localStorage.getItem("user_id");

    const userName = userId ? "ユーザー" : "ゲスト";

    const handleLogout = () => {
        localStorage.removeItem("user_id");
        navigate("/login");
    };

    return (
        <header className="appHeader">

            <div
                className="logo"
                onClick={() => navigate("/dashboard")}
            >
                日報アプリ
            </div>

            <div className="headerRight">

                <span className="userName">
                    {userName}
                </span>

                <button
                    className="logoutBtn"
                    onClick={handleLogout}
                >
                    ログアウト
                </button>

            </div>

        </header>
    );
}