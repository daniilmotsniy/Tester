#include <stdio.h>  
#include <stdlib.h>
#include <locale.h>
#include <time.h>
#include <iostream> 
#include <cstring> 
#include <vector>
#include <string>

using namespace std;
const int N = 512;

int getIndexOfChar(char*, char); // used in finding number of '+' position
void printResult(int);
int getResult(vector<char> input, vector<char> answers);
//char* selectTest(char* test_path[]);

int main ()
{
	setlocale(LC_ALL, "Rus");
	srand(time(NULL));
	
	char test_path[N/4];
	
	vector<string> all; //arrays
	vector<char> answers;
	vector<char> input;
	
	int i, j; //variables
	char tmp;
	
		FILE *T; //File
		char str[N];
		char *estr;
		
		puts("\nНачало теста, введите путь к документу с тестом\n");
		gets(test_path);

		T = fopen(test_path, "r");
		puts("\n");
		
			if (T == NULL) {printf ("Строка не найдена\n");}
			for(i = 0; 1; ++i){
				estr = fgets(str,sizeof(str),T);
			      	if (estr == NULL){
						break;
			      	}
				
					if(strstr(str, "+")){
						for(i = getIndexOfChar(str, '+'); i < N; ++i){
							str[i] = 0;
						}
						str[strlen(str)] = '\n';	// adding \n after right answer
						answers.push_back(str[0]);
					}
					
					if (str[0] != '\n' && str[1] != 0){
						all.push_back(str);
					}
			   }
			   
		if ( fclose (T) == EOF) printf ("Error \n");
  	
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
			
			printResult(getResult(input, answers));
			
    return 0;
} 

int getIndexOfChar(char* str, char symbol){
   	return strchr(str, symbol) - str;
}

int getResult(vector<char> input, vector<char> answers){
	int i, result = 0;
	for (i = 0; i < answers.size(); ++i) {
				if(answers[i] == input[i]){
					result++;
				}
			}
	return result = (result*100)/answers.size();
}

void printResult(int result){
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
/*
char (char test_path[N/4]){
	char* test_path_0 = strcat("textes/", test_path);
	return strcat(test_path_0, ".txt");
} */
