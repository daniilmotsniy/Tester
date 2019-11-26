#include <stdio.h>
#include <stdlib.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(int argc, char *argv[]) {
	 FILE *S1, *S2;
	  int x, y;
	  system("chcp 1251");
	  system("cls");
	  printf("¬ведите число : ");
	  scanf("%d", &x);
	  S1 = fopen("S1.txt", "w");
	  fprintf(S1, "%d", x);
	  fclose(S1);
	  S1 = fopen("S1.txt", "r");
	  S2 = fopen("S2.txt", "w");
	  fscanf(S1, "%d", &y);
	  y += 3;
	  fclose(S1);
	  fprintf(S2, "%d\n", y);
	  fclose(S2);
	return 0;
}
