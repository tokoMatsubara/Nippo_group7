import { useNavigate } from "react-router-dom";
import "../styles/Header.css";

export default function Header() {
    const navigate = useNavigate();

    // ユーザー名取得（なければnull）
    const userName = localStorage.getItem("user_name");

    // ログアウト処理
    const handleLogout = () => {
        localStorage.removeItem("user_id");
        localStorage.removeItem("user_name");
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

            {/* 右：ユーザー情報 */}
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