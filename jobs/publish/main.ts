import { context } from "../utils/github/github.ts";
import { join } from "https://deno.land/std@0.128.0/path/mod.ts";
import { copy } from "https://deno.land/std@0.128.0/fs/mod.ts";
import sparseClone from "../utils/clone-sparse.ts";
import { exec } from "../utils/execute.ts";
import config from "./config.ts";

export default async function publishMain(input: string, temp: string) {
  const src = join(input, "src");
  const publicDir = join(input, "public");

  const output = join(temp, "output");
  const outputSrc = join(output, "src"); // compatibility between meta and app-meta branch

  /// 1. Minify
  await onDirectory(src);
  await copy(publicDir, output, { overwrite: true });

  async function onDirectory(dir: string) {
    for await (const entry of Deno.readDir(dir)) {
      if (entry.isFile) await onFile(dir, entry);
      else await onDirectory(join(dir, entry.name));
    }
  }

  async function onFile(dir: string, entry: Deno.DirEntry) {
    const name = entry.name;
    const path = join(dir, name);
    const index = name.lastIndexOf(".");
    const extension = index == -1 ? null : name.slice(index + 1);
    switch (extension) {
      case null: {
        break;
      }

      case "json": {
        const string = await Deno.readTextFile(path);
        const result = JSON.stringify(JSON.parse(string));
        const toDir = join(outputSrc, dir);
        try {
          Deno.mkdir(toDir, { recursive: true });
        } catch (_) {
          // when directory exists already, this throws error
        }
        await Deno.writeTextFile(join(toDir, name), result);
        break;
      }
    }
  }

  /// 2. Publish
  const tempRepo = join(temp, "repo");
  const repo = exec.cd(tempRepo);

  await sparseClone({
    targetPath: tempRepo,
    url: `${context.serverUrl}/${context.repo.owner}/${context.repo.repo}`,
    ref: config.targetRef,
  });

  await repo.execute(["git", "config", "user.name", context.payload.pusher.name]);
  await repo.execute(["git", "config", "user.email", context.payload.pusher.email]);

  copy(output, tempRepo, { overwrite: true });

  const previous = context.payload.head_commit;
  const commitMessage = previous ? `🚀 Deploy@${previous.id}: ${previous.message}` : "🚀 Deploy from app-meta";
  await repo.execute(["git", "commit", "-a", "-m", commitMessage]);

  await repo.execute(["git", "push"]);
}
