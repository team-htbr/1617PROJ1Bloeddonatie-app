# bloeddonatie
[![Build Status](https://travis-ci.org/team-htbr/1617PROJ1Bloeddonatie-app.svg?branch=master)](https://travis-ci.org/team-htbr/1617PROJ1Bloeddonatie-app)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/08a78239de8f48aaa6396d4f8713bd56)](https://www.codacy.com/app/rubenthys22/1617PROJ1Bloeddonatie-app?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=team-htbr/1617PROJ1Bloeddonatie-app&amp;utm_campaign=Badge_Grade)

Bloeddonatie aims to motivate people to donate blood

## Firebase configuration
- Go to [Firebase Console](https://console.firebase.google.com/project/bloeddonatie-bd78c/settings/general/android:com.team_htbr.a1617proj1Bloeddonatie_app) and download the google-services.json file
- Place this file in the `app` directory

## Things we learned when developing an Android application

### Package names are case sensitive
When trying to send a notification to our application using Firebase Notifications we mistyped a capital `B` instead of `b` when adding our app to Firebase. This caused us to not receive any notifcations. 

