import { useMeQuery } from "../generated/graphql";

export function useMe() {
  const { error, data } = useMeQuery();

  if (error) {
    return { error };
  }

  if (!data) {
    return { me: null };
  }

  const { me } = data;

  return { me: me || null };
}
