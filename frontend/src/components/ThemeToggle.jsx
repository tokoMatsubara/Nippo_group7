import { useState, useEffect, useRef } from "react";

const themes = [
    "blueTheme",
    "orangeTheme",
    "greenTheme",
    "purpleTheme"
];

export default function ThemeToggle() {
    const [theme, setTheme] = useState("blueTheme");
    const [open, setOpen] = useState(false);
    const ref = useRef(null);

    // 初期化
    useEffect(() => {
        const savedTheme = localStorage.getItem("theme") || "blueTheme";
        setTheme(savedTheme);
    }, []);

    // テーマ適用
    useEffect(() => {
        document.body.classList.remove(...themes);
        document.body.classList.add(theme);
        localStorage.setItem("theme", theme);
    }, [theme]);

    // 外側クリックで閉じる
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (ref.current && !ref.current.contains(e.target)) {
                setOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <div className="themeWrapper" ref={ref}>
            {/* ボタン */}
            <button
                className="themeButton"
                onClick={() => setOpen(!open)}
            >
                Theme
            </button>

            {/* ドロップダウン */}
            {open && (
                <div className="themeDropdown">
                    {themes.map((t) => (
                        <div
                            key={t}
                            className={`themeItem ${theme === t ? "active" : ""}`}
                            onClick={() => {
                                setTheme(t);
                                setOpen(false);
                            }}
                        >
                            {t.replace("Theme", "")}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}