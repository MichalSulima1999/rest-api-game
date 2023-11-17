import { useState } from "react";
import {
  DefaultApiFp,
  FightActionRequest,
  FightActionResponse,
  FightDetails,
} from "../generated-sources/openapi";
import useServiceHelper from "./helpers/useServiceHelper";

const useFightService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();

  const getFight = async (
    characterId: number
  ): Promise<FightDetails | undefined> => {
    setIsLoading(true);
    const getFight = await api.getFight(characterId, {
      withCredentials: true,
    });

    return getResources(getFight, setIsLoading);
  };

  const performActionInFight = async (
    request: FightActionRequest
  ): Promise<FightActionResponse | undefined> => {
    setIsLoading(true);
    const performActionInFight = await api.performActionInFight(request, {
      withCredentials: true,
    });

    return getResources(performActionInFight, setIsLoading);
  };

  return {
    isLoading,
    getFight,
    performActionInFight,
  };
};

export default useFightService;
