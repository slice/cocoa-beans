# Cocoa Beans

Cocoa Beans is a small Fabric mod for Minecraft 1.20.4 that adds support for
native text editing shortcuts within in-game text fields. This mod only makes
sense to use on macOS, and it tries to make itself completely inert when
installed on Windows or Linux.

<!-- prettier-ignore-start -->

> [!CAUTION]
> This mod only makes an effort to augment text field widgets (e.g. the one
> used when sending chat messages). It doesn't implement handling for signs and
> books.

<!-- prettier-ignore-end -->

## Shortcuts

Only a subset is implemented at this time. See all shortcuts
[here](https://support.apple.com/en-us/HT201236#text).

| Shortcut | Effect                                       | Notes                                                              |
| -------- | -------------------------------------------- | ------------------------------------------------------------------ |
| ⌘←       | Moves the cursor to the start                | Vanilla only lets you do this through the Home key.                |
| ⌘↑       | Ditto.                                       | Ditto.                                                             |
| ⌃A       | Ditto.                                       | Ditto.                                                             |
| ⌘→       | Moves the cursor to the end                  | Vanilla only lets you do this through the End key.                 |
| ⌘↓       | Ditto.                                       | Ditto.                                                             |
| ⌃E       | Ditto.                                       | Ditto.                                                             |
| ⌥→       | Moves the cursor right a word                | Vanilla handles this, but it checks for Command instead of Option. |
| ⌥←       | Moves the cursor left a word                 | Vanilla handles this, but it checks for Command instead of Option. |
| ⌥⌫       | Deletes a word to the left of the caret      | &mdash;                                                            |
| ⌥⌦       | Deletes a word to the right of the caret     | &mdash;                                                            |
| ⌘⌫       | Deletes everything to the left of the caret  | Vanilla handles this shortcut by deleting a word instead.          |
| ⌘⌦       | Deletes everything to the right of the caret | Vanilla handles this shortcut by deleting a word instead.          |

<!-- prettier-ignore-start -->

> [!TIP]
> What Mac calls ⌫, or "delete", is usually called "backspace" elsewhere. It
> deletes to the left of the caret.
> Similarly, ⌦, or "forward delete", is usually called "delete" elsewhere and it
> deletes to the right of the caret.

<!-- prettier-ignore-end -->

As you might expect, adding ⇧ to any shortcut extends your selection through the
run of text that you would've traversed.

## Building

Java 17 or later is required to build. Fabric 0.15.7 or later is targeted.

```
./gradlew build
```
