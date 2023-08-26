import { axiosPrivate } from "../api/axios";
import { useEffect } from "react";
import useRefreshToken from "./useRefreshToken";
import { useStores } from "../store/RootStore";
import { AxiosError, AxiosRequestConfig } from "axios";

const useAxiosPrivate = () => {
  const refresh = useRefreshToken();
  const { authStore } = useStores();

  useEffect(() => {
    const requestIntercept = axiosPrivate.interceptors.request.use(
      (config) => {
        if (!config.headers["Authorization"]) {
          config.headers["Authorization"] = `Bearer ${authStore?.accessToken}`;
        }
        console.log(config);
        return config;
      },
      (error) => Promise.reject(error)
    );

    const responseIntercept = axiosPrivate.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const prevRequest = error?.config as AxiosRequestConfig & { sent?: boolean };
        if (error?.response?.status === 403 && !prevRequest?.sent) {
          prevRequest.sent = true;
          try {
            const newAccessToken = await refresh();
            prevRequest.headers = {
              ...prevRequest.headers,
              Authorization: `Bearer ${newAccessToken}`,
            };
            return axiosPrivate(prevRequest);
          } catch (refreshError) {
            return Promise.reject(refreshError);
          }
        }
        return Promise.reject(error);
      }
    );

    return () => {
      axiosPrivate.interceptors.request.eject(requestIntercept);
      axiosPrivate.interceptors.response.eject(responseIntercept);
    };
  }, [authStore, refresh]);

  return axiosPrivate;
};

export default useAxiosPrivate;
