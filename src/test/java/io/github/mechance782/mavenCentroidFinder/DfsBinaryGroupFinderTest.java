package io.github.mechance782.mavenCentroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class DfsBinaryGroupFinderTest {

    @Test
    public void findConnectedGroups_nullArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    @Test
    public void findConnectedGroups_nullSubArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = {
            null,
            {0, 0, 0}
        };

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    @Test
    public void findConnectedGroups_invalidArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = {
            {2, 3, 3, 3, 3},
            {3, 2, 2, 3, 3},
            {2, 3, 2, 3, 2},
            {2, 2, 3, 3, 2}
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    

    @Test
    public void findConnectedGroups_oneGroup(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = Arrays.asList(new Group(8, new Coordinate(3, 1)));
        int[][] image = {
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
        };

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_varyingGroupSizes(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = Arrays.asList(new Group(6, new Coordinate(2, 5)),
        new Group(5, new Coordinate(3, 2)), new Group(3, new Coordinate(5, 6)),
        new Group(2, new Coordinate(2, 0)));
        int[][] image = {
            {0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 1, 1}
        };

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_sameGroupSizes(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = Arrays.asList(new Group(9, new Coordinate(5, 6)),
        new Group(9, new Coordinate(1, 1)));
        int[][] image = {
            {1, 1, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1}
        };

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_smallGroups() {
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        int[][] image = {
            {0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 0, 0, 0, 1},
            {0, 0, 1, 0, 1},
            {0, 0, 1, 0, 0}
        };

        List<Group> expected = Arrays.asList(
            new Group(2, new Coordinate(4, 2)),
            new Group(2, new Coordinate(2, 3)),
            new Group(1, new Coordinate(3, 1)),
            new Group(1, new Coordinate(1, 0)),
            new Group(1, new Coordinate(0, 2))
        );

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_allSameRow() {
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        int[][] image = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        List<Group> expected = Arrays.asList(
            new Group(3, new Coordinate(1, 4)),
            new Group(2, new Coordinate(4, 4)),
            new Group(1, new Coordinate(9, 4)),
            new Group(1, new Coordinate(7, 4))
        );

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_allSameColumn() {
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        int[][] image = {
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0}
        };

        List<Group> expected = Arrays.asList(
            new Group(3, new Coordinate(5, 4)),
            new Group(2, new Coordinate(5, 0))
        );

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_varyingSizesDiagonal() {
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        int[][] image = {
            {1, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 1}
        };

        List<Group> expected = Arrays.asList(
            new Group(7, new Coordinate(4, 4)),
            new Group(4, new Coordinate(1, 1)),
            new Group(1, new Coordinate(0, 0))
        );

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

}
