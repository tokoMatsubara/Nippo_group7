import "../styles/Dashboard.css";
import blueBg from "../assets/blue.png";
import orangeBg from "../assets/orange.png";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logoIcon from "../assets/nippo_dash.png";

export default function Dashboard() {

    const [weeks, setWeeks] = useState([]);
    const [reminder, setReminder] = useState(null);
    const [remindIsRead, setRemindIsRead] = useState(true)
    const [loading, setLoading] = useState(true);

    const userId = localStorage.getItem("user_id");

    const userName = localStorage.getItem("user_name");
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
        const interval = setInterval(() => {
            fetchData();
        }, 60000);

    }, []);


    const fetchData = async () => {
        try {
            setLoading(true);
            // const USER_ID = localStorage.getItem("user_id");

            const weekRes = await fetch(
                `http://localhost:8080/api/weekly_list`, {
                method: "GET",
                credentials: "include"
            }
            );
            const weekData = await weekRes.json();

            const remindIsReadRes = await fetch(
                `http://localhost:8080/api/remind/is_read`, {
                method: "GET",
                credentials: "include"
            }
            );
            const remindIsReadData = await remindIsReadRes.json();

            // const reminderRes = await fetch(`/api/remind/${USER_ID}`);
            // const reminderData = await reminderRes.json();

            console.log(JSON.stringify(weekData, null, 2));
            console.log(JSON.stringify(remindIsReadData, null, 2));
            // 同じ startDate の重複を排除
            const uniqueWeeks = Array.from(
                new Map(weekData.summaries.map(w => [w.startDate, w])).values()
            );
            setWeeks(uniqueWeeks);
            setRemindIsRead(remindIsReadData.isRead);
            // setReminder(reminderData);

        } catch (err) {
            console.error(err);
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
                <div className="headerTitle">
                    <h1 className="title">ダッシュボード</h1>
                    <img src={logoIcon} alt="logo" className="logoIcon" />
                </div>
            </div>

            {/* リマインド */}
            <div className="body">

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