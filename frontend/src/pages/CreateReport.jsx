// 松原作成
import { useState } from "react";

function DailyCreate() {
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

  const yesterdayGoal =
    "昨日設定した目標がここに表示されます";

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm({
      ...form,
      [name]: value,
    });
  };

  return (
    <div className="daily-page">
      <header className="header">
        <div>
          <div className="page-label">日報作成</div>
          <h1>{today} の日報</h1>
        </div>

        <div className="header-actions">
          <span className="user-name">開発演習</span>

          <button className="header-btn">
            ダッシュボードへ戻る
          </button>

          <button className="header-btn">
            ログアウト
          </button>
        </div>
      </header>

      <div className="goal-card">
        <h3>昨日立てた今日の目標</h3>
        <p>{yesterdayGoal}</p>
      </div>

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

        <select
          className="select-box"
          name="condition"
          value={form.condition}
          onChange={handleChange}
        >
          <option value="不調">不調</option>
          <option value="普通">普通</option>
          <option value="良好">良好</option>
        </select>
      </div>

      <Section
        title="9. コメント"
        name="comment"
        value={form.comment}
        onChange={handleChange}
      />

      <div className="submit-area">
        <button
          className="submit-btn"
          onClick={() => alert("作成ボタン押下")}
        >
          作成
        </button>
      </div>
    </div>
  );
}

function Section({
  title,
  name,
  value,
  onChange,
}) {
  return (
    <div className="section-card">
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