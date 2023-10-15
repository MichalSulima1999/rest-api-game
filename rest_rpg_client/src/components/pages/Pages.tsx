import { Route, Routes, useLocation } from "react-router-dom";
import PersistLogin from "./PersistLogin";
import RequireAuth from "./RequireAuth";
import Login from "../auth/Login";
import Register from "../auth/Register";
import CharacterHome from "../character/home/CharacterHome";
import CharacterCreator from "../character/character_creator/CharacterCreator";
import CharacterDetails from "../character/CharacterDetails";
import CharacterStatistics from "../character/CharacterStatistics";
import Home from "../home/Home";
import AdminHome from "../admin/admin_home/AdminHome";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route element={<PersistLogin />}>
        {/* protected */}
        <Route element={<RequireAuth allowedRoles={["USER"]} />}>
          <Route path="/character/:characterId" element={<CharacterHome />}>
            <Route path="main" element={<CharacterDetails />} />
            <Route path="statistics" element={<CharacterStatistics />} />
          </Route>
          <Route path="/character/create" element={<CharacterCreator />} />
          <Route path="/home" element={<Home />} />
        </Route>
        <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
          <Route path="/admin">
            <Route path="home" element={<AdminHome />} />
          </Route>
        </Route>
        {/* public */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Route>
    </Routes>
  );
}

export default Pages;
