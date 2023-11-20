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
  Button,
  Heading,
} from "@chakra-ui/react";
import { useStores } from "../../store/RootStore";
import useCharacterService, {
  THUMBNAIL_URL,
} from "../../services/useCharacterService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import useEquipmentService from "../../services/useEquipmentService";

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
  const { characterStore, statisticsStore, equipmentStore } = useStores();
  const equipmentService = useEquipmentService();
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

  const handleUsePotion = () => {
    if (characterId) {
      equipmentService
        .usePotion(Number(characterId))
        .catch((e) => console.log(e));
    }
  };

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
            <Heading as="h3" size="lg">
              {t("EQUIPMENT.NAME")}
            </Heading>
            <Flex
              color="white"
              w="100%"
              direction={{ base: "column", md: "row" }}
              gap={2}
            >
              <Box
                gap={4}
                backgroundColor="gray.800"
                border="1px"
                borderRadius="lg"
                textAlign="left"
                p="8px"
                flex={1}
              >
                <Heading as="h3" size="lg">
                  {t("ITEM.TYPE.ARMOR")}
                </Heading>
                <Grid templateColumns="repeat(6, 1fr)">
                  <GridItem colSpan={2}>
                    <Text>{t("ITEM.NAME")}:</Text>
                    <Text>{t("ITEM.POWER")}:</Text>
                  </GridItem>
                  <GridItem colSpan={4}>
                    <Text>{equipmentStore.armor?.name}</Text>
                    <Text>{equipmentStore.armor?.power}</Text>
                  </GridItem>
                </Grid>
              </Box>
              <Box
                gap={4}
                backgroundColor="gray.800"
                border="1px"
                borderRadius="lg"
                textAlign="left"
                p="8px"
                flex={1}
              >
                <Heading as="h3" size="lg">
                  {t("ITEM.TYPE.WEAPON")}
                </Heading>
                <Grid templateColumns="repeat(6, 1fr)">
                  <GridItem colSpan={2}>
                    <Text>{t("ITEM.NAME")}:</Text>
                    <Text>{t("ITEM.POWER")}:</Text>
                  </GridItem>
                  <GridItem colSpan={4}>
                    <Text>{equipmentStore.weapon?.name}</Text>
                    <Text>{equipmentStore.weapon?.power}</Text>
                  </GridItem>
                </Grid>
              </Box>
            </Flex>
            <Box
              gap={4}
              backgroundColor="gray.800"
              border="1px"
              borderRadius="lg"
              textAlign="left"
              p="8px"
              flex={1}
            >
              <Grid templateColumns="repeat(6, 1fr)">
                <GridItem colSpan={2}>
                  <Text>{t("CHARACTER.STATISTICS.GOLD")}:</Text>
                  <Text>{t("CHARACTER.STATISTICS.NUMBER_OF_POTIONS")}:</Text>
                </GridItem>
                <GridItem colSpan={4}>
                  <Text>{equipmentStore.gold}</Text>
                  <Text>{equipmentStore.healthPotions}</Text>
                </GridItem>
              </Grid>
            </Box>
            <Button mt={4} onClick={handleUsePotion}>
              {t("ITEM.USE_POTION")}
            </Button>
          </Box>
        </Flex>
      </Skeleton>
    </Box>
  );
};

export default CharacterDetails;
