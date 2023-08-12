import useLocalStorage from "./useLocalStorage";
import { Dispatch, SetStateAction } from "react";

const useToggle = (key: string, initValue: boolean): [boolean, Dispatch<SetStateAction<boolean>>] => {
  const [value, setValue] = useLocalStorage<boolean>(key, initValue);

  const toggle: Dispatch<SetStateAction<boolean>> = (newValue?: SetStateAction<boolean>) => {
    setValue((prev) => {
      return typeof newValue === "boolean" ? newValue : !prev;
    });
  };

  return [value, toggle];
};

export default useToggle;
