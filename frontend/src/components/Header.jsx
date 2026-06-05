import { useNavigate } from "react-router-dom";
import "../styles/Header.css";

export default function Header() {
    const navigate = useNavigate();

    const userId = localStorage.getItem("user_id");

    const userName = localStorage.getItem("user_name");

    const handleLogout = () => {
        localStorage.removeItem("user_id");
        localStorage.removeItem("user_name");
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
                    {userName || "ゲスト"}
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