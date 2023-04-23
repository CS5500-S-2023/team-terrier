# Team Terrier's Casino Bot

## Team Members

YANTING LAI

MAIDI WANG

JIAYUE WU

Warm welcome to anyone reading this document, we are teeeeaaaaammmmm Terrier!

[Our Repo](https://github.com/CS5500-S-2023/team-terrier). Might change in the future, hopefully never.

[Internal Weekly Standup](https://docs.google.com/document/d/1u6i3ZB6oWx0LWF0-WjuNrm2Efz2PWLI4EcHhgXsZnlk/edit?usp=sharing). It's internal, thus private.

[Invitation to Discord Channel](https://discord.gg/aE6qc7FgtU). Just in case this link expires or changes, contact Maidi Wang via Teams.

[Evidence of Deployment](https://docs.google.com/document/d/1R9MpqoMrWiBuT3l0llq48raZmd4Wsz_G6D2BNeUy-Yk/edit?usp=sharing).

## For Developers

Welcome to our developing team, please utilize ctrl + f to find the answer you need and reach out in our teams channel in case this doesn't solve it.

We know the knowledge needed to start coding in a new framework is enormous, so this part is designed as a FAQ hashmap, aka memo for previously asked questions. The goal is to memorize and prevent asking the same questions over and over again. For the sake of simplicity, some FAQs require recursive searching. Recursive instructions will be indicated.

### Best Practices

1. Always run `./gradlew check` before commiting code.
2. Always run `./gradlew run` before creating a pull request.
3. Use `./gradlew spotlessApply` to format code.

### FAQs

Q: What is the definition of "Done" in our project?

A: The definition of done (DoD) is when all conditions, or acceptance criteria, that a software product must satisfy are met and ready to be accepted by a user, customer, team, or consuming system.

To be specific, in our project a definition of "done" consists of a checklist containing items as below:

- **Code is written with basic standards.** The most basic thing that needs to be completed for any user story or issue to be “done” is that it’s built.

- **Code is documented.**

- **Build has been made and deployed with unit tests.** We need to make sure we’ve gone through test plans and make sure we didn’t break anything else in the meantime.

- **Code is peer-reviewed.** To be “done”, the code needs to be checked by our team for any issues or bugs.

- **Help documentation is updated.**


Q: Where do I develop code?

A: All code will be written and executed on Github codespaces. Navigate to our [repo home](https://github.com/CS5500-S-2023/team-terrier), click the green **Code** button, select the codespaces tab, then create a codespace. Github will essentially open a VSCode in your browser in 2 minutes time.

Q: How do I run our bot?

A: Follow these instructions.

1. Get a bot token. (recursive)
2. Set bot token as an environment variable through command: `export BOT_TOKEN=<TOKEN>`.
3. Get a mongodb connection string. (recursive)
4. Set mongodb connection string as enviroment variable through command: `export MONGODB_URL=<CONNECTION_STRING>`. Note that it should be **MONGODB_URL**.
5. Invite bot to server. (recursive)
6. Test that the bot is working in 2 ways: the bot should be online, and specific slash commands should be available, such as `/terrier me`.

Q: How do I get a bot token?

A: Create an application in [Discord developer portal](https://discord.com/developers/applications), then create a bot in this application by clicking on the **Bot** tab on the left panel, and then clicking on **Add Bot**. You should see the token (reset token may be required) next to the icon and under the username.

Q: How do I get a mongodb connection string.

A: [Atlas guides](https://www.mongodb.com/docs/guides/atlas/connection-string/). Remember to fill-in the username and password with your actual credentials. Note that you may also have to change IP-TABLES to allow for access from anywhere.

Q: How do I invite our bot into a server?

A: [Instructions](https://discordjs.guide/preparations/adding-your-bot-to-servers.html#bot-invite-links). Customize by choosing other permissions under URL generator in your Discord Developer Portal. After you have the invitation link, paste it into your browser and you'll be redirected.

Q: How to view test coverage?

A: Supposing that you've already ran the tests and that a Jacoco test report has already been generated, in order to view it in your browser, use the following command and change the directory accordingly: `python3 -m http.server --directory build/reports/jacoco/ 8080`.
