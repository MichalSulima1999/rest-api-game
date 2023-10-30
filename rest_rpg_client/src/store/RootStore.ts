import React from "react";
import AuthStore from "./AuthStore";
import CharacterStore from "./CharacterStore";
import StatisticsStore from "./StatisticsStore";
import { EnemyStore } from "./EnemyStore";

export default class RootStore {
  authStore: AuthStore;
  characterStore: CharacterStore;
  statisticsStore: StatisticsStore;
  enemyStore: EnemyStore;

  constructor() {
    this.authStore = new AuthStore(this);
    this.characterStore = new CharacterStore(this);
    this.statisticsStore = new StatisticsStore(this);
    this.enemyStore = new EnemyStore(this);
  }
}

export const StoresContext = React.createContext(new RootStore());

export const useStores = () => React.useContext(StoresContext);
