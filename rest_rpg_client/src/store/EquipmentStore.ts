import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";
import { EquipmentDetails, ItemLite } from "../generated-sources/openapi";

export class EquipmentStore {
  private _id = 0;
  private _gold = "";
  private _healthPotions = 0;
  private _armor?: ItemLite = undefined;
  private _weapon?: ItemLite = undefined;
  private _rootStore: RootStore;

  get id() {
    return this._id;
  }

  set id(val: number) {
    this._id = val;
  }

  get gold() {
    return this._gold;
  }

  set gold(val: string) {
    this._gold = val;
  }

  get healthPotions() {
    return this._healthPotions;
  }

  set healthPotions(val: number) {
    this._healthPotions = val;
  }

  get armor() {
    return this._armor;
  }

  set armor(val: ItemLite | undefined) {
    this._armor = val;
  }

  get weapon() {
    return this._weapon;
  }

  set weapon(val: ItemLite | undefined) {
    this._weapon = val;
  }

  get rootStore() {
    return this._rootStore;
  }

  public equipmentDetails(details?: EquipmentDetails) {
    if (details) {
      this.id = details.id;
      this.gold = details.gold;
      this.healthPotions = details.healthPotions;
      this.armor = details.armor;
      this.weapon = details.weapon;
    }
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}
