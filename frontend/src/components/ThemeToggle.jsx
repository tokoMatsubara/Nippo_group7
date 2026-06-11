import { useState, useEffect, useRef } from "react";

const themes = [
    "blueTheme",
    "orangeTheme",
    "greenTheme",
    "purpleTheme"
];

export default function ThemeToggle() {
    const [theme, setTheme] = useState(() => {
        return localStorage.getItem("theme") || "blueTheme";
    });

    const [open, setOpen] = useState(false);
    const ref = useRef(null);

    // UI操作だけ（副作用削除）
    const changeTheme = (t) => {
        setTheme(t);
        localStorage.setItem("theme", t);
        document.body.classList.remove(...themes);
        document.body.classList.add(t);
    };

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
            <button
                className="themeButton"
                onClick={() => setOpen(!open)}
            >
                Theme
            </button>

            {open && (
                <div className="themeDropdown">
                    {themes.map((t) => (
                        <div
                            key={t}
                            className={`themeItem ${theme === t ? "active" : ""}`}
                            onClick={() => {
                                changeTheme(t);
                                setOpen(false);
                            }}
                            style={{
                                color: `var(--${t.replace("Theme", "").toLowerCase()}-primary2)`
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