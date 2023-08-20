import { makeAutoObservable } from "mobx";
import RootStore from "./RootStore";

export class StatisticsStore {
  private _maxHp = 100;
  private _currentHp = 100;
  private _maxMana = 100;
  private _currentMana = 10;
  private _damage = 10;
  private _magicDamage = 10;
  private _armor = 0;
  private _dodgeChance = 9.5;
  private _criticalChance = 9.5;
  private _currentXp = 0;
  private _xpToNextLevel = 500;
  private _currentLevel = 1;
  private _freeStatisticPoints = 10;
  private _strength = 10;
  private _dexterity = 10;
  private _constitution = 10;
  private _intelligence = 10;
  private _rootStore: RootStore;

  /**
   * Getter maxHp
   * @return {number}
   */
  public get maxHp(): number {
    return this._maxHp;
  }

  /**
   * Setter maxHp
   * @param {number} value
   */
  public set maxHp(value: number) {
    this._maxHp = value;
  }

  /**
   * Getter currentHp
   * @return {number}
   */
  public get currentHp(): number {
    return this._currentHp;
  }

  /**
   * Setter currentHp
   * @param {number} value
   */
  public set currentHp(value: number) {
    this._currentHp = value;
  }

  /**
   * Getter maxMana
   * @return {number}
   */
  public get maxMana(): number {
    return this._maxMana;
  }

  /**
   * Setter maxMana
   * @param {number} value
   */
  public set maxMana(value: number) {
    this._maxMana = value;
  }

  /**
   * Getter currentMana
   * @return {number}
   */
  public get currentMana(): number {
    return this._currentMana;
  }

  /**
   * Setter currentMana
   * @param {number} value
   */
  public set currentMana(value: number) {
    this._currentMana = value;
  }

  /**
   * Getter damage
   * @return {number}
   */
  public get damage(): number {
    return this._damage;
  }

  /**
   * Setter damage
   * @param {number} value
   */
  public set damage(value: number) {
    this._damage = value;
  }

  /**
   * Getter magicDamage
   * @return {number}
   */
  public get magicDamage(): number {
    return this._magicDamage;
  }

  /**
   * Setter magicDamage
   * @param {number} value
   */
  public set magicDamage(value: number) {
    this._magicDamage = value;
  }

  /**
   * Getter armor
   * @return {number}
   */
  public get armor(): number {
    return this._armor;
  }

  /**
   * Setter armor
   * @param {number} value
   */
  public set armor(value: number) {
    this._armor = value;
  }

  /**
   * Getter dodgeChance
   * @return {number}
   */
  public get dodgeChance(): number {
    return this._dodgeChance;
  }

  /**
   * Setter dodgeChance
   * @param {number} value
   */
  public set dodgeChance(value: number) {
    this._dodgeChance = value;
  }

  /**
   * Getter criticalChance
   * @return {number}
   */
  public get criticalChance(): number {
    return this._criticalChance;
  }

  /**
   * Setter criticalChance
   * @param {number} value
   */
  public set criticalChance(value: number) {
    this._criticalChance = value;
  }

  /**
   * Getter currentXp
   * @return {number}
   */
  public get currentXp(): number {
    return this._currentXp;
  }

  /**
   * Setter currentXp
   * @param {number} value
   */
  public set currentXp(value: number) {
    this._currentXp = value;
  }

  /**
   * Getter xpToNextLevel
   * @return {number}
   */
  public get xpToNextLevel(): number {
    return this._xpToNextLevel;
  }

  /**
   * Setter xpToNextLevel
   * @param {number} value
   */
  public set xpToNextLevel(value: number) {
    this._xpToNextLevel = value;
  }

  /**
   * Getter currentLevel
   * @return {number}
   */
  public get currentLevel(): number {
    return this._currentLevel;
  }

  /**
   * Setter currentLevel
   * @param {number} value
   */
  public set currentLevel(value: number) {
    this._currentLevel = value;
  }

  /**
   * Getter freeStatisticPoints
   * @return {number}
   */
  public get freeStatisticPoints(): number {
    return this._freeStatisticPoints;
  }

  /**
   * Setter freeStatisticPoints
   * @param {number} value
   */
  public set freeStatisticPoints(value: number) {
    this._freeStatisticPoints = value;
  }

  /**
   * Getter strength
   * @return {number}
   */
  public get strength(): number {
    return this._strength;
  }

  /**
   * Setter strength
   * @param {number} value
   */
  public set strength(value: number) {
    this._strength = value;
  }

  /**
   * Getter dexterity
   * @return {number}
   */
  public get dexterity(): number {
    return this._dexterity;
  }

  /**
   * Setter dexterity
   * @param {number} value
   */
  public set dexterity(value: number) {
    this._dexterity = value;
  }

  /**
   * Getter constitution
   * @return {number}
   */
  public get constitution(): number {
    return this._constitution;
  }

  /**
   * Setter constitution
   * @param {number} value
   */
  public set constitution(value: number) {
    this._constitution = value;
  }

  /**
   * Getter intelligence
   * @return {number}
   */
  public get intelligence(): number {
    return this._intelligence;
  }

  /**
   * Setter intelligence
   * @param {number} value
   */
  public set intelligence(value: number) {
    this._intelligence = value;
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}

export default StatisticsStore;
