package tsp.projects.recuit;

import java.util.Arrays;

public class Mutation {


    //documentation available : https://tel.archives-ouvertes.fr/tel-00126292/document

    //reverse mutation (overall the  best result when we don't use crossover)
    public static int[] mutationIM(int[] p) {
        //mockup data  https://tel.archives-ouvertes.fr/tel-00126292/document p.54
        // int length = 7;
        // int x1 = 1;
        // int x2 = 4;
        // int[] p = {1, 2, 3, 4, 5, 6, 7};

        int length = p.length;
        int min = 1;
        int x1 = (int) (Math.random() * ((length - 2) - min) + 1) + min;
        int x2 = (int) (Math.random() * ((length - 2) - min) + 1) + min;

        while (x2 == x1 || Math.abs(x2 - x1) == 1) x1 = (int) (Math.random() * ((length - 2) - min) + 1) + min;
        if (x2 < x1) {
            int n = x1;
            x1 = x2;
            x2 = n;
        }

        int[] child = new int[length];

        System.arraycopy(p, 0, child, 0, x1);

        for (int i = x2; i >= x1; i--) {
            child[x1 + (x2 - i)] = p[i];
        }


        System.arraycopy(p, x2 + 1, child, x2 + 1, length - x2 - 1);
        return child;
    }

    public static int[] mutationCIM(int[] p) {
        int min = 1;
        int x = (int) (Math.random() * ((p.length - 2) - min) + 1) + min;

        int index = 0;
        int[] mutated = new int[p.length];
        for (int i = x; i >= 0; i--) {
            mutated[index] = p[i];
            index++;
        }
        for (int i = p.length - 1; i > x; i--) {
            mutated[index] = p[i];
            index++;
        }

        return mutated;
    }

    public static int[] mutationTWORS(int[] p) {
        int min = 1;
        int x1 = (int) (Math.random() * ((p.length - 1) - min) + 1) + min;
        int x2 = (int) (Math.random() * ((p.length - 1) - min) + 1) + min;

        int temp = p[x1];
        p[x1] = p[x2];
        p[x2] = temp;
        return p;
    }

    public static int[] mutationTHRORS(int[] p) {
        int min = 1;
        int[] i = new int[3];
        for (int j = 0; j < 3; j++) {
            i[j] = (int) (Math.random() * ((p.length - 1) - min) + 1) + min;
        }
        Arrays.sort(i);
        int v1 = p[i[0]];
        int v2 = p[i[1]];
        int v3 = p[i[2]];
        p[i[0]] = v3;
        p[i[1]] = v1;
        p[i[2]] = v2;
        return p;
    }
}
