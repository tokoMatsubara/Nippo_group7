import { useNavigate } from "react-router-dom";
import "../styles/Header.css";

export default function Header() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("user_id");
        navigate("/login");
    };

    return (
        <header className="appHeader">

            {/* 左：アプリ名 */}
            <div
                className="logo"
                onClick={() => navigate("/dashboard")}
            >
                日報アプリ
            </div>

            {/* 右：ユーザー + ログアウト */}
            <div className="headerRight">

                <span className="userName">
                    ユーザー
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