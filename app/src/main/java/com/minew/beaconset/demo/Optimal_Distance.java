package com.minew.beaconset.demo;
//ㅎㅎ
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
//시험한번만 해보겔요
public class Optimal_Distance{
    public static int path[] = new int[5];
    static int width;
    static int height;
    static int map[][] = new int[10][10];
    static int check[][] = new int[10][10];
    static int dx[] = {0,0,-1,1};
    static int dy[] = { 1,-1,0,0};
    static int dis[][] = new int[10][10];
    static int res[][] = new int[4][4];
    static int inf = 1000000;
    static int visit[] = new int[4];
    static int visited[] = new int[4];
    static int d[] = new int[4];
    static int to_finish[] = new int[4];
    static int N = 4;
    static int items[] = new int[4];
    static int min_num = 100000000;
    static int result_array[] = new int[100];
    static int item_location[][] = new int[20][2];
    static Queue<Integer> queue_x = new LinkedList<>();
    static Queue<Integer> queue_y = new LinkedList<>();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        width = 10;
        height = 10;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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

        for (int i = 0; i < N; i++) {
            if(i==0) System.out.println("현재 위치를 입력하세요 : ");
            else System.out.println("물품 "+i+" 의 위치를 입력하세요 : ");
            for (int j = 0; j < 2; j++) {
                item_location[i][j]= sc.nextInt();
            }
        }

        for (int i = 0; i < N; i++) {
            int now_x = item_location[i][0];
            int now_y = item_location[i][1];

            queue_x.offer(now_x);
            queue_y.offer(now_y);

            bfs();

            for (int j = 0; j < N; j++) if (i != j) res[i][j] = dis[item_location[j][0]][item_location[j][1]];

            to_finish[i] = dis[height-1][width-1];


            for (int k = 0; k < height; k++) {
                for (int j = 0; j < width; j++) {
                    dis[k][j] = 0;
                    check[k][j] = 0;
                }
            }
        }
        for (int i = 1; i < N; i++) {
            items[i] = -1;
            visited[i] = 0;
        }
        tsp(0, 0, 1);
        // node숫자, 비용합, 개수
        for (int i = 0; i < N; i++) {
      //      System.out.println(result_array[i]+" ");
            path[i] = result_array[i];
        }
     //   System.out.println(min_num);

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

        if (count == N) {
            int i=N;

            if (min_num > costSum + to_finish[items[i - 1]]) {
                min_num = costSum + to_finish[items[i - 1]];
                for (int j = 0; j < N; j++) {
                    result_array[j] = items[j];
                }
            }
            visited[node] = 0;
            items[count - 1] = -1;
            return;
        }

        for (int i = 0; i < N; i++) {
            if (visited[i]== 0 && res[node][i] != 0) {
                tsp(i, costSum + res[node][i], count + 1);
            }
        }
        visited[node] = 0;
        items[count - 1] = -1;
    }
}