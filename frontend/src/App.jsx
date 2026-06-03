import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";


import Login from "./pages/Login";
import Register from "./pages/Register";
//import Dashboard from "./pages/Dashboard";
import CreateReport from "./pages/CreateReport";
//import DailyList from "./pages/DailyList";
import Remind from "./pages/Remind";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/create-report"
          element={<CreateReport />}
        />

        <Route
          path="/remind"
          element={<Remind />}
        />

        {/* 認証系 */}
        <Route path="/" element={<Navigate to="/login"/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register />} />

        {/* メイン 
        <Route path="/dashboard" element={<Dashboard />} />*/}

        {/* 日報系 
        <Route path="/create-report" element={<CreateReport />} />
        <Route path="/daily-list" element={<DailyList />} />*/}

        {/* リマインド */}
        <Route path="/remind" element={<Remind />} />

        {/* デフォルト */}
        <Route path="*" element={<Login />} />

      </Routes>
    </BrowserRouter>
  );
}


export default App;


// import React from "react";
// import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
// import Login from "./pages/login"; 
// // ➕ 【追加1】新規登録画面のファイルをインポートする（ファイル名やパスに合わせてね）
// import Register from "./pages/register"; 

// function App() {
//   return (
//     <Router>
//       <div style={{ background: "black", color: "white", minHeight: "100vh", padding: "20px" }}>
//         <p style={{ fontWeight: "bold", color: "blue" }}>★Reactの通信準備が完了しました！</p>
//         <hr />
        
//         <Routes>
//           <Route path="/" element={<Navigate to="/login" />} />
//           <Route path="/login" element={<Login />} />
          
//           {/* ➕ 【追加2】/register にアクセスされたときのルールを追加 */}
//           <Route path="/register" element={<Register />} />
//         </Routes>
//       </div>
//     </Router>
//   );
// }

// export default App;