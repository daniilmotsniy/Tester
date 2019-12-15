#include <stdio.h>  
#include <stdlib.h>
#include <locale.h>
#include <windows.h>
#include <time.h>
#include <iostream> 
#include <cstring> 
#include <vector>
#include <string>

using namespace std;
const int N = 1000;

int getIndexOfChar(char*, char); // finding number of '+'

int main ()
{
	SetTimer(NULL,1,1000,NULL);
	setlocale(LC_ALL, "Rus");
	srand(time(NULL));
	
	vector<char> answers; //array with answers
	vector<string> all;
	vector<char> input;
	
	{
		FILE *T;
		char str[N];
		char *estr, tmp;
		int i, j, result = 0;
		
			T = fopen("text.txt", "r");
			if (T == NULL) {printf ("Строка не найдена\n"); return -1;}
		
			for(i = 0; 1; i++)
			   {
				  estr = fgets(str,sizeof(str),T);
			      if (estr == NULL)
			      {
					break;
			      }
				
					if(strstr(str, "+")){
						for(i = getIndexOfChar(str, '+'); i < N; ++i){
							str[i] = 0;
						}
						str[strlen(str)] = '\n';	// adding \n after right answer
						answers.push_back(str[0]);
					}
					
					if (str[0] != '\n' && str[1] != 0) {
						all.push_back(str);
					}
			   }
			   
			if ( fclose (T) == EOF) printf ("Error \n");

			puts("\nНачало теста\n");
			
				for(j = 0; j < all.size()/4; ++j){
					
					for (i = 0; i < 4; ++i) {
						if (i % 4) {
							cout << all[i + j*4] << endl;
						} else {
							cout << "Вопрос: " << all[i + j*4] << endl;
						}
					}
					cin >> tmp;	//fixed
					cout << endl;
					input.push_back(tmp);
					system("pause");
					system("cls");
				}
	
			puts("\nКонец теста\n");
			
			for (i = 0; i < answers.size(); ++i) {
				if(answers[i] == input[i]){
					result++;
				}
			}
			
			result = (result*100)/answers.size();
			cout << "Ваш результат: " << result << "%" << endl << endl;
			
			if(result > 90 && result <= 100){
				printf("Отлично");
			} else if(result > 70 && result <= 90){
				printf("Хорошо");
			} else if(result > 50 && result <= 70){
				printf("Удовлетворительно");
			} else {
				printf("Неудовлетворительно");
			}
			
	}
    return 0;
} 

int getIndexOfChar(char* str, char symbol){
   	return strchr(str, symbol) - str;
}

