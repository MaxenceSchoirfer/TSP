package tsp.projects.genetic;

import java.util.ArrayList;


public class Crossover {


    //TO-DO I THINK TRANSFORM PARAMETERS INTO PATH CAN IMPROVE COMPLEXITY, WE CAN CONTINUE TO WORK WITH TABLE ON THE FUNCTION

    //Order crossover 1 (overall the  best result when we don't use mutations)
    public static int[][] crossoverOX1(int[] p1, int[] p2) {
        //mockup data  https://www.redalyc.org/pdf/2652/265219618002.pdf p.4
        /*
        int x1 = 1;
        int x2 = 4;

        int[] p1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] p2 = {8, 5, 7, 1, 2, 4, 9, 3, 6};*/

        int length = p1.length;
        int min = 1;
        int x1 = (int) (Math.random() * ((length - 2) - min) + 1) + min;
        int x2 = (int) (Math.random() * ((length - 2) - min) + 1) + min;

        while (x2 == x1 || Math.abs(x2 - x1) == 1) x1 = (int) (Math.random() * ((length - 2) - min) + 1) + min;
        if (x2 < x1) {
            int n = x1;
            x1 = x2;
            x2 = n;
        }


        int[] s1 = new int[length];
        int[] s2 = new int[length];

        for (int i = 0; i < length; i++) {
            if (i > x1 && i <= x2) {
                s1[i] = p1[i];
                s2[i] = p2[i];
            } else {
                s1[i] = -1;
                s2[i] = -1;
            }
        }


        //t1 = temporary table use to complete the first child
        ArrayList<Integer> t1 = new ArrayList<>();
        //t2 = temporary table use to complete the second child
        ArrayList<Integer> t2 = new ArrayList<>();

        //copy first the end and after the start of cities
        for (int i = x2 + 1; i < length; i++) {
            t1.add(p2[i]);
            t2.add(p1[i]);
        }
        for (int i = 0; i <= x2; i++) {
            t1.add(p2[i]);
            t2.add(p1[i]);
        }


        //remove already on child cities
        for (int i = x1; i <= x2; i++) {
            //remove the value of s1[i]; not the value on the position
            t1.remove(Integer.valueOf(s1[i]));
            t2.remove(Integer.valueOf(s2[i]));
        }


        //complete first the end of child
        for (int i = x2 + 1; i < length; i++) {
            s2[i] = t2.get(0);
            t2.remove(0);

            s1[i] = t1.get(0);
            t1.remove(0);
        }

        //and after the start
        for (int i = 0; i <= x1; i++) {
            s2[i] = t2.get(0);
            t2.remove(0);

            s1[i] = t1.get(0);
            t1.remove(0);
        }


        int[][] result = new int[2][length];
        result[0] = s1;
        result[1] = s2;
        return result;
    }


/*
    public static int[][] crossover(int[] p1, int[] p2, int length) {
        int[][] sons = new int[2][length];

        int[] s1 = new int[length];
        int[] s2 = new int[length];

        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                s1[i] = p1[i];
                s2[i] = -1;
            } else {
                s2[i] = p1[i];
                s1[i] = -1;
            }
        }

        int[] pclone = new int[length];
        for (int i = 0; i < length; i++) {
            pclone[i] = p2[i];
        }

        loopi:
        for (int i = 0; i < length; i++) {
            if (s1[i] == -1) {
                loopj:
                for (int j = 0; j < length; j++) {
                    if (pclone[j] != -1) {
                        for (int s : s1) {
                            if (s == pclone[j]) continue loopj;
                        }
                        s1[i] = pclone[j];
                        pclone[j] = -1;
                        continue loopi;
                    }
                }
            }
        }


        loopi:
        for (int i = 0; i < length; i++) {
            if (pclone[i] == -1) {
                loopj:
                for (int j = 0; j < length; j++) {
                    if (pclone[j] != -1) {
                        for (int s : s2) {
                            if (s == p2[j]) continue loopj;
                        }
                        s2[i] = pclone[j];
                        pclone[j] = -1;
                        continue loopi;
                    }
                }
            }
        }

        System.out.println("");

        return sons;
    }
    */
}
