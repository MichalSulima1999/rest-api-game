import * as Yup from "yup";
import { SkillEffect, SkillType } from "../../generated-sources/openapi";
import { classes } from "../character/CharacterValidation";

export const skillTypes = Object.values(SkillType);
export const skillEffects = Object.values(SkillEffect);

export const SkillCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  manaCost: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  goldCost: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  statisticPointsCost: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  type: Yup.string().oneOf(skillTypes, "WRONG_SKILL_TYPE").required("REQUIRED"),
  multiplier: Yup.number()
    .min(0.05, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  multiplierPerLevel: Yup.number()
    .min(0.05, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  effect: Yup.string().oneOf(skillEffects, "WRONG_SKILL_EFFECT"),
  effectDuration: Yup.number()
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  effectDurationPerLevel: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_POSITIVE")
    .required("REQUIRED"),
  effectMultiplier: Yup.number()
    .min(0.05, "MUST_BE_POSITIVE")
    .required("REQUIRED"),
  effectMultiplierPerLevel: Yup.number()
    .min(0.05, "MUST_BE_POSITIVE")
    .required("REQUIRED"),
  characterClass: Yup.string()
    .oneOf(classes, "WRONG_CLASS")
    .required("REQUIRED"),
});
