import "./App.css";
import { BrowserRouter, Routes, Route, useLocation, Navigate } from "react-router-dom";
import Header from "./components/Header";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CreateReport from "./pages/CreateReport";
import DailyList from "./pages/DailyList";
import Remind from "./pages/Remind";

function AppWrapper() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}

function App() {
  const location = useLocation();

  // ヘッダー非表示ページ
  const hideHeader =
    location.pathname === "/" ||
    location.pathname === "/login" ||
    location.pathname === "/register";

  return (
    <>
      {/* ヘッダー（ログイン・登録時は非表示） */}
      {!hideHeader && <Header />}

      {/* ルーティング */}
      <Routes>

        {/* 認証系 */}
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* メイン */}
        <Route path="/dashboard" element={<Dashboard />} />

        {/* 日報系 */}
        <Route path="/create-report" element={<CreateReport />} />
        <Route path="/daily-list" element={<DailyList />} />

        {/* リマインド */}
        <Route path="/remind" element={<Remind />} />

        {/* デフォルト */}
        <Route path="*" element={<Navigate to="/login" />} />

      </Routes>
    </>
  );
}

export default AppWrapper;