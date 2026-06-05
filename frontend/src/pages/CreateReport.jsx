import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/CreateReport.css";

const API_BASE = "http://localhost:8080/api";

function CreateReport() {
  const navigate = useNavigate();
  const [date, setDate] = useState("2026-04-01");
  const [contents, setContents] = useState([{ categoryId: 1, content: "" }]);
  const [form, setForm] = useState({
    learned: "",
    goodPoint: "",
    goodReason: "",
    issue: "",
    issueReason: "",
    action: "",
    tomorrowGoal: "",
    condition: "普通",
    comment: "",
  });
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const yesterdayGoal = "昨日設定した目標がここに表示されます";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleChangeContent = (index, field, value) => {
    const newContents = [...contents];
    newContents[index][field] = value;
    setContents(newContents);
  };

  const addRow = () => {
    setContents([...contents, { categoryId: 1, content: "" }]);
  };

  const handleSubmit = async () => {
    try {
      setLoading(true);
      const payload = { userId: 1, date, contents, report: form };
      const res = await fetch(`${API_BASE}/report`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      const data = await res.json();
      setResult(data);
    } catch (err) {
      console.error(err);
      setResult({ status: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="daily-page">
      <header className="header">
        <div>
          <h1 className="title">{date} の日報</h1>
        </div>
        <div className="header-actions">
          <button className="backButton" onClick={() => navigate("/dashboard")}>ダッシュボードへ戻る</button>
        </div>
      </header>

      <div className="card">
        {/* <div className="section-card card">
          <h2>日付</h2>
          <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
        </div> */}

        {/* <div className="section-card card">
          <h2>日報内容</h2>
          {contents.map((item, index) => (
            <div key={index} style={{ marginBottom: 10 }}>
              <input
                type="number"
                placeholder="カテゴリID"
                value={item.categoryId}
                onChange={(e) => handleChangeContent(index, "categoryId", Number(e.target.value))}
                style={{ width: 120 }}
              />
              <input
                type="text"
                placeholder="内容"
                value={item.content}
                onChange={(e) => handleChangeContent(index, "content", e.target.value)}
                style={{ marginLeft: 10, width: 300 }}
              />
            </div>
          ))}
          <button className="addButton" onClick={addRow}>＋追加</button>
        </div> */}

        <div className="goal-card card">
          <h3>昨日立てた今日の目標</h3>
          <p>{yesterdayGoal}</p>
        </div>

        <Section title="1. 今日学んだこと" name="learned" value={form.learned} onChange={handleChange} />
        <Section title="2. よかった点・できたこと" name="goodPoint" value={form.goodPoint} onChange={handleChange} />
        <Section title="3. その理由" name="goodReason" value={form.goodReason} onChange={handleChange} />
        <Section title="4. 課題・改善点" name="issue" value={form.issue} onChange={handleChange} />
        <Section title="5. その理由" name="issueReason" value={form.issueReason} onChange={handleChange} />
        <Section title="6. 改善するための行動" name="action" value={form.action} onChange={handleChange} />
        <Section title="7. 明日の目標" name="tomorrowGoal" value={form.tomorrowGoal} onChange={handleChange} />

        <div className="section-card card">
          <h3>8. 体調・気持ち</h3>
          <div className="radio-group">
            <label>
              <input type="radio" name="condition" value="良好" checked={form.condition === "良好"} onChange={handleChange} /> 良好
            </label>
            <label>
              <input type="radio" name="condition" value="普通" checked={form.condition === "普通"} onChange={handleChange} /> 普通
            </label>
            <label>
              <input type="radio" name="condition" value="不調" checked={form.condition === "不調"} onChange={handleChange} /> 不調
            </label>
          </div>
        </div>

        <div className="submit-area">
          <button className="primaryButton" type="button" onClick={() => { alert("作成しました"); navigate("/daily-list"); }}>
            作成
          </button>
        </div>
      </div>

      {/* <div className="section-card card">
        <h3>送信結果</h3>
        <pre style={{ marginTop: 20 }}>{JSON.stringify(result, null, 2)}</pre>
      </div> */}
    </div>
  );
}

function Section({ title, name, value, onChange }) {
  return (
    <div className="section-card card">
      <h3>{title}</h3>
      <textarea name={name} value={value} onChange={onChange} rows="5" />
    </div>
  );
}

export default CreateReport;
