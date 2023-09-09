import { Route, Routes, useLocation } from "react-router-dom";
import PersistLogin from "./PersistLogin";
import RequireAuth from "./RequireAuth";
import Login from "../auth/Login";
import Register from "../auth/Register";
import Home from "../character/home/Home";
import CharacterCreator from "../character_creator/CharacterCreator";
import CharacterDetails from "../character/CharacterDetails";
import CharacterStatistics from "../character/CharacterStatistics";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route element={<PersistLogin />}>
        {/* protected */}
        <Route element={<RequireAuth allowedRoles={["USER"]} />}>
          <Route path="/character/:characterId" element={<Home />}>
            <Route path="main" element={<CharacterDetails />} />
            <Route path="statistics" element={<CharacterStatistics />} />
          </Route>
          <Route path="/character/create" element={<CharacterCreator />} />
        </Route>
        {/* public */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Route>
    </Routes>
  );
}

export default Pages;
