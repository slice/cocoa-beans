# Cocoa Beans

Cocoa Beans is a small mod for Minecraft 1.20.4 that adds accurate shortcut
handling for text fields when used on macOS.

> [!CAUTION]
> This mod only makes an effort to augment text field widgets (e.g. the one
> used when sending chat messages). It doesn't implement handling for signs and
> books just yet.

## Shortcuts

| Shortcut | Effect                                       | Notes                                                                                                                |
| -------- | -------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| ⌘←       | Moves the cursor to the start                | Vanilla only lets you do this through Home.                                                                          |
| ⌘↑       | Ditto.                                       | Ditto.                                                                                                               |
| ⌃A       | Ditto.                                       | Ditto.                                                                                                               |
| ⌘→       | Moves the cursor to the end                  | Vanilla only lets you do this through End.                                                                           |
| ⌘↓       | Ditto.                                       | Ditto.                                                                                                               |
| ⌃E       | Ditto.                                       | Ditto.                                                                                                               |
| ⌥→       | Moves the cursor right a word                | Vanilla provides this, but it checks for Command instead.                                                            |
| ⌥←       | Moves the cursor left a word                 | Vanilla provides this, but it checks for Command instead.                                                            |
| ⌥⌫       | Deletes a word to the left of the caret      | Performed using the key that most people call "backspace".                                                           |
| ⌥⌦       | Deletes a word to the right of the caret     | Performed using the key that most people call "delete".                                                              |
| ⌘⌫       | Deletes everything to the left of the caret  | Vanilla handles this shortcut by deleting a word instead. Performed using the key that most people call "backspace". |
| ⌘⌦       | Deletes everything to the right of the caret | Vanilla handles this shortcut by deleting a word instead. Performed using the key that most people call "delete".    |

As you might expect, add ⇧ to any shortcut that traverses over text in order to
extend your selection instead.
