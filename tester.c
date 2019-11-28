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

	// �������� �������� �����
		
	if (Q == NULL || A == NULL) {printf ("������\n"); return -1;}

	   //������ (���������) ������ �� ����� � ����������� �����
	   while (1)
	   {
	      // ������ ����� ������  �� �����
	      estr_q = fgets(questions, sizeof(questions), Q);
	      estr_a = fgets(answers, sizeof(answers), A);
	
	      //�������� �� ����� ����� ��� ������ ������
	      if (estr_q == NULL || estr_a == NULL)
	      {
	         // ���������, ��� ������ ���������: �������� ����
	         // ��� ��� ������ ������
	         if ( feof (Q) != 0 || feof (A) != 0)
	         {  
	            break;
	         }
	         else
	         {
	            printf ("\n������ ������ �� �����\n");
	            break;
	         }
	      }
	      printf ("%s", questions);
	      printf ("%s", answers);
	   }
	
	   if ( fclose (Q) == EOF || fclose (A) == EOF) {
			printf ("������\n");
	   }
	  
	return 0;
}

