import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";
import {
  CharacterDetails,
  CharacterLite,
  SkillDetails,
} from "../generated-sources/openapi";
import { dateToSeconds } from "../helpers/DateHelper";

export class CharacterStore {
  private _id = -1;
  private _name = "";
  private _race = "";
  private _sex = "";
  private _characterClass = "";
  private _artwork = "";
  private _occupationType: string | undefined = "";
  private _occupationTime = 0;
  private _skills?: Array<SkillDetails> = [];
  private _rootStore: RootStore;

  get id() {
    return this._id;
  }

  set id(val: number) {
    this._id = val;
  }

  get name() {
    return this._name;
  }

  set name(val: string) {
    this._name = val;
  }

  get race() {
    return this._race;
  }

  set race(val: string) {
    this._race = val;
  }

  get sex() {
    return this._sex;
  }

  set sex(val: string) {
    this._sex = val;
  }

  get characterClass() {
    return this._characterClass;
  }

  set characterClass(val: string) {
    this._characterClass = val;
  }

  get artwork() {
    return this._artwork;
  }

  set artwork(val: string) {
    this._artwork = val;
  }

  get occupationType() {
    return this._occupationType;
  }

  set occupationType(val: string | undefined) {
    this._occupationType = val;
  }

  get occupationTime() {
    return this._occupationTime;
  }

  set occupationTime(val: number) {
    this._occupationTime = val;
  }

  get skills() {
    return this._skills;
  }

  set skills(val: Array<SkillDetails> | undefined) {
    this._skills = val;
  }

  get rootStore() {
    return this._rootStore;
  }

  public characterLite(characterLite: CharacterLite) {
    this.name = characterLite.name;
    this.race = characterLite.race;
    this.sex = characterLite.sex;
    this.characterClass = characterLite.characterClass;
    this.artwork = characterLite.artwork;
  }

  public characterDetails(characterDetails: CharacterDetails) {
    this.id = characterDetails.id;
    this.name = characterDetails.name;
    this.race = characterDetails.race;
    this.sex = characterDetails.sex;
    this.characterClass = characterDetails.characterClass;
    this.artwork = characterDetails.artwork;
    this.occupationType = characterDetails.occupation.occupationType;
    this.skills = characterDetails.skills;
    if (characterDetails.occupation.finishTime) {
      const time = dateToSeconds(
        new Date(characterDetails.occupation.finishTime)
      );
      if (time < 0) {
        this.occupationTime = 0;
      } else {
        this.occupationTime = time;
      }
    }
    this.rootStore.statisticsStore.statisticsLite(characterDetails.statistics);
    this.rootStore.equipmentStore.equipmentDetails(characterDetails.equipment);
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}

export default CharacterStore;
