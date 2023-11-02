import * as Yup from "yup";

export const AdventureCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  adventureLengthInMinutes: Yup.number()
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  xpForAdventure: Yup.number()
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  goldForAdventure: Yup.number()
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  enemy: Yup.number().min(0, "MUST_BE_POSITIVE").required("REQUIRED"),
});
