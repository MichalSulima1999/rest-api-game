import { makeAutoObservable } from "mobx";
import { AuthResponse } from "../classes/auth/Auth";
import RootStore from "./RootStore";

export class AuthStore {
  private _username = "";
  private _accessToken = "";
  private _role = "";
  private _rootStore: RootStore;

  /**
   * Getter username
   * @return {string}
   */
  public get username(): string {
    return this._username;
  }

  /**
   * Setter username
   * @param {string} value
   */
  public set username(value: string) {
    this._username = value;
  }

  /**
   * Getter accessToken
   * @return {string}
   */
  public get accessToken(): string {
    return this._accessToken;
  }

  /**
   * Setter accessToken
   * @param {string} value
   */
  public set accessToken(value: string) {
    this._accessToken = value;
  }

  /**
   * Getter roles
   * @return {string }
   */
  public get role(): string {
    return this._role;
  }

  /**
   * Setter roles
   * @param {string } value
   */
  public set role(value: string) {
    this._role = value;
  }

  public auth(authResponse: AuthResponse) {
    this._username = authResponse.username;
    this._accessToken = authResponse.token;
    this._role = authResponse.role;
  }

  constructor(rootStore: RootStore) {
    this._rootStore = rootStore;
    makeAutoObservable(this, {}, { autoBind: true });
  }
}

export default AuthStore;
