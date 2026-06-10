import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useLayoutEffect } from "react";

import Layout from "./components/Layout";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CreateReport from "./pages/CreateReport";
import DailyList from "./pages/DailyList";
import Remind from "./pages/Remind";

// テーマ適用関数（純粋関数）
const applyTheme = (theme) => {
  document.body.classList.remove("blueTheme", "orangeTheme");

  if (theme === "blue") {
    document.body.classList.add("blueTheme");
  } else {
    document.body.classList.add("orangeTheme"); // デフォルト
  }
};

function AppWrapper() {
  // layout崩れ・チラつき防止のため useLayoutEffect
  useLayoutEffect(() => {
    const theme = localStorage.getItem("theme") || "orange";
    applyTheme(theme);
  }, []);
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}

function App() {
  return (
    <Routes>

      {/* 認証系（Layoutなし） */}
      <Route path="/" element={<Navigate to="/login" />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* メイン（Layoutあり） */}
      <Route
        path="/dashboard"
        element={
          <Layout>
            <Dashboard />
          </Layout>
        }
      />

      <Route
        path="/create-report"
        element={
          <Layout>
            <CreateReport />
          </Layout>
        }
      />

      <Route
        path="/daily-list/:startDate/:endDate"
        element={
          <Layout>
            <DailyList />
          </Layout>
        }
      />

      <Route
        path="/remind"
        element={
          <Layout>
            <Remind />
          </Layout>
        }
      />

      {/* fallback */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
}

export default AppWrapper;