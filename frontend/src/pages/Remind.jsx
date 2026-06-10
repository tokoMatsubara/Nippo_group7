import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Remind.css";

function Remind() {
  const navigate = useNavigate();

  const [yesterdayGoal, setYesterdayGoal] = useState("");
  const [notificationEnabled, setNotificationEnabled] = useState(true);
  const [isSaved, setIsSaved] = useState(false);

  const [alarm, setAlarm] = useState(
    { hour: 18, minute: 0 }
  );

  useEffect(() => {
    fetchYesterdayGoal();
  }, []);

  const fetchYesterdayGoal = async () => {
    try {
      // const userId = localStorage.getItem("user_id");

      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);

      const dateStr = yesterday.toISOString().split("T")[0];

      const remindResponse = await fetch(
        `http://localhost:8080/api/remind`, {
          method: "GET",
          credentials: "include"
      });

      const remindSettingResponse = await fetch(
        `http://localhost:8080/api/remind/settings`, {
          method: "GET",
          credentials: "include"
      });

      if(remindResponse.status === 403){
        alert("アクセス権限がありません");
        navigate("/login")
      }
      if(remindSettingResponse.status === 403){
        alert("アクセス権限がありません");
        navigate("/login")
      }

      const remindData = await remindResponse.json();
      const settingData = await remindSettingResponse.json();

      console.log(remindData);
      console.log(remindData[0]);
      console.log(settingData);

      const goal = remindData[0].remindContent;
      const [hour, minute] = settingData["remindTime"].split(":");
      setNotificationEnabled(settingData["remindStatus"]);
      setAlarm({hour, minute});


      setYesterdayGoal(
        goal || "昨日の目標が登録されていません"
      );

    } catch (error) {
      console.error(error);
      setYesterdayGoal("取得に失敗しました");  
    }
  };

  // const addAlarm = () => {
  //   setAlarms([...alarm, { hour: 0, minute: 0 }]);
  //   setIsSaved(false);
  // };

  // const removeAlarm = (index) => {
  //   setAlarms(alarm.filter((_, i) => i !== index));
  //   setIsSaved(false);
  // };


  const updateAlarm = (field, value) => {
    setAlarm(prev => ({
      ...prev,
      [field]: value,
    }));

    setIsSaved(false);
  };


  const handleSave = async () => {
    try{
      const response = await fetch("http://localhost:8080/api/remind/settings", {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
          remindStatus: notificationEnabled,
          remindTime:`${String(alarm.hour).padStart(2, '0')}:${String(alarm.minute).padStart(2, '0')}`
        }),
        credentials: "include"
      });

      const data = await response.json();
      console.log(data);
      alert("保存しました");
      setIsSaved(true);

    }catch(error){
      console.log(error);
    }
    
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

        
        <div className="timeGroup">

          <select
            value={alarm.hour}
            onChange={(e) =>
              updateAlarm("hour", Number(e.target.value))
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
              updateAlarm("minute", Number(e.target.value))
            }
          >
            {[...Array(60)].map((_, i) => (
              <option key={i} value={i}>{i}</option>
            ))}
          </select>

          <span>分</span>
        </div>

        {' '}

        <button className="primaryButton" onClick={handleSave} disabled={isSaved}>
          {isSaved ? "保存済み" : "保存"}
        </button>

      </div>
    </div>
  );
}

export default Remind;