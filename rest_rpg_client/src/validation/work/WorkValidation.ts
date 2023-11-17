import * as Yup from "yup";

export const WorkCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  wage: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  workMinutes: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
});
