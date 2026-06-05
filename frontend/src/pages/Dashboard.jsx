import "../styles/Dashboard.css";
import "../styles/common.css";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
    const [weeks, setWeeks] = useState([]);
    const [reminder, setReminder] = useState(null);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const USER_ID = 1;

            const weekRes = await fetch(`/api/weekly-list/${USER_ID}`);
            const weekData = await weekRes.json();

            const reminderRes = await fetch(`/api/remind/${USER_ID}`);
            const reminderData = await reminderRes.json();

            setWeeks(weekData);
            setReminder(reminderData);

        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleWeekClick = (week) => {
        navigate(`/reports/week/${week.startDate}`);
    };

    const handleCreateReport = () => {
        navigate("/create-report");
    };

    if (loading) return <h2>Loading...</h2>;

    return (
        <div className="dashboardContainer">

            {/* ヘッダー */}
            <div className="header">

                <h1 className="dashboardTitle">ダッシュボード</h1>

                {/* 新規作成ボタン */}
                <button
                    className="addButton"
                    onClick={handleCreateReport}
                >
                    ＋新規日報作成
                </button>

                <button
                    className="primaryButton"
                    onClick={() => navigate("/remind")}
                >
                    🔔 リマインダー
                </button>

            </div>

            {/* リマインド */}
            <section className="section card">

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

                <h2>週一覧</h2>

                {weeks.map((week) => (
                    <div
                        key={week.startDate}
                        className="weekCard card"
                        onClick={() => handleWeekClick(week)}
                    >
                        <div className="weekTitle">
                            {week.startDate} ~ {week.endDate}
                        </div>

                        <div className="weekSummary">
                            {week.summary}
                        </div>
                    </div>
                ))}
            </section>

        </div>
    );
}