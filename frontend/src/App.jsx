import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import CreateReport from "./pages/CreateReport";
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
      </Routes>
    </BrowserRouter>
  );
}


export default App;