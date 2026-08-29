import {Navigate, Route, Routes} from "react-router";
import {ClientListPage} from "./pages/ClientListPage.tsx";

function App() {

  return (
      <Routes>
        <Route path="/" element={<Navigate to="/clients" replace />} />
        <Route path="/clients" element={<ClientListPage />} />
      </Routes>
  )
}

export default App
