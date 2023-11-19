import { useEffect, useState } from "react";
import useFightService from "../../../services/useFightService";
import { useParams } from "react-router-dom";
import {
  ElementAction,
  FightActionRequest,
  FightDetails,
} from "../../../generated-sources/openapi";
import { Box, Button, Flex, Text, Tooltip } from "@chakra-ui/react";
import useCharacterService from "../../../services/useCharacterService";
import { useStores } from "../../../store/RootStore";
import { useTranslation } from "react-i18next";

const Fight = () => {
  const { t } = useTranslation();
  const fightService = useFightService();
  const characterService = useCharacterService();
  const { characterStore, statisticsStore, fightStore, enemyStore } =
    useStores();
  const { characterId } = useParams();

  useEffect(() => {
    async function getCharacterIfNeeded() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
    }
    getCharacterIfNeeded().catch((error) => console.log(error));

    async function getFightInfo() {
      if (characterId) {
        await fightService.getFight(Number(characterId));
      }
    }
    getFightInfo().catch((error) => console.log(error));
  }, []);

  const handleFight = (action: ElementAction, skillId?: number) => {
    const request: FightActionRequest = {
      characterId: Number(characterId),
      action: action,
      skillId: skillId,
    };
    fightService.performActionInFight(request).catch((e) => console.log(e));
  };

  return (
    <Box p={4}>
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
          <Text>Potions: {fightStore.fightInfo?.playerPotions}</Text>
        </Box>
        <Box>
          <Text fontSize="xl" fontWeight="bold">
            {enemyStore.name}
          </Text>
          <Text>
            Health: {fightStore.enemyCurrentHp}/{enemyStore.hp}
          </Text>
          <Text>
            Mana: {fightStore.enemyCurrentMana}/{enemyStore.mana}
          </Text>
        </Box>
      </Flex>

      <Box mb={4}>
        <Text fontSize="lg" fontWeight="bold">
          Battle Log
        </Text>
        <Text>Enemy hit: {fightStore.fightInfo?.enemyHit}</Text>
        <Text>Enemy damage: {fightStore.fightInfo?.enemyDamage}</Text>
        <Text>Enemy action: {fightStore.fightInfo?.enemyAction}</Text>
        <Text>
          Player critical strike: {fightStore.fightInfo?.playerCriticalStrike}
        </Text>
        <Text>Player damage: {fightStore.fightInfo?.playerDamage}</Text>
        <Text>Player won: {fightStore.fightInfo?.playerWon}</Text>
      </Box>

      <Flex justify="center">
        <Button
          onClick={() => handleFight(ElementAction.NormalAttack)}
          colorScheme="red"
          mr={4}
        >
          {t("FIGHT.ATTACK.NORMAL")}
        </Button>
        {characterStore.skills?.map((skill) => (
          <Tooltip
            label={skill.multiplier * statisticsStore.damage}
            aria-label="A tooltip"
          >
            <Button
              onClick={() => handleFight(ElementAction.SpecialAttack, skill.id)}
              colorScheme="blue"
              mr={4}
            >
              {skill.name}
            </Button>
          </Tooltip>
        ))}
        <Button
          onClick={() => handleFight(ElementAction.UsePotion)}
          colorScheme="red"
          mr={4}
        >
          {t("FIGHT.ATTACK.USE_POTION")}
        </Button>
      </Flex>
    </Box>
  );
};

export default Fight;
