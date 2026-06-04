import React from "react";
import "../styles/common.css";
import Header from "./Header";

export default function Layout({ children }) {
    return (
        <div>
            {/* 共通ヘッダー */}
            <Header />

            {/* 画面本体 */}
            <div className="page-container">
                {children}
            </div>
        </div>
    );
}