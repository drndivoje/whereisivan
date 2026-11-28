---
name: android-detekt
description: Use whenever creating or editing Kotlin (.kt/.kts) code in android-client. Code must pass detekt with compose-rules (./gradlew :app:detekt). Explains the rules most often hit in this codebase and how to fix violations properly instead of suppressing them.
---

# Detekt-compliant Kotlin for android-client

All Kotlin in `android-client/` must pass:

```bash
cd android-client && ./gradlew :app:detekt
```

This is **enforced by hooks** in `.claude/settings.json` (`.claude/hooks/detekt-android.sh`):
- after every Edit/Write of an android-client `.kt`/`.kts` file, detekt runs and failures are reported back;
- when the turn ends, detekt runs again if any Kotlin source changed, and the turn cannot finish while it fails.

So write compliant code up front, and when the hook reports issues, fix them right away.

## Setup (for reference)

- detekt `2.0.0-alpha.6` (`dev.detekt` plugin) + `io.nlopez.compose.rules:detekt` (versions in `gradle/libs.versions.toml`)
- Config: `android-client/config/detekt/detekt.yml` (built on detekt defaults + `Compose:` rules)
- Baseline: `android-client/config/detekt/baseline.xml` (pre-existing issues only)
- Reports: `android-client/app/build/reports/detekt/`

## Hard rules

1. **Fix the code, don't hide the issue.** Do not add `@Suppress(...)`, do not edit `config/detekt/detekt.yml`, and do not regenerate or edit `baseline.xml` to get past a failure. Only do these if the user explicitly asks for it.
2. New code must not add new baseline entries. If you fix something already in the baseline, tell the user they can regenerate it with `./gradlew :app:detektBaseline`.
3. Don't finish with detekt failing.

## Compose rules — how to comply

Full docs: https://mrmans0n.github.io/compose-rules/rules/

- **ModifierMissing / ModifierWithoutDefault / ModifierNaming**: every public composable that emits UI takes `modifier: Modifier = Modifier`. It is the first optional parameter and is applied to the root layout only.
  ```kotlin
  @Composable
  fun TrackScreen(state: TrackUiState, onStart: () -> Unit, modifier: Modifier = Modifier) {
      Column(modifier = modifier) { /* children use fresh Modifier */ }
  }
  ```
- **ModifierReused / ModifierNotUsedAtRoot**: use the `modifier` parameter once, on the outermost element. Children start from `Modifier`.
- **ComposableParamOrder**: required params first, then `modifier`, then other optional params, then a trailing composable `content` lambda if there is one.
- **ParameterNaming**: event lambdas use present tense: `onClick`, `onValueChange`, not `onClicked` or `onChanged`.
- **ViewModelForwarding / ViewModelInjection**: only the screen-level (route) composable gets the ViewModel (`koinViewModel()`). It collects state and passes **plain state + lambdas** down. Never pass a ViewModel to child composables.
  ```kotlin
  @Composable
  fun TrackRoute(viewModel: ActivityViewModel = koinViewModel()) {
      val state by viewModel.state.collectAsStateWithLifecycle()
      TrackScreen(state = state, onStart = viewModel::start)
  }
  ```
- **MultipleEmitters**: a composable emits one root layout. Wrap multiple siblings in `Column`/`Row`/`Box`.
- **ContentEmitterReturningValues**: a composable either emits UI or returns a value, not both.
- **RememberMissing / MutableStateParam / StateParam**: wrap `mutableStateOf` and `derivedStateOf` in `remember`. Don't take `MutableState<T>` or `State<T>` as parameters. Take `T` plus an `onXChange: (T) -> Unit` lambda.
- **MutableParams / UnstableCollections**: no `MutableList` or `ArrayList` parameters. Use read-only `List` (or immutable collections).
- **PreviewPublic**: `@Preview` functions are `private`.
- **CompositionLocalAllowlist / CompositionLocalNaming**: avoid new `CompositionLocal`s. If one is necessary, name it `LocalXxx`.
- **LambdaParameterInRestartableEffect**: inside `LaunchedEffect`/`DisposableEffect`, wrap lambda params with `rememberUpdatedState`.

## detekt default rules most often hit here

- **MagicNumber**: move numbers into named constants (`private val TrackPadding = 16.dp`, `private const val UPDATE_INTERVAL_MS = 5_000L`). In Compose, put sizes in a `private object` or top-level `private val`.
- **NewLineAtEndOfFile**: every file ends with a newline.
- **LongMethod** (>60 lines): split big composables into smaller private composables.
- **MaxLineLength** (120): wrap long lines.
- **TooGenericExceptionCaught**: catch specific exceptions (`IOException`, `ClientRequestException`), not `Exception`.
- **ReturnCount / ComplexCondition / NestedBlockDepth**: use early guards, `when`, and extracted functions.
- `FunctionNaming` allows PascalCase for `@Composable` functions only.

## Workflow

1. Before writing, check the rules above for the kind of code you're adding.
2. After editing, the hook runs detekt. Read each `e: path:line:col message [Rule]` line and fix it at that location.
3. To run it by hand: `cd android-client && ./gradlew :app:detekt`.
