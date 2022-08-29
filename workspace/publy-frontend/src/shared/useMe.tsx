export function useMe() {
  const { error, data } = { error: null, data: null };

  if (error) {
    return { error };
  }

  if (!data) {
    return { me: null };
  }

  const { me } = data;

  return { me: me || null };
}
