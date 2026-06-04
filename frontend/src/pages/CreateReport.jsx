//動作確認用　宮田拓海
import React, { useState } from "react";

const API_BASE = "http://localhost:8080/api";

const CreateReport = () => {
  const [date, setDate] = useState("2026-04-01");

  const [contents, setContents] = useState([
    { categoryId: 1, content: "" },
  ]);

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  // ■ 内容変更
  const handleChangeContent = (index, field, value) => {
    const newContents = [...contents];
    newContents[index][field] = value;
    setContents(newContents);
  };

  // ■ 行追加
  const addRow = () => {
    setContents([...contents, { categoryId: 1, content: "" }]);
  };

  // ■ 送信
  const handleSubmit = async () => {
    try {
      setLoading(true);

      const payload = {
        userId: 1,
        date: date,
        contents: contents
      };

      console.log("送信データ:", payload);

      const res = await fetch(`${API_BASE}/report`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      const data = await res.json();
      console.log("レスポンス:", data);

      setResult(data);

    } catch (err) {
      console.error(err);
      setResult({ status: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>日報登録フォーム</h2>

      {/* 日付 */}
      <div>
        <label>日付：</label>
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />
      </div>

      <hr />

      {/* 入力リスト */}
      <h3>内容</h3>

      {contents.map((item, index) => (
        <div key={index} style={{ marginBottom: 10 }}>
          <input
            type="number"
            placeholder="カテゴリID"
            value={item.categoryId}
            onChange={(e) =>
              handleChangeContent(index, "categoryId", Number(e.target.value))
            }
            style={{ width: 120 }}
          />

          <input
            type="text"
            placeholder="内容"
            value={item.content}
            onChange={(e) =>
              handleChangeContent(index, "content", e.target.value)
            }
            style={{ marginLeft: 10, width: 300 }}
          />
        </div>
      ))}

      <button onClick={addRow}>＋追加</button>

      <Section
        title="1. 今日学んだこと"
        name="learned"
        value={form.learned}
        onChange={handleChange}
      />

      <Section
        title="2. よかった点・できたこと"
        name="goodPoint"
        value={form.goodPoint}
        onChange={handleChange}
      />

      <Section
        title="3. その理由"
        name="goodReason"
        value={form.goodReason}
        onChange={handleChange}
      />

      <Section
        title="4. 課題・改善点"
        name="issue"
        value={form.issue}
        onChange={handleChange}
      />

      <Section
        title="5. その理由"
        name="issueReason"
        value={form.issueReason}
        onChange={handleChange}
      />

      <Section
        title="6. 改善するための行動"
        name="action"
        value={form.action}
        onChange={handleChange}
      />

      <Section
        title="7. 明日の目標"
        name="tomorrowGoal"
        value={form.tomorrowGoal}
        onChange={handleChange}
      />

      <div className="section-card">
        <h3>8. 体調・気持ち</h3>

        <div className="radio-group">

          <label>
            <input
              type="radio"
              name="condition"
              value="良好"
              checked={form.condition === "良好"}
              onChange={handleChange}
            />
            良好
          </label>

          <label>
            <input
              type="radio"
              name="condition"
              value="普通"
              checked={form.condition === "普通"}
              onChange={handleChange}
            />
            普通
          </label>

          <label>
            <input
              type="radio"
              name="condition"
              value="不調"
              checked={form.condition === "不調"}
              onChange={handleChange}
            />
            不調
          </label>

        </div>
      </div>

      {/* 送信 */}
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? "送信中..." : "登録"}
      </button>

      {/* 結果 */}
      <pre style={{ marginTop: 20 }}>
        {JSON.stringify(result, null, 2)}
      </pre>
    </div>
  );
};

export default CreateReport;