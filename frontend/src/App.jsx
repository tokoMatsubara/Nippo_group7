<<<<<<< HEAD
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import CreateReport from "./pages/CreateReport";
=======
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CreateReport from "./pages/CreateReport";
import DailyList from "./pages/DailyList";
>>>>>>> 66bebb8f3530495b406748372764f67a9db11f8b
import Remind from "./pages/Remind";

function App() {
  return (
    <BrowserRouter>
      <Routes>
<<<<<<< HEAD
        <Route
          path="/create-report"
          element={<CreateReport />}
        />

        <Route
          path="/remind"
          element={<Remind />}
        />
=======

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

>>>>>>> 66bebb8f3530495b406748372764f67a9db11f8b
      </Routes>
    </BrowserRouter>
  );
}


export default App;