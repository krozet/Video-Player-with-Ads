# Video Player with Ads
#### Author: Keawa Rozet

This project's purpose is to create a video player that will load a 10 second "paid" ad before playing a selected video.
We grab a list of shows from an API and display that to the user which is sorted by category. If a video is selected, a
10 second ad will play before starting the video.

## Few things to note about the code:
- This app is coded using an MVP structure
- I am displaying the shows through a nested recyclerview solution
- The main technical algorithm of note can be found in the Shows.kt inside of init
   where I take the parsed json object (now turned into a Shows object which contains a list of shows)
   and I iterate through the list and place them into a Hashmap of <String,List> pairs to sort each show (value) into
   their respected category (key) by only iterating through the list once
- Currently tested on a Pixel 3 physical device with plans to make it runable on emulated devices
