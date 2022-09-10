import { useMeQuery } from "../generated/graphql";

export function useMe() {
  const { error, data } = useMeQuery();

  if (error) {
    return { error };
  }

  const { me = null } = data || {};

  return { me, loggedIn: !!me } as const;
}
