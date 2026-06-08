import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Remind.css";

function Remind() {
  const navigate = useNavigate();

  // yesterdayGoalとyesterdayIssueを読み込み中
  const [remindMessage, setRemindMessage] = useState("読み込み中...");
  const [notificationEnabled, setNotificationEnabled] = useState(true);
  const [isSaved, setIsSaved] = useState(false);
  const [alarms, setAlarms] = useState([{ hour: 12, minute: 0}]);

  useEffect(() => {


    const userId = 1; // テスト用ユーザーIDを1番とする

    fetch("http://localhost:8080/api/remind/${userId}")
      .then((res) => res.json())
      .then((data) => {

        // data.value[0] が存在すれば、そのメッセージをセット
        if (data.value && data.value.length > 0) {
          setRemindMessage(data.value[0].remindContent);
        } else {
          setRemindMessage("本日のリマインドメッセージはありません。");
        }
      })
    
      .catch((err) => {
        console.error("データ取得失敗:", err);
        setRemindMessage("データの取得に失敗しました。");
      });
     }, []);




  const addAlarm = () => {
    setAlarms([...alarms, { hour: 0, minute: 0 }]);
    setIsSaved(false);
  };

  const removeAlarm = (index) => {
    setAlarms(alarms.filter((_, i) => i !== index));
    setIsSaved(false);
  };

  const updateAlarm = (index, field, value) => {
    const newAlarms = [...alarms];
    newAlarms[index][field] = value;
    setAlarms(newAlarms);
    setIsSaved(false);
  };

  const handleSave = () => {
    console.log({ notificationEnabled, alarms });
    alert("保存しました");
    setIsSaved(true);
  };

  const handleClose = () => {
    navigate("/dashboard");
  };

  return (
    <div>

      {/* ヘッダーエリア */}
      <div className="header">
        <h1 className="title">🔔 リマインダー</h1>

        <button
          className="backButton"
          onClick={handleClose}
        >
          ダッシュボードへ戻る
        </button>
      </div>

      {/* カード ここをいったん減らす6/8 11:30
      <div className="card">
        <h2>前日に立てた今日の目標</h2>
        <p>{remindMessage}</p>
      </div>
      */}

      <div className="card">
        <h2>前日の課題・問題点</h2>
        <p>{remindMessage}</p>
      </div>

      <div className="card">

        <h2>リマインド通知設定</h2>

        <label>
          <input
            type="checkbox"
            checked={notificationEnabled}
            onChange={() => {
              setNotificationEnabled(!notificationEnabled);
              setIsSaved(false);
            }}
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
              className="dangerButton"
              onClick={() => removeAlarm(index)}
            >
              削除
            </button>
          </div>
        ))}

        <button className="addButton" onClick={addAlarm}>
          ＋ 通知時間を追加
        </button>

        {' '}

        <button className="primaryButton" onClick={handleSave} disabled={isSaved}>
          {isSaved ? "保存済み" : "保存"}
        </button>

      </div>
    </div>
  );
}

export default Remind;