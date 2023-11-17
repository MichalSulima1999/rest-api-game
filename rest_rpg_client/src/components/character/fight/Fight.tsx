import React, { useEffect, useState } from "react";
import useFightService from "../../../services/useFightService";
import { useParams } from "react-router-dom";
import {
  CharacterSkillBasics,
  ElementAction,
  FightActionRequest,
  FightDetails,
} from "../../../generated-sources/openapi";
import { Box, Button, Flex, Text } from "@chakra-ui/react";
import useCharacterService from "../../../services/useCharacterService";
import { useStores } from "../../../store/RootStore";
import { useTranslation } from "react-i18next";
import useSkillService from "../../../services/useSkillService";

const Fight = () => {
  const { t } = useTranslation();
  const fightService = useFightService();
  const characterService = useCharacterService();
  const skillService = useSkillService();
  const { characterStore, statisticsStore } = useStores();
  const { characterId } = useParams();
  const [fight, setFight] = useState<FightDetails | null>(null);
  const [characterSkills, setCharacterSkills] = useState<CharacterSkillBasics | null>(null);

  useEffect(() => {
    async function getCharacterIfNeeded() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
    }
    getCharacterIfNeeded().catch((error) => console.log(error));

    async function getFightInfo() {
      if (characterId) {
        const fightInfo = await fightService.getFight(Number(characterId));
        const characterSkills = await skillService.getCharacterSkills(Number(characterId));
        if (fightInfo) {
          setFight(fightInfo);
        }
        if (characterSkills) {
            setCharacterSkills(characterSkills);
          }
      }
    }
    getFightInfo().catch((error) => console.log(error));
  }, []);

  const handleFight = (action: ElementAction) => {
    const request: FightActionRequest = {
      characterId: Number(characterId),
      action: action,
    };
    fightService.performActionInFight(request).catch((e) => console.log(e));
  };

  return (
    <Box p={4}>
      {/* Pasek informacji o graczu i przeciwniku */}
      <Flex justify="space-between" mb={4}>
        <Box>
          <Text fontSize="xl" fontWeight="bold">
            {characterStore.name}
          </Text>
          <Text>
            Health: {statisticsStore.currentHp}/{statisticsStore.maxHp}
          </Text>
          <Text>
            Mana: {statisticsStore.currentMana}/{statisticsStore.maxMana}
          </Text>
        </Box>
        <Box>
          <Text fontSize="xl" fontWeight="bold">
            {fight?.enemy?.name}
          </Text>
          <Text>
            Health: {fight?.enemyCurrentHp}/{fight?.enemy?.hp}
          </Text>
          <Text>
            Mana: {fight?.enemyCurrentMana}/{fight?.enemy?.mana}
          </Text>
        </Box>
      </Flex>

      <Box mb={4}>
        <Text fontSize="lg" fontWeight="bold">
          Battle Log
        </Text>
        <Text></Text>
      </Box>

      {/* Pasek z przyciskami */}
      <Flex justify="center">
        <Button onClick={() => handleFight(ElementAction.NormalAttack)} colorScheme="red" mr={4}>
          {t("FIGHT.ATTACK.NORMAL")}
        </Button>
        {characterSkills?.content?.map(skill => skill.skill.)}
      </Flex>
    </Box>
  );
};

export default Fight;
