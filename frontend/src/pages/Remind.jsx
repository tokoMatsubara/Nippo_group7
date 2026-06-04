import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Remind.css";

function Remind() {
  const navigate = useNavigate();

  const yesterdayGoal =
    "Redux Toolkitの検証を完了し、認証周りの実装方針を決める";

  const yesterdayIssue =
    "createAsyncThunkのエラーハンドリングの理解が不足していた";

  const [notificationEnabled, setNotificationEnabled] = useState(true);

  const [alarms, setAlarms] = useState([
    { hour: 18, minute: 0 }
  ]);

  const addAlarm = () => {
    setAlarms([...alarms, { hour: 0, minute: 0 }]);
  };

  const removeAlarm = (index) => {
    setAlarms(alarms.filter((_, i) => i !== index));
  };

  const updateAlarm = (index, field, value) => {
    const newAlarms = [...alarms];
    newAlarms[index][field] = value;
    setAlarms(newAlarms);
  };

  const handleSave = () => {
    console.log({ notificationEnabled, alarms });
    alert("保存しました");
  };

  const handleClose = () => {
    navigate("/dashboard");
  };

  return (
    <div>

      {/* ヘッダーエリア */}
      <div className="headerRow">
        <h1>🔔 リマインダー</h1>

        <button
          className="backButton"
          onClick={handleClose}
        >
          ダッシュボードへ戻る
        </button>
      </div>

      {/* カード */}
      <div className="card">
        <h2>前日に立てた今日の目標</h2>
        <p>{yesterdayGoal}</p>
      </div>

      <div className="card">
        <h2>前日の課題・問題点</h2>
        <p>{yesterdayIssue}</p>
      </div>

      <div className="card">

        <h2>リマインド通知設定</h2>

        <label>
          <input
            type="checkbox"
            checked={notificationEnabled}
            onChange={() => setNotificationEnabled(!notificationEnabled)}
          />
          通知を有効にする
        </label>

        {alarms.map((alarm, index) => (
          <div key={index} className="timeGroup">

            <select
              value={alarm.hour}
              onChange={(e) =>
                updateAlarm(index, "hour", Number(e.target.value))
              }
            >
              {[...Array(24)].map((_, i) => (
                <option key={i} value={i}>{i}</option>
              ))}
            </select>

            <span>時</span>

            <select
              value={alarm.minute}
              onChange={(e) =>
                updateAlarm(index, "minute", Number(e.target.value))
              }
            >
              {[...Array(60)].map((_, i) => (
                <option key={i} value={i}>{i}</option>
              ))}
            </select>

            <span>分</span>

            <button
              className="deleteBtn"
              onClick={() => removeAlarm(index)}
            >
              削除
            </button>
          </div>
        ))}

        <button className="primaryButton" onClick={addAlarm}>
          ＋ 通知時間を追加
        </button>

        <button className="primaryButton" onClick={handleSave}>
          保存
        </button>

      </div>
    </div>
  );
}

export default Remind;