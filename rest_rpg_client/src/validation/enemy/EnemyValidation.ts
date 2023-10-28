import * as Yup from "yup";

export const EnemyCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  hp: Yup.number().min(1, "MUST_BE_GREATER_THAN_0").required("REQUIRED"),
  mana: Yup.number().min(1, "MUST_BE_GREATER_THAN_0").required("REQUIRED"),
  damage: Yup.number().min(1, "MUST_BE_GREATER_THAN_0").required("REQUIRED"),
  numberOfPotions: Yup.number().min(0, "MUST_BE_POSITIVE").required("REQUIRED"),
  skillId: Yup.number().min(0, "MUST_BE_POSITIVE").required("REQUIRED"),
  skillLevel: Yup.number()
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  enemyStrategy: Yup.array(
    Yup.object({
      event: Yup.number().min(0).required("REQUIRED"),
      action: Yup.number().min(0).required("REQUIRED"),
      priority: Yup.number().min(0, "MUST_BE_POSITIVE").required("REQUIRED"),
    })
  ).test("strategyTest", "STRATEGY_SHOULD_NOT_BE_NULL", function (values) {
    if (!values || values.length < 1) return true;
    return this.createError({
      path: "strategy",
      message: "STRATEGY_SHOULD_NOT_BE_NULL",
    });
  }),
});
