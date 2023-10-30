import { SkillCreateRequest } from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { useFormik } from "formik";
import { Box, Button, Flex, HStack, Heading } from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import useSkillService from "../../../services/useSkillService";
import {
  SkillCreateSchema,
  skillEffects,
  skillTypes,
} from "../../../validation/skill/SkillValidation";
import { classes } from "../../../validation/character/CharacterValidation";
import FormikRadioGroup from "../../forms/FormikRadioGroup";

const CreateSkill = () => {
  const { t } = useTranslation();
  const skillService = useSkillService();

  const handleSubmitFunc = async (values: SkillCreateRequest) => {
    await skillService.create(values);
  };

  const { values, errors, touched, handleChange, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      manaCost: 0,
      type: skillTypes[0],
      multiplier: 0,
      multiplierPerLevel: 0,
      effect: "",
      effectDuration: undefined,
      effectDurationPerLevel: undefined,
      effectMultiplier: undefined,
      effectMultiplierPerLevel: undefined,
      characterClass: classes[0],
    },
    validationSchema: SkillCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  return (
    <form onSubmit={handleSubmit}>
      <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("SKILL.CREATE")}</Heading>
          <FormikInput
            error={errors.name}
            touched={touched.name}
            value={values.name}
            handleChange={handleChange}
            inputType="text"
            inputName="name"
            translationKey="SKILL.NAME"
          />
          <FormikRadioGroup
            error={errors.type}
            touched={touched.type}
            value={values.type}
            handleChange={handleChange}
            radioValues={skillTypes}
            inputName="type"
            translationKey="SKILL.TYPE"
          />
          <FormikRadioGroup
            error={errors.characterClass}
            touched={touched.characterClass}
            value={values.characterClass}
            handleChange={handleChange}
            radioValues={classes}
            inputName="characterClass"
            translationKey="CHARACTER.CLASS"
          />
          <HStack spacing="8px" align={"end"}>
            <FormikInput
              error={errors.manaCost}
              touched={touched.manaCost}
              value={values.manaCost}
              handleChange={handleChange}
              inputType="number"
              inputName="manaCost"
              translationKey="SKILL.MANA_COST"
            />
            <FormikInput
              error={errors.multiplier}
              touched={touched.multiplier}
              value={values.multiplier}
              handleChange={handleChange}
              inputType="number"
              step={0.05}
              inputName="multiplier"
              translationKey="SKILL.MULTIPLIER"
            />
            <FormikInput
              error={errors.multiplierPerLevel}
              touched={touched.multiplierPerLevel}
              value={values.multiplierPerLevel}
              handleChange={handleChange}
              inputType="number"
              step={0.05}
              inputName="multiplierPerLevel"
              translationKey="SKILL.MULTIPLIER_PER_LEVEL"
            />
          </HStack>
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("SKILL.EFFECT.NAME")}</Heading>
          <FormikRadioGroup
            error={errors.effect}
            touched={touched.effect}
            value={values.effect}
            handleChange={handleChange}
            radioValues={skillEffects}
            inputName="effect"
            translationKey="SKILL.EFFECT"
            isRequired={false}
          />
          <HStack spacing="8px">
            <FormikInput
              error={errors.effectDuration}
              touched={touched.effectDuration}
              value={values.effectDuration}
              handleChange={handleChange}
              inputType="number"
              inputName="effectDuration"
              translationKey="SKILL.EFFECT.DURATION"
              isRequired={false}
            />
            <FormikInput
              error={errors.effectDurationPerLevel}
              touched={touched.effectDurationPerLevel}
              value={values.effectDurationPerLevel}
              handleChange={handleChange}
              inputType="number"
              inputName="effectDurationPerLevel"
              translationKey="SKILL.EFFECT.DURATION_PER_LEVEL"
              isRequired={false}
            />
          </HStack>

          <HStack spacing="8px">
            <FormikInput
              error={errors.effectMultiplier}
              touched={touched.effectMultiplier}
              value={values.effectMultiplier}
              handleChange={handleChange}
              inputType="number"
              step={0.05}
              inputName="effectMultiplier"
              translationKey="SKILL.EFFECT.MULTIPLIER"
              isRequired={false}
            />
            <FormikInput
              error={errors.effectMultiplierPerLevel}
              touched={touched.effectMultiplierPerLevel}
              value={values.effectMultiplierPerLevel}
              handleChange={handleChange}
              inputType="number"
              step={0.05}
              inputName="effectMultiplierPerLevel"
              translationKey="SKILL.EFFECT.MULTIPLIER_PER_LEVEL"
              isRequired={false}
            />
          </HStack>
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={skillService.isLoading}
            type="submit"
          >
            {t("SKILL.CREATE")}
          </Button>
        </Box>
      </Flex>
    </form>
  );
};

export default CreateSkill;
