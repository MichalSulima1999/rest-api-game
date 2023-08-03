import { Route, Routes, useLocation } from "react-router-dom";
import PersistLogin from "./PersistLogin";
import RequireAuth from "./RequireAuth";
import Login from "../auth/Login";
import Register from "../auth/Register";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route element={<PersistLogin />}>
        {/* protected */}
        <Route element={<RequireAuth allowedRoles={["USER"]} />}></Route>
        {/* public */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Route>
    </Routes>
  );
}

export default Pages;
