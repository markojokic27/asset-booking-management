export function decodeJwtPayload(token: string): unknown {
  const parts = token.split('.');
  if (parts.length < 2) throw new Error('Invalid JWT');

  const base64Url = parts[1];
  const base64 = base64Url.replaceAll('-', '+').replaceAll('_', '/');
  const padded = base64.padEnd(
    base64.length + ((4 - (base64.length % 4)) % 4),
    '='
  );

  return JSON.parse(atob(padded));
}

export function getUserIdFromAccessToken(
  token: string
): string | number | null {
  const payload = decodeJwtPayload(token) as Record<string, unknown>;
  const candidate =
    payload.userId ??
    payload.id ??
    payload.sub ??
    payload.user_id ??
    payload.uid;

  if (typeof candidate === 'string' || typeof candidate === 'number')
    return candidate;
  return null;
}
