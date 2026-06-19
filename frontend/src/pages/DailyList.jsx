import "../styles/DailyList.css";

import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

const days = ["月", "火", "水", "木", "金"];
const week = ["日", "月", "火", "水", "木", "金", "土"];

export default function DailyList() {
    const [selectedDay, setSelectedDay] = useState("月");
    const navigate = useNavigate();
    const params = useParams();

    const [weekData, setWeekData] = useState({
        weekStartDate: "",
        weekEndDate: "",
        days: []
    });

    useEffect(() => {
        fetchData();
        const interval = setInterval(fetchData, 60000);
        return () => clearInterval(interval);
    }, []);

    const fetchData = async () => {
        try {
            const res = await fetch(
                `http://localhost:8080/api/daily/${params.startDate}/${params.endDate}`,
                { method: "GET", credentials: "include" }
            );

            if (!res.ok) {
                const error = new Error(`HTTP ${res.status}`);
                error.status = res.status;
                throw error;
            }

            const data = await res.json();
            setWeekData(data);
        } catch (err) {
            if (err.status === 401) {
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            } else {
                alert("日報取得に失敗しました");
            }
        }
    };

    const selectedDaily = weekData.days.find(
        (d) => week[new Date(d.date).getDay()] === selectedDay
    );

    const hasDaily = (day) =>
        weekData.days.some(
            (d) => week[new Date(d.date).getDay()] === day
        );

    const getSelectedDate = () => {
        if (!weekData.weekStartDate) return null;
        const startDate = new Date(weekData.weekStartDate);
        const dayIndex = days.indexOf(selectedDay);
        const selectedDate = new Date(startDate);
        selectedDate.setDate(startDate.getDate() + dayIndex);
        return selectedDate.toISOString().split("T")[0];
    };

    const handleDelete = async () => {
        if (!selectedDaily) return;
        if (!window.confirm("本当にこの日報を削除しますか？")) return;

        try {
            const res = await fetch(
                `http://localhost:8080/api/delete/${selectedDaily.dailyId}`,
                { method: "DELETE", credentials: "include" }
            );

            if (!res.ok) throw new Error(`HTTP ${res.status}`);

            alert("削除しました");
            fetchData();
        } catch (err) {
            alert("削除に失敗しました");
        }
    };

    const handleCopy = async () => {
        if (!selectedDaily) return;

        const sorted = selectedDaily.contents
            .slice()
            .sort((a, b) => a.categoryId - b.categoryId);

        const text = sorted
            .map((i) => `【${i.categoryName}】\n${i.content}`)
            .join("\n\n");

        try {
            await navigator.clipboard.writeText(text);
            alert("コピーしました");
        } catch {
            alert("コピー失敗");
        }
    };

    const formatDate = (dateStr) => {
        const d = new Date(dateStr);
        return `${d.getMonth() + 1}月${d.getDate()}日`;
    };

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return (
        <div className="pageContainer">

            <div className="header">
                <h1 className="title">
                    Daily List（{formatDate(weekData.weekStartDate)} ～ {formatDate(weekData.weekEndDate)}）
                </h1>
            </div>

            <div className="pageContent">
                <div className="card">

                    {/* 週ボタン + コピー（見た目維持版） */}
                    <div style={{ display: "flex", alignItems: "center" }}>

                        <div className="dayButtons">
                            {days.map((day, index) => {
                                const date = new Date(weekData.weekStartDate);
                                date.setDate(date.getDate() + index);
                                date.setHours(0, 0, 0, 0);

                                const isToday = today.getTime() === date.getTime();
                                const isFuture = date.getTime() > today.getTime();

                                return (
                                    <button
                                        key={day}
                                        className={`dayButton 
                                            ${selectedDay === day ? "active" : ""} 
                                            ${isToday ? "today" : ""} 
                                            ${isFuture ? "future" : ""}`}
                                        onClick={() => {
                                            if (!isFuture) setSelectedDay(day);
                                        }}
                                    >
                                        <span className="dayTop">
                                            {date.getDate()} ({day})
                                        </span>

                                        <span className="dayMark">
                                            {hasDaily(day) ? "●" : "○"}
                                        </span>
                                    </button>
                                );
                            })}
                        </div>

                        {selectedDaily && (
                            <button className="iconButton copyButton" onClick={handleCopy}>
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="18"
                                    height="18"
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    strokeWidth="2"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                >
                                    <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
                                    <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
                                </svg>
                            </button>
                        )}

                    </div>

                    {/* 内容 */}
                    {!selectedDaily ? (
                        <>
                            <h3>{formatDate(getSelectedDate())}</h3>
                            <p>日報はありません</p>

                            <div style={{ textAlign: "center" }}>
                                <button
                                    className="primaryButton"
                                    onClick={() =>
                                        navigate("/create-report", {
                                            state: {
                                                date: getSelectedDate(),
                                                returnPath: `/daily-list/${params.startDate}/${params.endDate}`,
                                            },
                                        })
                                    }
                                >
                                    新規作成
                                </button>
                            </div>
                        </>
                    ) : (
                        <>
                            <h3>{formatDate(selectedDaily.date)}</h3>

                            <div className="section">
                                <div className="sectionItem">
                                    <div className="sectionTitle">
                                        要約
                                        <div className="sectionValue">
                                            {selectedDaily.summary}
                                        </div>
                                    </div>
                                </div>

                                {selectedDaily.contents
                                    .slice()
                                    .sort((a, b) => a.categoryId - b.categoryId)
                                    .map((item) => (
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
                        </>
                    )}
                </div>
            </div>

            {/* フッター */}
            <div className="footer-actions">
                {selectedDaily && (
                    <div className="footer-inner">

                        <div className="footer-center">
                            <button
                                className="primaryButton"
                                onClick={() =>
                                    navigate("/create-report", {
                                        state: {
                                            daily: selectedDaily,
                                            returnPath: `/daily-list/${params.startDate}/${params.endDate}`,
                                        },
                                    })
                                }
                            >
                                編集
                            </button>

                            <button
                                className="dangerButton"
                                onClick={handleDelete}
                            >
                                削除
                            </button>
                        </div>

                    </div>
                )}
            </div>

        </div>
    );
}