#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

if len(sys.argv) < 3:
    raise SystemExit("Usage: generate_patches_readme.py <owner/repo> <branch> [patches-list.json] [README.md]")

repo_full = sys.argv[1]
branch = sys.argv[2]
json_path = Path(sys.argv[3]) if len(sys.argv) > 3 else Path("patches-list.json")
readme_path = Path(sys.argv[4]) if len(sys.argv) > 4 else Path("README.md")

with json_path.open(encoding="utf-8") as f:
    data = json.load(f)

version = str(data.get("version", "0.0.0")).lstrip("v")
patches = data.get("patches", [])

def anchor(name):
    return re.sub(r"-+", "-", re.sub(r"[^a-z0-9]+", "-", name.lower())).strip("-")

def render_patch(patch):
    options = patch.get("options") or []
    opts = "<br>".join(f"• {o.get('title') or o.get('key') or ''}" for o in options)
    return f"| [{patch['name']}](#{anchor(patch['name'])}) | {(patch.get('description') or '').replace(chr(10), '<br>')} | {opts} |"

rows = [
    f"> **[v{version}](https://github.com/{repo_full}/releases/tag/v{version})**&nbsp;&nbsp;•&nbsp;&nbsp;`{branch}`&nbsp;&nbsp;•&nbsp;&nbsp;{len(patches)} patches total",
    "",
    "| Patch | Description | Options |",
    "|---|---|---|",
]
rows.extend(render_patch(p) for p in sorted(patches, key=lambda p: p["name"]))

generated = "\n".join(rows)
readme = readme_path.read_text(encoding="utf-8")
pattern = r"<!-- PATCHES_START(?:\s+EXPANDED)?\s*-->.*?<!-- PATCHES_END -->"
if not re.search(pattern, readme, flags=re.DOTALL):
    raise SystemExit("README.md is missing PATCHES_START/PATCHES_END markers")

readme = readme.replace(
    "https://morphe.software/add-source?github=xyz-user/xyz-patches",
    f"https://morphe.software/add-source?github={repo_full}",
).replace(
    "https://github.com/xyz-user/xyz-patches",
    f"https://github.com/{repo_full}",
)
readme = re.sub(
    pattern,
    f"<!-- PATCHES_START -->\n{generated}\n<!-- PATCHES_END -->",
    readme,
    flags=re.DOTALL,
)
readme_path.write_text(readme, encoding="utf-8")
