# Words - The Vocabulary App
[![Circle CI](https://img.shields.io/circleci/project/github/UnalignedByte/words-android.svg?style=plastic&label=CircleCI)](https://circleci.com/gh/UnalignedByte/words-android)
[![Latest Release](https://img.shields.io/github/release/UnalignedByte/words-android.svg?style=plastic&label=GitHub)](https://github.com/UnalignedByte/words-android/releases/latest)
[![Platform](https://img.shields.io/badge/Google_Play-Download-lightgrey.svg?style=plastic&colorA=green)](https://play.google.com/store/apps/details?id=com.unalignedbyte.words)

Words is an app that allows you to create lists of vocabulary that will help you with memorization.

The app is written in Java and uses SQLite for persistance. It also uses [Butter Knife](https://github.com/JakeWharton/butterknife), [SectionedRecyclerViewAdapter](https://github.com/luizgrp/SectionedRecyclerViewAdapter), and Fabric.

## Background
This project was a kind of vehicle to learn the whole process of creating and releasing an Android app. It is a conversion of [Words for iOS](https://github.com/UnalignedByte/words-ios). Both of these apps have equivalen feature set.

## Source
The app is split into three subpackages:

* groups - UI that allows for listing, adding, and editing groups
* words - Same as groups, but for words
* model - Data source and persistance of the data


## To Do
There is a plenty of room for changes and improvements:

* Subgroups
* Automatic revision scheduler
* Information and statistics on number of words/groups/characters
* Groups of words in list
* System widget (word of the day, etc)
* Unit test the model
* Introduce RxJava
* Introduce Kotlin

## Author
The project has been created by Rafał Grodziński.