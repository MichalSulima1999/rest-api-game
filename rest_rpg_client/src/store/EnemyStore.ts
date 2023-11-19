import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";
import { EnemyBasic, EnemyLite } from "../generated-sources/openapi";

export class EnemyStore {
  private _id = 0;
  private _name = "";
  private _hp = 0;
  private _mana = 0;
  private _damage = 0;
  private _numberOfPotions = 0;
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

  get hp() {
    return this._hp;
  }

  set hp(val: number) {
    this._hp = val;
  }

  get mana() {
    return this._mana;
  }

  set mana(val: number) {
    this._mana = val;
  }

  get damage() {
    return this._damage;
  }

  set damage(val: number) {
    this._damage = val;
  }

  get numberOfPotions() {
    return this._numberOfPotions;
  }

  set numberOfPotions(val: number) {
    this._numberOfPotions = val;
  }

  get rootStore() {
    return this._rootStore;
  }

  public enemyLite(lite: EnemyLite) {
    this.id = lite.id;
    this.name = lite.name;
  }

  public enemyBasic(basic?: EnemyBasic) {
    if (basic) {
      this.id = basic.id;
      this.name = basic.name;
      this.hp = basic.hp;
      this.damage = basic.damage;
      this.mana = basic.mana;
      this.numberOfPotions = basic.numberOfPotions;
    }
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}
