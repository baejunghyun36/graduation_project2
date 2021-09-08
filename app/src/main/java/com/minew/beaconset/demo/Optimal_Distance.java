package com.minew.beaconset.demo;

import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;

public class Optimal_Distance extends AppCompatActivity {



    public static int[] item_x = MainActivity.item_location_x;
    public static int[] item_y = MainActivity.item_location_y;

    public static String path[] = new String[20];


    private int nowx = SubActivity.now_x;
    private int nowy = SubActivity.now_y;


    static int width;
    static int height;
    static int map[][] = new int[1000][1000];
    static int check[][] = new int[1000][1000];
    static int dx[] = {0,0,-1,1};
    static int dy[] = { 1,-1,0,0};
    static int dis[][] = new int[1000][1000];
    static int res[][] = new int[100][100];
    static int inf = 1000000;
    static int visit[] = new int[100];
    static int visited[] = new int[100];
    static int d[] = new int[100];
    static int to_finish[] = new int[100];
    static int N = 4;
    static int items[] = new int[100];
    static int min_num = 100000000;
    static int result_array[] = new int[100];
    static int item_location[][] = new int[20][2];
    static Queue<Integer> queue_x = new LinkedList<>();
    static Queue<Integer> queue_y = new LinkedList<>();



    public Optimal_Distance start() {
        width = 480;
        height =650;


        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                map[i][j] = -1;
            }
        }
        map[1][1] = -2; //벽
        map[1][2] = -2; //벽
        map[3][1] = -2; //벽
        map[3][2] = -2; //벽
        map[8][8] = -2; //벽
        map[8][9] = -2; //벽
        map[9][9] = -3; //계산대

        for (int i = 0; i <= N; i++) {
            for (int j = 0; j < 2; j++) {
                if(i==0){
                    item_location[i][0]=nowx;
                    item_location[i][1]=nowy;
                }
                else{
                    if(j == 0){
                        item_location[i][j] = item_x[i-1];
                    }
                    else{
                        item_location[i][j] = item_y[i-1];
                    }
                }



            }
        }

        for (int i = 0; i <= N; i++) {
            int now_x = item_location[i][0];
            int now_y = item_location[i][1];

            queue_x.offer(now_x);
            queue_y.offer(now_y);

            bfs();

            for (int j = 0; j <= N; j++)
                if (i != j) res[i][j] = dis[item_location[j][0]][item_location[j][1]];

            to_finish[i] = dis[height-1][width-1];

            for (int k = 0; k < height; k++) {
                for (int j = 0; j < width; j++) {
                    dis[k][j] = 0;
                    check[k][j] = 0;
                }
            }
        }
        for (int i = 1; i <= N; i++) {
            items[i] = -1;
            visited[i] = 0;
        }
        tsp(0, 0, 1);
        // node숫자, 비용합, 개수
    /*    for (int i = 0; i < N; i++) {
            System.out.println(result_array[i]+" ");
            //    path[i] = Integer.toString(result_array[i]);
        }
        System.out.println(min_num);*/
        return null;
    }

    private static void bfs() {
        while (!queue_x.isEmpty()) {
            int x = queue_x.poll();
            int y = queue_y.poll();
            check[x][y]=1;
            for (int i = 0; i < 4; i++) {
                int xx = x + dx[i];
                int yy = y + dy[i];

                if (xx >= 0 && xx < height && yy >= 0 && yy < width && dis[xx][yy]==0&& check[xx][yy]!=1) {
                    if (map[xx][yy] != -2 && check[xx][yy]==0) {
                        dis[xx][yy] = dis[x][y] + 1;
                        queue_x.offer(xx);
                        queue_y.offer(yy);
                    }
                }
            }
        }
        //-2 은 장애물
        //-1 은 갈수있는길
    }
    private static void tsp(int node, int costSum, int count){
        visited[node] = 1;
        items[count - 1] = node;

        if (count == N+1) {
            int i=N+1;

            if (min_num > costSum + to_finish[items[i - 1]]) {
                min_num = costSum + to_finish[items[i - 1]];
                for (int j = 0; j <= N; j++) {
                    path[j] = Integer.toString(items[j]);
                }
            }
            visited[node] = 0;
            items[count - 1] = -1;
            return;
        }

        for (int i = 0; i <= N; i++) {
            if (visited[i]== 0 && res[node][i] != 0) {
                tsp(i, costSum + res[node][i], count + 1);
            }
        }
        visited[node] = 0;
        items[count - 1] = -1;
    }
}