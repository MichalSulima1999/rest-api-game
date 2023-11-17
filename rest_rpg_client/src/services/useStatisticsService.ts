import { useState } from "react";
import {
  DefaultApiFp,
  StatisticsDetails,
  StatisticsUpdateRequest,
} from "../generated-sources/openapi";
import useServiceHelper from "./helpers/useServiceHelper";
import { useStores } from "../store/RootStore";

const useStatisticsService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const { statisticsStore } = useStores();
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();

  const getStatistics = async (
    characterId: number
  ): Promise<StatisticsDetails | undefined> => {
    setIsLoading(true);
    const getStatistics = await api.getStatistics(characterId, {
      withCredentials: true,
    });

    return getResources(getStatistics, setIsLoading);
  };

  const trainCharacter = async (
    characterId: number,
    request: StatisticsUpdateRequest
  ) => {
    setIsLoading(true);
    const getStatistics = await api.trainCharacter(characterId, request, {
      withCredentials: true,
    });

    const statistics = getResources(
      getStatistics,
      setIsLoading,
      "STATISTICS.UPDATED_SUCCESSFULLY"
    );
    statistics
      .then((s) => {
        if (s) {
          statisticsStore.statisticsDetails(s);
        }
      })
      .catch((e) => console.log(e));
  };

  return {
    isLoading,
    getStatistics,
    trainCharacter,
  };
};

export default useStatisticsService;
