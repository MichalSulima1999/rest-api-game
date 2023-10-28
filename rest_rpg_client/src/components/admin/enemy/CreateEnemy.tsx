import React from "react";
import useEnemyService from "../../../services/useEnemyService";
import { EnemyCreateRequest } from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { EnemyCreateSchema } from "../../../validation/enemy/EnemyValidation";
import { useFormik } from "formik";
import {
  Box,
  Button,
  Flex,
  FormControl,
  Heading,
  Text,
} from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import FormikRadioGroup from "../../forms/FormikRadioGroup";

const CreateEnemy = () => {
  const { t } = useTranslation();
  const enemyService = useEnemyService();

  const handleSubmitFunc = async (values: EnemyCreateRequest) => {
    await enemyService.create(values);
  };

  const { values, errors, touched, handleChange, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      hp: 0,
      mana: 0,
      damage: 0,
      numberOfPotions: 0,
      skillId: 0,
      skillLevel: 0,
      enemyStrategy: [],
    },
    validationSchema: EnemyCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  return (
    <form onSubmit={handleSubmit}>
      <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("ENEMY.CREATE")}</Heading>
          <FormikInput
            error={errors.name}
            touched={touched.name}
            value={values.name}
            handleChange={handleChange}
            inputType="text"
            inputName="name"
            translationKey="ENEMY.NAME"
          />
          <FormikInput
            error={errors.hp}
            touched={touched.hp}
            value={values.hp}
            handleChange={handleChange}
            inputType="number"
            inputName="hp"
            translationKey="STATISTICS.HP"
          />
          <FormikInput
            error={errors.mana}
            touched={touched.mana}
            value={values.mana}
            handleChange={handleChange}
            inputType="number"
            inputName="mana"
            translationKey="STATISTICS.MANA"
          />
          <FormikInput
            error={errors.damage}
            touched={touched.damage}
            value={values.damage}
            handleChange={handleChange}
            inputType="number"
            inputName="damage"
            translationKey="STATISTICS.DAMAGE"
          />
          <FormikInput
            error={errors.numberOfPotions}
            touched={touched.numberOfPotions}
            value={values.numberOfPotions}
            handleChange={handleChange}
            inputType="number"
            inputName="numberOfPotions"
            translationKey="STATISTICS.NUMBER_OF_POTIONS"
          />
          {/* TODO: Lista pobierana z BE */}
          <FormikInput
            error={errors.skillId}
            touched={touched.skillId}
            value={values.skillId}
            handleChange={handleChange}
            inputType="number"
            inputName="skillId"
            translationKey="SKILLS.NAME"
          />
          <FormikInput
            error={errors.skillLevel}
            touched={touched.skillLevel}
            value={values.skillLevel}
            handleChange={handleChange}
            inputType="number"
            inputName="skillLevel"
            translationKey="SKILLS.LEVEL"
          />
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="2">
          <Heading mb={4}>{t("CHARACTER.STATISTICS.NAME")}</Heading>
          {/* <FormControl
            id={"freePoints"}
            mb={4}
            isInvalid={errors.statistics?.freePoints != null}
          >
            <Text onClick={() => console.log(errors)} fontSize="md">
              {t("CHARACTER.STATISTICS.FREE_POINTS")}:{" "}
              {values.statistics.freePoints}
            </Text>
            <Text>
              {errors.statistics?.freePoints &&
                t(`VALIDATION.${errors.statistics.freePoints}`)}
            </Text>
          </FormControl>
          <FormikInput
            error={errors.statistics?.strength}
            touched={touched.statistics?.strength}
            value={values.statistics?.strength}
            handleChange={handleChange}
            inputType="number"
            inputName="statistics.strength"
            translationKey="CHARACTER.STATISTICS.STRENGTH"
          />
          <FormikInput
            error={errors.statistics?.dexterity}
            touched={touched.statistics?.dexterity}
            value={values.statistics?.dexterity}
            handleChange={handleChange}
            inputType="number"
            inputName="statistics.dexterity"
            translationKey="CHARACTER.STATISTICS.DEXTERITY"
          />
          <FormikInput
            error={errors.statistics?.intelligence}
            touched={touched.statistics?.intelligence}
            value={values.statistics?.intelligence}
            handleChange={handleChange}
            inputType="number"
            inputName="statistics.intelligence"
            translationKey="CHARACTER.STATISTICS.INTELLIGENCE"
          />
          <FormikInput
            error={errors.statistics?.constitution}
            touched={touched.statistics?.constitution}
            value={values.statistics?.constitution}
            handleChange={handleChange}
            inputType="number"
            inputName="statistics.constitution"
            translationKey="CHARACTER.STATISTICS.CONSTITUTION"
          />
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={characterService.isLoading}
            type="submit"
          >
            {t("CHARACTER.CREATE")}
          </Button> */}
        </Box>
      </Flex>
    </form>
  );
};

export default CreateEnemy;
