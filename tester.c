#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
	system("chcp 1251");
	
	FILE *Q, *A;
	char *estr_q;
	char *estr_a;
	  
	char questions[100];
	char answers[100];

	Q = fopen("questions.txt", "r");
	A = fopen("answers.txt", "r");

	// Проверка открытия файла
		
	if (Q == NULL || A == NULL) {printf ("Ошибка\n"); return -1;}

	   //Чтение (построчно) данных из файла в бесконечном цикле
	   while (1)
	   {
	      // Чтение одной строки  из файла
	      estr_q = fgets(questions, sizeof(questions), Q);
	      estr_a = fgets(answers, sizeof(answers), A);
	
	      //Проверка на конец файла или ошибку чтения
	      if (estr_q == NULL || estr_a == NULL)
	      {
	         // Проверяем, что именно произошло: кончился файл
	         // или это ошибка чтения
	         if ( feof (Q) != 0 || feof (A) != 0)
	         {  
	            break;
	         }
	         else
	         {
	            printf ("\nОшибка чтения из файла\n");
	            break;
	         }
	      }
	      printf ("%s", questions);
	      printf ("%s", answers);
	   }
	
	   if ( fclose (Q) == EOF || fclose (A) == EOF) {
			printf ("Ошибка\n");
	   }
	  
	return 0;
}

