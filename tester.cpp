#include <stdio.h>  
#include <stdlib.h>
#include <locale.h>
#include <iostream> 
#include <cstring> 
#include <vector>
#include <string>

using namespace std;
const int N = 1000;

int main ()
{
	setlocale(LC_ALL, "Rus");
	
	vector<char> answers; //array with answers
	vector<string> all;
	vector<char> input;
	{	// block that allow to find answers
		FILE *T;
		char str[N];
		char *estr, tmp;
		int i, j, result = 0;
		
			T = fopen("text.txt", "r");
			if (T == NULL) {printf ("Stroke has not found\n"); return -1;}
		
			for(i = 0; 1; i++)
			   {
				  estr = fgets(str,sizeof(str),T);
			      if (estr == NULL)
			      {
			      	puts("Error");
					break;
			      }

					int flag = strchr(str, '+') - str ; // finding number of '+'
					
					if(strstr(str, "+")){
						for(i = flag; i < N; ++i){
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

			puts("\nBegining of the test\n");
			
				for(j = 0; j < all.size()/4; ++j){
					for (i = 0; i < 4; ++i) {
						if (i % 4) {
							cout << all[i + j*4] << endl;
						} else {
							cout << "Question: " << all[i + j*4] << endl;
						}
					}
					cin >> tmp;	//fixed
					cout << endl;
					input.push_back(tmp);
				}
	
			puts("\nEnd of the test\n");
			
			for (i = 0; i < answers.size(); ++i) {
				if(answers[i] == input[i]){
					result++;
				}
			}
			
			result = (result*100)/answers.size();
			cout << "Your result: " << result << "%" << endl << endl;
			
			if(result > 90 && result <= 100){
				printf("Nice");
			} else if(result > 70 && result <= 90){
				printf("Well");
			} else if(result > 50 && result <= 70){
				printf("Good");
			} else {
				printf("Bad");
			}
			
	}




   return 0;
} 

