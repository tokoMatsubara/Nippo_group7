import { useState, useEffect } from "react";

export default function ThemeToggle() {
    const [theme, setTheme] = useState("blueTheme");

    // 初期化（ページリロード対応）
    useEffect(() => {
        const savedTheme = localStorage.getItem("theme") || "blueTheme";

        setTheme(savedTheme);

        document.body.classList.remove("blueTheme", "orangeTheme");
        document.body.classList.add(savedTheme);
    }, []);

    const toggleTheme = () => {
        const newTheme =
            theme === "blueTheme" ? "orangeTheme" : "blueTheme";

        setTheme(newTheme);

        document.body.classList.remove("blueTheme", "orangeTheme");
        document.body.classList.add(newTheme);

        localStorage.setItem("theme", newTheme);
    };

    return (
        <label className="switch">
            <input
                type="checkbox"
                checked={theme === "orangeTheme"}
                onChange={toggleTheme}
            />
            <span className="slider" />
        </label>
    );
}