# FairEmail support

If you have a question, please check the frequently asked questions below first.
At the bottom you can find how to ask other questions, request features and report bugs.


## Authorizing accounts

In most cases the quick setup will be able to automatically identify the right configuration.

If the quick setup fails, you'll need to manually setup an account (to receive email) and an identity (to send email).
For this you'll need the IMAP and SMTP server addresses and port numbers, whether SSL/TLS or STARTTLS should be used
and your username (mostly, but not always, your email address) and your password.

Searching for *IMAP* and the name of the provider is mostly sufficient to find the right documentation.

In some cases you'll need to enable external access to your account and/or to use a special (app) password,
for instance when two factor authentication is enabled.

For authorizing:

* Gmail / G suite: see [question 6](#user-content-faq6)
* Outlook / Hotmail: see [question 14](#user-content-faq14)
* Microsoft Exchange: see [question 8](#user-content-faq8)
* Yahoo!: see [question 88](#user-content-faq88)

Please see [here](#user-content-faq22) for common error messages.

Related questions:

* [Why is ActiveSync not supported?](#user-content-faq133)
* [Why is OAuth not supported?](#user-content-faq111)

## Known problems

* ~~A [bug in Android 5.1 and 6](https://issuetracker.google.com/issues/37054851) causes apps to sometimes show a wrong time format. Toggling the Android setting *Use 24-hour format* might temporarily solve the issue. A workaround was added.~~
* ~~A [bug in Google Drive](https://issuetracker.google.com/issues/126362828) causes files exported to Google Drive to be empty. Google has fixed this.~~
* ~~Encryption with [YubiKey](https://www.yubico.com/) results into an infinite loop. FairEmail follows the latest version of the [OpenKeychain API](https://github.com/open-keychain/openpgp-api), so this is likely being caused by an external bug. This seems not be happening anymore.~~
* ~~A [bug in AndroidX](https://issuetracker.google.com/issues/78495471) lets FairEmail occasionally crash on long pressing or swiping. Google has fixed this.~~
* ~~A [bug in AndroidX ROOM](https://issuetracker.google.com/issues/138441698) causes sometimes a crash with "*... Exception while computing database live data ... Couldn't read row ...*". A workaround was added.~~
* A [bug in Android](https://issuetracker.google.com/issues/119872129) lets FairEmail crash with "*... Bad notification posted ...*" on some devices once after updating FairEmail and tapping on a notification.
* A [bug in Android](https://issuetracker.google.com/issues/62427912) sometimes causes a crash with "*... ActivityRecord not found for ...*" after updating FairEmail. Reinstalling ([source](https://stackoverflow.com/questions/46309428/android-activitythread-reportsizeconfigurations-causes-app-to-freeze-with-black)) might fix the problem.
* A bug in Nova Launcher on Android 5.x lets FairEmail crash with a *java.lang.StackOverflowError* when Nova Launcher has access to the accessibility service.
* The folder selector sometimes shows no folders for yet unknown reasons.
* A [bug in AndroidX](https://issuetracker.google.com/issues/64729576) makes it hard to grap the fast scroller.

## Planned features

* ~~Synchronize on demand (manual)~~
* ~~Semi-automatic encryption~~
* ~~Copy message~~
* ~~Colored stars~~
* ~~Notification settings per folder~~
* ~~Select local images for signatures~~ (this will not be added because it requires image file management and because images are not shown by default in most email clients anyway)
* ~~Show messages matched by a rule~~
* ~~[ManageSieve](https://tools.ietf.org/html/rfc5804)~~ (there are no maintained Java libraries with a suitable license and without dependencies and besides that, FairEmail has its own filter rules)
* ~~Search for messages with/without attachments~~ (this cannot be added because IMAP doesn't support searching for attachments)
* ~~Search for a folder~~ (filtering a hierarchical folder list is problematic)
* ~~Search suggestions~~
* ~~[Autocrypt Setup Message](https://autocrypt.org/autocrypt-spec-1.0.0.pdf) (section 4.4)~~ (IMO it is not a good idea to let an email client handle sensitive encryption keys for an exceptional use case while OpenKeychain can export keys too)
* ~~Generic unified folders~~
* ~~New message notification schedules per account~~ (implemented by added a time condition to rules, so messages can be snoozed in selected periods)
* ~~Copy accounts and identities~~
* ~~Pinch zoom~~ (not reliably possible in a scrolling list; the full message view can be zoomed instead)
* ~~More compact folder view~~
* ~~Compose lists and tables~~ (this requires a rich text editor, see [this FAQ](#user-content-faq99))
* ~~Pinch zoom text size~~
* ~~Display GIFs~~
* ~~Themes~~ (a grey light and dark theme were added because this is what most people seems to want)
* ~~Any day time condition~~ (any day doesn't really fit into the from/to date/time condition)
* ~~Send as attachment~~
* ~~Widget for selected account~~
* ~~Remind to attach files~~
* ~~Select domains to show images for~~ (this will be too complicated to use)
* Search for settings

Anything on this list is in random order and *might* be added in the near future.


## Frequently requested features

The design is based on many discussions and if you like you can discuss about it [in this forum](https://forum.xda-developers.com/android/apps-games/source-email-t3824168) too.
The goal of the design is to be minimalistic (no unnecessary menus, buttons, etc) and non distracting (no fancy colors, animations, etc).
All displayed things should be useful in one or another way and should be carefully positioned for easy usage.
Fonts, sizes, colors, etc should be material design whenever possible.

Since FairEmail is meant to be privacy friendly, the following will not be added:

* Opening links without confirmation
* Showing original messages without confirmation, see also [this FAQ](#user-content-faq35)
* Direct file/folder access: for security/privacy reasons (other) apps should use the [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider), see also [this FAQ](#user-content-faq49)

Confirmation is just one tap, which is just a small price for better privacy.
Note that your contacts could unknowingly send malicious messages if they got infected with malware.

Stripped and reformatted messages are often better readable than original messages because the margins are removed, and font colors and sizes are standardized.

FairEmail does not allow other apps access to your messages and attachments without your approval.

FairEmail follows all the best practices for an email client as decribed in [this EFF article](https://www.eff.org/deeplinks/2019/01/stop-tracking-my-emails).

## Frequently Asked Questions

* [(1) Which permissions are needed and why?](#user-content-faq1)
* [(2) Why is there a permanent notification shown?](#user-content-faq2)
* [(3) What are operations and why are they pending?](#user-content-faq3)
* [(4) How can I use an invalid security certificate / IMAP STARTTLS / an empty password?](#user-content-faq4)
* [(5) How can I customize the message view?](#user-content-faq5)
* [(6) How can I login to Gmail / G suite?](#user-content-faq6)
* [(7) Why are sent messages not appearing (directly) in the sent folder?](#user-content-faq7)
* [(8) Can I use a Microsoft Exchange account?](#user-content-faq8)
* [(9) What are identities / how do I add an alias?](#user-content-faq9)
* [~~(11) Why is POP not supported?~~](#user-content-faq11)
* [~~(10) What does 'UIDPLUS not supported' mean?~~](#user-content-faq10)
* [(12) How does encryption/decryption work?](#user-content-faq12)
* [(13) How does search on device/server work?](#user-content-faq13)
* [(14) How can I setup Outlook / Hotmail with 2FA?](#user-content-faq14)
* [(15) Why does the message text keep loading?](#user-content-faq15)
* [(16) Why are messages not being synchronized?](#user-content-faq16)
* [~~(17) Why does manual synchronize not work?~~](#user-content-faq17)
* [(18) Why is the message preview not always shown?](#user-content-faq18)
* [(19) Why are the pro features so expensive?](#user-content-faq19)
* [(20) Can I get a refund?](#user-content-faq20)
* [(21) How do I enable the notification light?](#user-content-faq21)
* [(22) What does account/folder error ... mean?](#user-content-faq22)
* [(23) Why do I get 'Too many simultaneous connections' or 'Maximum number of connections ... exceeded' ?](#user-content-faq23)
* [(24) What is browse messages on the server?](#user-content-faq24)
* [(25) Why can't I select/open/save an image, attachment or a file?](#user-content-faq25)
* [(26) Can I help to translate FairEmail in my own language?](#user-content-faq26)
* [(27) How can I differentiate external and embedded images?](#user-content-faq27)
* [(28) How can I manage status bar notifications?](#user-content-faq28)
* [(29) How can I get new message notifications for other folders?](#user-content-faq29)
* [(30) How can I use the provided quick settings?](#user-content-faq30)
* [(31) How can I use the provided shortcuts?](#user-content-faq31)
* [(32) How can I check if reading email is really safe?](#user-content-faq32)
* [(33) Why are edited sender addresses not working?](#user-content-faq33)
* [(34) How are identities matched?](#user-content-faq34)
* [(35) Why should I be careful with viewing images, attachments and the original message?](#user-content-faq35)
* [(36) How are settings files encrypted?](#user-content-faq36)
* [(37) How are passwords stored?](#user-content-faq37)
* [(39) How can I reduce the battery usage of FairEmail?](#user-content-faq39)
* [(40) How can I reduce the network usage of FairEmail?](#user-content-faq40)
* [(41) How can I fix the error 'Handshake failed' ?](#user-content-faq41)
* [(42) Can you add a new provider to the list of providers?](#user-content-faq42)
* [(43) Can you show the original ... ?](#user-content-faq43)
* [(44) Can you show contact photos / identicons in the sent folder?](#user-content-faq44)
* [(45) How can I fix 'This key is not available. To use it, you must import it as one of your own!' ?](#user-content-faq45)
* [(46) Why does the message list keep refreshing?](#user-content-faq46)
* [(47) How can I solve 'No primary account or no drafts folder' ?](#user-content-faq47)
* [~~(48) How can I solve 'No primary account or no archive folder' ?~~](#user-content-faq48)
* [(49) How can I fix 'An outdated app sent a file path instead of a file stream' ?](#user-content-faq49)
* [(50) Can you add an option to synchronize all messages?](#user-content-faq50)
* [(51) How are folders sorted?](#user-content-faq51)
* [(52) Why does it take some time to reconnect to an account?](#user-content-faq52)
* [(53) Can you stick the message action bar to the top/bottom?](#user-content-faq53)
* [~~(54) How do I use a namespace prefix?~~](#user-content-faq54)
* [(55) How can I mark all messages as read / move or delete all messages?](#user-content-faq55)
* [(56) Can you add support for JMAP?](#user-content-faq56)
* [(57) Can I use HTML in signatures?](#user-content-faq57)
* [(58) What does an open/closed email icon mean?](#user-content-faq58)
* [(59) Can original messages be opened in the browser?](#user-content-faq59)
* [(60) Did you known ...?](#user-content-faq60)
* [(61) Why are some messages shown dimmed?](#user-content-faq61)
* [(62) Which authentication methods are supported?](#user-content-faq62)
* [(63) How are images resized for displaying on screens?](#user-content-faq63)
* [~~(64) Can you add custom actions for swipe left/right?~~](#user-content-faq64)
* [(65) Why are some attachments shown dimmed?](#user-content-faq65)
* [(66) Is FairEmail available in the Google Play Family Library?](#user-content-faq66)
* [(67) How can I snooze conversations?](#user-content-faq67)
* [~~(68) Why can Adobe Acrobat reader not open PDF attachments / Microsoft apps not open attached documents?~~](#user-content-faq68)
* [(69) Can you add auto scroll up on new message?](#user-content-faq69)
* [(70) When will messages be auto expanded?](#user-content-faq70)
* [(71) How do I use filter rules?](#user-content-faq71)
* [(72) What are primary accounts/identities?](#user-content-faq72)
* [(73) Is moving messages across accounts safe/efficient?](#user-content-faq73)
* [(74) Why do I see duplicate messages?](#user-content-faq74)
* [(75) Can you make an iOS, Windows, Linux, etc version?](#user-content-faq75)
* [(76) What does 'Clear local messages' ?](#user-content-faq76)
* [(77) Why are messages sometimes shown with a small delay?](#user-content-faq77)
* [(78) How do I use schedules?](#user-content-faq78)
* [(79) How do I use synchronize on demand (manual)?](#user-content-faq79)
* [~~(80) How can I fix 'Unable to load BODYSTRUCTURE'?~~](#user-content-faq80)
* [~~(81) Can you make the background of the original message dark in the dark theme?~~](#user-content-faq81)
* [(82) What is a tracking image?](#user-content-faq82)
* [(83) What does 'User is authenticated but not connected' mean?](#user-content-faq83)
* [(84) What are local contacts for?](#user-content-faq84)
* [(85) Why is an identity not available?](#user-content-faq85)
* [~~(86) What are 'extra privacy features'?~~](#user-content-faq86)
* [(87) What does 'invalid credentials' mean?](#user-content-faq87)
* [(88) How can I use a Yahoo! account?](#user-content-faq88)
* [(89) How can I send plain text only messages?](#user-content-faq89)
* [(90) Why are some texts linked while not being a link?](#user-content-faq90)
* [~~(91) Can you add periodical synchronization to save battery power?~~](#user-content-faq91)
* [(92) Can you add spam filtering, verification of the DKIM signature and SPF authorization?](#user-content-faq92)
* [(93) Can you allow installation on external storage?](#user-content-faq93)
* [(94) What does the red/orange stripe at the end of the header mean?](#user-content-faq94)
* [(95) Why are not all apps shown when selecting an attachment or image?](#user-content-faq95)
* [(96) Where can I find the IMAP and SMTP settings?](#user-content-faq96)
* [(97) What is 'cleanup' ?](#user-content-faq97)
* [(98) Why can I still pick contacts after revoking contacts permissions?](#user-content-faq98)
* [(99) Can you add a rich text or markdown editor?](#user-content-faq99)
* [(100) How can I synchronize Gmail categories?](#user-content-faq100)
* [(101) What does the blue/orange dot at the bottom of the conversations mean?](#user-content-faq101)
* [(102) How can I enable auto rotation of images?](#user-content-faq102)
* [(103) How can I record audio?](#user-content-faq103)
* [(104) What do I need to know about error reporting?](#user-content-faq104)
* [(105) How does the roam-like-at-home option work?](#user-content-faq105)
* [(106) Which launchers can show the number of new messages?](#user-content-faq106)
* [(107) How do I used colored stars?](#user-content-faq107)
* [(108) Can you add permanently delete messages from any folder?](#user-content-faq108)
* [~~(109) Why is 'select account' available in official versions only?~~](#user-content-faq109)
* [(110) Why are (some) messages empty and/or attachments corrupted?](#user-content-faq110)
* [(111) Why is OAuth not supported?](#user-content-faq111)
* [(112) Which email provider do you recommend?](#user-content-faq112)
* [(113) How does biometric authentication work?](#user-content-faq113)
* [(114) Can you add an import for the settings of other email apps?](#user-content-faq114)
* [(115) Can you add email address chips?](#user-content-faq114)
* [~~(116) How can I show images in messages from trusted senders by default?~~](#user-content-faq116)
* [(117) Can you help me restore my purchase?](#user-content-faq117)
* [(118) What does 'Remove tracking parameters' exactly?](#user-content-faq118)
* [(119) Can you add colors to the unified inbox widget?](#user-content-faq119)
* [(120) Why are new message notifications not removed on opening the app?](#user-content-faq120)
* [(121) How are messages grouped into a conversation?](#user-content-faq121)
* [~~(122) Why is the recipient name/email address show with a warning color?~~](#user-content-faq122)
* [(123) What does 'force sync'?](#user-content-faq123)
* [(124) Why do I get 'Message too large or too complex to display'?](#user-content-faq124)
* [(125) What are the current experimental features?](#user-content-faq125)
* [(126) What does 'User is authenticated but not connected' mean?](#user-content-faq126)
* [(127) How can I fix 'Syntactically invalid HELO argument(s)'?](#user-content-faq127)
* [(128) How can I reset asked questions, for example to show images?](#user-content-faq128)
* [(129) Are ProtonMail, Tutanota supported?](#user-content-faq129)
* [(130) What does message error ... mean?](#user-content-faq130)
* [(131) Can you change the direction for swiping to previous/next message?](#user-content-faq131)
* [(132) Why are new message notifications silent?](#user-content-faq132)
* [(133) Why is ActiveSync not supported?](#user-content-faq133)
* [(134) Can you add deleting local messages?](#user-content-faq134)
* [(135) Why are trashed messages and drafts shown in conversations?](#user-content-faq135)
* [(136) How can I delete an account/identity/folder?](#user-content-faq136)
* [(137) How can I reset 'Don't ask again'?](#user-content-faq137)

[I have another question.](#user-content-support)

<a name="faq1"></a>
**(1) Which permissions are needed and why?**

The following Android permissions are needed:

* *have full network access* (INTERNET): to send and receive email
* *view network connections* (ACCESS_NETWORK_STATE): to monitor internet connectivity changes
* *run at startup* (RECEIVE_BOOT_COMPLETED): to start monitoring on device start
* *foreground service* (FOREGROUND_SERVICE): to run a foreground service on Android 9 Pie and later, see also the next question
* *prevent device from sleeping* (WAKE_LOCK): to keep the device awake while synchronizing messages
* *in-app billing* (BILLING): to allow in-app purchases
* Optional: *read your contacts* (READ_CONTACTS): to autocomplete addresses and to show photos
* Optional: *read the contents of your SD card* (READ_EXTERNAL_STORAGE): to accept files from other, outdated apps, see also [this FAQ](#user-content-faq49)
* Optional: *use fingerprint hardware* (USE_FINGERPRINT) and use *biometric hardware* (USE_BIOMETRIC): to use biometric authentication
* Optional: *find accounts on the device* (GET_ACCOUNTS): to use [OAuth](https://en.wikipedia.org/wiki/OAuth) instead of passwords
* Android 5.1 Lollipop and before: *use accounts on the device* (USE_CREDENTIALS): to use OAuth instead of passwords (not used/needed on later Android versions)

The following permissions are needed to show the count of unread messages as a badge (see also [this FAQ](#user-content-faq106)):

* *com.sec.android.provider.badge.permission.READ*
* *com.sec.android.provider.badge.permission.WRITE*
* *com.htc.launcher.permission.READ_SETTINGS*
* *com.htc.launcher.permission.UPDATE_SHORTCUT*
* *com.sonyericsson.home.permission.BROADCAST_BADGE*
* *com.sonymobile.home.permission.PROVIDER_INSERT_BADGE*
* *com.anddoes.launcher.permission.UPDATE_COUNT*
* *com.majeur.launcher.permission.UPDATE_BADGE*
* *com.huawei.android.launcher.permission.CHANGE_BADGE*
* *com.huawei.android.launcher.permission.READ_SETTINGS*
* *com.huawei.android.launcher.permission.WRITE_SETTINGS*
* *android.permission.READ_APP_BADGE*
* *com.oppo.launcher.permission.READ_SETTINGS*
* *com.oppo.launcher.permission.WRITE_SETTINGS*
* *me.everything.badger.permission.BADGE_COUNT_READ*
* *me.everything.badger.permission.BADGE_COUNT_WRITE*

FairEmail will keep a list of addresses you receive messages from and send messages to
and will use this list for contact suggestions when no contacts permissions is granted to FairEmail.
This means you can use FairEmail without the Android contacts provider (address book).
Note that you can still pick contacts without granting contacts permissions to FairEmail,
only suggesting contacts won't work without contacts permissions.

<br />

<a name="faq2"></a>
**(2) Why is there a permanent notification shown?**

A low priority permanent status bar notification with the number of accounts being synchronized and the number of operations pending (see the next question) is shown
to prevent Android from killing the service that takes care of continuous receiving email.
This is necessary because of the introduction of [doze mode](https://developer.android.com/training/monitoring-device-state/doze-standby) in Android 6 Marshmallow.
Doze mode will stop all apps when the screen is off for some time, unless the app did start a foreground service, which requires showing a status bar notification.

Most, if not all, other email apps don't show a notification
with the "side effect" that new messages are often not or late being reported and that messages are not or late being sent.

Android shows icons of high priority status bar notifications first and will hide the icon of FairEmail's notification if there is no space to show icons anymore.
In practice this means that the status bar notification doesn't take space in the status bar, unless there is space available.

You can switch to periodically synchronization of messages in the receive settings to remove the notification, but be aware that this might use more battery power.
See [here](#user-content-faq39) for more details about battery usage.

Android 8 Oreo might also shows a status bar notification with the text *Apps are running in the background*.
Please see [here](https://www.reddit.com/r/Android/comments/7vw7l4/psa_turn_off_background_apps_notification/) about how you can disable this notification.

Some people suggested to use [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) (FCM) instead of an Android service with a status bar notification,
but this would require email providers to send FCM messages or a central server where all messages are collected sending FCM messages.
The first is not going to happen and the last would have significant privacy implications.

If you came here by clicking on the notification, you should know that the next click will open the unified inbox instead.

<br />

<a name="faq3"></a>
**(3) What are operations and why are they pending?**

The low priority status bar notification shows the number of pending operations, which can be:

* *add*: add message to remote folder
* *move*: move message to another remote folder
* *copy*: copy message to another remote folder
* *fetch*: fetch (pushed) message
* *delete*: delete message from remote folder
* *seen*: mark message as read/unread in remote folder
* *answered*: mark message as answered in remote folder
* *flag*: add/remove star in remote folder
* *keyword*: add/remove IMAP flag in remote folder
* *headers*: download message headers
* *raw*: download raw message
* *body*: download message text
* *attachment*: download attachment
* *sync*: synchronize local and remote messages
* *subscribe*: subscribe to remote folder
* *send*: send message
* *exists*: check if message exists

Operations are processed only when there is a connection to the email server or when manually synchronizing.
See also [this FAQ](#user-content-faq16).

<br />

<a name="faq4"></a>
**(4) How can I use an invalid security certificate / IMAP STARTTLS / an empty password?**

Invalid security certificate (*Can't verify identity of server*): you should try to fix this by contacting your provider or by getting a valid security certificate
because invalid security certificates are insecure and allow [man-in-the-middle attacks](https://en.wikipedia.org/wiki/Man-in-the-middle_attack).
If money is an obstacle, you can get free security certificates from [Let’s Encrypt](https://letsencrypt.org).

Note that older Android versions might not recognize newer certification authorities like Let’s Encrypt causing connections to be considered insecure,
see also [here](https://developer.android.com/training/articles/security-ssl).

IMAP STARTTLS: the EFF [writes](https://www.eff.org/nl/deeplinks/2018/06/announcing-starttls-everywhere-securing-hop-hop-email-delivery):
"*Additionally, even if you configure STARTTLS perfectly and use a valid certificate, there’s still no guarantee your communication will be encrypted.*"

Empty password: your username is likely easily guessed, so this is very insecure.

If you still want to use an invalid security certificate, IMAP STARTTLS or an empty password,
you'll need to enable insecure connections in the account and/or identity settings.

Connections without encryption (either SSL or STARTTLS) are not supported because this is very insecure.

<br />

<a name="faq5"></a>
**(5) How can I customize the message view?**

In the three dot overflow menu you can enable or disable or select:

* *text size*: for three different font sizes
* *compact view*: for more condensed message items and a smaller message text font

In the display section of the settings you can enable or disable:

* *Unified inbox*: to disable the unified inbox and to list the folders selected for the unified inbox instead
* *Group by date*: show date header above messages with the same date
* *Conversation threading*: to disable conversation threading and to show individual messages instead
* *Show contact photos*: to hide contact photos
* *Show identicons*: to show generated contact avatars
* *Show names and email addresses*: to show names or to show names and email addresses
* *Show subject italic*: to show the message subject as normal text
* *Show stars*: to hide stars (favorites)
* *Show message preview*: to show two lines of the message text
* *Show address details by default*: to expand the addresses section by default
* *Use monospaced font for message text*: to use a fixed width typeface for message texts
* *Automatically show original message for known contacts*: to automatically show original messages for contacts on your device, please read [this FAQ](#user-content-faq35)
* *Automatically show images for known contacts*: to automatically show images for contacts on your device, please read [this FAQ](#user-content-faq35)
* *Conversation action bar*: to disable the bottom navigation bar

Note that messages can be previewed only when the message text was downloaded.
Larger message texts are not downloaded by default on metered (generally mobile) networks.
You can change this in the settings.

If the list of addresses is long, you can collapse the addresses section with the *less* icon at the top of the addresses section.

Some people ask:

* to show the subject bold, but bold is already being used to highlight unread messages
* to show the address or subject larger/smaller, but this would interfere with the text size option
* to move the star to the left, but it is much easier to operate the star on the right side

Unfortunately, it is impossible to make everybody happy and adding lots of settings would not only be confusing, but also never be sufficient.

<br />

<a name="faq6"></a>
**(6) How can I login to Gmail / G suite?**

You can use the quick setup wizard to easily setup a Gmail account and identity.

If you don't want to use an on device Gmail account,
you can either enable access for "less secure apps" and use your account password
or enable two factor authentication and use an app specific password.

Note that an app specific password is required when two factor authentication is enabled.

**Enable "Less secure apps"**

See [here](https://support.google.com/accounts/answer/6010255) about how to enable "less secure apps"
or go [directy to the setting](https://www.google.com/settings/security/lesssecureapps).

If you use multiple Gmail accounts, make sure you change the "less secure apps" setting of the right account(s).

Be aware that you need to leave the "less secure apps" settings screen by using the back arrow to apply the setting.

If you use this method, you should use a [strong password](https://en.wikipedia.org/wiki/Password_strength) for your Gmail account, which is a good idea anyway.
Note that using the [standard](https://tools.ietf.org/html/rfc3501) IMAP protocol in itself is not less secure.

When "less secure apps" is not enabled,
you'll get the error *Authentication failed - invalid credentials* for accounts (IMAP)
and *Username and Password not accepted* for identities (SMTP).

**App specific password**

See [here](https://support.google.com/accounts/answer/185833) about how to generate an app specific password.

You might get the alert "*Please log in via your web browser*".
This happens when Google considers the network that connects you to the internet (this could be a VPN) to to be unsafe.
This can be prevented by using an app specific password.

See [here](https://support.google.com/mail/answer/7126229) for Google's instructions
and [here](https://support.google.com/mail/accounts/answer/78754) for troubleshooting.

<br />

<a name="faq7"></a>
**(7) Why are sent messages not appearing (directly) in the sent folder?**

Sent messages are normally moved from the outbox to the sent folder as soon as your provider adds sent messages to the sent folder.
This requires a sent folder to be selected in the account settings and the sent folder to be set to synchronizing.

Some providers do not keep track of sent messages or the used SMTP server might not be related to the provider.
In these cases FairEmail will automatically add sent messages to the sent folder on synchronizing the sent folder, which will happen after a message have been sent.
Note that this will result in extra internet traffic.

~~If this doesn't happen, your provider might not keep track of sent messages or you might be using an SMTP server not related to the provider.~~
~~In these cases you can enable the advanced identity setting *Store sent messages* to let FairEmail add sent messages to the sent folder right after sending a message.~~
~~Note that enabling this setting might result in duplicate messages if your provider adds sent messages to the sent folder too.~~
~~Also beware that enabling this setting will result in extra data usage, especially when when sending messages with large attachments.~~

~~If sent messages in the outbox are not found in the sent folder on a full synchronize, they will be moved from the outbox to the sent folder too.~~
~~A full synchronize happens when reconnecting to the server or when synchronizing periodically or manually.~~
~~You'll likely want to enable the advanced setting *Store sent messages* instead to move messages to the sent folder sooner.~~

<br />

<a name="faq8"></a>
**(8) Can I use a Microsoft Exchange account?**

You can use a Microsoft Exchange account if it is accessible via IMAP.
See [here](https://support.office.com/en-us/article/what-is-a-microsoft-exchange-account-47f000aa-c2bf-48ac-9bc2-83e5c6036793) for more information.

Some older Exchange server versions have a bug causing empty message and corrupt attachments. Please see [this FAQ](#user-content-faq110) for a workaround.

Please see [this FAQ](#user-content-faq133) about ActiveSync support.

<br />

<a name="faq9"></a>
**(9) What are identities / how do I add an alias?**

Identities represent email addresses you are sending *from*.

Some providers allow you to have multiple email aliases.
You can configure these by setting the email address field to the alias address and setting the user name field to your main email address.

<br />

<a name="faq10"></a>
**~~(10) What does 'UIDPLUS not supported' mean?~~**

~~The error message *UIDPLUS not supported* means that your email provider does not provide the IMAP [UIDPLUS extension](https://tools.ietf.org/html/rfc4315).
This IMAP extension is required to implement two way synchronization, which is not an optional feature.
So, unless your provider can enable this extension, you cannot use FairEmail for this provider.~~

<br />

<a name="faq11"></a>
**~~(11) Why is POP not supported?~~**

~~Besides that any decent email provider supports [IMAP](https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol) these days,~~
~~using [POP](https://en.wikipedia.org/wiki/Post_Office_Protocol) will result in unnecessary extra battery usage and delayed new message notifications.~~
~~Moreover, POP is unsuitable for two way synchronization and more often than not people read and write messages on different devices these days.~~

~~Basically, POP supports only downloading and deleting messages from the inbox.~~
~~So, common operations like setting message attributes (read, starred, answered, etc), adding (backing up) and moving messages is not possible.~~

~~See also [what Google writes about it](https://support.google.com/mail/answer/7104828).~~

~~For example [Gmail can import messages](https://support.google.com/mail/answer/21289) from another POP account,~~
~~which can be used as a workaround for when your provider doesn't support IMAP.~~

~~tl;dr; consider to switch to IMAP.~~

<br />

<a name="faq12"></a>
**(12) How does encryption/decryption work?**

First of all you need to install and configure [OpenKeychain](https://f-droid.org/en/packages/org.sufficientlysecure.keychain/).

To encrypt and send a message just check the menu *Encrypt* and the message will be encrypted on sending.

To decrypt a received message, open the message and just tap the padlock icon just below the grey message action bar.

The first time you send an encrypted message you might be asked for a sign key.
FairEmail will automatically store the sign key ID in the selected identity for the next time.
If you need to reset the sign key, just save the identity to clear the sign key ID again.

You can enable *Encrypt by default* in the identity settings, which replaces *Send* by *Encrypt and send*.

FairEmail will send the [Autocrypt](https://autocrypt.org/) headers for other email clients.
Received messages are not decrypted automatically because of security reasons and because often manual interaction is required.

The decrypted message text and decrypted attachments are stored. If you want to undo this, you can use the *resync* message 'more' menu.

Inline PGP in received messages is supported, but inline PGP in outgoing messages is not supported,
see [here](https://josefsson.org/inline-openpgp-considered-harmful.html) about why not.

S/MIME is not supported because it is not used much and because key management is complex.
There are also [security concerns](https://security.stackexchange.com/a/83752).

Note that signed only or encrypted only messages are not supported, see here about why not:

* [OpenPGP Considerations Part I](https://k9mail.github.io/2016/11/24/OpenPGP-Considerations-Part-I.html)
* [OpenPGP Considerations Part II](https://k9mail.github.io/2017/01/30/OpenPGP-Considerations-Part-II.html)
* [OpenPGP Considerations Part III Autocrypt](https://k9mail.github.io/2018/02/26/OpenPGP-Considerations-Part-III-Autocrypt.html)

If you want, you can verify a signature by opening the *signature.asc* attachment.

Please see the [known problems](#known-problems) about YubiKey.

Please see [this comment](https://forum.xda-developers.com/showpost.php?p=79444379&postcount=5609)
about [these vulnerabilities](https://amp.thehackernews.com/thn/2019/04/email-signature-spoofing.html).

<br />

<a name="faq13"></a>
**(13) How does search on device/server work?**

You can start searching for messages on sender, recipient, subject, keyword or message text by using the magnify glass in the action bar of a folder.
You can also search from any app by select *Search email* in the copy/paste popup menu.

Messages will be searched on the device first (all accounts, all folders).
There will be an action button with a search again icon at the bottom to search on the server.
When the search was started in a specific folder,
the same folder will be searched in on the server,
else you can select which folder to search in on the server.

The IMAP protocol doesn't support searching in more than one folder at the same time.
Searching on the server is an expensive operation, therefore it is not possible to select multiple folders.

Searching local messages is case insensitive and on partial text.
The message text of local messages will not be searched if the message text was not downloaded yet.
Searching on the server might be case sensitive or case insensitive and might be on partial text or whole words, depending on the provider.

Searching messages on the device is a free feature, searching messages on the server is a pro feature.

<br />

<a name="faq14"></a>
**(14) How can I setup Outlook / Hotmail with 2FA?**

To use Outlook or Hotmail with two factor authentication enabled, you need to create an app password.
See [here](https://support.microsoft.com/en-us/help/12409/microsoft-account-app-passwords-two-step-verification) for the details.

See [here](https://support.office.com/en-us/article/pop-imap-and-smtp-settings-for-outlook-com-d088b986-291d-42b8-9564-9c414e2aa040) for Microsoft's instructions.

<br />

<a name="faq15"></a>
**(15) Why does the message text keep loading?**

The message header and message body are fetched separately from the server.
The message text of larger messages is not being pre-fetched on metered connections and need to be fetched on opening the message.
The message text will keep loading if there is no connection to the account, see also the next question.

You can check the account and folder list for the account and folder state (see the legend for the meaning of the icons)
and the operation list accessible via the main navigation menu for pending operations (see [this FAQ](#user-content-faq3) for the meaning of the operations).

In the advanced settings you can set the maximum size for automatically downloading of messages on metered connections.

Mobile connections are almost always metered and some (paid) Wi-Fi hotspots are too.

<br />

<a name="faq16"></a>
**(16) Why are messages not being synchronized?**

Possible causes of messages not being synchronized (sent or received) are:

* The account or folder(s) are not set to synchronize
* The number of days to synchronize is set to low
* There is no usable internet connection
* The email server is temporarily not available
* Android stopped the synchronization service

So, check your account and folder settings and check if the accounts/folders are connected (see the legend menu for the meaning of the icons).

On some devices, where there are lots of applications competing for memory, Android may stop the synchronization service as a last resort.
Some Android versions,
in particular of Huawei (see [here](https://www.forbes.com/sites/bensin/2016/07/04/push-notifications-not-coming-through-to-your-huawei-phone-heres-how-to-fix-it/) for a fix)
or Xiaomi (see [here](https://www.forbes.com/sites/bensin/2016/11/17/how-to-fix-push-notifications-on-xiaomis-miui-8-for-real/) for a fix)
stop apps and services too aggressively.
See also [this dedicated website](https://dontkillmyapp.com/).

<br />

<a name="faq17"></a>
**~~(17) Why does manual synchronize not work?~~**

~~If the *Synchronize now* menu is dimmed, there is no connection to the account.~~

~~See the previous question for more information.~~

<br />

<a name="faq18"></a>
**(18) Why is the message preview not always shown?**

The preview of the message text cannot be shown if the message body has not been downloaded yet.
See also [this FAQ](#user-content-faq15).

<br />

<a name="faq19"></a>
**(19) Why are the pro features so expensive?**

The right question is "*why are there so many taxes and fees?*":

* VAT: 25 % (depending on your country)
* Google fee: 30 %
* Income tax: 50 %
* <sub>Paypal fee: 5-10 % depending on the country/amount</sub>

So, what is left for the developer is just a fraction of what you pay.

Note that only some convenience and advanced features need to be purchased which means that FairEmail is basically free to use.

Also note that most free apps will appear not to be sustainable in the end, whereas FairEmail is properly maintained and supported,
and that free apps may have a catch, like sending privacy sensitive information to the internet.

I have been working on FairEmail almost every day for about a year, so I think the price is more than reasonable.
For this reason there won't be discounts either.

<br />

<a name="faq20"></a>
**(20) Can I get a refund?**

If a purchased pro feature doesn't work as intended
and this isn't caused by a problem in the free features
and I cannot fix the problem in a timely manner, you can get a refund.
In all other cases there is no refund possible.
In no circumstances there is a refund possible for any problem related to the free features,
since there wasn't paid anything for them and because they can be evaluated without any limitation.
I take my responsibility as seller to deliver what has been promised
and I expect that you take responsibility for informing yourself of what you are buying.


<a name="faq21"></a>
**(21) How do I enable the notification light?**

Before Android 8 Oreo: there is an advanced option in the setup for this.

Android 8 Oreo and later: see [here](https://developer.android.com/training/notify-user/channels) about how to configure notification channels.
You can use the button *Manage notifications* in the setup to directly go to the Android notification settings.
Note that apps cannot change notification settings, including the notification light setting, on Android 8 Oreo and later anymore.
Apps designed and targeting older Android versions might still be able to control the contents of notifications,
but such apps cannot be updated anymore and recent Android versions will show a warning that such apps are outdated.

<br />

<a name="faq22"></a>
**(22) What does account/folder error ... mean?**

FairEmail does not hide errors like similar apps often do, so it is easier to diagnose problems.
Also, FairEmail will always retry again later, so transient errors will automatically be solved.

The errors *... Couldn't connect to host ...*, *... Connection refused ...* or *... Network unreachable ...*
mean that FairEmail was not able to connect to the email server.

The error *... Software caused connection abort ...*
means that the email server or something between FairEmail and the email server actively terminated an existing connection.
This can for example happen when connectivity was abruptly lost. A typical example is turning on flight mode.

The error *... BYE Logging out ...*, *... Connection reset by peer ...* or *... Broken pipe ...* means that the email server actively terminated an existing connection.

The error *... Connection closed by peer ...* might be caused by a not updated Exchange server,
see [here](https://blogs.technet.microsoft.com/pki/2010/09/30/sha2-and-windows/) for more information.

The error *... Read timed out ...* means that the email server is not responding anymore or that the internet connection is bad.

The error *... Invalid credentials ...* for a Gmail account which was added with the quick setup wizard
might be caused by having removed the selected account from your device or by having revoked account (contact) permissions from FairEmail.
Account permissions are required to periodically refresh the [OAuth](https://developers.google.com/gmail/imap/xoauth2-protocol) token
(a kind of password used to login to your Gmail account).
Just start the wizard (but do not select an account) to grant the required permissions again.

The warning *... Unsupported encoding ...* means that the character set of the message is unknown or not supported.
FairEmail will assume ISO-8859-1 (Latin1), which will in most cases result in showing the message correctly.

See [here](https://linux.die.net/man/3/connect) for what error codes like EHOSTUNREACH and ETIMEDOUT mean.

Possible causes are:

* A firewall or router is blocking connections to the server
* The host name or port number is invalid
* The are problems with the internet connection
* The email server is refusing to accept connections
* The email server is refusing to accept a message, for example because it is too large or contains unacceptable links
* There are too many connections to the server, see also the next question

Many public Wi-Fi networks block outgoing email to prevent spam.
Sometimes you can workaround this by using another SMTP port. See the documentation of the provider for the usable port numbers.

If you are using a VPN, the VPN provider might block the connection because it is too aggressively trying to prevent spam.

FairEmail will automatically try to connect again after a delay.
This delay will be doubled after each failed attempt to prevent draining the battery and to prevent from being locked out permanently.

When in doubt, you can ask for [support](#user-content-support).

<br />

<a name="faq23"></a>
**(23) Why do I get 'Too many simultaneous connections' or 'Maximum number of connections ... exceeded' ?**

The message *Too many simultaneous connections* is sent by the email server
when there are too many folder connections for the same email account at the same time.

Possible causes are:

* There are multiple email clients connected to the same account
* The same email client is connected multiple times to the same account
* The previous connection was terminated abruptly for example by abruptly losing internet connectivity, for example when turning on flight mode

If only FairEmail is connecting to the email server, first try to wait half an hour to see if the problem resolves itself,
else enable the folder setting '*Poll instead of synchronize*' for some folders.
The poll interval can be configured in the account settings.

The maximum number of simultaneous folder connections for Gmail is 15,
so you can synchronize at most 15 folders simultaneously on *all* your devices at the same time.
See [here](https://support.google.com/mail/answer/7126229) for details.

<br />

<a name="faq24"></a>
**(24) What is browse messages on the server?**

Browse messages on the server will fetch messages from the email server in real time
when you reach the end of the list of synchronized messages, even when the folder is set to not synchronize.
You can disable this feature in the advanced account settings.

<br />

<a name="faq25"></a>
**(25) Why can't I select/open/save an image, attachment or a file?**

If a menu item to select/open/save a file is disabled (dimmed) or not available,
the [storage access framework](https://developer.android.com/guide/topics/providers/document-provider),
a standard Android component, is probably not present,
for example because your custom ROM does not include it or because it was removed.
FairEmail does not request storage permissions, so this framework is required to select files and folders.
No app, except maybe file managers, targeting Android 4.4 KitKat or later should ask for storage permissions because it would allow access to *all* files.

<br />

<a name="faq26"></a>
**(26) Can I help to translate FairEmail in my own language?**

Yes, you can translate the texts of FairEmail in your own language [here](https://crowdin.com/project/open-source-email).
Registration is free.

<br />

<a name="faq27"></a>
**(27) How can I differentiate external and embedded images?**

External image:

![External image](https://raw.githubusercontent.com/google/material-design-icons/master/image/1x_web/ic_image_black_48dp.png)

Embedded image:

![Embedded image](https://raw.githubusercontent.com/google/material-design-icons/master/image/1x_web/ic_photo_library_black_48dp.png)

Broken image:

![Broken image](https://raw.githubusercontent.com/google/material-design-icons/master/image/1x_web/ic_broken_image_black_48dp.png)

Note that downloading external images from a remote server can be used to record you did see a message, which you likely don't want if the message is spam or malicious.

<br />

<a name="faq28"></a>
**(28) How can I manage status bar notifications?**

In the setup you'll find a button *Manage notifications* to directly navigate to the Android notifications settings for FairEmail.

On Android 8.0 Oreo and later you can manage the properties of the individual notification channels,
for example to set a specific notification sound or to show notifications on the lock screen.

FairEmail has the following notification channels:

* Service: used for the notification of the synchronize service, see also [this FAQ](#user-content-faq2)
* Send: used for the notification of the send service
* Notifications: used for new message notifications
* Warning: used for warning notifications
* Error: used for error notifications

See [here](https://developer.android.com/guide/topics/ui/notifiers/notifications#ManageChannels) for details on notification channels.
In short: tap on the notification channel name to access the channel settings.

On Android before Android 8 Oreo you can set the notification sound in the settings.

See [this FAQ](#user-content-faq21) if your device has a notification light.

<br />

<a name="faq29"></a>
**(29) How can I get new message notifications for other folders?**

Just long press a folder, select *Edit properties*,
and enable either *Show in unified inbox*
or *Notify new messages* (available on Android 7 Nougat and later only)
and tap *Save*.

<br />

<a name="faq30"></a>
**(30) How can I use the provided quick settings?**

There are quick settings (settings tiles) available to:

* globally enable/disable synchronization
* show the number of new messages and marking them as seen (not read)

Quick settings require Android 7.0 Nougat or later.
The usage of settings tiles is explained [here](https://support.google.com/android/answer/9083864).

<br />

<a name="faq31"></a>
**(31) How can I use the provided shortcuts?**

There are shortcuts available to:

* compose a new message to a favorite contact
* setup accounts, identities, etc

Shortcuts require Android 7.1 Nougat or later.
The usage of shortcuts is explained [here](https://support.google.com/android/answer/2781850).

<br />

<a name="faq32"></a>
**(32) How can I check if reading email is really safe?**

You can use the [Email Privacy Tester](https://www.emailprivacytester.com/) for this.

<br />

<a name="faq33"></a>
**(33) Why are edited sender addresses not working?**

Most providers accept validated addresses only when sending messages to prevent spam.

For example Google modifies the message headers like this:

```
From: Somebody <somebody@example.org>
X-Google-Original-From: Somebody <somebody+extra@example.org>
```

This means that the edited sender address was automatically replaced by a validated address before sending the message.

Note that this is independent of receiving messages.

<br />

<a name="faq34"></a>
**(34) How are identities matched?**

Identities are as expected matched by account.
For incoming messages the *to* and *cc* address will be checked and for outgoing messages the *from* addresses will be checked.
Archived messages will be considered as incoming messages, but additionally the *from* address will be checked.

The matched address will be shown as *via* in the addresses section.

Matched identities can be used to color code messages.
The identity color takes precedence over the account color.
Setting identity colors is a pro feature.

<br />

<a name="faq35"></a>
**(35) Why should I be careful with viewing images, attachments and the original message?**

Viewing remotely stored images (see also [this FAQ](#user-content-faq27)) might not only tell the sender that you have seen the message,
but will also leak your IP address.

Opening attachments or viewing an original message might load remote content and execute scripts,
that might not only cause privacy sensitive information to leak, but can also be a security risk.

Note that your contacts could unknowingly send malicious messages if they got infected with malware.

FairEmail formats messages again causing messages to look different from the original, but also uncovering phishing links.

The Gmail app shows images by default by downloading the images through a Google proxy server.
Since the images are downloaded from the source server [in real-time](https://blog.filippo.io/how-the-new-gmail-image-proxy-works-and-what-this-means-for-you/),
this is even less secure because Google is involved too without providing much benefit.

<br />

<a name="faq36"></a>
**(36) How are settings files encrypted?**

Short version: AES 256 bit

Long version:

* The 256 bit key is generated with *PBKDF2WithHmacSHA1* using a 128 bit secure random salt and 65536 iterations
* The cipher is *AES/CBC/PKCS5Padding*

<br />

<a name="faq37"></a>
**(37) How are passwords stored?**

All supported Android versions [encrypt all user data](https://source.android.com/security/encryption/full-disk.html),
so all data, including usernames, passwords, messages, etc, is stored encrypted.

<br />

<a name="faq39"></a>
**(39) How can I reduce the battery usage of FairEmail?**

Recent Android versions by default report *app usage* as a percentage in the Android battery settings screen.
Confusingly, *app usage* is not the same as *battery usage*.
The app usage will be very high because FairEmail is using a foreground service which is considered as constant app usage by Android.
However, this doesn't mean that FairEmail is constantly using battery power.
The real battery usage can be seen by using the three dot overflow menu *Show full device usage*.
As a rule of thumb the battery usage should be below or in any case not be much higher than *Mobile network standby*.
If this isn't the case, please let me know.

It is inevitable that synchronizing messages will use battery power because it requires network access and accessing the messages database.

Reconnecting to an email server will use extra battery power, so an unstable internet connection will result in extra battery usage.
In this case you might want to synchronize periodically, for example each hour, instead of continuously.
Note that polling frequently (more than every 30-60 minutes) will likely use more battery power than synchronizing always
because connection to the server and comparing the local and remotes messages are expensive operations.

Most of the battery usage, not considering viewing messages, is due to synchronization (receiving and sending) of messages.
So, to reduce the battery usage, set the number of days to synchronize message for to a lower value,
especially if there are a lot of recent messages in a folder.
Long press a folder name in the folders list and select *Edit properties* to access this setting.

If you have at least once a day internet connectivity, it is sufficient to synchronize messages just for one day.

Note that you can set the number of days to *keep* messages for to a higher number than to *synchronize* messages for.
You could for example initially synchronize messages for a large number of days and after this has been completed
reduce the number of days to synchronize messages for, but leave the number of days to keep messages for.

In the receive settings you can enable to always synchronize starred messages,
which will allow you to keep older messages around while synchronizing messages for a limited number of days.

Disabling the folder option *Automatically download message texts and attachments*
will result in less network traffic and thus less battery usage.
You could disable this option for example for the sent folder and the archive.

Synchronizing messages at night is mostly not useful, so you can save on battery usage by not synchronizing at night.
In the settings you can select a schedule for message synchronization (this is a pro feature). See also [this FAQ](#user-content-faq78).

FairEmail will by default synchronize the folder list on each connection.
Since folders are mostly not created, renamed and deleted very often, you can save some network and battery usage by disabling this in the receive settings.

FairEmail will by default check if old messages were deleted from the server on each connection.
If you don't mind that old messages that were delete from the server are still visible in FairEmail, you can save some network and battery usage by disabling this in the receive settings.

Some providers don't follow the IMAP standard and don't keep connections open long enough, forcing FairEmail to reconnect often, causing extra battery usage.
You can inspect the *Log* via the main navigation menu to check if there are frequent reconnects.
You can workaround this by lowering the keep-alive interval in the advanced account settings to for example 9 or 15 minutes.

Some providers send every two minutes something like '*Still there*' resulting in network traffic and your device to wake up and causing unnecessary extra battery usage.
You can inspect the *Log* via the main navigation menu to check if your provider is doing this.
If your provider is using [Dovecot](https://www.dovecot.org/) as IMAP server,
you could ask your provider to change the [imap_idle_notify_interval](https://wiki.dovecot.org/Timeouts) setting to a higher value or better yet, to disable this.
If your provider is not able or willing to change/disable this, you should consider to switch to periodically instead of continuous synchronization.
You can change this in the receive settings.

If you got the message *This provider does not support push messages* while configuring an account,
consider switching to a modern provider which supports push messages (IMAP IDLE) to reduce battery usage.

If your device has an [AMOLED](https://en.wikipedia.org/wiki/AMOLED) screen,
you can save battery usage while viewing messages by switching to the black theme.

Finally, make sure you are using [the latest version](https://github.com/M66B/FairEmail/releases/).

<br />

<a name="faq40"></a>
**(40) How can I reduce the network usage of FairEmail?**

You can reduce the network usage basically in the same way as reducing battery usage, see the previous question for suggestions.

By default FairEmail does not download message texts and attachments larger than 256 KiB when there is a metered (mobile or paid Wi-Fi) internet connection.
You can change this in the connection settings.

<br />

<a name="faq41"></a>
**(41) How can I fix the error 'Handshake failed' ?**

There are several possible causes, so please read to the end of this answer.

The error '*Handshake failed ... WRONG_VERSION_NUMBER*' might mean that you are trying to connect to an IMAP or SMTP server
without an encrypted connection, typically using port 143 (IMAP) and port 25 (SMTP).

Most providers provide encrypted connections using different ports, typically port 993 (IMAP) and port 465/587 (SMTP).

If your provider doesn't support encrypted connections, you should ask to make this possible.
If this isn't an option, you could enable *Allow insecure connections* both in the advanced settings AND the account/identity settings.

See also [this FAQ](#user-content-faq4).

The error '*Handshake failed ... SSLV3_ALERT_ILLEGAL_PARAMETER*' is either caused by a bug in the SSL protocol implementation
or by a too short DH key on the email server and can unfortunately not be fixed by FairEmail.

<br />

<a name="faq42"></a>
**(42) Can you add a new provider to the list of providers?**

If the provider is used by more than a few people, yes, with pleasure.

The following information is needed:

```
<provider
	name="Gmail"
	link="https://support.google.com/mail/answer/7126229" // setup instructions
	type="com.google"> // this is not needed
	<imap
		host="imap.gmail.com"
		port="993"
		starttls="false" />
	<smtp
		host="smtp.gmail.com"
		port="465"
		starttls="false" />
```

The EFF [writes](https://www.eff.org/nl/deeplinks/2018/06/announcing-starttls-everywhere-securing-hop-hop-email-delivery):
"*Additionally, even if you configure STARTTLS perfectly and use a valid certificate, there’s still no guarantee your communication will be encrypted.*"

So, pure SSL connections are safer than using [STARTTLS](https://en.wikipedia.org/wiki/Opportunistic_TLS) and therefore preferred.

Please make sure receiving and sending messages works properly before contacting me to add a provider.

See below about how to contact me.

<br />

<a name="faq43"></a>
**(43) Can you show the original ... ?**

Show original, shows the original message as the sender has sent it, including original fonts, colors, margins, etc.
FairEmail does and will not alter this in any way,
except for requesting [TEXT_AUTOSIZING](https://developer.android.com/reference/android/webkit/WebSettings.LayoutAlgorithm),
which will *attempt* to make small text more readable.

<br />

<a name="faq44"></a>
**~~(44) Can you show contact photos / identicons in the sent folder?~~**

~~Contact photos and identicons are always shown for the sender because this is necessary for conversation threads.~~
~~Getting contact photos for both the sender and receiver is not really an option because getting contact photo is an expensive operation.~~

<br />

<a name="faq45"></a>
**(45) How can I fix 'This key is not available. To use it, you must import it as one of your own!' ?**

You'll get the message *This key is not available. To use it, you must import it as one of your own!*
when trying to decrypt a message with a public key. To fix this you'll need to import the private key.

<br />

<a name="faq46"></a>
**(46) Why does the message list keep refreshing?**

If you see a 'spinner' at the top of the message list, the folder is still being synchronized with the remote server.
You can see the progress of the synchronization in the folder list. See the legend about what the icons and numbers mean.

The speed of your device and internet connection and the number of days to synchronize messages for determine how long synchronization will take.
Note that you shouldn't set the number of days to synchronize messages for to more than one day in most cases, see also [this FAQ](#user-content-faq39).

<br />

<a name="faq47"></a>
**(47) How can I solve 'No primary account or no drafts folder' ?**

You'll get the error message *No primary account or no drafts folder* when trying to compose a message
while there is no account set to be the primary account or when there is no drafts folder selected for the primary account.
This can happen for example when you start FairEmail to compose a message from another app.
FairEmail needs to know where to store the draft,
so you'll need to select one account to be the primary account and/or you'll need to select a drafts folder for the primary account.

This can also happen when you try to reply to a message or to forward a message from an account with no drafts folder
while there is no primary account or when the primary account does not have a drafts folder.

<br />

<a name="faq48"></a>
**~~(48) How can I solve 'No primary account or no archive folder' ?~~**

~~You'll get the error message *No primary account or no archive folder* when searching for messages from another app.
FairEmail needs to know where to search,
so you'll need to select one account to be the primary account and/or you'll need to select a archive folder for the primary account.~~

<br />

<a name="faq49"></a>
**(49) How can I fix 'An outdated app sent a file path instead of a file stream' ?**

You likely selected or sent an attachment or image with an outdated file manager
or an outdated app which assumes all apps still have storage permissions.
For security and privacy reasons modern apps like FairEmail have no full access to all files anymore.
This can result into the error message *An outdated app sent a file path instead of a file stream*
if a file name instead of a file stream is being shared with FairEmail because FairEmail cannot randomly open files.

You can fix this by switching to an up-to-date file manager or an app designed for recent Android versions.
Alternatively, you can grant FairEmail read access to the storage space on your device in the Android app settings.
Note that this workaround [won't work on Android Q](https://developer.android.com/preview/privacy/scoped-storage) anymore.

See also [question 25](#user-content-faq25)
and [what Google writes about it](https://developer.android.com/training/secure-file-sharing/share-file#RespondToRequest).

<br />

<a name="faq50"></a>
**(50) Can you add an option to synchronize all messages?**

A synchronize all (download all) messages will not be added
because it can easily result in out of memory errors and the available storage space filling up.
It can also easily result in a lot of battery and data usage.
Mobile devices are just not very suitable to download and store years of messages.
You can better use the search on server function (see [question 13](#user-content-faq13)), which is faster and more efficient.
Note that searching through a lot of messages stored locally would only delay searching and use extra battery power.

<br />

<a name="faq51"></a>
**(51) How are folders sorted?**

Folders are first sorted on account order (by default on account name)
and within an account with special, system folders on top, followed by folders set to synchronize.
Within each category the folders are sorted on (display) name.
You can set the display name by long pressing a folder in the folder list and selecting *Edit properties*.

The navigation (hamburger) menu item *Order folders* in the setup can be used to manually order the folders.

<br />

<a name="faq52"></a>
**(52) Why does it take some time to reconnect to an account?**

There is no reliable way to know if an account connection was terminated gracefully or forcefully.
Trying to reconnect to an account while the account connection was terminated forcefully too often can result in problems
like [too many simultaneous connections](#user-content-faq23) or even the account being blocked.
To prevent such problems, FairEmail waits 90 seconds until trying to reconnect again.

You can long press *Settings* in the navigation menu to reconnect immediately.

<br />

<a name="faq53"></a>
**(53) Can you stick the message action bar to the top/bottom?**

The message action bar works on a single message and the bottom action bar works on all the messages in the conversation.
Since there is often more than one message in a conversation, this is not possible.
Moreover, there are quite some message specific actions, like forwarding.

Moving the message action bar to the bottom of the message is visually not appealing because there is already a conversation action bar at the bottom of the screen.

Note that there are not many, if any, email apps that display a conversation as a list of expandable messages.
This has a lot of advantages, but the also causes the need for message specific actions.

<br />

<a name="faq54"></a>
**~~(54) How do I use a namespace prefix?~~**

~~A namespace prefix is used to automatically remove the prefix providers sometimes add to folder names.~~

~~For example the Gmail spam folder is called:~~

```
[Gmail]/Spam
```

~~By setting the namespace prefix to *[Gmail]* FairEmail will automatically remove *[Gmail]/* from all folder names.~~

<br />

<a name="faq55"></a>
**(55) How can I mark all messages as read / move or delete all messages?**

You can use multiple select for this.
Long press the first message, don't lift your finger and slide down to the last message.
Then use the three dot action button to execute the desired action.

<br />

<a name="faq56"></a>
**(56) Can you add support for JMAP?**

There are almost no providers offering the [JMAP](https://jmap.io/) protocol,
so it is not worth a lot of effort to add support for this to FairEmail.

<br />

<a name="faq57"></a>
**(57) Can I use HTML in signatures?**

Yes, you can use HTML in signatures if you paste HTML formatted text into the signature field or use the *Edit as HTML* button.

See [here](https://stackoverflow.com/questions/44410675/supported-html-tags-on-android-textview) for which HTML tags are supported.

You can for example paste this into the signature field:

This is *italic*, this is *bold* and this is [a link](https://example.org).

Alternatively, you can use the button *Edit as HTML*.

<br />

<a name="faq58"></a>
**(58) What does an open/closed email icon mean?**

The email icon in the folder list can be open (outlined) or closed (solid):

![External image](https://raw.githubusercontent.com/google/material-design-icons/master/communication/1x_web/ic_mail_outline_black_48dp.png)

Message bodies and attachments are not downloaded by default.

![External image](https://raw.githubusercontent.com/google/material-design-icons/master/communication/1x_web/ic_email_black_48dp.png)

Message bodies and attachments are downloaded by default.

<br />

<a name="faq59"></a>
**(59) Can original messages be opened in the browser?**

For security reasons the files with the original message texts are not accessible to other apps, so this is not possible.
In theory the [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider) could be used to share these files,
but even Google's Chrome cannot handle this.

<br />

<a name="faq60"></a>
**(60) Did you know ... ?**

* Did you know that starred messages are by default synchronized/kept? (this can be changed in the receive settings)
* Did you know that you can long press the 'write message' icon to go to the drafts folder?
* Did you know there is an advanced option to mark messages read when they are moved? (archiving and trashing is also moving)
* Did you know that you can select text (or an email address) in any app on recent Android versions and let FairEmail search for it?
* Did you know that FairEmail has a tablet mode? Rotate your device in landscape mode and conversation threads will be opened in a second column if there is enough screen space.
* Did you know that you can long press a reply template to create a draft message from the template?
* Did you know that you can long press, hold and swipe to select a range of messages?
* Did you know that you can retry sending messages by using pull-down-to-refresh in the outbox?
* Did you know that you can swipe a conversation left or right to go to the next or previous conversation?
* Did you know that you can tap on an image to see where it will be downloaded from?
* Did you know that you can long press the folder icon in the action bar to select an account?
* Did you know that you can long press the star icon in a conversation thread to set a colored star?
* Did you know that you can open the navigation drawer by swiping from the left, even when viewing a conversation?
* Did you know that you can long press the floating reply button to reply to all?

<br />

<a name="faq61"></a>
**(61) Why are some messages shown dimmed?**

Messages shown dimmed are locally moved messages for which the move is not confirmed by the server yet.
This can happen when the folder is set to not synchronize, when there is no connection to the server or when the messages are too old to be synchronized.
Eventually, these messages will be synchronized when the connection to the server is restored or will be deleted if they are too old to be synchronized.

You can view these messages, but you cannot move these messages again until the previous move has been confirmed by the server.

Some providers don't keep track of sent messages or you might be using an SMTP server not related to the provider.
This will result in messages in the sent folder never to be synchronized.
See [this FAQ](#user-content-faq7) for more information on this.

<br />

<a name="faq62"></a>
**(62) Which authentication methods are supported?**

The following authentication methods are supported and used in this order:

* LOGIN
* PLAIN
* NTLM (untested)

SASL authentication methods, like CRAM-MD5, are not supported
because [JavaMail for Android](https://javaee.github.io/javamail/Android) does not support SASL authentication.
If using secure connections, a must today, there is little value in using CRAM-MD5 anyway.

If your provider requires an unsupported authentication method, you'll likely get the error message *authentication failed*.

<br />

<a name="faq63"></a>
**(63) How are images resized for displaying on screens?**

Large inline or attached [PNG](https://en.wikipedia.org/wiki/Portable_Network_Graphics) and [JPEG](https://en.wikipedia.org/wiki/JPEG) images
will automatically be resized for displaying on screens.
This is because email messages are limited in size, depending on the provider mostly between 10 and 50 MB.
Images will by default be resized to a maximum width and height of about 1440 pixels and saved with a compression ratio of 90 %.
Images are scaled down using whole number factors to reduce memory usage and to retain image quality.
There is an advanced option to disable automatically resizing and to set the target image size.

<br />

<a name="faq64"></a>
**~~(64) Can you add custom actions for swipe left/right?~~**

~~The most natural thing to do when swiping a list entry left or right is to remove the entry from the list.~~
~~The most natural action in the context of an email app is moving the message out of the folder to another folder.~~
~~You can select the folder to move to in the account settings.~~

~~Other actions, like marking messages read and snoozing messages are available via multiple selection.~~
~~You can long press a message to start multiple selection. See also [this question](#user-content-faq55).~~

~~Swiping left or right to mark a message read or unread is unnatural because the message first goes away and later comes back in a different shape.~~
~~Note that there is an advanced option to mark messages automatically read on moving,~~
~~which is in most cases a perfect replacement for the sequence mark read and move to some folder.~~
~~You can also mark messages read from new message notifications.~~

~~If you want to read a message later, you can hide it until a specific time by using the *snooze* menu.~~

<br />

<a name="faq65"></a>
**(65) Why are some attachments shown dimmed?**

Inline (image) attachments are shown dimmed.
[Inline attachments](https://tools.ietf.org/html/rfc2183) are supposed to be downloaded and shown automatically,
but since FairEmail doesn't always download attachments automatically, see also [this FAQ](#user-content-faq40),
FairEmail shows all attachment types. To distinguish inline and regular attachments, inline attachments are shown dimmed.

<br />

<a name="faq66"></a>
**(66) Is FairEmail available in the Google Play Family Library?**

There are [too many fees and taxes](#user-content-faq19), Google alone already takes 30 %,
to justify making FairEmail available in the [Google Play Family Library](https://support.google.com/googleone/answer/7007852).

<br />

<a name="faq67"></a>
**(67) How can I snooze conversations?**

Multiple select one of more conversations (long press to start multiple selecting), tap the three dot button and select *Snooze ...*.
Alternatively, use the *Snooze ...* 'more' menu in the expanded message view.
Select the time the conversation(s) should snooze and confirm by tapping OK.
The conversations will be hidden for the selected time and shown again afterwards.
You will receive a new message notification as reminder.

It is also possible to snooze messages with [a rule](#user-content-faq71).

You can show snoozed messages by using the *Snoozed* item in the three dot overflow menu.

You can tap on the small snooze icon to see until when a conversation is snoozed.

By selecting a zero snooze duration you can cancel snoozing.

<br />

<a name="faq68"></a>
**~~(68) Why can Adobe Acrobat reader not open PDF attachments / Microsoft apps not open attached documents?~~**

~~Adobe Acrobat reader and Microsoft apps still expects full access to all stored files,~~
~~while apps should use the [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider) since Android KitKat (2013)~~
~~to have access to actively shared files only. This is for privacy and security reasons.~~

~~You can workaround this by saving the attachment and opening it from the Adobe Acrobat reader / Microsoft app,~~
~~but you are advised to install an up-to-date and preferably open source PDF reader / document viewer,~~
~~for example one listed [here](https://github.com/offa/android-foss#-document--pdf-viewer).~~

<br />

<a name="faq69"></a>
**(69) Can you add auto scroll up on new message?**

The message list is automatically scrolled up when navigating from a new message notification or after a manual refresh.
Always automatically scrolling up on arrival of new messages would interfere with your own scrolling,
but if you like you can enable this in the settings.

<br />

<a name="faq70"></a>
**(70) When will messages be auto expanded?**

When navigation to a conversation one message will be expanded if:

* There is just one message in the conversation
* There is exactly one unread message in the conversation

There is one exception: the message was not downloaded yet
and the message is too large to download automatically on a metered (mobile) connection.
You can set or disable the maximum message size on the 'connection' settings tab.

Duplicate (archived) messages, trashed messages and draft messages are not counted.

Messages will automatically be marked read on expanding, unless this was disabled in the behavior settings.

<br />

<a name="faq71"></a>
**(71) How do I use filter rules?**

You can edit filter rules by long pressing a folder in the folder list.
The rules will be applied to new messages received in the folder, not to existing messages.

You'll need to give a rule a name and you'll need to define the order in which a rule should be executed relative to other rules.

You can disable a rule and you can stop processing other rules after a rule has been executed.

All the conditions of a rule need to be true for a filter rule to be executed.
Conditions are optional, but there needs to be at least one condition.
You can use multiple rules, possibly with a *stop processing*, for an *or* condition.

Matching is not case sensitive, unless you use [regular expressions](https://en.wikipedia.org/wiki/Regular_expression).

In the *more* message menu there is an item to create a rule for a received message with the most common conditions filled in.

You can select one of these actions to apply to matching messages:

* Mark as read
* Mark as unread
* Snooze
* Add star
* Move
* Copy
* Reply template
* Automation

Filter rules are applied direct after the message header has been fetched, before the message text has been downloaded,
so it is not possible to apply filter conditions and actions to the message text.
Note that large message texts are downloaded on demand on a metered connection to save data.

To debug rules you can long press *Operations* to see logging about the evaluation of rule conditions.
Since message headers are not downloaded and stored by default to save on battery and data usage and to save storage space
it is not possible to preview which messages would match the rule conditions.

Using filter rules is a pro feature.

<br />

<a name="faq72"></a>
**(72) What are primary accounts/identities?**

The primary account is used when the account is ambiguous, for example when starting a new draft from the unified inbox.

Similarly, the primary identity of an account is used when the identity is ambiguous.

There can be just one primary account and there can be just one primary identity per account.

<br />

<a name="faq73"></a>
**(73) Is moving messages across accounts safe/efficient?**

Moving messages across accounts is safe because the raw, original messages will be downloaded and moved
and because the source messages will be deleted only after the target messages have been added

Batch moving messages across accounts is efficient if both the source folder and target folder are set to synchronize,
else FairEmail needs to connect to the folder(s) for each message.

<br />

<a name="faq74"></a>
**(74) Why do I see duplicate messages?**

Some providers, notably Gmail, list all messages in all folders, except trashed messages, in the archive (all messages) folder too.
FairEmail shows all these messages, except for one, dimmed, to indicate that these messages are in fact the same message.

Gmail allows one message to have multiple labels, which are presented to FairEmail as folders.
This means that messages with multiple labels will be shown multiple times as well.

You can hide duplicate messages by disabling *Show duplicates* in the three dots overflow menu.

<br />

<a name="faq75"></a>
**(75) Can you make an iOS, Windows, Linux, etc version?**

A lot of knowledge and experience is required to successfully develop an app for a specific platform,
which is why I develop apps for Android only.

<br />

<a name="faq76"></a>
**(76) What does 'Clear local messages' ?**

The folder menu *Clear local messages* removes messages from the device which are present on the server too.
It does not delete messages from the server.
This can be useful after changing the folder settings to not download the message content (text and attachments), for example to save space.

<br />

<a name="faq77"></a>
**(77) Why are messages sometimes shown with a small delay?**

Depending on the speed of your device (processor speed and maybe even more memory speed) messages might be displayed with a small delay.
FairEmail is designed to dynamically handle a large number of messages without running out of memory.
This means that messages needs to be read from a database and that this database needs to be watched for changes, both of which might cause small delays.

Some convenience features, like grouping messages to display conversation threads and determining the previous/next message, take a little extra time.
Note that there is no *the* next message because in the meantime a new message might have been arrived.

When comparing the speed of FairEmail with similar apps this should be part of the comparison.
It is easy to write a similar, faster app which just displays a lineair list of messages while possible using too much memory,
but it is not so easy to properly manage resource usage and to offer more advanced features like conversation threading.

FairEmail is based on the state-of-the-art [Android architecture components](https://developer.android.com/topic/libraries/architecture/),
so there is little room for performance improvements.

<br />

<a name="faq78"></a>
**(78) How do I use schedules?**

In the settingss you can enable scheduling and set the time to turn synchronizing automatically on and off.

An end time equal to or earlier than the start time is considered to be 24 hours later.

Turning FairEmail on or off, for example by using [a quick settings tile](#user-content-faq30), will not turn scheduling off.
This means that the next schedule event will still turn FairEmail on or off.

You can also automate turning synchronization on and off by sending these commands to FairEmail:

```
(adb shell) am startservice -a eu.faircode.email.ENABLE
(adb shell) am startservice -a eu.faircode.email.DISABLE
```

Sending these commands will automatically turn scheduling off.

It is also possible to just enable/disable one account, for example the account with the name *Gmail*:
```
(adb shell) am startservice -a eu.faircode.email.ENABLE --es account Gmail
(adb shell) am startservice -a eu.faircode.email.DISABLE --es account Gmail
```

If you just want to automate checking for new messages, you can do this:

```
(adb shell) adb shell am startservice -a eu.faircode.email.POLL
```

You can automatically send commands with for example [Tasker](https://tasker.joaoapps.com/userguide/en/intents.html):

```
New task: Something recognizable
Action Category: Misc/Send Intent
Action: eu.faircode.email.ENABLE
Target: Service
```

To enable/disable an account with the name *Gmail*:

```
Extras: account:Gmail
```

Account names are case sensitive.

Automation can be used for more advanced schedules,
like for example multiple synchronization periods per day or different synchronization periods for different days.

It is possible to install FairEmail in multiple user profiles, for example a personal and a work profile, and to configure FairEmail differently in each profile,
which is another possibility to have different synchronization schedules and to synchronize a different set of accounts.

It is also possible to create [rules](#user-content-faq71) with a time condition and to snooze messages until the end time of the time condition.
This way it is possible to snooze business related messages until the start of the business hours.

Scheduling is a pro feature.

<br />

<a name="faq79"></a>
**(79) How do I use synchronize on demand (manual)?**

Normally, FairEmail maintains a connection to the configured email servers whenever possible to receive messages in real-time.
If you don't want this, for example to not be disturbed or to save on battery usage,
just disable synchronization in the advanced option (accessible via the button at the bottom of the setup screen).
This will stop the background service which takes care of automatic synchronization and will remove the associated status bar notification.
You can use pull-down-to-refresh in a folder or use the folder menu *Synchronize now* to manually synchronize messages.
This will start the synchronization service for 60 seconds for all configured accounts.

The synchronization process will also be started to execute [operations](#user-content-faq3),
for example to mark a message read, move a message or store a draft.
This is to keep the local and remote message store synchronized.

If you want to synchronize some or all folders of an account manually, just disable synchronization for the folders (but not of the account).

You'll likely want to disabled [browse on server](#user-content-faq24) too.

<br />

<a name="faq80"></a>
**~~(80) How can I fix 'Unable to load BODYSTRUCTURE' ?~~**

~~The error message *Unable to load BODYSTRUCTURE* is caused by bugs in the email server,~~
~~see [here](https://javaee.github.io/javamail/FAQ#imapserverbug) for more details.~~

~~FairEmail already tries to workaround these bugs, but if this fail you'll need to ask for support from your provider.~~

<br />

<a name="faq81"></a>
**~~(81) Can you make the background of the original message dark in the dark theme?~~**

~~The original message is shown as the sender has sent it, including all colors.~~
~~Changing the background color would not only make the original view not original anymore, it can also result in unreadable messages.~~

<br />

<a name="faq82"></a>
**(82) What is a tracking image?**

Please see [here](https://en.wikipedia.org/wiki/Web_beacon) about what a tracking image exactly is.
In short tracking images keep track if you opened a message.

FairEmail automatically recognizes images with a surface of less than or equal to 25 pixels as tracking images.
FairEmail automatically removes the link of such images, which makes such images appear as broken, and adds a remark about this below the image.

Automatic recognition of tracking images can be disabled in the behavior settings.

<br />

<a name="faq83"></a>
**(83) What does 'User is authenticated but not connected' mean?**

This is likely a confusing Microsoft Exchange (Office365) message telling that the password is invalid.

Less likely is  that you are synchronizing too many folders.
This can also happen due to abruptly losing connectivity resulting in not properly closing connections.

So, double check the password or reduce the number of folders to synchronize.

<br />

<a name="faq84"></a>
**(84) What are local contacts for?**

Local contact information is based on names and addresses found in incoming and outgoing messages.

The main use of the local contacts storage is to offer auto completion when no contacts permission has been granted to FairEmail.

Another use is to generate [shortcuts](#user-content-faq31) on recent Android versions to quickly send a message to frequently contacted people.
This is also why the number of times contacted and the last time contacted is being recorded
and why you can make a contact a favorite or exclude it from favorites by long pressing it.

The list of contacts is sorted on number of times contacted and the last time contacted.

Local contacts will be used for auto completion only when no contacts permission has been granted to FairEmail.
You can revoke contacts permission in the Android app settings.

<br />

<a name="faq85"></a>
**(85) Why is an identity not available?**

An identity is available for sending a new message or replying or forwarding an existing message only if:

* the identity is set to synchronize (send messages)
* the associated account is set to synchronize (receive messages)
* the associated account has a drafts folder

FairEmail will try to select the best identity based on the *to* address of the message replied to / being forwarded.

<br />

<a name="faq86"></a>
**~~(86) What are 'extra privacy features'?~~**

~~The advanced option *extra privacy features* enables:~~

* ~~Looking up the owner of the IP address of a link~~
* ~~Detection and removal of [tracking images](#user-content-faq82)~~

<br />

<a name="faq87"></a>
**(87) What does 'invalid credentials' mean?**

The error message *invalid credentials* means either that the user name and/or password is incorrect,
for example because the password was changed or expired, or that the account authorization has expired.

If the password is incorrect/expired, you will have to update the password in the account and/or identity settings.

If the account authorization has expired, you will have to select the account again.
You will likely need to save the associated identity again as well.

<br />

<a name="faq88"></a>
**(88) How can I use a Yahoo! account?**

For the correct settings, see [here](https://help.yahoo.com/kb/SLN4075.html).

You might need to enable "*less secure sign in*" for "*outdated*" apps,
see [here](https://help.yahoo.com/kb/grant-temporary-access-outdated-apps-account-settings-sln27791.html) for more information.
You can directly access this setting [here](https://login.yahoo.com/account/security#less-secure-apps).

If you enable "*less secure sign in*", you should use a [strong password](https://en.wikipedia.org/wiki/Password_strength) for your Yahoo! account, which is a good idea anyway.
Note that using the [standard](https://tools.ietf.org/html/rfc3501) IMAP protocol in itself is not less secure and not outdated.

<br />

<a name="faq89"></a>
**(89) How can I send plain text only messages?**

By default FairEmail sends each message both as plain text and as HTML formatted text because almost every receiver expects formatted messages these days.
If you want/need to send plain text messages only, you can enable this in the advanced identity options.
You might want to create a new identity for this if you want/need to select sending plain text messages on a case-by-case basis.

<br />

<a name="faq90"></a>
**(90) Why are some texts linked while not being a link?**

FairEmail will automatically link not linked web [IRI](https://nl.wikipedia.org/wiki/Internationalized_resource_identifier)s for your convenience.
However, texts and links are not easily distinguished,
especially not with lots of [top level domains](https://en.wikipedia.org/wiki/List_of_Internet_top-level_domains) being words.
This is why texts with dots are sometimes incorrectly recognized as links, which is better than not recognizing some links.

<br />

<a name="faq91"></a>
**~~(91) Can you add periodical synchronization to save battery power?~~**

~~Synchronizing messages is an expensive proces because the local and remote messages need to be compared,~~
~~so periodically synchronizing messages will not result in saving battery power, more likely the contrary.~~

~~See [this FAQ](#user-content-faq39) about optimizing battery usage.~~


<br />

<a name="faq92"></a>
**(92) Can you add spam filtering, verification of the DKIM signature and SPF authorization?**

Spam filtering, verification of the [DKIM](https://en.wikipedia.org/wiki/DomainKeys_Identified_Mail) signature
and [SPF](https://en.wikipedia.org/wiki/Sender_Policy_Framework) authorization is a task of email servers, not of an email client.

Of course you can report messages as spam with FairEmail,
which will move the reported messages to the spam folder and train the spam filter of the provider, which is how it is supposed to work.

Also, FairEmail can show a small red warning flag
when DKIM, SPF or [DMARC](https://en.wikipedia.org/wiki/DMARC) authentication failed on the receiving server.
You can enable/disable [authentication verification](https://en.wikipedia.org/wiki/Email_authentication) in the behavior settings.

FairEmail can show a warning flag too when the domain name of the (reply) email address of the sender does not define an MX record pointing to an email server.
This can be enabled in the receive settings. Be aware that this will slow down synchronization of messages significantly.

If legitimate messages are failing authentication, you should notify the sender because this will result in a high risk of messages ending up in the spam folder.
Moreover, without proper authentication there is a risk the sender will be impersonated.
The sender might use [this tool](https://www.mail-tester.com/) to check authentication and other things.

<br />

<a name="faq93"></a>
**(93) Can you allow installation on external storage?**

FairEmail uses services and alarms, provides widgets and listens for the boot completed event to be started on device start, so this is not possible.
See also [here](https://developer.android.com/guide/topics/data/install-location).

<br />

<a name="faq94"></a>
**(94) What does the red/orange stripe at the end of the header mean?**

The red/orange stripe at the left side of the header means that the DKIM, SPF or DMARC authentication failed.
See also [this FAQ](#user-content-faq92).

<br />

<a name="faq95"></a>
**(95) Why are not all apps shown when selecting an attachment or image?**

For privacy and security reasons FairEmail does not have permissions to directly access files,
instead the Storage Access Framework, available and recommended since Android 4.4 KitKat (released in 2013), is used to select files.
If an app is listed depends on if the app implements a [document provider](https://developer.android.com/guide/topics/providers/document-provider).

Android Q will make it harder and maybe even impossible to directly access files,
see [here](https://developer.android.com/preview/privacy/scoped-storage) and [here](https://www.xda-developers.com/android-q-storage-access-framework-scoped-storage/) for more details.

<br />

<a name="faq96"></a>
**(96) Where can I find the IMAP and SMTP settings?**

The IMAP settings are part of the (custom) account settings and the SMTP settings are part of the identity settings.

<br />

<a name="faq97"></a>
**(97) What is 'cleanup' ?**

About each four hours FairEmail runs a cleanup job that:

* Removes old message texts
* Removes old attachment files
* Removes old image files
* Removes old local contacts
* Removes old log entries

Note that the cleanup job will only run when the synchronize service is active.

<br />

<a name="faq98"></a>
**(98) Why can I still pick contacts after revoking contacts permissions?**

After revoking contacts permissions Android does not allow FairEmail access to your contacts anymore.
However, picking contacts is delegated to and done by Android and not by FairEmail, so this will still be possible without contacts permissions.

<br />

<a name="faq99"></a>
**(99) Can you add a rich text or markdown editor?**

FairEmail provides common text formatting (bold, italic, underline, text size and color) via the Android text selection menu.

A [Rich text](https://en.wikipedia.org/wiki/Formatted_text) or [Markdown](https://en.wikipedia.org/wiki/Markdown) editor
would not be used by many people on a small mobile device and, more important,
Android doesn't support a rich text editor and most rich text editor open source projects are abandoned.
See [here](https://forum.xda-developers.com/showpost.php?p=79061829&postcount=4919) for some more details about this.

<br />

<a name="faq100"></a>
**(100) How can I synchronize Gmail categories?**

You can synchronize Gmail categories by creating filters to label categorized messages:

* Create a new filter via Gmail > Settings (wheel) > Filters and Blocked Addresses > Create a new filter
* Enter a category search (see below) in the *Has the words* field and click *Create filter*
* Check *Apply the label* and select a label and click *Create filter*

Possible categories:

```
category:social
category:updates
category:forums
category:promotions
```

Pull down the folders list in FairEmail to refresh the folder list
and long press the category folders to enable synchronization.

<br />

<a name="faq101"></a>
**(101) What does the blue/orange dot at the bottom of the conversations mean?**

The dot shows the relative position of the conversation in the message list.
The dot will be show orange when the conversation is the first or last in the message list, else it will be blue.
The dot is meant as an aid when swiping left/right to go to the previous/next conversation.

<br />

<a name="faq102"></a>
**(102) How can I enable auto rotation of images?**

Images will automatically be rotated when automatic resizing of images is enabled in the settings (enabled by default).
However, automatic rotating depends on the [Exif](https://en.wikipedia.org/wiki/Exif) information to be present and to be correct,
which is not always the case. Particularly not when taking a photo with a camara app from FairEmail.

Note that only [JPEG](https://en.wikipedia.org/wiki/JPEG) and [PNG](https://en.wikipedia.org/wiki/Portable_Network_Graphics) images can contain Exif information.

<br />

<a name="faq103"></a>
**(103) How can I record audio?**

You can record audio if you have a recording app installed
which supports the [RECORD_SOUND_ACTION](https://developer.android.com/reference/android/provider/MediaStore.Audio.Media#RECORD_SOUND_ACTION) intent.
If no supported app is installed, FairEmail will not show a record audio action/icon.

Unfortunately and surprisingly, most recording apps do not seem to support this intent (they should).

<br />

<a name="faq104"></a>
**(104) What do I need to know about error reporting?**

* Error reports will help improve FairEmail
* Error reporting is optional and opt-in
* Error reporting can be enabled/disabled in the settings, section miscellaneous
* Error reports will automatically be sent anonymously to [Bugsnag](https://www.bugsnag.com/)
* Bugsnag for Android is [open source](https://github.com/bugsnag/bugsnag-android)
* See [here](https://docs.bugsnag.com/platforms/android/automatically-captured-data/) about what data will be sent in case of errors
* See [here](https://docs.bugsnag.com/legal/privacy-policy/) for the privacy policy of Bugsnag
* Error reports will be sent to *sessions.bugsnag.com:443* and *notify.bugsnag.com:443*

<br />

<a name="faq105"></a>
**(105) How does the roam-like-at-home option work?**

FairEmail will check if the country code of the SIM card and the country code of the network
are in the [EU roam-like-at-home countries](https://en.wikipedia.org/wiki/European_Union_roaming_regulations#Territorial_extent)
and assumes no roaming if the country codes are equal and the advanced roam-like-at-home option is enabled.

So, you don't have to disable this option if you don't have an EU SIM or are not connected to an EU network.

<br />

<a name="faq106"></a>
**(106) Which launchers can show the number of new messages?**

Please [see here](https://github.com/leolin310148/ShortcutBadger#supported-launchers)
for a list of launchers which can show the number of new messages.

Note that this needs to be enabled in the advance options (default enabled).

<br />

<a name="faq107"></a>
**(107) How do I use colored stars?**

You can set a colored star via the *more* message menu, via multiple selection (started by long pressing a message),
by long pressing a star in a conversation or automatically by using [rules](#user-content-faq71).

You need to know that colored stars are not supported by the IMAP protocol and can therefore not be synchronized to an email server.
This means that colored stars will not be visible in other email clients and will be lost on downloading messages again.
However, the stars (without color) will be synchronized and will be visible in other email clients, when supported.

Some email clients use IMAP keywords for colors.
However, not all servers support IMAP keywords and besides that there are no standard keywords for colors.

<br />

<a name="faq108"></a>
**~~(108) Can you add permanently delete messages from any folder?~~**

~~When you delete messages from a folder the messages will be moved to the trash folder, so you have a chance to restore the messages.~~
~~You can permanently delete messages from the trash folder.~~
~~Permanently delete messages from other folders would defeat the purpose of the trash folder, so this will not be added.~~

<br />

<a name="faq109"></a>
**~~(109) Why is 'select account' available in official versions only?~~**

~~Using *select account* to select and authorize Google accounts require special permission from Google for security and privacy reasons.~~
~~This special permission can only be acquired for apps a developer manages and is responsible for.~~
~~Third party builds, like the F-Droid builds, are managed by third parties and are the responsibility of these third parties.~~
~~So, only these third parties can acquire the required permission from Google.~~
~~Since these third parties do not actually support FairEmail, they are most likely not going to request the required permission.~~

~~You can solve this in two ways:~~

* ~~Switch to the official version of FairEmail, see [here](https://github.com/M66B/FairEmail/blob/master/README.md#downloads) for the options~~
* ~~Use app specific passwords, see [this FAQ](#user-content-faq6)~~

~~Using *select account* in third party builds is not possible in recent versions anymore.~~
~~In older versions this was possible, but it will now result in the error *UNREGISTERED_ON_API_CONSOLE*.~~

<br />

<a name="faq110"></a>
**(110) Why are (some) messages empty and/or attachments corrupt?**

Empty messages and/or corrupt attachments are probably being caused by a bug in the server software.
Older Microsoft Exchange software is known to cause this problem.
Mostly you can workaround this by disabling *Partial fetch* in the advanced account settings.

After disabling this setting, you can use the message 'more' (three dots) menu to 'resync' empty messages.
Alternatively, you can *Delete local messages* by long pressing the folder(s) in the folder list and synchronize all messages again.

Disabling *Partial fetch* will result in more memory usage.

<br />

<a name="faq111"></a>
**(111) Why is OAuth not supported?**

OAuth is supported for Gmail via the quick setup wizard.
The Android account manager will be used to fetch and refresh OAuth tokens for the selected account.

Outlook and Hotmail do not properly support OAuth for IMAP/SMTP connections.
[MSAL](https://github.com/AzureAD/microsoft-authentication-library-for-android) is supported for business accounts only
and requires embedding a client secret in the app, which is not a good idea for an open source app.

<br />

<a name="faq112"></a>
**(112) Which email provider do you recommend?**

Which email provider is best for you depends on your wishes/requirements.
Please see the websites of [Restore privacy](https://restoreprivacy.com/secure-email/) or [Privacy Tools](https://www.privacytools.io/providers/email/)
for a list of privacy friendly email providers with advantages and disadvantages.

<br />

<a name="faq113"></a>
**(113) How does biometric authentication work?**

If your device has a biometric sensor, for example a fingerprint sensor, you can enable/disable biometric authentication in the navigation (hamburger) menu of the setup screen.
When enabled FairEmail will require biometric authentication after a period of inactivity or after the screen has been turned off while FairEmail was running.
Activity is navigation within FairEmail, for example opening a conversation thread.
The inactivity period duration can be configured in the miscellaneous settings.
When biometric authentication is enabled new message notifications will not show any content and FairEmail won't be visible on the Android recents screen.

Biometric authentication is meant to prevent others from seeing your messages only.
FairEmail relies on device encryption for data encryption, see also [this FAQ](#user-content-faq37).

Biometric authentication is a pro feature.

<br />

<a name="faq114"></a>
**(114) Can you add an import for the settings of other email apps?**

The format of the settings files of most other email apps is not documented, so this is difficult.
Sometimes it is possible to reverse engineer the format, but as soon as the settings format changes things will break.
Also, settings are often incompatible.
For example, FairEmail has unlike most other email apps settings for the number of days to synchronize messages for
and for the number of days to keep messages for, mainly to save on battery usage.
Moreover, setting up an account/identity with the quick setup is simple, so it is not really worth the effort.

<br />

<a name="faq115"></a>
**(115) Can you add email address chips?**

Email address [chips](https://material.io/design/components/chips.html) look nice, but cannot be edited,
which is quite inconvenient when you made a typo in an email address.

Chips are not suitable for showing in a list
and since the message header in a list should look similar to the message header of the message view it is not an option to use chips for viewing messages.

Reverted [commit](https://github.com/M66B/FairEmail/commit/2c80c25b8aa75af2287f471b882ec87d5a5a5015).

<br />

<a name="faq116"></a>
**~~(116) How can I show images in messages from trusted senders by default?~~**

~~You can show images in messages from trusted senders by default by enabled the display setting *Automatically show images for known contacts*.~~

~~Contacts in the Android contacts list are considered to be known and trusted,~~
~~unless the contact is in the group / has the label '*Untrusted*' (case insensitive).~~

<br />

<a name="faq38"></a>
<a name="faq117"></a>
**(117) Can you help me restore my purchase?**

Google manages all purchases, so as developer I have little control over purchases.
So, basically the only thing I can do, is give some advice:

* Make sure you have an active, working internet connection
* Make sure you are logged in with the right Google account and that there is nothing wrong with your Google account
* Open the Play store application and wait at least a minute to give it time to synchronize with the Google servers
* Open FairEmail and navigate to the pro features screen to let FairEmail check the purchases

You can also try to clear the cache of the Play store app via the Android apps settings.
Restarting the device might be necessary to let the Play store recognize the purchase correctly.

Note that:

* Purchases are stored in the Google cloud and cannot get lost
* There is no time limit on purchases, so they cannot expire
* Google does not expose details (name, e-mail, etc) about buyers to developers
* An application like FairEmail cannot select which Google account to use

If you cannot solve the problem with the purchase, you will have to contact Google about it.

<br />

<a name="faq118"></a>
**(118) What does 'Remove tracking parameters' exactly?**

Checking *Remove tracking parameters* will remove all [UTM parameters](https://en.wikipedia.org/wiki/UTM_parameters) from a link.

<br />

<a name="faq119"></a>
**(119) Can you add colors to the unified inbox widget?**

The widget is designed to look good on most home/launcher screens by making it monochrome and by using a half transparent background.
This way the widget will nicely blend in, while still being properly readable.

Adding (account) colors will cause problems with some backgrounds and will cause readability problems, which is why this won't be added.

<br />

<a name="faq120"></a>
**(120) Why are new message notifications not removed on opening the app?**

New message notifications will be removed on swiping notifications away or on marking the associated messages read.
Opening the app will not remove new message notifications.
This gives you a choice to leave new message notifications as a reminder that there are still unread messages.

On Android 7 Nougat and later new message notifications will be [grouped](https://developer.android.com/training/notify-user/group).
Tapping on the summary notification will open the unified inbox.
The summary notification can be expanded to view individual new message notifications.
Tapping on an individual new message notification will open the conversation the message it is part of.
See [this FAQ](#user-content-faq70) about when messages in a conversation will be auto expanded and marked read.

<br />

<a name="faq121"></a>
**(121) How are messages grouped into a conversation?**

By default FairEmail groups messages in conversations. This can be turned of in the display settings.

FairEmail groups messages based on the standard *Message-ID*, *In-Reply-To* and *References* headers.
FairEmail does not group on other criteria, like the subject,
because this could result in grouping unrelated messages and would be at the expense of increased battery usage.

<br />

<a name="faq122"></a>
**~~(122) Why is the recipient name/email address show with a warning color?~~**

~~The recipient name and/or email address in the addresses section will be shown in a warning color~~
~~when the sender domain name and the domain name of the *to* address do not match.~~
~~Mostly this indicates that the message was received *via* an account with another email address.~~

<br />

<a name="faq123"></a>
**(123) What does 'force sync'?**

FairEmail will wait a fixed time after connectivity changes
and will use a logarithmic back-off time after failing to connect to an account to prevent from being locked out.
*Force sync* will reset all timers and restart the synchronization service.
This should not normally be used.

<br />

<a name="faq124"></a>
**(124) Why do I get 'Message too large or too complex to display'?**

The message *Message too large or too complex to display* will be shown if there are more than 100,000 characters or more than 500 links in a message.
Reformatting and displaying such messages will take too long. You can try to use the original message view, powered by the browser, instead.

<br />

<a name="faq125"></a>
**(125) What are the current experimental features?**

The current experimental features are:

* Small, dimmed unread / read icon at the bottom of the messages to quicly toggle filtering of read messages
* Small, dimmed starred / unstarred icon at the bottom of the messages to quicly toggle filtering of starred messages
* Small, dimmed infinite / timelapse icon at the bottom of the messages to quicly toggle filtering of snoozed messages

<br />

<a name="faq126"></a>
**(126) What does 'User is authenticated but not connected' mean?**

The message *User is authenticated but not connected* is caused by a bug in older versions of Microsoft's Exchange server.
This message in fact means that the password was invalid, likely because it was changed.

<br />

<a name="faq127"></a>
**(127) How can I fix 'Syntactically invalid HELO argument(s)'?**

You can likely fix the error *Syntactically invalid HELO argument(s)* by disabling the advanced indentity option *Use local IP address instead of host name*.

<br />

<a name="faq128"></a>
**(128) How can I reset asked questions, for example to show images?**

You can reset asked questions via the three dots overflow menu in the miscellaneous settings.

<br />

<a name="faq129"></a>
**(129) Are ProtonMail, Tutanota supported?**

ProtonMail uses a proprietary email protocol
and [does not directly support IMAP](https://protonmail.com/support/knowledge-base/imap-smtp-and-pop3-setup/),
so you cannot use FairEmail to access ProtonMail.

Tutanota uses a proprietary email protocol
and [does not support IMAP](https://tutanota.com/faq/#imap),
so you cannot use FairEmail to access Tutanota.

<br />

<a name="faq130"></a>
**(130) What does message error ... mean?**

The warning *No server found at ...* means that there was no email server registered at the indicated domain name.
Replying to the message might not be possible and might result in an error.
This could indicate a falsified email address and/or spam.

The error *... ParseException ...* means that there is a problem with a received message, likely caused by a bug in the sending software.
FairEmail will workaround this is in most cases, so this message can mostly be considered as a warning instead of an error.

The error *...SendFailedException...* means that there was a problem while sending a message.
The error will almost always include a reason. Common reasons are that the message was too big or that one or more recipient addresses were invalid.

Please see [here](#user-content-faq22) for other error messages in the outbox.

<br />

<a name="faq131"></a>
**(131) Can you change the direction for swiping to previous/next message?**

If you read from left to right, swiping to the left will show the next message.
Similarly, if you read from right to left, swiping to the right will show the next message.

This behavior seems quite natural to me, also because it is similar to turning pages.

Anyway, there is a behavior setting to reverse the swipe direction.

<br />

<a name="faq132"></a>
**(132) Why are new message notifications silent?**

Notifications are silent by default on some MIUI versions.
Please see [here](http://en.miui.com/thread-3930694-1-1.html) how you can fix this.

There is a bug in some Android versions
causing [setOnlyAlertOnce](https://developer.android.com/reference/android/app/Notification.Builder#setOnlyAlertOnce(boolean)) to mute notifications.
Since FairEmail shows new message notifications right after fetching the message headers
and FairEmail needs to update new message notifications after fetching the message text later, this cannot be fixed or worked around by FairEmail.

Android might rate limit the notification sound, which can cause some new message notifications to be silent.

<br />

<a name="faq133"></a>
**(133) Why is ActiveSync not supported?**

The Microsoft Exchange ActiveSync protocol [is patented](https://en.wikipedia.org/wiki/Exchange_ActiveSync#Licensing) and can therefore not be supported.
For this reason you won't find many, if any, other email clients supporting ActiveSync.

<br />

<a name="faq134"></a>
**(134) Can you add deleting local messages?**

Since locally deleted messages would be downloaded again on the next sync it is not possible to permanently delete local messages.

As an alternative you can snooze messages, which will hide messages for a selected time.

<br />

<a name="faq135"></a>
**(135) Why are trashed messages and drafts shown in conversations?**

Individual messages will rarely be trashed and mostly this happens by accident.
Showing trashed messages in conversations makes it easier to find them back.

You can permanently delete a message using the message three-dots *delete* menu, which will remove the message from the conversation.
Note that this irreversible.

Similarly, drafts are shown in conversations to find them back in the context where they belong.
It is easy to read through the received messages before continuing to write the draft later.

<br />

<a name="faq136"></a>
**(136) How can I delete an account/identity/folder?**

Deleting an account/identity/folder is a little bit hidden to prevent accidents.

* Account: Setup > Step 1 > Manage > Tap account
* Identity: Setup > Step 2 > Manage > Tap identity
* Folder: Long press the folder in the folder list > Edit properties

In the three-dots overflow menu at the top right there is an item to delete the account/identity/folder.

<br />

<a name="faq137"></a>
**(137) How can I reset 'Don't ask again'?**

You can reset all questions set to not to be asked again in the three-dots overflow menu of the miscellaneous settings.

<br />

## Support

If you have another question, want to request a feature or report a bug, you can use [this forum](https://forum.xda-developers.com/android/apps-games/source-email-t3824168).
Registration is free.

If you are a supporter of the project, you can get limited personal support by using [this form](https://contact.faircode.eu/?product=fairemailsupport).
