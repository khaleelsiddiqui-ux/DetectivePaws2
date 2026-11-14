# Detective Paws — your money-making detective game

A cute, fully animated cat detective game for Android, with music and character voices. Detective Paws solves 50 cases: listen to the client, search a living scene for 5 hidden clues before time runs out, then pick the culprit from a suspect lineup.
It's ready to earn from Google AdMob ads. No coding needed to build it, install it, or publish it. Follow the parts in order.

**The cases:** 50 mysteries, from *The Missing Birthday Cake* to the grand finale *The Vanishing Town*. They take place in 13 animated scenes: a kitchen (day, night and birthday party), an art museum (two galleries), a park (summer, autumn, winter and the Winter Festival), a library (day and night) and a beach (day and sunset). Later cases give less time and use smaller clues, so the game gets harder as players go.

**Sound:** background music changes with the moment (a sneaky theme on the menus, playful music while searching, suspense at the lineup). A narrator reads each case title and the reveal, and all 21 characters speak their lines in their own voices, with lip sync: their mouths (and beaks) follow each word as it is spoken. Players can turn music 🎵, sound effects 🔔 and voices 🗣️ on or off from the title screen or the pause menu.

**About the voices:** the app uses the phone's built-in text-to-speech, so it works offline and costs nothing. Almost every Android phone has it already. If a phone is silent, the player can install or update **Speech Services by Google** from the Play Store.

**How it earns money**

| Where | Ad type | Why players accept it |
|---|---|---|
| "Hint" while searching (1 free per case, then ads) | Rewarded video | Circles a clue they're stuck on |
| "+30 seconds" when time runs out | Rewarded video | Saves the case they're about to lose |
| "×2 Coins" after solving a case | Rewarded video | Doubles their reward |
| "+40 Free Coins" in Outfits | Rewarded video | Helps unlock outfits faster |
| Every 3rd "Next case" | Interstitial (full-screen) | Short, not too often |

Coins unlock 6 detective outfits (Sherlock, Noir, Gentleman, Artiste, Officer, Royal), from 200 to 1,500 coins. Stars (1–3 per case) reward fast solving and correct first guesses, so players replay cases. With 50 cases there are many more chances to show ads per player.

---|---|---|
| "Keep running" after a crash | Rewarded video | They choose it to save a good run |
| "×2 Eggs" after a run | Rewarded video | Doubles what they collected |
| "+50 Free Eggs" in Outfits | Rewarded video | Helps unlock outfits faster |
| Every 3rd "Run again" | Interstitial (full-screen) | Short, not too often |

Eggs unlock 8 duck outfits (Skater, Mallard, Chef, Ninja, Pirate, Party, Royal), from 150 to 2,500 eggs. That keeps players coming back and choosing to watch ads.

---|---|---|
| "Continue" after losing | Rewarded video | They choose it to keep their score |
| "×2 Coins" after a game | Rewarded video | Doubles what they earned |
| "+25 Free Coins" in Skins | Rewarded video | Helps them unlock skins faster |
| Every 3rd "Play Again" | Interstitial (full-screen) | Short, not too often |

Coins unlock 6 skins (100 to 1,000 coins), which keeps players coming back and choosing to watch ads.

---

## Part 1 — Get the APK (about 15 minutes, all in your web browser)

GitHub is a free website that will build the APK for you on its own computers.

1. **Create a free account** at **github.com** and sign in.
2. Click the **+** at the top right → **New repository**.
   - Name: `DetectivePaws`
   - Choose **Private**
   - Click **Create repository**.
3. On the next page click the link **"uploading an existing file"**.
4. **Unzip** `DetectivePaws.zip` on your computer. Open the `DetectivePaws` folder, select **everything inside it** and drag it all into the GitHub upload box.
5. Wait until every file has finished uploading, then click the green **Commit changes** button.
6. Click the **Actions** tab at the top of your repository.
   - If you see **"Build APK"** running (yellow dot), wait about 5–8 minutes until it turns into a green ✓.
   - **If the Actions tab is empty**, your computer skipped the hidden `.github` folder. Fix it in 1 minute:
     1. Go to the **Code** tab → **Add file** → **Create new file**.
     2. In the name box type exactly: `.github/workflows/build-apk.yml`
     3. Open `BUILD-FILE-backup.yml` (in your repository or on your computer), copy all the text, and paste it into the big box.
     4. Click **Commit changes**. Go back to **Actions** and the build will start.
7. When it shows a green ✓, click on that build. Scroll down to **Artifacts** and click **DetectivePaws-test-APK** to download it.
8. Unzip the download. Inside is **app-debug.apk** — that's your game!

**Install it on your Android phone:** send the APK to your phone (email, Google Drive, WhatsApp to yourself, or USB). Tap it. If Android asks, allow "Install unknown apps" for that app, then tap **Install**.

> The test version shows Google's **sample ads** (they say "Test Ad"). That's correct — real ads come in Part 3.

**Changing anything later:** edit a file on GitHub (open it → pencil ✏️ icon → **Commit changes**). GitHub automatically builds a fresh APK every time.

---

## Part 2 — Make it yours (optional but recommended)

All of these can be edited directly on GitHub with the pencil ✏️ icon:

| What | File | What to change |
|---|---|---|
| Game name on the phone | `app/src/main/res/values/strings.xml` | `Detective Paws` |
| **App ID (must do before Play Store)** | `app/build.gradle` | `com.detectivepaws.game` → e.g. `com.yourname.detectivepaws` (lowercase letters, numbers, dots only). It can never change after you publish. |
| Outfit prices and colors | `app/src/main/assets/www/index.html` | Look for `LOOKS` |
| Case stories, clues, suspects | `app/src/main/assets/www/index.html` | Look for `C("` — each case is plain text you can edit or copy to make a new one |
| Character voices (pitch and speed) | `app/src/main/assets/www/index.html` | Look for `VOICES` |
| Music | `app/src/main/assets/www/index.html` | Look for `TRACKS` |
| How often full-screen ads show | `app/src/main/assets/www/index.html` | Look for `save.games % 3` (3 = every 3rd game) |
| Icons | `app/src/main/res/mipmap-*/ic_launcher.png` | Replace with your own PNGs, same names and sizes |

---

## Part 3 — Connect real ads (this is what pays you)

1. Sign up at **admob.google.com** with a Google account. Add your **payment details** and **tax info** under *Payments* so you can be paid.
2. **Apps → Add app** → Android → "No, it's not listed yet" (you can link it to Play later) → name it Detective Paws.
3. Copy the **App ID** (it looks like `ca-app-pub-1234567890123456~1234567890`).
4. In the app, go to **Ad units → Add ad unit**:
   - Create a **Rewarded** ad unit (name: `Rewarded`). Copy its ID (has a `/` in it).
   - Create an **Interstitial** ad unit (name: `Interstitial`). Copy its ID.
5. On GitHub open `app/src/main/res/values/strings.xml` → pencil ✏️ and replace the three test IDs with your three real IDs. Commit.
6. **Privacy & messaging** (in AdMob): create a **GDPR message** for Detective Paws. The game already shows it automatically to players in Europe/UK — this is required to earn from those players.

> ⚠️ **Never tap your own real ads, and don't ask friends to.** AdMob detects it and can permanently close your account. To test, keep using the test IDs, or add your phone as a test device in AdMob → *Settings → Test devices*.

New AdMob accounts usually take a few days to start serving real ads at full volume. That's normal.

---

## Part 4 — Publish on Google Play

### Step 1: Developer account
Sign up at **play.google.com/console**. There's a **one-time registration fee** and identity verification.

New *personal* accounts must first run a **closed test with at least 12 testers for 14 days** before going public. Ask friends/family to join (they just need an Android phone and to opt in through a link). Start this early.

### Step 2: Turn on the Play Store build
You received a separate folder called **signing** with `SIGNING-KEY-KEEP-PRIVATE.txt` and `detectivepaws-upload-key.jks`. **Keep both safe forever and never upload them to GitHub.** You need them for every future update.

On GitHub: **Settings → Secrets and variables → Actions → New repository secret**. Add these four, copying values from `SIGNING-KEY-KEEP-PRIVATE.txt`:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Then go to **Actions → Build APK → Run workflow**. When it finishes, download **DetectivePaws-PlayStore-release**. Inside is an **.aab** file — that's what you upload to Google Play.

### Step 3: Fill in the store listing
In Play Console, **Create app**, then complete everything on the dashboard checklist:
- **Store listing:** use the images in the `store-graphics` folder (icon 512×512, feature graphic 1024×500) plus 2+ screenshots from your phone.
- **Privacy policy:** required because the game shows ads. Free generators exist online (search "app privacy policy generator") — mention Google AdMob. Host it free on Google Sites.
- **Contains ads:** Yes.
- **Data safety:** AdMob collects device identifiers and approximate location for ads. Google's AdMob help pages explain exactly what to tick.
- **Content rating:** fill the questionnaire (cartoon mystery game, no violence).
- **Target audience:** choose **13+** or older. Choosing children brings much stricter ad rules.

Upload the `.aab` to **Testing → Closed testing**, add your 12 testers, and after 14 days apply for **Production**.

---

## Part 5 — Growing your earnings

Ads pay per view, so income depends almost entirely on **how many people play, and how often**.

- **Post short gameplay videos** (TikTok, Instagram Reels, YouTube Shorts). Clue-finding moments and the "Busted!" reveal are great content. This is free and is the #1 way indie games get players.
- **Ask for reviews** from your testers — ratings help Play Store ranking.
- **Update often.** Add new cases (new stories reuse the existing scenes), new outfits, a daily case, or holiday mysteries. Each update gives a small visibility boost.
- **Watch your AdMob dashboard** to see which ad placement earns most.
- Next upgrades worth adding: a **"Remove ads" purchase** (Google Play Billing), a **daily login reward**, and **leaderboards** (Google Play Games).

Honest expectations: most first games earn a little, and earnings grow with players. Treat this one as your launchpad — every game after it gets faster and better.

---

## Project map (for the curious)

```
DetectivePaws/
├── app/src/main/assets/www/index.html   ← the whole game: cases, scenes, characters (HTML + JavaScript)
├── app/src/main/java/.../MainActivity.java ← Android wrapper + AdMob ads
├── app/src/main/res/values/strings.xml  ← app name + AdMob IDs
├── app/build.gradle                      ← app ID, versions, signing
├── .github/workflows/build-apk.yml       ← tells GitHub how to build the APK
├── BUILD-FILE-backup.yml                 ← visible copy of the line above
└── store-graphics/                       ← Play Store icon + feature graphic
```

If you ever install **Android Studio** on a computer, you can also open this folder directly in it and press ▶ Run.
