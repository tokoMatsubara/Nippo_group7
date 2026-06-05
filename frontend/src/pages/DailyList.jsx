import "../styles/DailyList.css";

import React, { useState,useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";



const mockWeekData = {
    week_start_date: "2026-06-01",
    week_end_date: "2026-06-05",
    weekly_summary_content: "今週はReactと設計を学習しました(まだMockです)",
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
    const navigate = useNavigate();
    const params = useParams();

    // const weekData = mockWeekData;
    const [weekData, setWeekData] = useState({});

    useEffect(() => {
        fetchData();
    }, []);

    // BackendへのAPI
    const fetchData = async () => {
        const userId = localStorage.getItem("user_id");
        
        const response = await fetch(`api/daily/userId/params.startDate/params.endDate`);
        const data = await response.json();
        setWeekData(data);
        console.log(data);
    }

    const selectedDaily = weekData.dailies.find(
        (d) => d.day === selectedDay
    );

    const hasDaily = (day) =>
        weekData.dailies.some((d) => d.day === day);

    return (
        <div className="container">
            <div className="header">
                <h1 className="title">日報一覧</h1>

                <button
                    className="backButton"
                    onClick={() => navigate("/dashboard")}
                >
                    ダッシュボードへ戻る
                </button>
            </div>

            {/* 週表示 */}
            <h2 className="title">
                {dailies.weekStartDate} ～ {dailies.weekEndDate}
            </h2>

            {/* 週要約 */}
            <p className="weekSummary">
                {/** まだMockBackend変える必要性あり */}
                {/* {weekData.weekly_summary_content} */}
                要約です
            </p>

            {/* 曜日 */}
            <div className="dayButtons">
                {days.map((day) => (
                    <button
                        key={day}
                        className={`dayButton ${selectedDay === day ? "active" : ""}`}
                        onClick={() => setSelectedDay(day)}
                    >
                        {day} {hasDaily(day) ? "●" : "○"}
                    </button>
                ))}
            </div>

            {/* カード */}
            <div className="card">

                {!selectedDaily ? (
                    <p>日報はありません</p>
                ) : (
                    <>
                        <h3 className="date">{selectedDaily.date}</h3>

                        <p className="summary">
                            {selectedDaily.daily_summary_content}
                        </p>


                        {/* 詳細 */}
                        <div className="section">
                            {selectedDaily.details.map((item) => (
                                <div key={item.categoryId} className="sectionItem">
                                    <strong className="sectionTitle">
                                        {item.categoryName}
                                    </strong>

                                    <p className="sectionValue">
                                        {item.content}
                                    </p>
                                </div>
                            ))}
                        </div>

                        {/* 共通ボタン化 */}
                        <div className="actions">
                            <button className="primaryButton">
                                編集
                            </button>

                            <button className="dangerButton">
                                削除
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}