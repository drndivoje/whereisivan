#!/usr/bin/env bash
# Enforces detekt (with compose-rules) on android-client Kotlin code.
#
#   detekt-android.sh post  -> PostToolUse (Edit|Write): runs detekt when an
#                              android-client .kt/.kts file was edited.
#   detekt-android.sh stop  -> Stop: backstop for changes made any other way
#                              (e.g. Bash). Blocks the turn from ending while
#                              detekt fails.
#
# A hash of all android-client Kotlin sources is stored after every clean run,
# so the Stop check is free when nothing changed since the last pass.
set -u

MODE="${1:-post}"
ROOT="${CLAUDE_PROJECT_DIR:-$(cd "$(dirname "$0")/../.." && pwd)}"
ANDROID="$ROOT/android-client"
STATE_DIR="$ANDROID/build/detekt-hook"
CLEAN_HASH="$STATE_DIR/last-clean.sha"
BLOCKS="$STATE_DIR/stop-blocks"
MAX_STOP_BLOCKS=3

input="$(cat)"

sources_hash() {
  (cd "$ANDROID" && find . -path '*/build' -prune -o \( -name '*.kt' -o -name '*.kts' \) -type f -print0 \
    | sort -z | xargs -0 sha1sum 2>/dev/null; sha1sum config/detekt/* 2>/dev/null) | sha1sum | cut -d' ' -f1
}

run_detekt() {
  (cd "$ANDROID" && ./gradlew :app:detekt -q --console=plain 2>&1)
}

issues() {
  grep -E '^(e: |See https|> Analysis failed)' | sed "s|$ANDROID/||"
}

mark_clean() {
  mkdir -p "$STATE_DIR"
  sources_hash > "$CLEAN_HASH"
  rm -f "$BLOCKS"
}

case "$MODE" in
  post)
    file="$(jq -r '.tool_input.file_path // .tool_response.filePath // empty' <<<"$input")"
    case "$file" in
      "$ANDROID"/*.kt | "$ANDROID"/*.kts) ;;
      *) exit 0 ;;
    esac
    case "$file" in */build/*) exit 0 ;; esac

    if output="$(run_detekt)"; then
      mark_clean
      exit 0
    fi
    {
      echo "detekt (compose-rules) FAILED after editing ${file#$ROOT/}."
      echo "Fix every issue below in the code. Do NOT add @Suppress, edit config/detekt/*, or regenerate the baseline to hide them."
      echo "Follow the android-detekt skill."
      issues <<<"$output"
    } >&2
    exit 2
    ;;

  stop)
    [ -d "$ANDROID" ] || exit 0
    current="$(sources_hash)"
    if [ -f "$CLEAN_HASH" ] && [ "$current" = "$(cat "$CLEAN_HASH")" ]; then
      exit 0
    fi

    if output="$(run_detekt)"; then
      mark_clean
      exit 0
    fi

    mkdir -p "$STATE_DIR"
    count=$(( $(cat "$BLOCKS" 2>/dev/null || echo 0) + 1 ))
    echo "$count" > "$BLOCKS"
    summary="$(issues <<<"$output")"

    if [ "$count" -gt "$MAX_STOP_BLOCKS" ]; then
      # Avoid an endless loop; tell the user instead.
      rm -f "$BLOCKS"
      jq -n --arg msg "detekt still fails in android-client after $MAX_STOP_BLOCKS fix attempts. Run: cd android-client && ./gradlew :app:detekt" \
        '{systemMessage: $msg}'
      exit 0
    fi

    jq -n --arg reason "detekt (compose-rules) fails for android-client. Fix these issues before finishing (no @Suppress, no config/baseline changes). Follow the android-detekt skill.
$summary" '{decision: "block", reason: $reason}'
    exit 0
    ;;
esac
