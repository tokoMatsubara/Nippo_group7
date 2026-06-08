import "../styles/DailyList.css";

import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

const days = ["月", "火", "水", "木", "金"];
const week = ["日", "月", "火", "水", "木", "金", "土"];

export default function DailyList() {
    const [selectedDay, setSelectedDay] = useState("月");
    const navigate = useNavigate();
    const params = useParams();

    // const weekData = mockWeekData;
    const [weekData, setWeekData] = useState({
        weekStartDate: "",
        weekEndDate: "",
        days: []
    });

    useEffect(() => {
        fetchData();
    }, []);

    // BackendへのAPI
    const fetchData = async () => {
        const userId = localStorage.getItem("user_id");

        const response = await fetch(`http://localhost:8080/api/daily/${userId}/${params.startDate}/${params.endDate}`);
        const data = await response.json();
        setWeekData(data);
        console.log(data);
    }

    const selectedDaily = weekData.days.find(
        (d) => week[new Date(d.date).getDay()] === selectedDay
    );

    const hasDaily = (day) =>
        weekData.days.some(
            (d) => week[new Date(d.date).getDay()] === day
        );
    const handleDelete = async () => {
        if (!selectedDaily) return;
        const ok = window.confirm("本当にこの日報を削除しますか？");
        if (!ok) return;

        try {
            const res = await fetch(`http://localhost:8080/api/delete/${selectedDaily.dailyId}`, {
                method: 'DELETE',
            });

            if (res.ok) {
                alert('削除しました');
                await fetchData();
            } else {
                const err = await res.json().catch(() => ({}));
                console.error('delete error', err);
                alert('削除に失敗しました');
            }
        } catch (e) {
            console.error(e);
            alert('削除に失敗しました');
        }
    };

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
                {weekData.weekStartDate} ～ {weekData.weekEndDate}
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
                            {selectedDaily.summary}
                        </p>


                        {/* 詳細 */}
                        <div className="section">
                            {selectedDaily.contents.map((item) => (
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
                            <button
                                className="primaryButton"
                                onClick={() => navigate("/create-report", {
                                    state: {
                                        daily: selectedDaily,
                                        returnPath: `/daily-list/${params.startDate}/${params.endDate}`,
                                    },
                                })}
                            >
                                編集
                            </button>

                            <button className="dangerButton" onClick={handleDelete}>
                                削除
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}