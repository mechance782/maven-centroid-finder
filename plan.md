[Our plan!](https://docs.google.com/document/d/1irP67iczhL_jzM9z1sWvsa0mlkXtkqLNO2YOq1rsqEA/edit?usp=sharing)

Testing:
5 videos of red dots on white background.
3 vidoes are of 1 dot at various different frame rates (60, 30, 24)
1 video tests behavior when the target color leaves the frame
1 video tests behavior with multiple dots of various sizes that match the target color
target color is color picked from a frame of the video: #793736 
threshold will be 20 across all videos. A smaller threshold may be necessary for less clear videos,
but this is a dark, primary color against a white background with bright lighting.

Videos are all in 4k, dimensions: 3840 x 2160

Expected behavior:
In each video, the dot starts in the center of the screen, moves to the top, moves to the bottom, then returns to the center.
So, the expected data will have very small differences in the x centroid location, 
and the location of the y centroid should start in the center of the screen (~ 1050), move to the top( < 1050),
then move to the bottom ( > 1050), before returning to the center.
For the video where the mark exits the screen for a second, we expect to see the centroid location recorded as -1, -1 for that timestamp.

Actual Behavior:
In every video, the centroid's y-axis starts and ends within 100 pixels of the center of the screen ( 1050 +- 100 ). 
The centroid's x axis does not move more than 100 pixels from the start to the end of the video.
The y axis starts in the center, then becomes smaller (signifying movement to the top of the y-axis),
then becomes larger (signifying movement to the bottom of the y-axis). 
The range of y-axis' recorded are 226 - 2047.

In the video where the centroid moves off of the screen and there's no other target color to track,
a location of -1, -1 was recorded for the second it was off of the screen.

The actual behavior matches the expected behavior, so we can confidently say that our centroid tracker is working as intended.