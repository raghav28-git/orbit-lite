import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

interface AuditLog {
  id: number;
  userEmail: string;
  environmentId: number;
  action: string;
  resource: string;
  resourceId: number;
  details: string;
  createdAt: string;
}

function AuditLogs() {
  const { projectId, environmentId } = useParams();
  const navigate = useNavigate(); 
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadAuditLogs = async () => {
      try {
        const response = await api.get(
          `/projects/${projectId}/environments/${environmentId}/audit-logs`
        );

        setLogs(response.data);
      } catch (error) {
        console.error("Failed to load audit logs:", error);
      } finally {
        setLoading(false);
      }
    };

    loadAuditLogs();
  }, [projectId, environmentId]);

return (
  <div className="dashboard-page">
    <header className="topbar">
      <div>
        <h1>ORBIT</h1>
        <span>Lite</span>
      </div>

      <button
        onClick={() => {
          localStorage.removeItem("orbit_token");
          navigate("/");
        }}
      >
        Logout
      </button>
    </header>

    <main className="dashboard-content">
      <button
        className="back-button"
        onClick={() => navigate(`/projects/${projectId}`)}
      >
        ← Back to Environment
      </button>

      <div className="page-heading">
        <h2>Audit Logs</h2>
        <p>
          Track configuration and API key activity.
        </p>
      </div>

      {loading ? (
        <p>Loading audit logs...</p>
      ) : logs.length === 0 ? (
        <p>No audit logs found.</p>
      ) : (
        <div className="audit-list">
          {logs.map((log) => (
            <div key={log.id} className="audit-card">
              <div className="audit-main">
                <span className="audit-action">
                  {log.action}
                </span>

                <p>{log.details}</p>
              </div>

              <div className="audit-meta">
                <span>
                  {log.resource} #{log.resourceId}
                </span>

                <span>{log.userEmail}</span>

                <span>
                  {new Date(
                    log.createdAt
                  ).toLocaleString()}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </main>
  </div>
);
}

export default AuditLogs;