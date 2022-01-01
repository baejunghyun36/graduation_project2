package com.minew.beaconset.demo;

import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;

public class Optimal_Distance extends AppCompatActivity {



    public static int[] item_x = MainActivity.item_location_x;
    public static int[] item_y = MainActivity.item_location_y;
    public static int[] item_id = MainActivity.id;
    public static int N = MainActivity.Basket_index+1;

    public static String path[] = new String[20];
    final static int nowX = SubActivity.nowX/10;
    final static int nowY = SubActivity.nowY/10;

    static int width;
    static int height;
    static int map[][] = new int[101][101];
    static int check[][] = new int[101][101];
    static int dx[] = {0,0,-1,1};
    static int dy[] = { 1,-1,0,0};
    static int dis[][] = new int[101][101];
    static int res[][] = new int[101][101];
    static int inf = 1000000;
    static int visit[] = new int[60];
    static int visited[] = new int[60];
    static int d[] = new int[40];
    static int to_finish[] = new int[60];
    //    static int N = 35;
    static int items[] = new int[60];
    static int min_num = 100000000;
    public static int result_array[] = new int[101];

    static int item_location[][] = new int[60][2];
    static Queue<Integer> queue_x = new LinkedList<>();
    static Queue<Integer> queue_y = new LinkedList<>();

    static int section[][] = new int[6][2];
    static int section_cnt[]= new int[6];

    static int section_A[][]= new int[60][3];
    static int section_B[][]= new int[60][3];
    static int section_C[][]= new int[60][3];
    static int section_D[][]= new int[60][3];
    static int sum =0;
    static int a_ind=1;
    static int b_ind=1;
    static int c_ind=1;
    static int d_ind=1;

    public static int cnt = 0;
    public static int[] arr = new int[35];  //결과값

    public static int section_res[] = new int[60];
    static int section_N = 5;
    public static int start[][] = new int[1][2];
    static int count_x= 100;
    static int count_y=100;


    public Optimal_Distance start() {
        width = 101;
        height = 101;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = -1;
            }
        }
        section[0][0]=nowX;
        section[0][1]=nowY;
        section[1][0]= 25;
        section[1][1]= 30;
        section[2][0]= 75;
        section[2][1]= 30;
        section[3][0]= 25;
        section[3][1]= 90;
        section[4][0]= 75;
        section[4][1]= 90;

        for (int i = 0; i < section_N; i++) {
            int now_x = section[i][0];
            int now_y = section[i][1];
            queue_x.offer(now_x);
            queue_y.offer(now_y);
            //해당 좌표값에서 맵의 가중치 구하기. 너비우선탐색
            bfs();

            for (int j = 0; j < section_N; j++) if (i != j) res[i][j] = dis[section[j][0]][section[j][1]];
            //해당 좌표값에서 다른 섹션의 중간 좌표값 까지 가는 가중치 값 구하기. 즉 가중치 그래프 생성
            to_finish[i] = dis[height-1][3]; //계산대 위치

            for (int k = 0; k < height; k++) {
                for (int j = 0; j < width; j++) {
                    dis[k][j] = 0;
                    check[k][j] = 0;
                }
            }
        }
        tspsection(0, 0, 1); //돌아봐야할 섹션 순서 정하기
        min_num=10000000;
        item_location[0][0]=section[0][0]; //현재 위치 x값
        item_location[0][1]=section[0][1]; //현재 위치 y값
        for (int i = 0; i < N-1; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    item_location[i+1][j] = item_x[i]; //item의 좌표값 모두 담기
                } else {
                    item_location[i+1][j] = item_y[i];
                }
            }
        }
        for(int i=1; i<N; i++) { //아이템 개수만큼
            int now_x = item_location[i][0]; //
            int now_y = item_location[i][1];
            if(now_x>=0&&now_x<=49&&now_y>=0&&now_y<=49) {//해당구역에 아이템이 위치하면 각섹션별에 좌표값 저장
                section_A[a_ind][0]=now_x;
                section_A[a_ind][1]=now_y;
//                arr[cnt++] = item_id[i-1];
//                arr[cnt++]=item_id[i];
                section_A[a_ind++][2]=item_id[i-1];//아이템 번호 저장

            }
            if(now_x>=0&&now_x<=49&&now_y>=50&&now_y<=99) {
                section_B[b_ind][0]=now_x;
                section_B[b_ind][1]=now_y;
//                arr[cnt++]=item_id[i-1];
                section_B[b_ind++][2]=item_id[i-1];

            }
            if(now_x>=50&&now_x<=99&&now_y>=0&&now_y<=49) {
                section_C[c_ind][0]=now_x;
                section_C[c_ind][1]=now_y;
                section_C[c_ind++][2]=item_id[i-1];

            }
            if(now_x>=50&&now_x<=99&&now_y>=50&&now_y<=99) {
                section_D[d_ind][0]=now_x;
                section_D[d_ind][1]=now_y;
                section_D[d_ind++][2]=item_id[i-1];
            }
        }
        for (int q = 0; q< 5;q++) {
            //현위치
            if(result_array[q]==0) {
                start[0][0] = section[0][0]; //시작 좌표값 저장
                start[0][1] = section[0][1];
            }
            //a구역
            else if(result_array[q]==1) {
                section_A[0][0] = start[0][0]; //시작 좌표값 먼저 세팅
                section_A[0][1] = start[0][1];
                for (int i = 0; i <a_ind; i++) {
                    int now_x = section_A[i][0];
                    int now_y = section_A[i][1];
                    queue_x.offer(now_x);
                    queue_y.offer(now_y);
                    bfs();  //start x, y 좌표에서 섹션 a구역에 위치한 모든 아이템까지의 가중치 구하기
                    for (int j = 0; j < a_ind; j++) if (i != j) res[i][j] = dis[section_A[j][0]][section_A[j][1]];
                    //각 아이템으로 연결되어있는 가중치 값들 저장하기
                    if(q==4)to_finish[i]=dis[count_x][count_y];
                    else {
                        if (result_array[q+1]==1)to_finish[i]=dis[25][25];
                        else if(result_array[q+1]==2)to_finish[i] = dis[25][75];
                        else if (result_array[q+1]==3)to_finish[i]=dis[75][25];
                        else if (result_array[q+1]==4)to_finish[i]=dis[60][40];
                    }
                    for (int k = 0; k < height; k++) {
                        for (int j = 0; j < width; j++) {
                            dis[k][j] = 0;
                            check[k][j] = 0;
                        }
                    }
                }
                for (int i = 1; i <N; i++) {
                    items[i] = -1;
                    visited[i] = 0;
                }

                tspabc(0, 0, 1, a_ind);
                // node숫자, 비용합, 개수
                for (int i = 1; i < a_ind; i++) {
                    arr[cnt++] = section_A[section_res[i]][2];//arr에 아이템 순서 담기

                    if(i==(a_ind-1)) {
                        start[0][0]=section_A[section_res[i]][0];  //마지막에 위치한 아이템의 좌표값 담기
                        start[0][1]=section_A[section_res[i]][1];
                    }
                }
                sum+=min_num;
            }
            //B구역
            else if(result_array[q]==2) {
                section_B[0][0] = start[0][0];
                section_B[0][1] = start[0][1];
                for (int i = 0; i <b_ind; i++) {
                    int now_x = section_B[i][0];
                    int now_y = section_B[i][1];
                    queue_x.offer(now_x);
                    queue_y.offer(now_y);
                    bfs();

                    for (int j = 1; j < b_ind; j++) if (i != j) res[i][j] = dis[section_B[j][0]][section_B[j][1]];

                    if(q==4)to_finish[i]=dis[count_x][count_y];
                    else {
                        if (result_array[q+1]==1)to_finish[i]=dis[25][25];
                        else if(result_array[q+1]==2)to_finish[i] = dis[25][75];
                        else if (result_array[q+1]==3)to_finish[i]=dis[75][25];
                        else if (result_array[q+1]==4)to_finish[i]=dis[60][40];
                    }

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

                tspabc(0, 0, 1, b_ind);
                // node숫자, 비용합, 개수
                for (int i = 1; i < b_ind; i++) {
                    arr[cnt++] = section_B[section_res[i]][2];
                    if(i==b_ind-1) {
                        start[0][0]=section_B[section_res[i]][0];
                        start[0][1]=section_B[section_res[i]][1];
                    }
                }
                sum+=min_num;
            }
            //C구역
            else if(result_array[q]==3) {
                section_C[0][0] = start[0][0];
                section_C[0][1] = start[0][1];
                for (int i = 0; i <c_ind; i++) {
                    int now_x = section_C[i][0];
                    int now_y = section_C[i][1];
                    queue_x.offer(now_x);
                    queue_y.offer(now_y);
                    bfs();

                    for (int j = 0; j < c_ind; j++) if (i != j) res[i][j] = dis[section_C[j][0]][section_C[j][1]];

                    if(q==4)to_finish[i]=dis[count_x][count_y];
                    else {
                        if (result_array[q+1]==1)to_finish[i]=dis[25][25];
                        else if(result_array[q+1]==2)to_finish[i] = dis[25][75];
                        else if (result_array[q+1]==3)to_finish[i]=dis[75][25];
                        else if (result_array[q+1]==4)to_finish[i]=dis[60][40];
                    }

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

                tspabc(0, 0, 1, c_ind);
                // node숫자, 비용합, 개수

                for (int i = 1; i < c_ind; i++) {
                    arr[cnt++] = section_C[section_res[i]][2];
                    if(i==c_ind-1) {
                        start[0][0]=section_C[section_res[i]][0];
                        start[0][1]=section_C[section_res[i]][1];
                    }
                }
                sum+=min_num;
            }

            //D구역

            else  {
                section_D[0][0] = start[0][0];
                section_D[0][1] = start[0][1];
                for (int i = 0; i <d_ind; i++) {
                    int now_x = section_D[i][0];
                    int now_y = section_D[i][1];
                    queue_x.offer(now_x);
                    queue_y.offer(now_y);
                    bfs();
                    for (int j = 1; j < d_ind; j++) if (i != j) res[i][j] = dis[section_D[j][0]][section_D[j][1]];

                    if(q==4)to_finish[i]=dis[count_x][count_y];
                    else {
                        if (result_array[q+1]==1)to_finish[i]=dis[25][25];
                        else if(result_array[q+1]==2)to_finish[i] = dis[25][75];
                        else if (result_array[q+1]==3)to_finish[i]=dis[75][25];
                        else if (result_array[q+1]==4)to_finish[i]=dis[60][40];
                    }

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

                tspabc(0, 0, 1, d_ind);
                // node숫자, 비용합, 개수

                for (int i = 1; i < d_ind; i++) {
                    arr[cnt++] = section_D[section_res[i]][2];
                }
                sum+=min_num;
                //        System.out.println("min_num : " + min_num);

                //        System.out.println("카운터");
            }
            min_num=1000000;

        }
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
    private static void tspabc(int node, int costSum, int count, int n ){
        visited[node] = 1;
        items[count - 1] = node;
        if (count == n) {
            int i= n;
            if (min_num > costSum + to_finish[items[n - 1]]) {
                min_num = costSum + to_finish[items[n - 1]];
                for (int j = 0; j < n; j++) {
                    section_res[j] = items[j];
                }
            }
            visited[node] = 0;
            items[count - 1] = -1;
            return;
        }

        for (int i = 0; i < n; i++) {
            if (visited[i]== 0 && res[node][i] != 0) {
                tspabc(i, costSum + res[node][i], count + 1, n);
            }
        }
        visited[node] = 0;
        items[count - 1] = -1;
    }

    private static void tspsection(int node, int costSum, int count){
        visited[node] = 1;
        section_cnt[count - 1] = node;
        if (count == section_N) {
            int i=section_N;
            if (min_num > costSum + to_finish[section_cnt[i - 1]]) {
                min_num = costSum + to_finish[section_cnt[i - 1]];
                for (int j = 0; j < section_N; j++) {

                    result_array[j] = section_cnt[j]; //돌아봐야할 순서 먼저 정하기
                }
            }
            visited[node] = 0;
            section_cnt[count - 1] = -1;
            return;
        }

        for (int i = 0; i < section_N; i++) {
            if (visited[i]== 0 && res[node][i] != 0) {
                tspsection(i, costSum + res[node][i], count + 1);
            }
        }
        visited[node] = 0;
        section_cnt[count - 1] = -1;
    }
}