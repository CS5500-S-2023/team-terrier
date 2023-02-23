# Team Terrier's Casino Bot

# Team members

YANTING LAI

Warm welcome to anyone reading this document, we are teeeeaaaaammmmm Terrier!

[Our Repo](https://github.com/CS5500-S-2023/team-terrier). Might change in the future, hopefully never.

[Internal Weekly Standup](https://docs.google.com/document/d/1u6i3ZB6oWx0LWF0-WjuNrm2Efz2PWLI4EcHhgXsZnlk/edit?usp=sharing). It's internal, thus private.

## For Developers

Welcome to our developing team, please utilize ctrl + f to find the answer you need and reach out in our teams channel in case this doesn't solve it.

We know the knowledge needed to start coding in a new framework is enormous, so this part is designed as a FAQ hashmap, aka memo for previously asked questions. The goal is to memorize and prevent asking the same questions over and over again.

### Best Practices

1. Always run `gradlew check` before commiting code.

### FAQs

Q: Where do I develop code?
A: All code will be written and executed on Github codespaces. Navigate to our [repo home](https://github.com/CS5500-S-2023/team-terrier), click the green **Code** button, select the codespaces tab, then create a codespace. Github will essentially open a VSCode in your browser in 2 minutes time.

Q: How do I run our bot?
A: Follow these steps carefully.

1. Create an application in [Discord developer portal](https://discord.com/developers/applications) by clicking on **New Application** on the top right corner with whatever name you want.
2. Create a bot in this application by clicking on the **Bot** tab on the left panel, and then clicking on **Add Bot**.
3. Copy this bot's token and come back to Github codespace's terminal.
4. In the terminal, type the following command with your bot's token: `export BOT_TOKEN=<TOKEN>`
5. Run the following command in the terminal at the root directory of the project: `./gradlew run`. If you see the bot stuck at 80%, that means it's running.
6. Go back to Discord developer portal and click on **OAuth2** on the left panel, and then **URL Generator**.
7. On the right, select **bot** and **applications.commands**, then copy the URL on the bottom.
8. Create a new server/channel in your own discord account, then paste the URL in your browser, and add the bot to your server.
9. Test that the bot is working in 2 ways: the bot should be online, and specific slash commands should be available, such as `/button`.
