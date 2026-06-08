import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/CreateReport.css";

const API_BASE = "http://localhost:8080/api";

function toYmd(date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, '0');
  const d = String(date.getDate()).padStart(2, '0');
  return `${y}-${m}-${d}`;
}

function getWeekRange(date = new Date()) {
  const day = date.getDay(); // 0(日)〜6(土)

  // 月曜始まりに調整
  const diffToMonday = day === 0 ? -6 : 1 - day;

  const start = new Date(date);
  start.setDate(date.getDate() + diffToMonday);

  const end = new Date(start);
  end.setDate(start.getDate() + 6);

  return {
    startDate: start.toISOString().split("T")[0],
    endDate: end.toISOString().split("T")[0],
  };
}

const week = getWeekRange();


function CreateReport() {
  const navigate = useNavigate();
  const [date, setDate] = useState(toYmd(new Date()));
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
      const storedUserId = localStorage.getItem("user_id");
      const userId = storedUserId ? Number(storedUserId) : null;
      if (!userId) {
        alert("ログインしてください");
        navigate("/login");
        return;
      }
      const payload = {
        userId,
        date: toYmd(new Date()),// "今日の日付",
        contents: [
          { categoryId: 1, content: form.learned },
          { categoryId: 2, content: form.goodPoint },
          { categoryId: 3, content: form.goodReason },
          { categoryId: 4, content: form.issue },
          { categoryId: 5, content: form.issueReason },
          { categoryId: 6, content: form.action },
          { categoryId: 7, content: form.tomorrowGoal },
          { categoryId: 8, content: form.condition },
          { categoryId: 9, content: form.comment },
        ],
      };
      const res = await fetch(`${API_BASE}/report`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      const data = await res.json();
      setResult(data);

      if (res.ok) {
        alert("保存しました");
      } else {
        alert("保存に失敗しました");
      }

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

        <div className="goal-card card">
          <h3>昨日立てた今日の目標</h3>
          <p>{yesterdayGoal}</p>
        </div>

        <div className="section-card card">
          <h3>1. 今日学んだこと</h3>
          <textarea name="learned" value={form.learned} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>2. よかった点・できたこと</h3>
          <textarea name="goodPoint" value={form.goodPoint} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>3. その理由</h3>
          <textarea name="goodReason" value={form.goodReason} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>4. 課題・改善点</h3>
          <textarea name="issue" value={form.issue} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>5. その理由</h3>
          <textarea name="issueReason" value={form.issueReason} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>6. 改善するための行動</h3>
          <textarea name="action" value={form.action} onChange={handleChange} rows="5" />
        </div>
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

        <Section title="9. コメント" name="comment" value={form.comment} onChange={handleChange} />

        <div className="submit-area">
          <button className="primaryButton" type="button" onClick={() => { handleSubmit(); navigate(`/daily-list/${week.startDate}/${week.endDate}`); }}>
            作成
          </button>
        </div>

      </div>
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