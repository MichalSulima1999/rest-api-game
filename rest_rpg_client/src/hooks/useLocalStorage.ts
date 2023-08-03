import { useState, useEffect } from "react";

type InitialValue<T> = T | (() => T);

const getLocalValue = <T>(key: string, initValue: InitialValue<T>): T => {
  if (typeof window === "undefined") {
    return typeof initValue === "function"
      ? (initValue as () => T)()
      : initValue;
  }

  const value = localStorage.getItem(key);
  if (value) {
    const localValue = JSON.parse(value) as T;
    if (localValue) return localValue;
  }

  if (typeof initValue === "function") return (initValue as () => T)();

  return initValue;
};

const useLocalStorage = <T>(
  key: string,
  initValue: InitialValue<T>
): [T, React.Dispatch<React.SetStateAction<T>>] => {
  const [value, setValue] = useState<T>(() => {
    return getLocalValue<T>(key, initValue);
  });

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue];
};

export default useLocalStorage;
