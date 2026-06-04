//動作確認用　宮田拓海
import React, { useEffect, useState } from "react";

const API_BASE = "http://localhost:8080/api";

const DailyList = () => {
    const userId = 1;
    const startDate = "2026-06-01";
    const endDate = "2026-06-07";

    const [dailyData, setDailyData] = useState(null);
    const [selectedDay, setSelectedDay] = useState(null);

    const [mode, setMode] = useState("view"); // view / edit
    const [editData, setEditData] = useState(null);

    // ■ 初期取得
    useEffect(() => {
        fetch(`${API_BASE}/daily/${userId}/${startDate}/${endDate}`)
            .then((res) => res.json())
            .then((data) => {
                console.log("API:", data);
                setDailyData(data);
            });
    }, []);

    // ■ 日付選択
    const handleSelectDay = (day) => {
        setSelectedDay(day);
        setMode("view");

        setEditData({
            dailyId: day.dailyId,
            date: day.date,
            summary: day.summary || "",
            contents: (day.contents || []).map((c) => ({
                categoryId: c.categoryId,
                content: c.content,
            })),
        });
    };

    // ■ 編集モード
    const switchToEdit = () => setMode("edit");
    const switchToView = () => setMode("view");

    // ■ 編集変更
    const handleChange = (index, field, value) => {
        const newContents = [...editData.contents];

        newContents[index][field] =
            field === "categoryId" ? Number(value) : value;

        setEditData({
            ...editData,
            contents: newContents,
        });
    };

    // ■ 更新（PUT）
    const handleUpdate = async () => {
        const res = await fetch(`${API_BASE}/update`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(editData),
        });

        const data = await res.json();
        console.log("更新:", data);

        alert("更新完了");
        setMode("view");
    };

    // ■ 削除（DELETE）
    const handleDelete = async () => {
        const confirmDelete = window.confirm("本当に削除しますか？");
        if (!confirmDelete) return;

        const res = await fetch(
            `${API_BASE}/delete/${editData.dailyId}`,
            {
                method: "DELETE",
            }
        );

        const data = await res.json();
        console.log("削除:", data);

        alert("削除完了");

        setEditData(null);
        setSelectedDay(null);
        setMode("view");

        // 再取得（任意）
        // window.location.reload();
    };

    return (
        <div style={{ display: "flex", padding: 20, gap: 20 }}>

            {/* ■ 左：一覧 */}
            <div style={{ width: "40%" }}>
                <h3>日報一覧</h3>

                {dailyData?.days?.map((day, index) => (
                    <div
                        key={index}
                        onClick={() => handleSelectDay(day)}
                        style={{
                            padding: 10,
                            marginBottom: 8,
                            border: "1px solid #ccc",
                            cursor: "pointer",
                            background:
                                selectedDay?.date === day.date
                                    ? "#eef"
                                    : "#fff",
                        }}
                    >
                        <strong>{day.date}</strong>
                        <div style={{ fontSize: 12 }}>
                            {day.summary}
                        </div>
                    </div>
                ))}
            </div>

            {/* ■ 右：詳細 */}
            <div style={{ width: "60%", borderLeft: "1px solid #ddd", paddingLeft: 20 }}>

                {!editData && <p>日付を選択してください</p>}

                {editData && mode === "view" && (
                    <>
                        <h3>詳細</h3>

                        <h4>{editData.date}</h4>

                        <p style={{ color: "#666" }}>
                            {editData.summary}
                        </p>

                        <hr />

                        <ul>
                            {editData.contents?.map((c, i) => (
                                <li key={i}>
                                    <strong>[{c.categoryId}]</strong>{" "}
                                    {c.content}
                                </li>
                            ))}
                        </ul>

                        <button onClick={switchToEdit}>
                            編集
                        </button>

                        <button
                            onClick={handleDelete}
                            style={{ marginLeft: 10, color: "red" }}
                        >
                            削除
                        </button>
                    </>
                )}

                {editData && mode === "edit" && (
                    <>
                        <h3>編集</h3>
                        <h4>{editData.date}</h4>

                        {editData.contents.map((c, i) => (
                            <div key={i} style={{ marginBottom: 10 }}>
                                <input
                                    type="number"
                                    value={c.categoryId}
                                    onChange={(e) =>
                                        handleChange(i, "categoryId", e.target.value)
                                    }
                                    style={{ width: 80 }}
                                />

                                <input
                                    type="text"
                                    value={c.content}
                                    onChange={(e) =>
                                        handleChange(i, "content", e.target.value)
                                    }
                                    style={{ marginLeft: 10, width: 300 }}
                                />
                            </div>
                        ))}

                        <button onClick={handleUpdate}>
                            更新（PUT）
                        </button>

                        <button
                            onClick={switchToView}
                            style={{ marginLeft: 10 }}
                        >
                            キャンセル
                        </button>
                    </>
                )}
            </div>
        </div>
    );
};

export default DailyList;
