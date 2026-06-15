import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../styles/CreateReport.css";
import logoIcon from "../assets/nippo_create.png";

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
  const location = useLocation();
  const editDaily = location.state?.daily || null;
  const returnPath = location.state?.returnPath ?? `/daily-list/${week.startDate}/${week.endDate}`;
  const [date, setDate] = useState(location.state?.date ?? editDaily?.date ?? toYmd(new Date()));
  const [dailyId, setDailyId] = useState(editDaily?.dailyId ?? null);

  const [condition, setCondition] = useState({
    health: "普通",
    mood: "普通",
  });

  const [form, setForm] = useState(() => {
    if (!editDaily?.contents) {
      return {
        learned: "",
        goodPoint: "",
        goodReason: "",
        issue: "",
        issueReason: "",
        action: "",
        tomorrowGoal: "",
        comment: "",
      };
    }

    const mapped = {
      learned: "",
      goodPoint: "",
      goodReason: "",
      issue: "",
      issueReason: "",
      action: "",
      tomorrowGoal: "",
      comment: "",
    };

    editDaily.contents.forEach((item) => {
      switch (item.categoryId) {
        case 1:
          mapped.learned = item.content;
          break;
        case 2:
          mapped.goodPoint = item.content;
          break;
        case 3:
          mapped.goodReason = item.content;
          break;
        case 4:
          mapped.issue = item.content;
          break;
        case 5:
          mapped.issueReason = item.content;
          break;
        case 6:
          mapped.action = item.content;
          break;
        case 7:
          mapped.tomorrowGoal = item.content;
          break;
        case 8: {
          const text = item.content;

          const health = text.match(/体調・・・(.+?)、/);
          const mood = text.match(/気持ち・・・(.+)/);

          setCondition({
            health: health?.[1] || "普通",
            mood: mood?.[1] || "普通",
          });
          break;
        }
        case 9:
          mapped.comment = item.content;
          break;
        default:
          break;
      }
    });

    return mapped;
  });
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [yesterdayGoal, setYesterdayGoal] = useState("前日の日報はありません");
  const [error, setError] = useState("");

  // 前日の日報を取得する処理
  useEffect(() => {
    const fetchYesterdayGoal = async () => {

      try {
        // const userId = localStorage.getItem("user_id");
        const res = await fetch(
          `${API_BASE}/daily/previous-goal`, {
          method: "GET",
          credentials: "include"
        });

        if (!res.ok) {
          const error = new Error(`HTTP ${res.status}`);
          error.status = res.status;   
          throw error;
        }

        const data = await res.json();

        console.log("previous-goal", data);

        setYesterdayGoal(
          data.goal || "前営業日の日報はありません"
        );

      } catch (err) {
        if (err.status === 401) {
          console.log("401認証エラー");
          alert("認証エラーです。ログインしなおしてください");
          navigate("/login");
        } else {
          console.error(err);
          setYesterdayGoal("前営業日の日報はありません");
        }
      }
    };

  fetchYesterdayGoal();
}, []);

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
      if (
        !date ||
        !form.learned ||
        !form.goodPoint ||
        !form.goodReason ||
        !form.issue ||
        !form.issueReason ||
        !form.action ||
        !form.tomorrowGoal ||
        !form.condition
      ) {
        setError("未入力の項目があります");
        return;
      }
      setLoading(true);
      const healthText =
        condition.health === "その他"
          ? condition.healthOther
          : condition.health;

      const moodText =
        condition.mood === "その他"
          ? condition.moodOther
          : condition.mood;

      const conditionText =
        `体　調・・・${healthText}\n気持ち・・・${moodText}`;
      const contentsPayload = [
        { categoryId: 1, content: form.learned },
        { categoryId: 2, content: form.goodPoint },
        { categoryId: 3, content: form.goodReason },
        { categoryId: 4, content: form.issue },
        { categoryId: 5, content: form.issueReason },
        { categoryId: 6, content: form.action },
        { categoryId: 7, content: form.tomorrowGoal },
        { categoryId: 8, content: conditionText },
        { categoryId: 9, content: form.comment },
      ];

      const payload = editDaily
        ? { dailyId, contents: contentsPayload }
        : {
          date,
          contents: contentsPayload,
        };


      const endpoint = editDaily ? "/update" : "/report";
      const method = editDaily ? "PUT" : "POST";
      const res = await fetch(`${API_BASE}${endpoint}`, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
        credentials: "include"
      });

      if (!res.ok) {
        const error = new Error(`HTTP ${res.status}`);
        error.status = res.status;   // ← これを足すのが肝
        throw error;
      }

      const data = await res.json();
      setResult(data);
      alert(editDaily ? "更新しました" : "保存しました");
      navigate(-1);

    } catch (err) {
      if (err.status === 401) {
        console.log("401認証エラー");
        alert("認証エラーです。ログインしなおしてください");
        navigate("/login");
      } else {
        console.error("Failed to create report:", err);
        alert(editDaily ? "更新に失敗しました" : "保存に失敗しました");
      }
      setResult({ status: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="daily-page">
      <header className="header">
        <div className="headerTop">
          <div className="headerTitle">
            <h1 className="title">日報{editDaily ? "編集" : "作成"}</h1>
            <img src={logoIcon} alt="logo" className="logoIcon" />
            <button className="backButton" type="button" onClick={() => navigate(-1)}>
              キャンセル
            </button>
          </div>
        </div>
      </header>

      <div>

        <div className="section-card card">
          <h3>日付</h3>
          <input
            className="datebox"
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
          />
        </div>

        <div className="goal-card card">
          <h3>昨日立てた今日の目標</h3>
          <p>{yesterdayGoal}</p>
        </div>

        <div className="section-card card">
          <h3>1. 今日学んだこと<span style={{ color: "red" }}>*</span></h3>
          <textarea name="learned" value={form.learned} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>2. よかった点・できたこと<span style={{ color: "red" }}>*</span></h3>
          <textarea name="goodPoint" value={form.goodPoint} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>3. その理由<span style={{ color: "red" }}>*</span></h3>
          <textarea name="goodReason" value={form.goodReason} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>4. 課題・改善点<span style={{ color: "red" }}>*</span></h3>
          <textarea name="issue" value={form.issue} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>5. その理由<span style={{ color: "red" }}>*</span></h3>
          <textarea name="issueReason" value={form.issueReason} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>6. 改善するための行動<span style={{ color: "red" }}>*</span></h3>
          <textarea name="action" value={form.action} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>7. 明日の目標<span style={{ color: "red" }}>*</span></h3>
          <textarea name="action" value={form.tomorrowGoal} onChange={handleChange} rows="5" />
        </div>
        <div className="section-card card">
          <h3>8. 体調・気持ち<span style={{ color: "red" }}>*</span></h3>
          <div className="radio-group">
            <p style={{ width: "100%" }}>体　調</p>

            {["良好", "普通", "不調", "その他"].map((v) => (
              <label key={v}>
                <input
                  type="radio"
                  name="health"
                  value={v}
                  checked={condition.health === v}
                  onChange={(e) =>
                    setCondition((prev) => ({
                      ...prev,
                      health: e.target.value,
                    }))
                  }
                />
                {v}
              </label>
            ))}

            {/* その他入力（体調） */}
            {condition.health === "その他" && (
              <input
                type="text"
                placeholder="体調を入力"
                value={condition.healthOther || ""}
                onChange={(e) =>
                  setCondition((prev) => ({
                    ...prev,
                    healthOther: e.target.value,
                  }))
                }
              />
            )}
          </div>

          {/* =========================
      気持ち
  ========================= */}
          <div className="radio-group" style={{ marginTop: "12px" }}>
            <p style={{ width: "100%" }}>気持ち</p>

            {["良好", "普通", "不調", "その他"].map((v) => (
              <label key={v}>
                <input
                  type="radio"
                  name="mood"
                  value={v}
                  checked={condition.mood === v}
                  onChange={(e) =>
                    setCondition((prev) => ({
                      ...prev,
                      mood: e.target.value,
                    }))
                  }
                />
                {v}
              </label>
            ))}

            {/* その他入力（気持ち） */}
            {condition.mood === "その他" && (
              <input
                type="text"
                placeholder="気持ちを入力"
                value={condition.moodOther || ""}
                onChange={(e) =>
                  setCondition((prev) => ({
                    ...prev,
                    moodOther: e.target.value,
                  }))
                }
              />
            )}
          </div>
        </div>

        <Section title="9. コメント" name="comment" value={form.comment} onChange={handleChange} />

        <div className="submit-area">
          {error && (
            <div style={{ color: "red", marginBottom: "10px" }}>
              ⚠ {error}
            </div>
          )}
          <button className="primaryButton" type="button" onClick={handleSubmit}>
            保存
          </button>

          <button className="backButton" type="button" onClick={() => navigate(-1)}>
            キャンセル
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