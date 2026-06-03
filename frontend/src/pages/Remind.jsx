// 松原作成 → 今藤CSS作成

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Remind.css";

function Remind() {
  const navigate = useNavigate();

  const yesterdayGoal =
    "Redux Toolkitの検証を完了し、認証周りの実装方針を決める";

  const yesterdayIssue =
    "createAsyncThunkのエラーハンドリングの理解が不足していた";

  // ==========================
  // 通知設定（複数管理）
  // ==========================
  const [notificationEnabled, setNotificationEnabled] = useState(true);

  const [alarms, setAlarms] = useState([
    { hour: 18, minute: 0 }
  ]);

  // 追加
  const addAlarm = () => {
    setAlarms([...alarms, { hour: 0, minute: 0 }]);
  };

  // 削除
  const removeAlarm = (index) => {
    const newAlarms = alarms.filter((_, i) => i !== index);
    setAlarms(newAlarms);
  };

  // 更新
  const updateAlarm = (index, field, value) => {
    const newAlarms = [...alarms];
    newAlarms[index][field] = value;
    setAlarms(newAlarms);
  };

  const handleSave = () => {
    console.log({
      notificationEnabled,
      alarms,
    });

    alert("保存しました");
  };

  const handleClose = () => {
    console.log("閉じる");
    navigate("/dashboard"); // ダッシュボードに移動
  };

  return (
    <div className="remindContainer">

      {/* ヘッダー */}
      <div className="remindHeader">
        <h1 className="remindTitle">🔔 リマインダー</h1>

        <button className="closeBtn" onClick={handleClose}>
          ✕
        </button>
      </div>

      {/* 目標 */}
      <div className="card">
        <h2>前日に立てた今日の目標</h2>
        <p>{yesterdayGoal}</p>
      </div>

      {/* 課題 */}
      <div className="card">
        <h2>前日の課題・問題点</h2>
        <p>{yesterdayIssue}</p>
      </div>

      {/* 通知設定 */}
      <div className="card">

        <h2>リマインド通知設定</h2>

        {/* ON/OFF */}
        <div className="formRow">
          <label>
            <input
              type="checkbox"
              checked={notificationEnabled}
              onChange={() =>
                setNotificationEnabled(!notificationEnabled)
              }
            />
            通知を有効にする
          </label>
        </div>

        {/* 通知時間一覧 */}
        {alarms.map((alarm, index) => (
          <div key={index} className="timeGroup">

            {/* 時 */}
            <select
              className="timeSelect"
              value={alarm.hour}
              onChange={(e) =>
                updateAlarm(index, "hour", Number(e.target.value))
              }
            >
              {[...Array(24)].map((_, i) => (
                <option key={i} value={i}>
                  {i}
                </option>
              ))}
            </select>

            <span>時</span>

            {/* 分 */}
            <select
              className="timeSelect"
              value={alarm.minute}
              onChange={(e) =>
                updateAlarm(index, "minute", Number(e.target.value))
              }
            >
              {[...Array(60)].map((_, i) => (
                <option key={i} value={i}>
                  {i}
                </option>
              ))}
            </select>

            <span>分</span>

            {/* 削除 */}
            <button
              className="deleteBtn"
              onClick={() => removeAlarm(index)}
            >
              削除
            </button>

          </div>
        ))}

        {/* 追加ボタン */}
        <button className="addBtn" onClick={addAlarm}>
          ＋ 通知時間を追加
        </button>

        {/* 保存 */}
        <button className="saveBtn" onClick={handleSave}>
          保存
        </button>

      </div>
    </div>
  );
}

export default Remind;