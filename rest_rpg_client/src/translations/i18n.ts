/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-return */
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import Backend from "i18next-http-backend";
import { DateTime } from "luxon";

i18n
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    debug: true,
    fallbackLng: "en",
    interpolation: {
      escapeValue: false,
    },
  })
  .catch((error) => {
    console.error("Error initializing i18n:", error);
  });

i18n.services.formatter?.add("DATE_HUGE", (value, lng, options) => {
  return DateTime.fromJSDate(value)
    .setLocale(lng as string)
    .toLocaleString(DateTime.DATE_HUGE);
});

export default i18n;
