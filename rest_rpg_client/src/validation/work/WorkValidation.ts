import * as Yup from "yup";
import { ResourceType } from "../../generated-sources/openapi";

export const resourceTypes = Object.values(ResourceType);

export const WorkCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  resourceType: Yup.string()
    .oneOf(resourceTypes, "WRONG_RESOURCE_TYPE")
    .required("REQUIRED"),
  resourceAmount: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  workMinutes: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
});
