// Layout.jsx
import Header from "./Header";
import "../styles/common.css";

export default function Layout({ children }) {
    return (
        <div className="appWrapper">
            <Header />

            <main className="appContent">
                {children}
            </main>
        </div>
    );
}