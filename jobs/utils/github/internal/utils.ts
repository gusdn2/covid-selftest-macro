import { OctokitOptions } from "https://cdn.skypack.dev/@octokit/core/dist-types/types?dts";

export function getAuthString(
  token: string,
  options: OctokitOptions,
): string | undefined {
  if (!token && !options.auth) {
    throw new Error("Parameter token or opts.auth is required");
  } else if (token && options.auth) {
    throw new Error("Parameters token and opts.auth may not both be specified");
  }

  return typeof options.auth === "string" ? options.auth : `token ${token}`;
}

// export function getProxyAgent(destinationUrl: string): http.Agent {
//   const hc = new httpClient.HttpClient();
//   return hc.getAgent(destinationUrl);
// }

export function getApiBaseUrl(): string {
  return Deno.env.get("GITHUB_API_URL") || "https://api.github.com";
}
