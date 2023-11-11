import {
  AdventureCreateRequest,
  EnemyLites,
} from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { useFormik } from "formik";
import { Box, Button, Flex, Heading, Skeleton } from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import useAdventureService from "../../../services/useAdventureService";
import { AdventureCreateSchema } from "../../../validation/adventure/AdventureValidation";
import useEnemyService from "../../../services/useEnemyService";
import { useEffect, useState } from "react";
import FormikSelect from "../../forms/FormikSelect";

const CreateAdventure = () => {
  const { t } = useTranslation();
  const adventureService = useAdventureService();
  const enemyService = useEnemyService();
  const [enemies, setEnemies] = useState<EnemyLites>();

  useEffect(() => {
    async function getEnemies() {
      const enemies = await enemyService.getAllEnemies();
      if (enemies) {
        setEnemies(enemies);
      }
    }
    getEnemies().catch((error) => console.log(error));
  }, []);

  const handleSubmitFunc = async (values: AdventureCreateRequest) => {
    await adventureService.create(values);
  };

  const { values, errors, touched, handleChange, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      adventureLengthInMinutes: 0,
      xpForAdventure: 0,
      goldForAdventure: 0,
      enemy: 0,
    },
    validationSchema: AdventureCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  return (
    <form onSubmit={handleSubmit}>
      <Box p={8} bg="blackAlpha.800" color="white">
        <Heading mb={4}>{t("ADVENTURE.CREATE")}</Heading>
        <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
          <Box flex={2}>
            <FormikInput
              error={errors.name}
              touched={touched.name}
              value={values.name}
              handleChange={handleChange}
              inputType="text"
              inputName="name"
              translationKey="ADVENTURE.NAME"
            />
          </Box>

          <Skeleton isLoaded={!enemyService.isLoading} flex={1}>
            <FormikSelect
              error={errors.enemy}
              touched={touched.enemy}
              value={values.enemy}
              handleChange={handleChange}
              selectValues={enemies?.content?.map(({ id: value, name }) => ({
                value,
                name,
              }))}
              inputName="enemy"
              translationKey="ENEMY"
              placeholder={t("ENEMY.NAME")}
            />
          </Skeleton>
        </Flex>
        <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
          <FormikInput
            error={errors.adventureLengthInMinutes}
            touched={touched.adventureLengthInMinutes}
            value={values.adventureLengthInMinutes}
            handleChange={handleChange}
            inputType="number"
            inputName="adventureLengthInMinutes"
            translationKey="ADVENTURE.LENGTH"
          />
          <FormikInput
            error={errors.xpForAdventure}
            touched={touched.xpForAdventure}
            value={values.xpForAdventure}
            handleChange={handleChange}
            inputType="number"
            inputName="xpForAdventure"
            translationKey="CHARACTER.STATISTICS.XP"
          />
          <FormikInput
            error={errors.goldForAdventure}
            touched={touched.goldForAdventure}
            value={values.goldForAdventure}
            handleChange={handleChange}
            inputType="number"
            inputName="goldForAdventure"
            translationKey="CHARACTER.STATISTICS.GOLD"
          />
        </Flex>
        <Button
          mt={4}
          colorScheme="teal"
          isLoading={adventureService.isLoading}
          type="submit"
        >
          {t("ADVENTURE.CREATE")}
        </Button>
      </Box>
    </form>
  );
};

export default CreateAdventure;
