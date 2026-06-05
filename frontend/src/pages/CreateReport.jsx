import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/CreateReport.css";

function DailyCreate() {
  const navigate = useNavigate();

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

  const today = new Date().toLocaleDateString("ja-JP", {
    year: "numeric",
    month: "numeric",
    day: "numeric",
    weekday: "short",
  });

  const yesterdayGoal = "昨日設定した目標がここに表示されます";

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm({
      ...form,
      [name]: value,
    });
  };

  return (
    <div className="daily-page">

      {/* ヘッダー */}
      <header className="header">

        <div>
          <h1 className="title">{today} の日報</h1>
        </div>

        <div className="header-actions">

          <button
            className="backButton"
            onClick={() => navigate("/dashboard")}
          >
            ダッシュボードへ戻る
          </button>

        </div>
      </header>

      {/* 目標 */}
      <div className="goal-card card">
        <h3>昨日立てた今日の目標</h3>
        <p>{yesterdayGoal}</p>
      </div>

      {/* フォーム */}
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

      {/* 体調 */}
      <div className="section-card card">

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

      <Section
        title="9. コメント"
        name="comment"
        value={form.comment}
        onChange={handleChange}
      />

      {/* 送信 */}
      <div className="submit-area">

        <button
          className="primaryButton"
          onClick={() => {
            alert("作成しました");
            navigate("/daily-list");
          }}
        >
          作成
        </button>

      </div>

    </div>
  );
}

/* Sectionコンポーネント */
function Section({ title, name, value, onChange }) {
  return (
    <div className="section-card card">
      <h3>{title}</h3>

      <textarea
        name={name}
        value={value}
        onChange={onChange}
        rows="5"
      />
    </div>
  );
}

export default DailyCreate;