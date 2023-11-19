import { useState } from "react";
import { DefaultApiFp, FightActionRequest } from "../generated-sources/openapi";
import useServiceHelper from "./helpers/useServiceHelper";
import { useStores } from "../store/RootStore";

const useFightService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();
  const { fightStore } = useStores();

  const getFight = async (characterId: number) => {
    setIsLoading(true);
    const getFight = await api.getFight(characterId, {
      withCredentials: true,
    });

    const response = getResources(getFight, setIsLoading);
    response
      .then((r) => {
        if (r) {
          fightStore.fightDetails(r);
        }
      })
      .catch((e) => console.log(e));
  };

  const performActionInFight = async (request: FightActionRequest) => {
    setIsLoading(true);
    const performActionInFight = await api.performActionInFight(request, {
      withCredentials: true,
    });

    const response = getResources(performActionInFight, setIsLoading);
    response
      .then((r) => {
        if (r) {
          fightStore.fightAction(r);
        }
      })
      .catch((e) => console.log(e));
  };

  return {
    isLoading,
    getFight,
    performActionInFight,
  };
};

export default useFightService;
