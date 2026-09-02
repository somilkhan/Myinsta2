#!/usr/bin/env bash
set -euo pipefail

APK="${1:-}"
if [[ -z "$APK" || ! -f "$APK" ]]; then
  echo "Usage: $0 /path/to/base.apk" >&2
  exit 2
fi

work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT

unzip -q -o "$APK" 'classes*.dex' -d "$work/apk"
: > "$work/strings.txt"
while IFS= read -r dex; do
  strings "$dex" >> "$work/strings.txt"
done < <(find "$work/apk" -name '*.dex' -type f | sort)

# These anchors are deliberately conservative. A match proves the target APK
# contains the feature's current vocabulary; it does not promote a feature to
# runtime-verified status by itself.
checks=(
  'direct_v2/threads/%s/toggle_typing_indicator_control/'
  'mark_thread_seen'
  'message_revoked'
  'revoke_notification'
  'Is ad pod'
  'ReelViewerFragment'
  'DownloadOptionsBottomSheetFragment'
  'DirectThreadFragment.saveMessageMedia'
  'accounts/set_biography/'
  'profile_bio'
)

failed=0
printf '%-6s %s\n' STATUS ANCHOR
printf '%-6s %s\n' ------ ------
for needle in "${checks[@]}"; do
  if grep -Fqx "$needle" "$work/strings.txt" 2>/dev/null; then
    printf '%-6s %s\n' FOUND "$needle"
  elif grep -Fq "$needle" "$work/strings.txt"; then
    printf '%-6s %s\n' FOUND "$needle"
  else
    printf '%-6s %s\n' MISS "$needle"
    failed=1
  fi
done

# Print a compact candidate index for follow-up bytecode mapping.
printf '\nCandidate download/profile symbols:\n'
grep -E 'DownloadOptionsBottomSheetFragment|DirectThreadFragment.saveMessageMedia|accounts/set_biography/|profile_bio' "$work/strings.txt" | sort -u | head -100 || true

if [[ "$failed" -ne 0 ]]; then
  echo
  echo "One or more anchors are absent. Do not blindly reuse an older fingerprint."
  exit 1
fi

echo
echo "All baseline anchors found. This is evidence only; runtime verification remains required."
