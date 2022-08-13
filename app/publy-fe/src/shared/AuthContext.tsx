import React, { useContext } from "react";
import { useApolloClient } from "@apollo/client";

type IAuthContext = {
  token?: string | null;
  setToken(newToken: string): void;
};
const AuthContext = React.createContext<IAuthContext>({
  setToken() {},
});

type AuthContextProviderProps = { children: React.ReactNode };

export function AuthContextProvider({ children }: AuthContextProviderProps) {
  const client = useApolloClient();
  const [token, setToken] = React.useState<string | null>("");

  React.useEffect(() => {
    setToken(localStorage.getItem("publy.token") || null);
  }, []);

  function updateToken(newToken: string | null) {
    if (!newToken) {
      localStorage.removeItem("publy.token");
    } else {
      localStorage.setItem("publy.token", newToken);
    }

    setToken(newToken);
    client.resetStore();
  }

  return (
    <AuthContext.Provider
      value={{
        token,
        setToken: updateToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuthContext(): IAuthContext {
  return useContext(AuthContext);
}
