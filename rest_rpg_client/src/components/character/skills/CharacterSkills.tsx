import { useState, useEffect } from "react";
import {
  ChakraProvider,
  extendTheme,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Box,
  Skeleton,
  Button,
  Heading,
} from "@chakra-ui/react";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import useSkillService from "../../../services/useSkillService";
import {
  CharacterSkillBasics,
  SkillBasicPage,
} from "../../../generated-sources/openapi";
import { useStores } from "../../../store/RootStore";
import useCharacterService from "../../../services/useCharacterService";
import Pagination from "../../tables/Pagination";

const theme = extendTheme({
  components: {
    Table: {
      baseStyle: {
        borderRadius: "lg",
        boxShadow: "md",
      },
    },
  },
});

const pageSize = 10;

function CharacterSkills() {
  const { t } = useTranslation();
  const skillService = useSkillService();
  const characterService = useCharacterService();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<SkillBasicPage | null>(null);
  const [characterSkills, setCharacterSkills] =
    useState<CharacterSkillBasics | null>(null);
  const { characterStore } = useStores();
  const { characterId } = useParams();

  useEffect(() => {
    async function getCharacterSkills() {
      if (characterId) {
        const skills = await skillService.getCharacterSkills(
          parseInt(characterId)
        );
        if (skills) {
          setCharacterSkills(skills);
        }
      }
    }
    getCharacterSkills().catch((error) => console.log(error));

    async function getSkillsPage() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
      const skills = await skillService.findSkills({
        characterClassIn: [characterStore.characterClass],
        idNotIn: characterSkills?.content?.map((s) => s.skill.id),
        pagination: { pageNumber: page, elements: pageSize },
      });
      if (skills) {
        setData(skills);
      }
    }
    getSkillsPage().catch((error) => console.log(error));
  }, [page]);

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleLearnSkill = (skillId: number) => {
    if (characterId) {
      skillService
        .learnSkill(skillId, parseInt(characterId))
        .catch((e) => console.log(e));
    }
  };

  return (
    <ChakraProvider theme={theme}>
      <Box p={4} bgColor={"gray.800"} color={"white"}>
        <Skeleton isLoaded={!skillService.isLoading}>
          <Heading mb={4}>{t("SKILL.LEARNED")}</Heading>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("SKILL.NAME")}</Th>
                <Th>{t("SKILL.TYPE.NAME")}</Th>
                <Th>{t("CHARACTER.CLASS.NAME")}</Th>
                <Th>{t("SKILL.EFFECT.NAME")}</Th>
                <Th>{t("SKILL.LEVEL")}</Th>
                <Th>{t("SKILL.GOLD_COST")}</Th>
                <Th>{t("SKILL.STATISTIC_POINTS_COST")}</Th>
                <Th>{t("SKILL.LEARN")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {characterSkills?.content?.map((item, index) => (
                <Tr key={index}>
                  <Td>{item.skill.name}</Td>
                  <Td>{t(`SKILL.TYPE.${item.skill.type}`)}</Td>
                  <Td>{t(`CHARACTER.CLASS.${item.skill.characterClass}`)}</Td>
                  <Td>
                    {item.skill.effect &&
                      t(`SKILL.EFFECT.${item.skill.effect}`)}
                  </Td>
                  <Td>{item.level}</Td>
                  <Td>{item.skill.skillTraining.goldCost}</Td>
                  <Td>{item.skill.skillTraining.statisticPointsCost}</Td>
                  <Td>
                    <Button onClick={() => handleLearnSkill(item.skill.id)}>
                      {t("SKILL.LEARN")}
                    </Button>
                  </Td>
                </Tr>
              ))}
            </Tbody>
          </Table>

          <Heading mb={4}>{t("SKILL.LEARN")}</Heading>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("SKILL.NAME")}</Th>
                <Th>{t("SKILL.TYPE.NAME")}</Th>
                <Th>{t("CHARACTER.CLASS.NAME")}</Th>
                <Th>{t("SKILL.EFFECT.NAME")}</Th>
                <Th>{t("SKILL.GOLD_COST")}</Th>
                <Th>{t("SKILL.STATISTIC_POINTS_COST")}</Th>
                <Th>{t("SKILL.LEARN")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {data?.content.map((item, index) => (
                <Tr key={index}>
                  <Td>{item.name}</Td>
                  <Td>{t(`SKILL.TYPE.${item.type}`)}</Td>
                  <Td>{t(`CHARACTER.CLASS.${item.characterClass}`)}</Td>
                  <Td>{item.effect && t(`SKILL.EFFECT.${item.effect}`)}</Td>
                  <Td>{item.skillTraining.goldCost}</Td>
                  <Td>{item.skillTraining.statisticPointsCost}</Td>
                  <Td>
                    <Button onClick={() => handleLearnSkill(item.id)}>
                      {t("SKILL.LEARN")}
                    </Button>
                  </Td>
                </Tr>
              ))}
            </Tbody>
          </Table>

          {data && (
            <Pagination
              currentPage={data.number || 1}
              totalPages={
                data.totalElements
                  ? Math.ceil(data.totalElements / pageSize)
                  : 1
              }
              onPageChange={handlePageChange}
            />
          )}
        </Skeleton>
      </Box>
    </ChakraProvider>
  );
}

export default CharacterSkills;
