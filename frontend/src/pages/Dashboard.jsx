//動作確認用　宮田拓海
import React, { useEffect, useState } from "react";

const API_BASE = "http://localhost:8080/api";

const Dashboard = () => {
    const [weeklyList, setWeeklyList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchWeeklyList = async () => {
            try {
                setLoading(true);
                setError(null);

                const res = await fetch(`${API_BASE}/weekly_list/1`);
                const data = await res.json();

                console.log("weekly API:", data);

                const list = data?.summaries ?? data?.list ?? [];

                setWeeklyList(Array.isArray(list) ? list : []);
            } catch (err) {
                console.error(err);
                setError("週リスト取得に失敗しました");
                setWeeklyList([]);
            } finally {
                setLoading(false);
            }
        };

        fetchWeeklyList();
    }, []);

    return (
        <div style={{ padding: 20 }}>
            <h2>ダッシュボード</h2>

            {loading && <p>読み込み中...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

            <h3>週リスト</h3>

            {!loading && weeklyList.length === 0 && (
                <p>データがありません</p>
            )}

            {Array.isArray(weeklyList) &&
                weeklyList.map((week, index) => (
                    <div
                        key={index}
                        style={{
                            padding: 12,
                            margin: 8,
                            border: "1px solid #ccc",
                            borderRadius: 6,
                        }}
                    >
                        {/* ■ 週の期間（日付表示） */}
                        <div style={{ fontWeight: "bold" }}>
                            {week.startDate} 〜 {week.endDate}
                        </div>

                        {/* ■ 要約 */}
                        <div style={{ marginTop: 6 }}>
                            {week.summary ?? week.content}
                        </div>
                    </div>
                ))}
        </div>
    );
};

export default Dashboard;