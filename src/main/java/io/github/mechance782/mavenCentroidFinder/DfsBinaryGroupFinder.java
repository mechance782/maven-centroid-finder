package io.github.mechance782.mavenCentroidFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method
    * (size first, then x, then y). That is, the largest group will be first, the 
    * smallest group will be last, and ties will be broken first by descending 
    * y value, then descending x value.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if (image == null){
            throw new NullPointerException();
        }
        // create shallow copy of image array to keep track of visited pixels without editing original array
        int[][] imageClone = image.clone();
        // create list to add groups to
        List<Group> groups = new ArrayList<Group>();
        // nested for loop to find start of each group
        for (int y = 0; y < imageClone.length; y++){
            // if image[y] is null, throw null pointer
            if (imageClone[y] == null){
                throw new NullPointerException();
            }
            for(int x = 0; x < imageClone[y].length; x++){
                // if image[y][x] is not a 1 or a 0, throw illegal argument
                if (imageClone[y][x] != 1 && imageClone[y][x] != 0){
                    throw new IllegalArgumentException();
                }

                // when group is found, perform dfs with that x, y as starting point
                if (imageClone[y][x] == 1){
                    // dfs will need to fill an array with the size of the group and a sum of the group's x and y coordinates
                    // index 0 will have size, 1 will have sum of xs, 2 will have sum of ys
                    int[] groupInfo = new int[3];

                    dfs(imageClone, y, x, groupInfo);
                    // a new Group record will be created and added to the List using this info^
                    int size = groupInfo[0];
                    int xCentroid = groupInfo[1] / size;
                    int yCentroid = groupInfo[2] / size;

                    groups.add(new Group(size, new Coordinate(xCentroid, yCentroid)));
                }
            }
        }
        
        // sort list and return it
        Collections.sort(groups, Collections.reverseOrder());
        return groups;
    }

    private void dfs(int[][] image, int startY, int startX, int[] groupInfo){

        // create 2d array of direction options
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});

        while (!stack.isEmpty()){
            int[] node = stack.pop();

            int x = node[0];
            int y = node[1];
            // if node is invalid or already visited, skip forward
            if (y < 0 || y >= image.length || x < 0 || x >= image[y].length || image[y][x] == 0) continue;

            // set node to visited
            image[y][x] = 0;

            // save node information
            groupInfo[0] += 1;
            groupInfo[1] += x;
            groupInfo[2] += y;

            // check nodes in every direction
            for (int[] dir: directions){
                stack.push(new int[]{x + dir[0], y + dir[1]});
            }

        }
    }
    
}
