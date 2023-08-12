import { AxiosResponse } from "axios";
import axios from "../api/axios";
import { AuthResponse } from "../classes/auth/Auth";
import { useStores } from "../store/RootStore";

const useRefreshToken = () => {
  const { authStore } = useStores();

  const refresh = async () => {
    const response: AxiosResponse<AuthResponse> = await axios.get(
      "/refresh-token/refresh",
      {
        withCredentials: true,
      }
    );
    authStore.auth(response.data);
    return response.data.token;
  };
  return refresh;
};

export default useRefreshToken;
