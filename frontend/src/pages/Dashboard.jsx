import "../styles/Dashboard.css";
import blueBg from "../assets/blue.png";
import orangeBg from "../assets/orange.png";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {

    const [weeks, setWeeks] = useState([]);
    const [reminder, setReminder] = useState(null);
    const [remindIsRead, setRemindIsRead] = useState(true)
    const [loading, setLoading] = useState(true);

    const userId = localStorage.getItem("user_id");

    const userName = localStorage.getItem("user_name");
    const navigate = useNavigate();

    useEffect(() => {
        fetchWeeklyData();
        fetchRemindData();
        const interval = setInterval(() => {
            fetchWeeklyData();
            fetchRemindData();
        }, 60000);

        return () => clearInterval(interval);

    }, []);

    const fetchWeeklyData = async () => {
        try{
            setLoading(true);
            const res = await fetch(
                `http://localhost:8080/api/weekly_list`, {
                method: "GET",
                credentials: "include"
            });

            if (!res.ok) {
                const error = new Error(`HTTP ${res.status}`);
                error.status = res.status;   // ← これを足すのが肝
                throw error;
            }

            const weekData = await res.json();
            console.log(JSON.stringify(weekData, null, 2));

            // 同じ startDate の重複を排除
            const uniqueWeeks = Array.from(
                new Map(weekData.summaries.map(w => [w.startDate, w])).values()
            );
            setWeeks(uniqueWeeks);

        }catch(err){
            console.error(err);
            if(err.status === 401){
                console.error("401認証エラー");
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            }else{
                console.error("Failed to data");
                alert('データの取得に失敗しました');
            }

        }finally{
            setLoading(false);
        }
    }

    const fetchRemindData = async () => {
        try {
            setLoading(true);
            const res = await fetch(
                `http://localhost:8080/api/remind/is_read`, {
                method: "GET",
                credentials: "include"
            });

            if (!res.ok) {
                const error = new Error(`HTTP ${res.status}`);
                error.status = res.status;   // ← これを足すのが肝
                throw error;
            }

            const remindIsReadData = await res.json();


            console.log(JSON.stringify(remindIsReadData, null, 2));
            setRemindIsRead(remindIsReadData.isRead);

        } catch (err) {
            console.error(err);
            if(err.status === 401){
                console.error("401認証エラー");
                alert("認証エラーです。ログインしなおしてください");
                navigate("/login");
            }else{
                console.error("Failed to data");
                alert('データの取得に失敗しました');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleWeekClick = (week) => {
        navigate(`/daily-list/${week.startDate}/${week.endDate}`);
    };

    const handleCreateReport = () => {
        navigate("/create-report");
    };

    const formatDate = (dateStr) => {
        const date = new Date(dateStr);

        const month = date.getMonth() + 1;
        const day = date.getDate();

        return `${month}月${day}日`;
    };

    if (loading) return <h2>Loading...</h2>;

    return (

        <>
            {/* ヘッダー */}
            <div className="header">

                <h1 className="title">ダッシュボード</h1>

            </div>

            {/* リマインド */}
            <div className="body">

                <section className="card">

                    <h2 className="remindTitle">明日の目標と課題</h2>

                    {reminder?.insights?.nextActions?.length ? (
                        <ul className="list">
                            {reminder.insights.nextActions.map((item, i) => (
                                <li key={i}>{item}</li>
                            ))}
                        </ul>
                    ) : (
                        <p>データなし</p>
                    )}
                </section>

                {/* 週一覧 */}
                <section className="section">

                    <h2 className="weekTitle">週一覧</h2>

                    {weeks.map((week) => (
                        <div
                            key={week.startDate}
                            className="button"
                            onClick={() => handleWeekClick(week)}
                        >
                            <div className="weekContent">

                                <div className="weekTitle">
                                    {formatDate(week.startDate)} ~ {formatDate(week.endDate)}
                                </div>
                                <h4 className="summaryTitle">週次要約</h4>
                                <div className="weekSummary">
                                    {week.content}
                                </div>
                            </div>
                            <div className="weekArrow">
                                ›
                            </div>

                        </div>
                    ))}
                </section>
            </div>

        </>
    );
}