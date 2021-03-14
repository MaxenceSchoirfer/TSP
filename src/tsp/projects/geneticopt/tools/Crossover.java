package tsp.projects.geneticopt.tools;

import java.util.ArrayList;
import java.util.Arrays;


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

    public static int[][] crossoverNone(int[] p1, int[] p2){
        int[][] children = new int[2][p1.length];
        children[0] = p1;
        children[1] = p2;
        return children;
    }

    public static int[][] crossoverMPX(int[] p1, int[] p2) {
        //mockup data  https://tel.archives-ouvertes.fr/tel-00126292/document p.51 (pdf p.70)
        /*
        int x1 = 1;
        int x2 = 3;

        int[] p1 = {1, 2, 3, 4, 5, 6, 7};
        int[] p2 = {7, 5, 1, 3, 2, 6, 4};*/

        int min = 1;
        int x1 = (int) (Math.random() * ((p1.length - 2) - min) + 1) + min;
        int x2 = (int) (Math.random() * ((p1.length - 2) - min) + 1) + min;

        int[] child1 = new int[p1.length];
        int[] child2 = new int[p1.length];
        Arrays.fill(child1, -1);
        Arrays.fill(child2, -1);


        int x1p2 = 0;

        for (int i = 0; i < p2.length; i++) {
            if (p2[i] == p1[x1]) {
                x1p2 = i;
                break;
            }
        }

        int[] p1onC1 = new int[x2 - x1 + 1];
        int[] p2onC2 = new int[x2 - x1 + 1];
        for (int i = x1p2; i < p2.length; i++) {
            child1[i] = p1[x1 - x1p2 + i];
            child2[i] = p2[x1 - x1p2 + i];
            p1onC1[i - x1p2] = p1[x1 - x1p2 + i];
            p2onC2[i - x1p2] = p2[x1 - x1p2 + i];
        }


        buildChildMPX(p2, child1, x1p2, p1onC1);
        buildChildMPX(p1, child2, x1p2, p2onC2);


        int[][] children = new int[2][p1.length];
        children[0] = child1;
        children[1] = child2;
        return children;
    }

    private static void buildChildMPX(int[] parent, int[] child, int indexOnOtherParent, int[] alreadyOnChild) {
        int j = 0;
        for (int i = 0; i < indexOnOtherParent; i++) {
            loop:
            while (true) {
                for (int value : alreadyOnChild) {
                    if (parent[j] == value) {
                        j++;
                        continue loop;
                    }
                }
                child[i] = parent[j];
                j++;
                break;
            }
        }
    }

    //doesn't work
    public static int[][] crossoverERDX(int[] p, int[] p9) {
        //mockup data  https://tel.archives-ouvertes.fr/tel-00126292/document p.51 (pdf p.70)
        // -1 on all cities

        int x1 = 1;
        int x2 = 1;

        int[] p1 = {0, 1, 2, 3, 4, 5, 6};
        int[] p2 = {6, 4, 0, 2, 1, 5, 3};

        int[][] neighbours = new int[5][p1.length];
        for (int i = 0; i < p1.length; i++) {
            neighbours[0][i] = i;
        }


        int n1 = -1, n2 = -1, n3 = -1, n4 = -1, nextIndex, previousIndex;

        for (int i = 0; i < p1.length; i++) {

//            int nextIndex = (i + 1) % p1.length;
//            int previousIndex = i - 1;
//            if (i == 0) previousIndex = p1.length - 1;
//
//            n1 = p1[nextIndex];
//            n2 = p1[previousIndex];

            //cherche voisin dans parent 1
            for (int j = 0; j < p1.length; j++) {
                if (p1[j] == i) {
                    nextIndex = (j + 1) % p1.length;
                    previousIndex = j - 1;
                    if (j == 0) previousIndex = p1.length - 1;

                    n1 = p1[nextIndex];
                    n2 = p1[previousIndex];
                    break;
                }
            }

            //cherche voisin dans parent2
            for (int j = 0; j < p2.length; j++) {
                if (p2[j] == i) {
                    nextIndex = (j + 1) % p2.length;
                    previousIndex = j - 1;
                    if (j == 0) previousIndex = p2.length - 1;

                    n3 = p2[nextIndex];
                    n4 = p2[previousIndex];
                    break;
                }
            }

            neighbours[1][i] = n1;
            neighbours[2][i] = n2;
            if (n3 != n1 && n3 != n2) neighbours[3][i] = n3;
            else neighbours[3][i] = -1;

            if (n4 != n1 && n4 != n2) neighbours[4][i] = n4;
            else neighbours[4][i] = -1;


        }

        int[][] children = new int[2][p1.length];
        int[][] copy = neighbours.clone();
        int x = (int) (Math.random() * ((p1.length - 1) + 1));
        children[0][0] = p1[x];
        copy[0][p1[x]] = -1;

        //on construit le reste de l'enfant 1
        for (int i = 1; i < p1.length; i++) {
            int numberNeighbours = 5;
            int indexLessNeighbours = -1;

            //dernier sommet insert
            int[] neighboursCity = {copy[1][children[0][i - 1]], copy[2][children[0][i - 1]], copy[3][children[0][i - 1]], copy[4][children[0][i - 1]]};
            for (int j = 0; j < neighboursCity.length; j++) {
                int[] n = {neighbours[1][neighboursCity[j]], neighbours[2][neighboursCity[j]], neighbours[3][neighboursCity[j]], neighbours[4][neighboursCity[j]]};
                int numberN = 0;
                for (int value : n) {
                    if (value != -1) numberN++;
                }
                if (numberN < numberNeighbours) {
                    numberNeighbours = numberN;
                    indexLessNeighbours = j;
                }
            }
            children[0][i] = neighboursCity[indexLessNeighbours];
            for (int j = 1; j < copy.length; j++) {
                for (int k = 0; k < copy[0].length; k++) {
                    if (copy[j][k] == neighboursCity[indexLessNeighbours]) copy[j][k] = -1;
                }
            }
            //   copy[0][neighboursCity[indexLessNeighbours]] = -1;

        }
        return children;
    }

}
