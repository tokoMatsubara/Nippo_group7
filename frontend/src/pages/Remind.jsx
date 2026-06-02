// 松原作成
import { useState } from "react";

function Remind() {
  // ==========================
  // TODO:
  // 後でAPIから取得する
  // 前日の日報データ
  // ==========================
  const yesterdayGoal =
    "Redux Toolkitの検証を完了し、認証周りの実装方針を決める";

  const yesterdayIssue =
    "createAsyncThunkのエラーハンドリングの理解が不足していた";

  // ==========================
  // TODO:
  // DB保存値で初期化する
  // ==========================
  const [notificationEnabled, setNotificationEnabled] = useState(true);

  const [alarmTime, setAlarmTime] = useState("18:00");

  const handleSave = () => {
    // ==========================
    // TODO:
    // APIへ通知設定を保存
    // ==========================
    console.log({
      notificationEnabled,
      alarmTime,
    });

    alert("保存しました");
  };

  const handleClose = () => {
    // ==========================
    // TODO:
    // モーダルを閉じる処理
    // または画面遷移
    // ==========================
    console.log("閉じる");
  };

  return (
    <div>
      {/* ヘッダー */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "30px",
        }}
      >
        <h1>🔔 リマインダー</h1>

        <button onClick={handleClose}>
          ✕
        </button>
      </div>

      {/* 今日の目標 */}
      <div
        style={{
          border: "1px solid #ccc",
          padding: "20px",
          marginBottom: "20px",
        }}
      >
        <h2>前日に立てた今日の目標</h2>

        {/* TODO: API取得値を表示 */}
        <p>{yesterdayGoal}</p>
      </div>

      {/* 課題・問題点 */}
      <div
        style={{
          border: "1px solid #ccc",
          padding: "20px",
          marginBottom: "20px",
        }}
      >
        <h2>前日の課題・問題点</h2>

        {/* TODO: API取得値を表示 */}
        <p>{yesterdayIssue}</p>
      </div>

      {/* 通知設定 */}
      <div
        style={{
          border: "1px solid #ccc",
          padding: "20px",
        }}
      >
        <h2>リマインド通知設定</h2>

        <div style={{ marginBottom: "15px" }}>
          <label>
            <input
              type="checkbox"
              checked={notificationEnabled}
              onChange={() =>
                setNotificationEnabled(
                  !notificationEnabled
                )
              }
            />
            通知を有効にする
          </label>
        </div>

        <div style={{ marginBottom: "20px" }}>
          <label>
            通知時間：
            <input
              type="time"
              value={alarmTime}
              onChange={(e) =>
                setAlarmTime(e.target.value)
              }
            />
          </label>
        </div>

        <button onClick={handleSave}>
          保存
        </button>
      </div>
    </div>
  );
}

export default Remind;