import {
  Box,
  Button,
  Flex,
  FormControl,
  Heading,
  Text,
} from "@chakra-ui/react";
import { useFormik } from "formik";
import { useTranslation } from "react-i18next";
import {
  CharacterCreateSchema,
  characterCreateMaxSkillpoints,
  classes,
  races,
  sexes,
} from "../../validation/character/CharacterValidation";
import useCharacterService from "../../services/useCharacterService";
import { CharacterCreateRequest } from "../../generated-sources/openapi";
import FormikRadioGroup from "./FormikRadioGroup";
import FormikInput from "./FormikInput";
import { useEffect } from "react";

const CharacterCreator = () => {
  const { t } = useTranslation();
  const characterService = useCharacterService();

  const handleSubmitFunc = async (values: CharacterCreateRequest) => {
    await characterService.create(values);
  };

  const { values, errors, touched, handleChange, setFieldValue, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      race: races[0],
      sex: sexes[0],
      characterClass: classes[0],
      artwork: "HUMAN_MALE_1",
      statistics: {
        strength: 10,
        dexterity: 10,
        constitution: 10,
        intelligence: 10,
        freePoints: 50,
      },
    },
    validationSchema: CharacterCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  useEffect(() => {
    async function fetchData() {
      const newValue =
        characterCreateMaxSkillpoints -
        values.statistics.constitution -
        values.statistics.strength -
        values.statistics.dexterity -
        values.statistics.intelligence;
      await setFieldValue("statistics.freePoints", Math.max(0, newValue));
    }
    fetchData().catch((error) => console.log(error));
  }, [
    values.statistics.constitution,
    values.statistics.strength,
    values.statistics.dexterity,
    values.statistics.intelligence,
    setFieldValue
  ]);

  return (
    <form onSubmit={handleSubmit}>
      <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("CHARACTER.CREATE")}</Heading>
          <FormikInput
            error={errors.name}
            touched={touched.name}
            value={values.name}
            handleChange={handleChange}
            inputType="text"
            inputName="name"
            translationKey="CHARACTER.NAME"
          />
          <FormikRadioGroup
            error={errors.race}
            touched={touched.race}
            value={values.race}
            handleChange={handleChange}
            radioValues={races}
            inputName="race"
            translationKey="CHARACTER.RACE"
          />
          <FormikRadioGroup
            error={errors.sex}
            touched={touched.sex}
            value={values.sex}
            handleChange={handleChange}
            radioValues={sexes}
            inputName="sex"
            translationKey="CHARACTER.SEX"
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
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={characterService.isLoading}
            type="submit"
          >
            {t("CHARACTER.CREATE")}
          </Button>
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="2">
          <Heading mb={4}>{t("CHARACTER.STATISTICS.NAME")}</Heading>
          <FormControl
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
        </Box>
      </Flex>
    </form>
  );
};

export default CharacterCreator;
