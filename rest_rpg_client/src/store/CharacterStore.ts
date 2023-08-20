import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";
import { CharacterLite } from "../generated-sources/openapi";

export class CharacterStore {
  private _name = "";
  private _race = "";
  private _sex = "";
  private _characterClass = "";
  private _artwork = "";
  private _rootStore: RootStore;

  get name() {
    return this._name
  }
  
  set name(val: string) {
    this._name = val
  }
  
  get race() {
    return this._race
  }
  
  set race(val: string) {
    this._race = val
  }
  
  get sex() {
    return this._sex
  }
  
  set sex(val: string) {
    this._sex = val
  }
  
  get characterClass() {
    return this._characterClass
  }
  
  set characterClass(val: string) {
    this._characterClass = val
  }
  
  get artwork() {
    return this._artwork
  }
  
  set artwork(val: string) {
    this._artwork = val
  }
  
  get rootStore() {
    return this._rootStore
  }

  public characterLite(characterLite: CharacterLite) {
    this.name = characterLite.name;
    this.race = characterLite.race;
    this.sex = characterLite.sex;
    this.characterClass = characterLite.characterClass;
    this.artwork = characterLite.artwork;
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}

export default CharacterStore;
