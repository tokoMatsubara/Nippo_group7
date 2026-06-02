import React, { useEffect, useState } from "react";

export default function Dashboard() {
    const [weeks, setWeeks] = useState([]);
    const [reminder, setReminder] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const USER_ID = 1; // 仮

            // ① 週一覧取得
            const weekRes = await fetch(`/api/weekly-list/${USER_ID}`);
            const weekData = await weekRes.json();

            // ② リマインド取得
            const reminderRes = await fetch(`/api/remind/${USER_ID}`);
            const reminderData = await reminderRes.json();

            setWeeks(weekData);
            setReminder(reminderData);

        } catch (err) {
            console.error("Dashboard error:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleWeekClick = (week) => {
        window.location.href = `/reports/week/${week.startDate}`;
    };

    const handleCreateReport = () => {
        window.location.href = "/reports/new";
    };

    if (loading) return <h2>Loading...</h2>;

    return (
        <div>

            <h1>ダッシュボード</h1>

            {/* リマインド */}
            <section>
                <h2>明日の目標と課題</h2>

                {reminder?.insights?.nextActions?.length ? (
                    <ul>
                        {reminder.insights.nextActions.map((item, i) => (
                            <li key={i}>{item}</li>
                        ))}
                    </ul>
                ) : (
                    <p>リマインドなし</p>
                )}
            </section>

            {/* ボタン */}
            <button onClick={handleCreateReport}>
                新規日報作成
            </button>

            {/* 週一覧 */}
            <section>
                <h2>週一覧</h2>

                {weeks.map((week) => (
                    <div
                        key={week.startDate}
                        onClick={() => handleWeekClick(week)}
                        style={{ cursor: "pointer", border: "1px solid #ccc", margin: "10px", padding: "10px" }}
                    >
                        <h3>
                            {week.startDate} ~ {week.endDate}
                        </h3>
                        <p>{week.summary}</p>
                    </div>
                ))}
            </section>

        </div>
    );
}