import { useEffect, useState } from "react";
import useEnemyService from "../../../services/useEnemyService";
import {
  EnemyCreateRequest,
  SkillLites,
  StrategyElementCreateRequest,
} from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import {
  EnemyCreateSchema,
  strategyActions,
  strategyEvents,
} from "../../../validation/enemy/EnemyValidation";
import { FormikErrors, useFormik } from "formik";
import { Box, Button, Flex, Heading, Skeleton } from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import FormikSelect from "../../forms/FormikSelect";
import useSkillService from "../../../services/useSkillService";

const CreateEnemy = () => {
  const { t } = useTranslation();
  const enemyService = useEnemyService();
  const skillService = useSkillService();
  const [skills, setSkills] = useState<SkillLites>();

  useEffect(() => {
    async function getSkills() {
      const skills = await skillService.getAllSkills();
      if (skills) {
        setSkills(skills);
      }
    }
    getSkills().catch((error) => console.log(error));
  }, []);

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
      enemyStrategy: strategyEvents.map((el) => ({
        event: el,
        action: strategyActions[0],
        priority: 1,
      })),
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
            translationKey="CHARACTER.STATISTICS.HP"
          />
          <FormikInput
            error={errors.mana}
            touched={touched.mana}
            value={values.mana}
            handleChange={handleChange}
            inputType="number"
            inputName="mana"
            translationKey="CHARACTER.STATISTICS.MANA"
          />
          <FormikInput
            error={errors.damage}
            touched={touched.damage}
            value={values.damage}
            handleChange={handleChange}
            inputType="number"
            inputName="damage"
            translationKey="CHARACTER.STATISTICS.DAMAGE"
          />
          <FormikInput
            error={errors.numberOfPotions}
            touched={touched.numberOfPotions}
            value={values.numberOfPotions}
            handleChange={handleChange}
            inputType="number"
            inputName="numberOfPotions"
            translationKey="CHARACTER.STATISTICS.NUMBER_OF_POTIONS"
          />
          <Skeleton isLoaded={!skillService.isLoading}>
            <FormikSelect
              error={errors.skillId}
              touched={touched.skillId}
              value={values.skillId}
              handleChange={handleChange}
              selectValues={skills?.content?.map(({ id: value, name }) => ({
                value,
                name,
              }))}
              inputName="skillId"
              translationKey="SKILL"
              placeholder={t("SKILL.NAME")}
            />
          </Skeleton>
          <FormikInput
            error={errors.skillLevel}
            touched={touched.skillLevel}
            value={values.skillLevel}
            handleChange={handleChange}
            inputType="number"
            inputName="skillLevel"
            translationKey="SKILL.LEVEL"
          />
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="3">
          <Heading mb={4}>{t("STRATEGY.NAME")}</Heading>
          {strategyEvents.map((el, i) => (
            <>
              <Flex
                key={el}
                align={"baseline"}
                color="white"
                w="100%"
                direction={{ base: "column", md: "row" }}
              >
                <Box flex={2}>
                  <FormikInput
                    error={
                      errors.enemyStrategy &&
                      errors.enemyStrategy[i] &&
                      (
                        errors.enemyStrategy[
                          i
                        ] as FormikErrors<StrategyElementCreateRequest>
                      ).event
                    }
                    touched={
                      touched.enemyStrategy && touched.enemyStrategy[i].event
                    }
                    value={t(`STRATEGY.${values.enemyStrategy[i].event}`)}
                    inputType="text"
                    inputName={`enemyStrategy[${i}].event`}
                    translationKey="STRATEGY.EVENT"
                    disabled
                  />
                </Box>
                <Box flex={3}>
                  <FormikSelect
                    error={
                      errors.enemyStrategy &&
                      errors.enemyStrategy[i] &&
                      (
                        errors.enemyStrategy[
                          i
                        ] as FormikErrors<StrategyElementCreateRequest>
                      ).action
                    }
                    touched={
                      touched.enemyStrategy && touched.enemyStrategy[i].action
                    }
                    value={values.enemyStrategy[i].action}
                    handleChange={handleChange}
                    selectValues={strategyActions.map((action) => ({
                      name: t(`STRATEGY.${action}`),
                      value: action,
                    }))}
                    inputName={`enemyStrategy[${i}].action`}
                    translationKey="STRATEGY.ACTION"
                  />
                </Box>
                <Box flex={1}>
                  <FormikInput
                    error={
                      errors.enemyStrategy &&
                      errors.enemyStrategy[i] &&
                      (
                        errors.enemyStrategy[
                          i
                        ] as FormikErrors<StrategyElementCreateRequest>
                      ).priority
                    }
                    touched={
                      touched.enemyStrategy && touched.enemyStrategy[i].priority
                    }
                    value={values.enemyStrategy[i].priority}
                    handleChange={handleChange}
                    inputType="number"
                    inputName={`enemyStrategy[${i}].priority`}
                    translationKey="STRATEGY.PRIORITY"
                  />
                </Box>
              </Flex>
            </>
          ))}
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={enemyService.isLoading}
            type="submit"
          >
            {t("CHARACTER.CREATE")}
          </Button>
        </Box>
      </Flex>
    </form>
  );
};

export default CreateEnemy;
