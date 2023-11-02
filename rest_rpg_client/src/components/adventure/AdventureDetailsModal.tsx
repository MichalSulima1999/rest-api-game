import React, { useEffect, useState } from "react";
import {
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Text,
  Box,
  Skeleton,
} from "@chakra-ui/react";
import useAdventureService from "../../services/useAdventureService";
import { AdventureDetails } from "../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import { useStores } from "../../store/RootStore";

interface AdventureDetailsProps {
  isOpen: boolean;
  onClose: () => void;
  adventureId: number;
}

const AdventureDetailsModal: React.FC<AdventureDetailsProps> = ({
  isOpen,
  onClose,
  adventureId,
}) => {
  const { t } = useTranslation();
  const adventureService = useAdventureService();
  const [adventure, setAdventure] = useState<AdventureDetails | null>(null);
  const { characterStore } = useStores();
  const { characterId } = useParams();

  useEffect(() => {
    if (!adventureId) return;
    async function getAdventurePage() {
      const adventure = await adventureService.getAdventure(adventureId);
      if (adventure) {
        setAdventure(adventure);
      }
    }
    getAdventurePage().catch((error) => console.log(error));
  }, [adventureId]);

  const handleStartAdventure = () => {
    if (adventureId && characterId) {
      adventureService
        .startAdventure(adventureId, Number(characterId))
        .catch((e) => console.log(e));
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="lg">
      <ModalOverlay />
      <ModalContent bgColor={"darkcyan"} color={"white"}>
        <Skeleton isLoaded={!adventureService.isLoading}>
          <ModalHeader>{adventure?.name}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Text>
              {t("ADVENTURE.TIME")}: {adventure?.adventureTimeInMinutes} minutes
            </Text>
            <Text>
              {t("CHARACTER.STATISTICS.XP")}: {adventure?.xpForAdventure}
            </Text>
            <Text>
              {t("CHARACTER.STATISTICS.GOLD")}: {adventure?.goldForAdventure}
            </Text>
            {adventure?.enemy && (
              <Box mt={4}>
                <Text fontWeight="bold">{t("ENEMY.MAIN")} </Text>
                <Text>
                  {t("ENEMY.NAME")}: {adventure?.enemy.name}
                </Text>
                <Text>
                  {t("CHARACTER.STATISTICS.HP")}: {adventure?.enemy.hp}
                </Text>
                <Text>
                  {t("CHARACTER.STATISTICS.MANA")}: {adventure?.enemy.mana}
                </Text>
                <Text>
                  {t("CHARACTER.STATISTICS.DAMAGE")}: {adventure?.enemy.damage}
                </Text>
                <Text>
                  {t("SKILL.LEVEL")}: {adventure?.enemy.skillLevel}
                </Text>
                <Text>
                  {t("CHARACTER.STATISTICS.NUMBER_OF_POTIONS")}
                  {": "}
                  {adventure?.enemy.numberOfPotions}
                </Text>
                <Box mt={4}>
                  <Text fontWeight="bold">{t("SKILL.MAIN")}</Text>
                  <Text>
                    {t("SKILL.NAME")}: {adventure?.enemy.skill.name}
                  </Text>
                  <Text>
                    {t("SKILL.TYPE.NAME")}:{" "}
                    {t(`SKILL.TYPE.${adventure?.enemy.skill.type}`)}
                  </Text>
                  {adventure?.enemy.skill.effect && (
                    <Text>
                      {t("SKILL.EFFECT.NAME")}:{" "}
                      {t(`SKILL.EFFECT.${adventure?.enemy.skill.effect}`)}
                    </Text>
                  )}
                </Box>
              </Box>
            )}
          </ModalBody>
          <ModalFooter>
            <Button
              colorScheme="blue"
              onClick={handleStartAdventure}
              mr={3}
              isDisabled={characterStore.occupationType != ""}
            >
              {t("ADVENTURE.START")}
            </Button>
            <Button colorScheme="blue" onClick={onClose}>
              {t("CLOSE")}
            </Button>
          </ModalFooter>
        </Skeleton>
      </ModalContent>
    </Modal>
  );
};

export default AdventureDetailsModal;
