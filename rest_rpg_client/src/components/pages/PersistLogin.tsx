import { Outlet } from "react-router-dom";
import { useState, useEffect } from "react";
import useRefreshToken from "../../hooks/useRefreshToken";
import useLocalStorage from "../../hooks/useLocalStorage";
import { useStores } from "../../store/RootStore";
import { observer } from "mobx-react";

const PersistLogin = observer(() => {
  const [isLoading, setIsLoading] = useState(true);
  const refresh = useRefreshToken();
  const { authStore } = useStores();
  const [persist] = useLocalStorage("persist", false);

  useEffect(() => {
    let isMounted = true;

    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (err) {
        console.error(err);
      } finally {
        isMounted && setIsLoading(false);
      }
    };
    
    !authStore.accessToken && persist
      ? verifyRefreshToken()
          .then(() => {
            console.log("Token refreshed");
          })
          .catch((err) => {
            console.log(err);
          })
      : setIsLoading(false);

    return () => {
      isMounted = false;
    };
  }, [authStore.accessToken, persist, refresh]);

  return (
    <>{!persist ? <Outlet /> : isLoading ? <p>Loading...</p> : <Outlet />}</>
  );
});

export default PersistLogin;
