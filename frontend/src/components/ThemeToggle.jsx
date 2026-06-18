import { useState, useEffect, useRef } from "react";


const themes = [
    "blueTheme",
    "orangeTheme",
    "greenTheme",
    "purpleTheme"
];

export default function ThemeToggle() {
    const [theme, setTheme] = useState(() => {
        return localStorage.getItem("theme");
    });

    const [open, setOpen] = useState(false);
    const ref = useRef(null);

    // UI操作だけ（副作用削除）
    const changeTheme = async (t) => {
        setTheme(t);
        localStorage.setItem("theme", t);
        document.body.classList.remove(...themes);
        document.body.classList.add(t);

        // バックエンドのDBにも保存するリクエスト
        try {
            const response = await fetch("/api/user/theme", {

                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ userTheme: t.replace("Theme", "").toLowerCase() }),
                credentials: "include"
            });

            if (!response.ok) {
                throw new Error("サーバーエラーが発生しました");
            }

            const data = await response.json();
            console.log("DB更新成功:", data.message);
        } catch (error) {
            console.error("テーマのDB更新に失敗しました:", error);
        }


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
                テーマ
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