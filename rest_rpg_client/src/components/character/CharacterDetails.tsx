import { useEffect } from "react";
import {
  Box,
  Text,
  Center,
  Skeleton,
  Image,
  Flex,
  Grid,
  GridItem,
  Progress,
  ThemeTypings,
} from "@chakra-ui/react";
import { useStores } from "../../store/RootStore";
import useCharacterService, {
  THUMBNAIL_URL,
} from "../../services/useCharacterService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

interface ProgressStatProps {
  currentValue: number;
  maxValue: number;
  color: ThemeTypings["colorSchemes"];
}

const ProgressStat = ({ currentValue, maxValue, color }: ProgressStatProps) => {
  return (
    <Center>
      <Progress
        marginRight={4}
        width="100%"
        colorScheme={color}
        size="lg"
        value={currentValue}
        max={maxValue}
      />
      <Text width="50%">
        {currentValue}/{maxValue}
      </Text>
    </Center>
  );
};

const CharacterDetails = () => {
  const { characterStore, statisticsStore } = useStores();
  const { getUserCharacter, isLoading } = useCharacterService();
  const { characterId } = useParams();
  const { t } = useTranslation();

  useEffect(() => {
    async function getCharacter() {
      if (characterStore.id == -1 && characterId) {
        await getUserCharacter(parseInt(characterId));
      }
    }
    getCharacter().catch((error) => console.log(error));
  }, []);

  return (
    <Box backgroundColor="gray.900">
      <Skeleton isLoaded={!isLoading}>
        <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
          <Box p={8} bg="blackAlpha.800" color="white" flex="1">
            <Center>
              <Box
                borderRadius="lg"
                p="1"
                boxShadow="md"
                maxWidth="20vw"
                backgroundColor="gray.700"
              >
                <Image src={THUMBNAIL_URL + "/" + characterStore.artwork} />
              </Box>
            </Center>
          </Box>
          <Box p={8} bg="blackAlpha.800" color="white" flex="2">
            <Flex
              color="white"
              w="100%"
              direction={{ base: "column", md: "row" }}
              gap={2}
            >
              <Grid
                templateColumns="repeat(6, 1fr)"
                gap={4}
                backgroundColor="gray.800"
                border="1px"
                borderRadius="lg"
                textAlign="left"
                p="8px"
                flex={1}
              >
                <GridItem colSpan={2}>
                  <Text>{t("CHARACTER.NAME_SHORT")}:</Text>
                  <Text>{t("CHARACTER.SEX.NAME")}:</Text>
                  <Text>{t("CHARACTER.RACE.NAME")}:</Text>
                  <Text>{t("CHARACTER.CLASS.NAME")}:</Text>
                </GridItem>
                <GridItem colSpan={4}>
                  <Text>{characterStore.name}</Text>
                  <Text>{t(`CHARACTER.SEX.${characterStore.sex}`)}</Text>
                  <Text>{t(`CHARACTER.RACE.${characterStore.race}`)}</Text>
                  <Text>
                    {t(`CHARACTER.CLASS.${characterStore.characterClass}`)}
                  </Text>
                </GridItem>
              </Grid>
              <Grid
                templateColumns="repeat(6, 1fr)"
                gap={4}
                backgroundColor="gray.800"
                border="1px"
                borderRadius="lg"
                textAlign="left"
                p="8px"
                flex={1}
              >
                <GridItem colSpan={2}>
                  <Text>{t("CHARACTER.STATISTICS.HP")}:</Text>
                  <Text>{t("CHARACTER.STATISTICS.MANA")}:</Text>
                  <Text>{t("CHARACTER.LEVEL")}:</Text>
                  <Text>{t("CHARACTER.STATISTICS.XP")}:</Text>
                </GridItem>
                <GridItem colSpan={4}>
                  <ProgressStat
                    currentValue={statisticsStore.currentHp}
                    maxValue={statisticsStore.maxHp}
                    color="green"
                  />
                  <ProgressStat
                    currentValue={statisticsStore.currentMana}
                    maxValue={statisticsStore.maxMana}
                    color="cyan"
                  />
                  <Text>{statisticsStore.currentLevel}</Text>
                  <ProgressStat
                    currentValue={statisticsStore.currentXp}
                    maxValue={statisticsStore.xpToNextLevel}
                    color="purple"
                  />
                </GridItem>
              </Grid>
            </Flex>
          </Box>
        </Flex>
      </Skeleton>
    </Box>
  );
};

export default CharacterDetails;
