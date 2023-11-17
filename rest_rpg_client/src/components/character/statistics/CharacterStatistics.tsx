import { Box, Button, Flex, Skeleton, Spacer, VStack } from "@chakra-ui/react";
import { useEffect } from "react";
import { useStores } from "../../../store/RootStore";
import useCharacterService from "../../../services/useCharacterService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useFormik } from "formik";
import { StatisticsUpdateSchema } from "../../../validation/statistics/StatisticsValidation";
import { StatisticsUpdateRequest } from "../../../generated-sources/openapi";
import useStatisticsService from "../../../services/useStatisticsService";
import FormikInput from "../../forms/FormikInput";

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
  const statisticsService = useStatisticsService();

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

  const handleSubmitFunc = async (values: StatisticsUpdateRequest) => {
    if (characterId) {
      await statisticsService.trainCharacter(parseInt(characterId), values);
    }
  };

  const { values, errors, touched, handleChange, handleSubmit, setFieldValue } =
    useFormik({
      initialValues: {
        strength: 0,
        dexterity: 0,
        constitution: 0,
        intelligence: 0,
        freePoints: 0,
      },
      validationSchema: StatisticsUpdateSchema,
      onSubmit: handleSubmitFunc,
    });

  useEffect(() => {
    async function updateFreePoints() {
      const newValue =
        statisticsStore.freeStatisticPoints -
        values.constitution -
        values.strength -
        values.dexterity -
        values.intelligence;
      await setFieldValue("freePoints", Math.max(0, newValue));
    }

    updateFreePoints().catch((error) => console.log(error));
  }, [
    values.constitution,
    values.strength,
    values.dexterity,
    values.intelligence,
    statisticsStore.freeStatisticPoints,
    setFieldValue,
  ]);

  return (
    <Box backgroundColor="gray.900">
      <Skeleton isLoaded={!isLoading}>
        <Box p={8} pb={0} bg="blackAlpha.800" color="white">
          <Statistic
            name={t("CHARACTER.STATISTICS.FREE_POINTS")}
            value={values.freePoints}
          />
        </Box>

        <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
          <Box p={8} bg="blackAlpha.800" color="white" flex="1">
            <VStack spacing={4} align="stretch">
              <form onSubmit={handleSubmit}>
                <Flex
                  color="white"
                  w="100%"
                  direction={{ base: "column", md: "row" }}
                >
                  <Box flex="2">
                    <Statistic
                      name={t("CHARACTER.STATISTICS.STRENGTH")}
                      value={statisticsStore.strength}
                    />
                  </Box>
                  <Box flex="1">
                    <FormikInput
                      error={errors.strength}
                      touched={touched.strength}
                      value={values.strength}
                      handleChange={handleChange}
                      inputType="number"
                      inputName="strength"
                      translationKey="CHARACTER.STATISTICS.STRENGTH"
                      disabled={statisticsStore.freeStatisticPoints < 1}
                    />
                  </Box>
                </Flex>
                <Flex
                  color="white"
                  w="100%"
                  direction={{ base: "column", md: "row" }}
                >
                  <Box flex="2">
                    <Statistic
                      name={t("CHARACTER.STATISTICS.DEXTERITY")}
                      value={statisticsStore.dexterity}
                    />
                  </Box>
                  <Box flex="1">
                    <FormikInput
                      error={errors.dexterity}
                      touched={touched.dexterity}
                      value={values.dexterity}
                      handleChange={handleChange}
                      inputType="number"
                      inputName="dexterity"
                      translationKey="CHARACTER.STATISTICS.DEXTERITY"
                      disabled={statisticsStore.freeStatisticPoints < 1}
                    />
                  </Box>
                </Flex>
                <Flex
                  color="white"
                  w="100%"
                  direction={{ base: "column", md: "row" }}
                >
                  <Box flex="2">
                    <Statistic
                      name={t("CHARACTER.STATISTICS.INTELLIGENCE")}
                      value={statisticsStore.strength}
                    />
                  </Box>
                  <Box flex="1">
                    <FormikInput
                      error={errors.intelligence}
                      touched={touched.intelligence}
                      value={values.intelligence}
                      handleChange={handleChange}
                      inputType="number"
                      inputName="intelligence"
                      translationKey="CHARACTER.STATISTICS.INTELLIGENCE"
                      disabled={statisticsStore.freeStatisticPoints < 1}
                    />
                  </Box>
                </Flex>
                <Flex
                  color="white"
                  w="100%"
                  direction={{ base: "column", md: "row" }}
                >
                  <Box flex="2">
                    <Statistic
                      name={t("CHARACTER.STATISTICS.CONSTITUTION")}
                      value={statisticsStore.constitution}
                    />
                  </Box>
                  <Box flex="1">
                    <FormikInput
                      error={errors.constitution}
                      touched={touched.constitution}
                      value={values.constitution}
                      handleChange={handleChange}
                      inputType="number"
                      inputName="constitution"
                      translationKey="CHARACTER.STATISTICS.CONSTITUTION"
                      disabled={statisticsStore.freeStatisticPoints < 1}
                    />
                  </Box>
                </Flex>
                <Button
                  mt={4}
                  colorScheme="teal"
                  isLoading={statisticsService.isLoading}
                  type="submit"
                >
                  {t("STATISTICS.UPDATE")}
                </Button>
              </form>
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
