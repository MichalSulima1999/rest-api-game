import "./navbar.css";
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          Rest RPG
        </Link>
        <div className="navbar-links">
          <Link to="/register" className="navbar-link">
            Register
          </Link>
          <Link to="/login" className="navbar-link">
            Login
          </Link>
          <Link to="/logout" className="navbar-link">
            Logout
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
