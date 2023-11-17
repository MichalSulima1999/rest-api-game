import * as Yup from "yup";

export const StatisticsUpdateSchema = Yup.object()
  .shape({
    strength: Yup.number().min(0).required("REQUIRED"),
    dexterity: Yup.number().min(0).required("REQUIRED"),
    constitution: Yup.number().min(0).required("REQUIRED"),
    intelligence: Yup.number().min(0).required("REQUIRED"),
    freePoints: Yup.number().min(0).required("REQUIRED"),
  })
  .test(
    "sum",
    "Sum of values must not exceed available points",
    function (values) {
      const { strength, dexterity, constitution, intelligence, freePoints } =
        values;
      const sum = strength + dexterity + constitution + intelligence;
      return sum <= freePoints;
    }
  );
