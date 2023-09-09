import { Box, Flex, Skeleton, Spacer, VStack } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useStores } from "../../store/RootStore";
import useCharacterService from "../../services/useCharacterService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

interface StatisticProps {
  name: string;
  value: string | number;
}

export const Statistic = ({ name, value }: StatisticProps) => {
  return (
    <Box>
      <Flex backgroundColor="gray.900" borderRadius="lg">
        <Box p="4">{name}</Box>
        <Spacer />
        <Box p="4">{value}</Box>
      </Flex>
    </Box>
  );
};

const CharacterStatistics = () => {
  const { statisticsStore } = useStores();
  const { getCharacterStatistics, isLoading } = useCharacterService();
  const { characterId } = useParams();
  const { t } = useTranslation();

  useEffect(() => {
    async function getStatistics() {
      if (characterId) {
        const statistics = await getCharacterStatistics(parseInt(characterId));
        if (statistics) {
          statisticsStore.statisticsDetails(statistics);
        }
      }
    }
    getStatistics().catch((error) => console.log(error));
  }, []);

  return (
    <Box backgroundColor="gray.900">
      <Skeleton isLoaded={!isLoading}>
        <Box p={8} pb={0} bg="blackAlpha.800" color="white">
          <Statistic
            name={t("CHARACTER.STATISTICS.FREE_POINTS")}
            value={statisticsStore.freeStatisticPoints}
          />
        </Box>

        <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
          <Box p={8} bg="blackAlpha.800" color="white" flex="1">
            <VStack spacing={4} align="stretch">
              <Statistic
                name={t("CHARACTER.STATISTICS.STRENGTH")}
                value={statisticsStore.strength}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.DEXTERITY")}
                value={statisticsStore.dexterity}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.INTELLIGENCE")}
                value={statisticsStore.intelligence}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.CONSTITUTION")}
                value={statisticsStore.constitution}
              />
            </VStack>
          </Box>
          <Box p={8} bg="blackAlpha.800" color="white" flex="2">
            <VStack spacing={4} align="stretch">
              <Statistic
                name={t("CHARACTER.STATISTICS.DAMAGE")}
                value={statisticsStore.damage}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.MAGIC_DAMAGE")}
                value={statisticsStore.magicDamage}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.ARMOR")}
                value={statisticsStore.armor}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.DODGE_CHANCE")}
                value={`${statisticsStore.dodgeChance}%`}
              />
              <Statistic
                name={t("CHARACTER.STATISTICS.CRITICAL_CHANCE")}
                value={`${statisticsStore.criticalChance}%`}
              />
            </VStack>
          </Box>
        </Flex>
      </Skeleton>
    </Box>
  );
};

export default CharacterStatistics;
