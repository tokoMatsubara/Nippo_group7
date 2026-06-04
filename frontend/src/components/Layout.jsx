import Header from "./Header";
import "../styles/layout.css";

export default function Layout({ children }) {
    return (
        <div className="appLayout">
            <Header />
            <main className="appMain">
                {children}
            </main>
        </div>
    );
}