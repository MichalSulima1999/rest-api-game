import * as Yup from "yup";
import { ItemType } from "../../generated-sources/openapi";

export const itemTypes = Object.values(ItemType);

export const ItemCreateSchema = Yup.object().shape({
  name: Yup.string().required("REQUIRED"),
  type: Yup.string().oneOf(itemTypes, "WRONG_SKILL_TYPE").required("REQUIRED"),
  power: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
  price: Yup.number()
    .integer("MUST_BE_INTEGER")
    .min(1, "MUST_BE_GREATER_THAN_0")
    .required("REQUIRED"),
});
