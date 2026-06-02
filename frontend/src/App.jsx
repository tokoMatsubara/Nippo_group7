import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CreateReport from "./pages/CreateReport";
import DailyList from "./pages/DailyList";
import Remind from "./pages/Remind";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* 認証系 */}
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
        <Route path="*" element={<Login />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;