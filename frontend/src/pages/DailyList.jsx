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
        // const userId = localStorage.getItem("user_id");

        const response = await fetch(`http://localhost:8080/api/daily/${params.startDate}/${params.endDate}`, {
            method: "GET",
            credentials: "include"
        });
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

    // 選択された曜日の日付を計算
    const getSelectedDate = () => {
        if (!weekData.weekStartDate) return null;
        const startDate = new Date(weekData.weekStartDate);
        const dayIndex = days.indexOf(selectedDay);
        const selectedDate = new Date(startDate);
        selectedDate.setDate(startDate.getDate() + dayIndex);
        return selectedDate.toISOString().split('T')[0];
    };

    const handleDelete = async () => {
        if (!selectedDaily) return;
        const ok = window.confirm("本当にこの日報を削除しますか？");
        if (!ok) return;

        try {
            const res = await fetch(`http://localhost:8080/api/delete/${selectedDaily.dailyId}`, {
                method: 'DELETE',
                credentials: "include"
            });

            if (res.ok) {
                alert('削除しました');
                await fetchData();
            } else {
                const err = await res.json().catch(() => ({}));
                console.error('delete error', err);
                alert('削除に失敗しました');
                alert('ログインしなおして下さい');
                navigate("/login");
            }
        } catch (e) {
            console.error(e);
            alert('削除に失敗しました');
        }
    };

    const formatDate = (dateStr) => {
        const date = new Date(dateStr);

        const month = date.getMonth() + 1;
        const day = date.getDate();

        return `${month}月${day}日`;
    };

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return (
        <>
            <div className="header">
                <h1 className="title">日報一覧（{formatDate(weekData.weekStartDate)} ～ {formatDate(weekData.weekEndDate)}）</h1>

                {/* 週表示 */}
                {/* <h2 className="title">
                    {formatDate(weekData.weekStartDate)} ～ {formatDate(weekData.weekEndDate)}
                </h2> */}

                {/* 曜日 */}
                <div className="dayButtons">
                    {days.map((day, index) => {
                        const date = new Date(weekData.weekStartDate);
                        date.setDate(date.getDate() + index);
                        date.setHours(0, 0, 0, 0);

                        const dayNumber = date.getDate();
                        const dayLabel = day;

                        const isToday =
                            today.getTime() === date.getTime();
                        const isFuture =
                            date.getTime() > today.getTime();

                        return (
                            <button
                                key={day}
                                className={`dayButton 
                            ${selectedDay === day ? "active" : ""} 
                            ${isToday ? "today" : ""} 
                            ${isFuture ? "future" : ""}`}
                                onClick={() => {
                                    if (isFuture) return;   // ← ここで無効化
                                    setSelectedDay(day);
                                }}
                            >
                                <span className="dayTop">
                                    {dayNumber} ({dayLabel})
                                </span>

                                <span className="dayMark">
                                    {hasDaily(day) ? "●" : "○"}
                                </span>
                            </button>
                        );
                    })}
                </div>
            </div>

            {/* カード */}
            <div className="card">

                {!selectedDaily ? (
                    <>
                        <h3>{getSelectedDate()}</h3>
                        <p>日報はありません</p>
                        <div className="actions" style={{ justifyContent: 'center' }}>
                            <button
                                className="primaryButton"
                                onClick={() => navigate("/create-report", {
                                    state: {
                                        date: getSelectedDate(),
                                        returnPath: `/daily-list/${params.startDate}/${params.endDate}`,
                                    },
                                })}
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
                                <div className="sectionTitle">要約
                                    <div className="sectionValue">
                                        {selectedDaily.summary}
                                    </div>
                                </div>
                            </div>


                            {/* 詳細 */}
                            {selectedDaily.contents.map((item, index) => (
                                <div key={index} className="sectionItem">
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
        </>
    );
}