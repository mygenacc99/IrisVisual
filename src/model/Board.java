package model;

public class Board {
    public int i;
    public int j;

    public Board(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "Feature: " + (i+1) + " " + (j+1);
    }
}
