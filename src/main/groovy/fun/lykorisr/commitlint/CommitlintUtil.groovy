package fun.lykorisr.commitlint

import org.gradle.api.InvalidUserDataException
import java.util.regex.Pattern

@Singleton
class CommitlintUtil {
    final String STYLE_EXCEPTION = "Wrong GitMoji commit style"
    final Set<String> emojis = ["🎨", "⚡️", "🔥", "🐛", "🚑️", "✨", "📝", "🚀", "💄", "🎉", "✅", "🔒️", "🔐", "🔖", "🚨", "🚧", "💚", "⬇️", "⬆️", "📌", "👷", "📈", "♻️", "➕", "➖", "🔧", "🔨", "🌐", "✏️", "💩", "⏪️", "🔀", "📦️", "👽️", "🚚", "📄", "💥", "🍱", "♿️", "💡", "🍻", "💬", "🗃️", "🔊", "🔇", "👥", "🚸", "🏗️", "📱", "🤡", "🥚", "🙈", "📸", "⚗", "🔍️", "🏷️", "🌱", "🚩", "🥅", "💫", "🗑️", "🛂", "🩹", "🧐", "⚰️", "🧪", "👔", "🩺", "🧱", "🧑‍💻", "💸"]
    final Set<String> types = ["feat", "fix", "refactor", "docs", "test", "perf", "revert", "style", "build", "ci", "wip", "chore"]

    void validate(String msg) {

        String line = msg
                .split("\n")
                .findAll { !it.trim().startsWith("#") }[0]

        def regex = /(\S+)\s(\w+):\s(.*)/

        def matcher = line =~ regex

        if (matcher.matches()) {
            // Check emoji
            if (!emojis.contains(matcher[0][1])) {
                throw new InvalidUserDataException(STYLE_EXCEPTION)
            }

            // check type
            if (!types.contains(matcher[0][2])) {
                throw new InvalidUserDataException(STYLE_EXCEPTION)
            }

            // check message
            if (matcher[0][3].toString().length() == 0) {
                throw new InvalidUserDataException(STYLE_EXCEPTION)
            }

        } else {
            throw new InvalidUserDataException(STYLE_EXCEPTION)
        }


    }
}
