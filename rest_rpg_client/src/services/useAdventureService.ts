import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import {
  AdventureBasicPage,
  AdventureCreateRequest,
  AdventureDetails,
  AdventureLite,
  AdventureSearchRequest,
  DefaultApiFp,
} from "../generated-sources/openapi";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useServiceHelper from "./helpers/useServiceHelper";
import { useStores } from "../store/RootStore";

const useAdventureService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { t } = useTranslation();
  const axiosPrivate = useAxiosPrivate();
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();
  const { characterStore } = useStores();

  const create = async (
    request: AdventureCreateRequest
  ): Promise<AdventureLite> => {
    setIsLoading(true);
    const createAdventure = await api.createAdventure(request, {
      withCredentials: true,
    });

    return createAdventure(axiosPrivate)
      .then((response) => {
        successToast(t("ADVENTURE.CREATION_SUCCESSFUL"));
        return response.data;
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
          errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
        const adventure: AdventureLite = {
          id: -1,
          name: "",
          adventureTimeInMinutes: 0,
        };
        return adventure;
      })
      .finally(() => setIsLoading(false));
  };

  const editAdventure = async (
    adventureId: number,
    request: AdventureCreateRequest
  ): Promise<AdventureLite | undefined> => {
    setIsLoading(true);
    const editAdventure = await api.editAdventure(adventureId, request, {
      withCredentials: true,
    });

    return getResources(editAdventure, setIsLoading, "ADVENTURE.EDITED");
  };

  const deleteAdventure = async (adventureId: number) => {
    setIsLoading(true);
    const deleteAdventure = await api.deleteAdventure(adventureId, {
      withCredentials: true,
    });

    return getResources(deleteAdventure, setIsLoading, "ADVENTURE.DELETED");
  };

  const getAdventure = async (
    adventureId: number
  ): Promise<AdventureDetails | undefined> => {
    setIsLoading(true);
    const getAdventure = await api.getAdventure(adventureId, {
      withCredentials: true,
    });

    return getResources(getAdventure, setIsLoading);
  };

  const findAdventures = async (
    request: AdventureSearchRequest
  ): Promise<AdventureBasicPage | undefined> => {
    setIsLoading(true);
    const findAdventures = await api.findAdventures(request, {
      withCredentials: true,
    });

    return getResources(findAdventures, setIsLoading);
  };

  const startAdventure = async (
    adventureId: number,
    characterId: number
  ): Promise<AdventureLite | undefined> => {
    setIsLoading(true);
    const startAdventure = await api.startAdventure(adventureId, characterId, {
      withCredentials: true,
    });

    return startAdventure(axiosPrivate)
      .then((response) => {
        characterStore.occupationType = "Adventure";
        successToast(t("ADVENTURE.STARTED"));
        return response.data;
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
          errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
        const adventure: AdventureLite = {
          id: -1,
          name: "",
          adventureTimeInMinutes: 0,
        };
        return adventure;
      })
      .finally(() => setIsLoading(false));
  };

  const endAdventure = async (
    characterId: number
  ): Promise<AdventureLite | undefined> => {
    setIsLoading(true);
    const endAdventure = await api.endAdventure(characterId, {
      withCredentials: true,
    });

    return endAdventure(axiosPrivate)
      .then((response) => {
        characterStore.occupationType = "Fight";
        successToast(t("FIGHT.STARTED"));
        return response.data;
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
          errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
        const adventure: AdventureLite = {
          id: -1,
          name: "",
          adventureTimeInMinutes: 0,
        };
        return adventure;
      })
      .finally(() => setIsLoading(false));
  };

  return {
    isLoading,
    create,
    editAdventure,
    deleteAdventure,
    getAdventure,
    findAdventures,
    startAdventure,
    endAdventure,
  };
};

export default useAdventureService;
