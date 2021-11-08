import React, { useCallback, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../../core';
import { login as loginApi } from '../../core/authApi';
import {Plugins} from "@capacitor/core";

const log = getLogger('AuthProvider');

type LoginFn = (username?: string, password?: string) => void;
type LogoutFn = () => void;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  login?: LoginFn;
  logout?: LogoutFn;
  pendingAuthentication?: boolean;
  username?: string;
  password?: string;
  token: string;
  tokenFound: boolean
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
  token: '',
  tokenFound: false
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: PropTypes.ReactNodeLike,
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const { isAuthenticated, isAuthenticating, authenticationError, pendingAuthentication, token, tokenFound } = state;
  const login = useCallback<LoginFn>(loginCallback, []);
  const logout = useCallback<LogoutFn>(logoutCallback, []);
  useEffect(authenticationEffect, [pendingAuthentication]);
  useEffect(() => {
    async function checkForToken() {
      if (tokenFound)
        return
      const {Storage} = Plugins;
      const {keys} = await Storage.keys();
      if (keys.indexOf("token") !== -1) {
        const token = await Storage.get({ key: 'token' });
        setState({
          ...state,
          token: token.value!!,
          tokenFound: true,
          pendingAuthentication: false,
          isAuthenticated: true,
          isAuthenticating: false,
        })
      }
    }
    checkForToken()
  }, [state, tokenFound])
  const value = { isAuthenticated, login, logout, isAuthenticating, authenticationError, token, tokenFound };
  log('render');
  return (
      <AuthContext.Provider value={value}>
        {children}
      </AuthContext.Provider>
  );

  function loginCallback(username?: string, password?: string): void {
    log('login');
    setState({
      ...state,
      pendingAuthentication: true,
      username,
      password
    });
  }

  function logoutCallback() {
    const {Storage} = Plugins;
    Storage.remove({key: 'token'})
    setState({
      ...state,
      token: '',
      isAuthenticated: false,
      tokenFound: false
    })
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    }

    async function authenticate() {
      if (!pendingAuthentication || tokenFound) {
        log('authenticate, !pendingAuthentication, return');
        return;
      }
      try {
        log('authenticate...');
        setState({
          ...state,
          isAuthenticating: true,
        });
        const { username, password } = state;
        const { token } = await loginApi(username, password);
        if (canceled) {
          return;
        }
        log('authenticate succeeded');
        const {Storage} = Plugins;
        await Storage.set({
          key: 'token',
          value: token
        })
        setState({
          ...state,
          token,
          pendingAuthentication: false,
          isAuthenticated: true,
          isAuthenticating: false,
        });
      } catch (error) {
        if (canceled) {
          return;
        }
        log('authenticate failed');
        setState({
          ...state,
          // @ts-ignore
          authenticationError: error,
          pendingAuthentication: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
