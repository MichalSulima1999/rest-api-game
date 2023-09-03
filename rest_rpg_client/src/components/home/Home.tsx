import React from "react";
import { Outlet } from "react-router-dom";
import HomeNavbar from "./HomeNavbar";

const Home = () => {
  return (
    <div>
      <HomeNavbar />
      HOME
      <Outlet />
    </div>
  );
};

export default Home;
