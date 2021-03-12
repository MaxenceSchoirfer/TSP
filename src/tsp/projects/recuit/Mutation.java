package tsp.projects.recuit;

public class Mutation {


    //TO-DO IDEM CROSSOVER, WORK WITH PATH
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
}
