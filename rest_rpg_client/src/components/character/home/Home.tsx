import { Outlet } from "react-router-dom";
import HomeNavbar from "./HomeNavbar";

const Home = () => {
  return (
    <div>
      <HomeNavbar />
      <Outlet />
    </div>
  );
};

export default Home;
