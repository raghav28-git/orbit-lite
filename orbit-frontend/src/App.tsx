import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Environment from "./pages/Environment";
import Configurations from "./pages/Configurations";
import ConfigurationDetails from "./pages/ConfigurationDetails";
import ApiKeys from "./pages/ApiKeys";
import AuditLogs from "./pages/AuditLogs";

import ProtectedRoute from "./auth/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/projects/:projectId"
          element={
            <ProtectedRoute>
              <Environment />
            </ProtectedRoute>
          }
        />

        <Route
          path="/projects/:projectId/environments/:environmentId"
          element={
            <ProtectedRoute>
              <Configurations />
            </ProtectedRoute>
          }
        />

        <Route
          path="/projects/:projectId/environments/:environmentId/configurations/:configurationId"
          element={
            <ProtectedRoute>
              <ConfigurationDetails />
            </ProtectedRoute>
          }
        />

        <Route
          path="/projects/:projectId/environments/:environmentId/api-keys"
          element={
            <ProtectedRoute>
              <ApiKeys />
            </ProtectedRoute>
          }
        />

        <Route
          path="/projects/:projectId/environments/:environmentId/audit-logs"
          element={
            <ProtectedRoute>
              <AuditLogs />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;