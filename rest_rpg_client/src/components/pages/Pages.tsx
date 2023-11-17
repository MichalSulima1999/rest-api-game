import { Route, Routes, useLocation } from "react-router-dom";
import PersistLogin from "./PersistLogin";
import RequireAuth from "./RequireAuth";
import Login from "../auth/Login";
import Register from "../auth/Register";
import CharacterHome from "../character/home/CharacterHome";
import CharacterCreator from "../character/character_creator/CharacterCreator";
import CharacterDetails from "../character/CharacterDetails";
import CharacterStatistics from "../character/statistics/CharacterStatistics";
import UserHome from "../home/UserHome";
import AdminHome from "../admin/admin_home/AdminHome";
import CreateEnemy from "../admin/enemy/CreateEnemy";
import CreateSkill from "../admin/skill/CreateSkill";
import CreateAdventure from "../admin/adventure/CreateAdventure";
import AdminShowAdventures from "../admin/adventure/AdminShowAdventures";
import ShowAdventures from "../adventure/ShowAdventures";
import CreateItem from "../admin/item/CreateItem";
import CreateWork from "../admin/work/CreateWork";
import CharacterSkills from "../character/skills/CharacterSkills";
import Shop from "../character/shop/Shop";
import ShowWorks from "../character/work/ShowWorks";
import Fight from "../character/fight/Fight";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route element={<PersistLogin />}>
        {/* protected */}
        <Route element={<RequireAuth allowedRoles={["USER"]} />}>
          <Route path="/user">
            <Route path="character/:characterId" element={<CharacterHome />}>
              <Route path="main" element={<CharacterDetails />} />
              <Route path="statistics" element={<CharacterStatistics />} />
              <Route path="adventure">
                <Route path="show" element={<ShowAdventures />} />
              </Route>
              <Route path="skill">
                <Route path="show" element={<CharacterSkills />} />
              </Route>
              <Route path="shop">
                <Route path="show" element={<Shop />} />
              </Route>
              <Route path="work">
                <Route path="show" element={<ShowWorks />} />
              </Route>
              <Route path="fight" element={<Fight />} />
            </Route>
            <Route path="character/create" element={<CharacterCreator />} />
            <Route path="home" element={<UserHome />} />
          </Route>
        </Route>
        <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
          <Route path="/admin">
            <Route path="home" element={<AdminHome />} />
            <Route path="enemy/create" element={<CreateEnemy />} />
            <Route path="skill">
              <Route path="create" element={<CreateSkill />} />
            </Route>
            <Route path="adventure">
              <Route path="create" element={<CreateAdventure />} />
              <Route path="show" element={<AdminShowAdventures />} />
            </Route>
            <Route path="item">
              <Route path="create" element={<CreateItem />} />
            </Route>
            <Route path="work">
              <Route path="create" element={<CreateWork />} />
            </Route>
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
