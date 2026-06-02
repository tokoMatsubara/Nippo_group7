//　今藤

import React, { useState } from "react";

const mockWeekData = {
    week_start_date: "2026-06-01",
    week_end_date: "2026-06-05",
    weekly_summary_content: "今週はReactと設計を学習しました",
    dailies: [
        {
            day: "月",
            date: "2026-06-01",
            daily_summary_content: "React基礎",
            details: [
                { category_id: 1, category_name: "今日学んだこと", content: "JSXの基本" },
                { category_id: 2, category_name: "よかった点", content: "理解が早かった" },
                { category_id: 3, category_name: "その理由", content: "事前学習していたから" },
            ],
        },
        {
            day: "火",
            date: "2026-06-02",
            daily_summary_content: "コンポーネント設計",
            details: [
                { category_id: 1, category_name: "今日学んだこと", content: "propsの使い方" },
                { category_id: 2, category_name: "課題・改善点", content: "状態管理が難しい" },
            ],
        },
    ],
};

const days = ["月", "火", "水", "木", "金"];

export default function DailyList() {
    const [selectedDay, setSelectedDay] = useState("月");

    const weekData = mockWeekData;

    const selectedDaily = weekData.dailies.find(
        (d) => d.day === selectedDay
    );

    const hasDaily = (day) =>
        weekData.dailies.some((d) => d.day === day);

    return (
        <div style={{ padding: "20px", fontFamily: "sans-serif" }}>

            {/* ① 週表示 */}
            <h2>
                {weekData.week_start_date} ～ {weekData.week_end_date}
            </h2>

            {/* ② 週要約 */}
            <p style={{ color: "#555" }}>
                {weekData.weekly_summary_content}
            </p>

            {/* ③ 曜日ボタン */}
            <div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
                {days.map((day) => (
                    <button
                        key={day}
                        onClick={() => setSelectedDay(day)}
                        style={{
                            padding: "6px 12px",
                            background: selectedDay === day ? "#007bff" : "#eee",
                            color: selectedDay === day ? "#fff" : "#000",
                            border: "none",
                            borderRadius: "4px",
                        }}
                    >
                        {day} {hasDaily(day) ? "●" : "○"}
                    </button>
                ))}
            </div>

            {/* ④ 日報ヘッダー */}
            <div style={{ marginBottom: "20px" }}>
                {!selectedDaily ? (
                    <p>日報はありません</p>
                ) : (
                    <>
                        <h3>{selectedDaily.date}</h3>
                        <p style={{ color: "#666" }}>
                            {selectedDaily.daily_summary_content}
                        </p>

                        <div style={{ display: "flex", gap: "10px" }}>
                            <button>編集</button>
                            <button style={{ color: "red" }}>削除</button>
                        </div>

                        {/* ⑤ 詳細 */}
                        <div style={{ marginTop: "20px" }}>
                            {selectedDaily.details.map((item) => (
                                <div key={item.category_id} style={{ marginBottom: "12px" }}>
                                    <strong>{item.category_name}</strong>
                                    <p style={{ margin: "4px 0" }}>{item.content}</p>
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}