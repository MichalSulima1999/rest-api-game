import React from "react";
import AuthStore from "./AuthStore";
import CharacterStore from "./CharacterStore";
import StatisticsStore from "./StatisticsStore";
import { EnemyStore } from "./EnemyStore";
import { FightStore } from "./FightStore";
import { EquipmentStore } from "./EquipmentStore";

export default class RootStore {
  authStore: AuthStore;
  characterStore: CharacterStore;
  statisticsStore: StatisticsStore;
  enemyStore: EnemyStore;
  fightStore: FightStore;
  equipmentStore: EquipmentStore;

  constructor() {
    this.authStore = new AuthStore(this);
    this.characterStore = new CharacterStore(this);
    this.statisticsStore = new StatisticsStore(this);
    this.enemyStore = new EnemyStore(this);
    this.fightStore = new FightStore(this);
    this.equipmentStore = new EquipmentStore(this);
  }
}

export const StoresContext = React.createContext(new RootStore());

export const useStores = () => React.useContext(StoresContext);
