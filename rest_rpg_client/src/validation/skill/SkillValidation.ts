import * as Yup from "yup";
import { SkillEffect, SkillType } from "../../generated-sources/openapi";
import { classes } from "../character/CharacterValidation";

export const skillTypes = Object.values(SkillType);
export const skillEffects = Object.values(SkillEffect);

export const SkillCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  type: Yup.string().oneOf(skillTypes, "WRONG_SKILL_TYPE").required("REQUIRED"),
  multiplier: Yup.number()
    .min(0.05, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  multiplierPerLevel: Yup.number()
    .min(0.05, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  effect: Yup.string().oneOf(skillEffects, "WRONG_SKILL_EFFECT"),
  effectDuration: Yup.number().min(1, "MUST_BE_GREATER_THAN_0"),
  effectDurationPerLevel: Yup.number().min(1, "MUST_BE_POSITIVE"),
  effectMultiplier: Yup.number().min(0.5, "MUST_BE_POSITIVE"),
  effectMultiplierPerLevel: Yup.number().min(0.5, "MUST_BE_POSITIVE"),
  characterClass: Yup.string()
    .oneOf(classes, "WRONG_CLASS")
    .required("REQUIRED"),
});
