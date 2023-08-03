import React from "react";
import { useLocation, Navigate, Outlet } from "react-router-dom";
import { useStores } from "../../store/RootStore";

interface Props {
  allowedRoles: string[];
}

const RequireAuth: React.FC<Props> = ({ allowedRoles }) => {
  const { authStore } = useStores();
  const location = useLocation();

  return allowedRoles?.includes(authStore?.role) ? (
    <Outlet />
  ) : authStore?.accessToken ? (
    <Navigate to="/unauthorized" state={{ from: location }} replace />
  ) : (
    <Navigate to="/account/login" state={{ from: location }} replace />
  );
};

export default RequireAuth;
