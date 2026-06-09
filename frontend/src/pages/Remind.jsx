import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Remind.css";

function Remind() {
  const navigate = useNavigate();

  const [yesterdayGoal, setYesterdayGoal] = useState("");

  useEffect(() => {
    fetchYesterdayGoal();
  }, []);

  const fetchYesterdayGoal = async () => {
    try {
      // const userId = localStorage.getItem("user_id");

      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);

      const dateStr = yesterday.toISOString().split("T")[0];

      const response = await fetch(
        `http://localhost:8080/api/remind`, {
          method: "GET",
          credentials: "include"
      });

      const data = await response.json();

      console.log(data);
      console.log(data[0]);

      const goal = data[0].remindContent;


      setYesterdayGoal(
        goal || "昨日の目標が登録されていません"
      );

    } catch (error) {
      console.error(error);
      setYesterdayGoal("取得に失敗しました");  
    }
  };

  const [notificationEnabled, setNotificationEnabled] = useState(true);
  const [isSaved, setIsSaved] = useState(false);

  const [alarms, setAlarms] = useState([
    { hour: 18, minute: 0 }
  ]);

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
    <div className="container">

      {/* ヘッダーエリア */}
      <div className="header">

        <h1 className="title">🔔 リマインド</h1>

      </div>

      {/* カード */}
      <div className="card">
        <h2 className="remindTitle">前日に立てた今日の目標</h2>
        <p>{yesterdayGoal}</p>
      </div>

      <div className="card">

        <h2 className="remindTitle">リマインド通知設定</h2>

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