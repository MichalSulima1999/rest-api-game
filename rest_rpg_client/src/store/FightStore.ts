import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";
import {
  ElementAction,
  FightActionResponse,
  FightDetails,
  FightEffectLite,
} from "../generated-sources/openapi";

interface FightInfo {
  enemyHit?: boolean;
  enemyDamage?: number;
  enemyAction?: ElementAction;
  playerDamage?: number;
  playerCurrentHp?: number;
  playerCurrentMana?: number;
  playerCriticalStrike?: boolean;
  playerPotions?: number;
  playerWon?: boolean;
}

export class FightStore {
  private _id? = 0;
  private _enemyCurrentHp? = 0;
  private _enemyCurrentMana? = 0;
  private _active? = false;
  private _fightEffects?: Array<FightEffectLite> = [];
  private _fightInfo?: FightInfo = {};
  private _rootStore: RootStore;

  get id() {
    return this._id;
  }

  set id(val: number | undefined) {
    this._id = val;
  }

  get enemyCurrentHp() {
    return this._enemyCurrentHp;
  }

  set enemyCurrentHp(val: number | undefined) {
    this._enemyCurrentHp = val;
  }

  get enemyCurrentMana() {
    return this._enemyCurrentMana;
  }

  set enemyCurrentMana(val: number | undefined) {
    this._enemyCurrentMana = val;
  }

  get active() {
    return this._active;
  }

  set active(val: boolean | undefined) {
    this._active = val;
  }

  get fightEffects() {
    return this._fightEffects;
  }

  set fightEffects(val: Array<FightEffectLite> | undefined) {
    this._fightEffects = val;
  }

  get fightInfo() {
    return this._fightInfo;
  }

  set fightInfo(val: FightInfo | undefined) {
    this._fightInfo = val;
  }

  get rootStore() {
    return this._rootStore;
  }

  public fightAction(fightActionResponse: FightActionResponse) {
    const fight = fightActionResponse?.fight;
    this.id = fight?.id;
    this.enemyCurrentHp = fight?.enemyCurrentHp;
    this.enemyCurrentMana = fight?.enemyCurrentMana;
    this.active = fight?.active;
    this.fightEffects = fight?.fightEffects;
    this._rootStore.statisticsStore.currentHp =
      fightActionResponse.playerCurrentHp!;
    this._rootStore.statisticsStore.currentMana =
      fightActionResponse.playerCurrentMana!;
    this._rootStore.enemyStore.enemyBasic(fight?.enemy);
    this.fightInfo = {
      enemyHit: fightActionResponse.enemyHit,
      enemyDamage: fightActionResponse.enemyDamage,
      enemyAction: fightActionResponse.enemyAction,
      playerDamage: fightActionResponse.playerDamage,
      playerCurrentHp: fightActionResponse.playerCurrentHp,
      playerCurrentMana: fightActionResponse.playerCurrentMana,
      playerCriticalStrike: fightActionResponse.playerCriticalStrike,
      playerPotions: fightActionResponse.playerPotions,
      playerWon: fightActionResponse.playerWon,
    };
  }

  public fightDetails(details: FightDetails) {
    this.id = details.id;
    this.enemyCurrentHp = details.enemyCurrentHp;
    this.enemyCurrentMana = details.enemyCurrentMana;
    this.active = details.active;
    this.fightEffects = details.fightEffects;
    this._rootStore.enemyStore.enemyBasic(details.enemy);
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}
