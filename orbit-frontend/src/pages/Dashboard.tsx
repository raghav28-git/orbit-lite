import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

interface Project {
  id: number;
  name: string;
  description?: string;
}

function Dashboard() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const loadProjects = async () => {
      try {
        const response = await api.get("/projects");
        setProjects(response.data);
      } catch (error) {
        console.error("Failed to load projects:", error);
      } finally {
        setLoading(false);
      }
    };

    loadProjects();
  }, []);

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
        <div className="page-heading">
          <h2>Projects</h2>
          <p>Manage your configuration environments.</p>
        </div>

        {loading ? (
          <p>Loading projects...</p>
        ) : projects.length === 0 ? (
          <p>No projects found.</p>
        ) : (
          <div className="project-grid">
            {projects.map((project) => (
              <div
                key={project.id}
                className="project-card"
                onClick={() =>
                  navigate(`/projects/${project.id}`)
                }
              >
                <h3>{project.name}</h3>
                <p>{project.description}</p>
                <span>Open project →</span>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default Dashboard;