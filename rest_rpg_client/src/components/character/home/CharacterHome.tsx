import { Outlet } from "react-router-dom";
import HomeNavbar from "./HomeNavbar";

const CharacterHome = () => {
  return (
    <div>
      <HomeNavbar />
      <Outlet />
    </div>
  );
};

export default CharacterHome;
