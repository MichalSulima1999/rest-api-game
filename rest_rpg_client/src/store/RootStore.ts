import React from "react";
import AuthStore from "./AuthStore";

export default class RootStore {
  authStore: AuthStore;

  constructor() {
    this.authStore = new AuthStore(this);
  }
}

export const StoresContext = React.createContext(new RootStore());

export const useStores = () => React.useContext(StoresContext);
